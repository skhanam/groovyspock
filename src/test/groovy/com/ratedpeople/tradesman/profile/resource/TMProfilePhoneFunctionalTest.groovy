package com.ratedpeople.tradesman.profile.resource

import com.ratedpeople.user.resource.AbstractUserToken;

import com.ratedpeople.user.resource.AbstractUserToken;
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.ByteArrayBody
import org.apache.http.entity.mime.content.FileBody
/**
 * 
 */


/**
 * @author shabana.khanam
 *
 */
class TMProfilePhoneFunctionalTest extends AbstractUserToken{

	private static final String PROFILE_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"
	
	private static final String MATCH_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/match"

	
	def "Add TM Phone"() {
		given :
			String responseCode = null
			def json = new JsonBuilder()
			json{
				"userId" USER_ID_TM
				"number" CommonVariable.DEFAULT_LANDLINE_PREFIX  + RANDOM_MOBILE
				"phoneType" "EMERGENCY_PHONE"
			}
			
			println "Json is " +  json.toString()
			println "********************************"
			println "Test running ..  Add Tradesman Phone"
		when:
			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				uri.path = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"+USER_ID_TM+"/phones"
				println "uri.path   :"+uri.path
				println "Access Token TM : "+ ACCESS_TOKEN_TM
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
				body = json.toString()
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{
						"Results  : "+ "$it"
					}
				}
	
				response.failure = { resp, reader ->
					responseCode = resp.statusLine.statusCode
					println " stacktrace : "+reader.each{"$it"}
				}
			}
		then:
			responseCode == CommonVariable.STATUS_201
		cleanup:
		DatabaseHelper.executeQuery("DELETE FROM tmprofile.phone WHERE phone_type='EMERGENCY_PHONE' and tm_profile_id=1");

	}
	
	
	
	def "Update TM Phone"() {
		given :
			String responseCode = null
			def  getId = DatabaseHelper.select("select id from tmprofile.phone where tm_profile_id =  1 and phone_type = 'OFFICE_PHONE'")
			if (getId.startsWith("[{id=")){
				getId = getId.replace("[{id=", "").replace("}]","")
				println "Profile trade id : " +getId
			}
			def json = new JsonBuilder()
			json{
				"userId" 1
				"number" CommonVariable.DEFAULT_LANDLINE_PREFIX  + RANDOM_MOBILE
				"phoneType" "EMERGENCY_PHONE"
			}
			println "Json is " +  json.toString()
			println "********************************"
			println "Test running ..  Update Tradesman Phone"
			
		when:
			HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
				uri.path = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"+1+"/phones/"+getId
				println "uri.path   :"+uri.path
				println "Access Token TM : "+ ACCESS_TOKEN_TM
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
				body = json.toString()
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{
						"Results  : "+ "$it"
					}
				}
	
				response.failure = { resp, reader ->
					responseCode = resp.statusLine.statusCode
					println " stacktrace : "+reader.each{"$it"}
				}
			}
		then:
			responseCode == CommonVariable.STATUS_200
		cleanup:
		DatabaseHelper.executeQuery("Update tmprofile.phone set phone_type='OFFICE_PHONE' where phone_type='EMERGENCY_PHONE' and tm_profile_id=1")
			
	}
	
}
