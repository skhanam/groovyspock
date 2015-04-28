/**
 * 
 */
package com.ratedpeople.user.resource

import groovy.json.*
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import spock.lang.Specification

import com.ratedpeople.support.DatabaseHelper
import com.ratedpeople.support.DataValues
import com.ratedpeople.user.token.AbstractHomeowner;

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
/**
 * @author shabana.khanam
 *
 */

class RegisterUserFunctionalTest extends AbstractHomeowner {
	
	private static final String REGISTER_USER_HO_URI = DataValues.requestValues.get("USERSERVICE")+"v1.0/homeowners/register"
	private static final String REGISTER_USER_TM_URI = DataValues.requestValues.get("USERSERVICE")+"v1.0/tradesmen/register"
	private final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(DataValues.requestValues.get("URL"))
	private static final GET_TOKEN_URI = DataValues.requestValues.get("AUTHSERVICE") + 'oauth/token'
	private static String getToken
	private static final ME_URI = DataValues.requestValues.get("USERSERVICE") +"v1.0/me"
	
//	def "Create  User TM"()
//	{
//		given:
//			String responseCode = null
//			def json = new JsonBuilder()
//			json {
//		
//				"username" DataValues.requestValues.get("TMUSER")
//				"password" DataValues.requestValues.get("PASSWORD")
//			}
//			println "Json is " +  json.toString()
//			println "********************************"
//			println "Test Running ........  Create  User TM"
//			
//		when:
//			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
//				uri.path = REGISTER_USER_TM_URI
//				body = json.toString()
//				requestContentType = ContentType.JSON
//				println "Uri is " + uri
//				
//				response.success = { resp, reader ->
//					println "Success"
//					println "Got response: ${resp.statusLine}"
//					println "Content-Type: ${resp.headers.'Content-Type'}"
//					responseCode = resp.statusLine.statusCode
//					reader.each{
//						"Results  : "+ "$it"
//					}
//				}
//
//				response.failure = {
//					resp, reader -> println " stacktrace : "+reader.each{"$it"}
//					println 'Not found'
//				}
//			}
//		then:
//			responseCode == DataValues.requestValues.get("STATUS201")
//	}
	
	
		def "Generate and Verify Reset password"(){
			given:
				String responseStatus = null
				println "********************************"
				println "Test Running ........  Reset password"
			when:
				HTTP_BUILDER.request(Method.POST){
					headers.Accept = 'application/json'
					uri.path = DataValues.requestValues.get("USERSERVICE")+"v1.0/users/"+USER_ID_DYNAMIC_HO+"/resetpassword"
					
					println "Uri : " + uri
					
					response.success = { resp, reader ->
						println "Success"
						println "Got response: ${resp.statusLine}"
						println "Content-Type: ${resp.headers.'Content-Type'}"
						
						responseStatus = resp.statusLine.statusCode
		
					}
					
					response.failure = { 
						resp, reader -> println " stacktrace : "+reader.each{"$it"}
						println 'Not found'
					}
				}
						
				try{
					getToken = DatabaseHelper.select("select token from uaa.user_password_token where user_id= '${USER_ID_DYNAMIC_HO}'")
					println "********************************"
					println "Printing Token in Verify Reset Password  :"+getToken
					if (getToken.startsWith("[{token")){
						getToken = getToken.replace("[{token=", "").replace("}]","")
						println "token is : " +getToken
					}
				}catch(Exception e){
					println e.getMessage()
				}
			
				println "********************************"
				println "Test Running ........  Verify Reset password"
			
				HTTP_BUILDER.request(Method.PUT){
					headers.Accept = 'application/json'
					uri.path = DataValues.requestValues.get("USERSERVICE")+"v1.0/users/"+USER_ID_DYNAMIC_HO+"/resetpassword"
					uri.query = [
						token : getToken,
						password : DataValues.requestValues.get("PASSWORD")
					]
					
					println "Uri : " + uri
					
					response.success = {
						resp, reader ->
						println "Success"
						println "Got response: ${resp.statusLine}"
						println "Content-Type: ${resp.headers.'Content-Type'}"
						
						responseStatus = resp.statusLine.statusCode
					}
					responseStatus == DataValues.requestValues.get("STATUS201")
				}
			then:
				responseStatus == DataValues.requestValues.get("STATUS200")
		}
}
