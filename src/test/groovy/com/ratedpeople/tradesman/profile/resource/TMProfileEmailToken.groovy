/**
 * 
 */
package com.ratedpeople.tradesman.profile.resource

import com.ratedpeople.user.resource.AbstractTradesman;
import com.ratedpeople.user.resource.AbstractUserToken;
import com.ratedpeople.support.CommonVariable;
import com.ratedpeople.support.DatabaseHelper;

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.ByteArrayBody
import org.apache.http.entity.mime.content.FileBody

/**
 * @author shabana.khanam
 *
 */
class TMProfileEmailToken extends AbstractUserToken{

	private static final String PROFILE_PREFIX = CommonVariable.USER_SERVICE_PREFIX + "v1.0/users/"
	
	private static final String MATCH_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/match"
	
	def "Regenerate Email validation Token"(){
		given:
				String responseCode = null
				
		when:
			println "********************************"
			println "Test Running .... Regenerate Email validation Token"
				HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
					uri.path = PROFILE_PREFIX +USER_ID_TM  + "/email/token"
					headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
					requestContentType = ContentType.JSON
					println "Uri is " + uri
					response.success = { resp, reader ->
						println "Success"
						println "Got response: ${resp.statusLine}"
						println "Content-Type: ${resp.headers.'Content-Type'}"
						responseCode = resp.statusLine.statusCode
						reader.each{ "Results  : "+ "$it" }
					}
					response.failure = { resp, reader ->
						println " stacktrace : "+reader.each{"$it"}
						println 'Not found'
						responseCode = resp.statusLine.statusCode
					}
				}
			then:
				responseCode == CommonVariable.STATUS_200
}


def "Validate Email with Token"(){
	given:
			String responseCode = null
			def getToken = DatabaseHelper.select("select token from tmprofile.email_validation_token where user_id = '${USER_ID_TM}'")
			println "Token for email  is :"+getToken
			if (getToken.startsWith("[{token")){
				getToken = getToken.replace("[{token=", "").replace("}]","")
				println "Token is  is : " +getToken
			}
			
	when:
		println "********************************"
		println "Test Running ....  Validate Email with Token"
			HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
				uri.path = PROFILE_PREFIX +USER_ID_TM  + "/email/verify"
				uri.query = [
					token:getToken
					]
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{ "Results  : "+ "$it" }
				}
				response.failure = { resp, reader ->
					println " stacktrace : "+reader.each{"$it"}
					println 'Not found'
					responseCode = resp.statusLine.statusCode
				}
			}
		then:
			responseCode == CommonVariable.STATUS_200
}

}
