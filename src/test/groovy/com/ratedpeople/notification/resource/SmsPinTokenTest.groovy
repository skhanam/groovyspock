/**
 * 
 */
package com.ratedpeople.notification.resource

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper
import com.ratedpeople.user.resource.AbstractUserToken
/**
 * @author shabana.khanam
 *
 */
class SmsPinTokenTest extends AbstractUserToken{
	
	private static final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(CommonVariable.SERVER_URL)
    private static String getpinToken
	
	def testRegeneratePINToken(){
		println "****************************************************"
		println "SmsPinToken"
		println "Test Running ............. :  testRegeneratePINToken"
		
		given:
			cleanDB()
			String responseStatus = null
		when:
			HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
				uri.path = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/users/" + USER_ID_HO + "/generatepin"
				println "uri path : "+uri.path
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_HO
				requestContentType = ContentType.JSON
				println "Uri is " + uri
	
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseStatus = resp.statusLine.statusCode
					reader.each{ "Results  : "+ "$it" }
				}
	
				response.failure = { resp, reader ->
					println "Request failed with status ${resp.status}"
					reader.each{ println "Error values : "+"$it" }
					responseStatus = resp.statusLine.statusCode
				}
			}
		then:
			responseStatus == CommonVariable.STATUS_200
	}

	private cleanDB() {
		def getphoneId = DatabaseHelper.select("select phone_id from hoprofile.ho_profile where user_id =  '${USER_ID_HO}'")
		getpinToken = DatabaseHelper.select("select pin_token from hoprofile.phone where id IN ( select phone_id from hoprofile.ho_profile where user_id = '${USER_ID_HO}')")
		if (getpinToken.startsWith("[{pin_token")){
			getpinToken = getpinToken.replace("[{pin_token=", "").replace("}]","")
			println "pin token is : " +getpinToken
		}
		DatabaseHelper.executeQuery("update hoprofile.phone set pin_generated_times= '0', pin_validation_times= '0' where pin_token = '${getpinToken}'")
	}


	def "testValidate pin"(){
		println "****************************************************"
		println "SmsPinToken"
		println "Test Running ............. :  testValidate pin"

		given:
			String responseStatus = null
			try{
				def getphoneId = DatabaseHelper.select("select phone_id from hoprofile.ho_profile where user_id =  '${USER_ID_HO}'")
				getpinToken = DatabaseHelper.select("select pin_token from hoprofile.phone where id IN ( select phone_id from hoprofile.ho_profile where user_id = '${USER_ID_HO}')")
				if (getpinToken.startsWith("[{pin_token")){
					getpinToken = getpinToken.replace("[{pin_token=", "").replace("}]","")
					println "pin token is : " +getpinToken
				}
			}catch(Exception e){
				println e.getMessage()
			}
			def json = new JsonBuilder()
			json{ "pinToken" getpinToken }
			println "Json is    : "+json.toString()
		when:
			HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
				uri.path = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/users/" + USER_ID_HO + "/validatepin"
				println "uri path : "+uri.path
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_HO
				body = json.toString()
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					responseStatus = resp.statusLine.statusCode
					println "Content-Type: ${resp.headers.'Content-Type'}"
					reader.each{ "Results  : "+ "$it" }
				}
	
				response.failure = { resp, reader ->
					println "Request failed with status ${resp.status}"
					reader.each{ println "Error values : "+"$it" }
					responseStatus = resp.statusLine.statusCode
				}
			}
		then:
			responseStatus ==  CommonVariable.STATUS_200
			cleanup:
			DatabaseHelper.executeQuery("update hoprofile.phone set pin_generated_times= '0' where pin_token = '${getpinToken}'")
	}
}
