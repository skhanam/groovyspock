/**
 * 
 */
package com.ratedpeople.notification.resource

import com.ratedpeople.user.resource.AbstractUserToken;
import groovyx.net.http.HTTPBuilder;
import groovyx.net.http.ContentType
import com.ratedpeople.support.DataValues
import groovyx.net.http.Method
import groovy.json.JsonBuilder
import com.ratedpeople.support.DatabaseHelper
/**
 * @author shabana.khanam
 *
 */
class SmsPinTokenTest extends AbstractUserToken{
	
	private final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(DataValues.requestValues.get("URL"))
    private static String getpinToken
	
	def testRegeneratePINToken(){
		println "****************************************************"
		println "SmsPinToken"
		println "Test Running ............. :  testRegeneratePINToken"
		
		given:
		String responseStatus = null
		
		
		when:
		HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
						uri.path = DataValues.requestValues.get("PROFILESERVICE")+"v1.0/users/"+USER_ID_HO+"/generatepin"
						println "uri path : "+uri.path
						headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_HO
						requestContentType = ContentType.JSON
						println "Uri is " + uri
						
						response.success = { resp, reader ->
							println "Success"
							println "Got response: ${resp.statusLine}"
							println "Content-Type: ${resp.headers.'Content-Type'}"
							responseStatus = resp.statusLine.statusCode
							reader.each{
								"Results  : "+ "$it"
							}
						}
			
						response.failure = {
							resp, reader -> println " stacktrace : "+reader.each{"$it"}
							println 'Not found'
						}
					}
				then:
					responseStatus == DataValues.requestValues.get("STATUS200")

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
		json{
			"pinToken" getpinToken
		}
		println "Json is    : "+json.toString()
		when:
		HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
						uri.path = DataValues.requestValues.get("PROFILESERVICE")+"v1.0/users/"+USER_ID_HO+"/validatepin"
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
							reader.each{
								"Results  : "+ "$it"
							}
						}
			
						response.failure = {
							resp, reader -> println " stacktrace : "+reader.each{"$it"}
							println 'Not found'
						}
					}
				then:
					responseStatus == DataValues.requestValues.get("STATUS200")
		cleanup:
		DatabaseHelper.executeQuery("update hoprofile.phone set pin_generated_times= '0' where pin_token = '${getpinToken}'")			
	
	}

}
