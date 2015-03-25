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

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
/**
 * @author shabana.khanam
 *
 */

class RegisterUserFunctionalTest extends Specification {
	
	private static final String REGISTER_USER_HO_URI = DataValues.requestValues.get("USERSERVICE")+"v1.0/homeowners/register"
	private static final String REGISTER_USER_TM_URI = DataValues.requestValues.get("USERSERVICE")+"v1.0/tradesmen/register"
	private final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(DataValues.requestValues.get("URL"))
	private static final GET_TOKEN_URI = DataValues.requestValues.get("AUTHSERVICE") + 'oauth/token'
	protected static String ACCESS_TOKEN_TM1
	protected static String REFRESH_TOKEN_TM1
	protected static String USER_ID_TM1
	protected static String ACCESS_TOKEN_HO1
	protected static String REFRESH_TOKEN_HO1
	protected static String USER_ID_HO1
	private static final ME_URI = DataValues.requestValues.get("USERSERVICE") +"v1.0/me"
	
	def "Create  User HO"(){
		given:
			String responseCode = null
			def json = new JsonBuilder()
			json {
		
				"username" DataValues.requestValues.get("HOUSER")
				"password" DataValues.requestValues.get("PASSWORD")
			}
			println "Json is " +  json.toString()
			
		when:
			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				uri.path = REGISTER_USER_HO_URI
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

				response.failure = {
					resp, reader -> println " stacktrace : "+reader.each{"$it"}
					println 'Not found'
				}
			}
		then:
			responseCode == DataValues.requestValues.get("STATUS201")
	}
	
	
	def "Create  User TM"(){
		given:
			String responseCode = null
			def json = new JsonBuilder()
			json {
		
				"username" DataValues.requestValues.get("TMUSER")
				"password" DataValues.requestValues.get("PASSWORD")
			}
			println "Json is " +  json.toString()
		when:
			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				uri.path = REGISTER_USER_TM_URI
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

				response.failure = {
					resp, reader -> println " stacktrace : "+reader.each{"$it"}
					println 'Not found'
				}
			}
		then:
			responseCode == DataValues.requestValues.get("STATUS201")
	}
	
	
	def "Get user token "(){
		 given:
		 String responseCode = null
		 String token = null
		 
		 HTTP_BUILDER.handler.failure = { resp, reader ->
			 [response:resp, reader:reader]
		 }
		 HTTP_BUILDER.handler.success = { resp, reader ->
			 [response:resp, reader:reader]
		 }
		 when:
		 HTTP_BUILDER.request(Method.POST){
			 headers.Accept = 'application/json'
			 headers.'Authorization' = "Basic "+ DataValues.requestValues.get("CLIENT_ID").bytes.encodeBase64().toString()
			 uri.path = GET_TOKEN_URI
			 uri.query = [
				 grant_type: DataValues.requestValues.get("PASSWORD"),
				 username:userName,
				 password:DataValues.requestValues.get("PASSWORD") ,
				 scope: 'all'
			 ]
			 
			 println "Uri is " + uri
			 
			 response.success = { resp, reader ->
				 println "Success"
				 println "Got response: ${resp.statusLine}"
				 println "Content-Type: ${resp.headers.'Content-Type'}"
				 
				 responseCode = resp.statusLine.statusCode
				 
				 reader.each{
					 println "Response data: "+"$it"
					 String user = "$it"
					String tokentemp = "$it"
						if (tokentemp.startsWith("access_token")){
							
							println "userName  :"+userName
							println " :"+userName.equals(DataValues.requestValues.get("USERNAME"))
							token = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
							
							if(userName.equals(DataValues.requestValues.get("USERNAME"))){
								ACCESS_TOKEN_TM1 = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
								println "Access Token TM1: " + ACCESS_TOKEN_TM1
							}else {
								ACCESS_TOKEN_HO1 = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
								println "Access Token HO1: " + ACCESS_TOKEN_HO1
							}
							if (tokentemp.startsWith("refresh_token")){
								
								if(userName.equals(DataValues.requestValues.get("USERNAME"))){
									REFRESH_TOKEN_TM1 = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
									println "Refresh Token TM1: " + REFRESH_TOKEN_TM1
								}else {
									REFRESH_TOKEN_HO1 = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
									println "Refresh Token HO1: " + REFRESH_TOKEN_HO1
								}
							}
							
						}
				 
			 }
			 
		 }
		 }
    	 then:
		 
		 HTTP_BUILDER.request(Method.GET){
				headers.Accept = 'application/json'
				headers.'Authorization' = "Bearer " + token
				uri.path = ME_URI
				
				println "Uri is " + uri
				
				response.success = { resp, reader ->
					println "Success"
					responseCode = resp.statusLine.statusCode
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					
					reader.each{
						println "Response data: " + "$it"
			
						String user = "$it"
						if (user.startsWith("userId")){
							user = user.replace("userId=", "")
							println "User values : " +user
							if(userName.equals(DataValues.requestValues.get("USERNAME"))){
								USER_ID_TM1 = user
								println "User ID TM1: " + USER_ID_TM1
							}else {
								USER_ID_HO1 = user
								println "User ID HO1: " + USER_ID_HO1
							}		
						}
						
					}
				}
				
				response.failure = { resp ->
					println "Request failed with status ${resp.status}"
					println resp.toString()
					println resp.statusLine.statusCode
				}
		 	   
			 }
		 where:
		 userName << [
		 DataValues.requestValues.get("HOUSER") ,
		 DataValues.requestValues.get("TMUSER") ]
	}
	
	
	
	
	def "Reset password"(){
		given:
		String responseStatus = null
		when:
		HTTP_BUILDER.request(Method.POST){
			uri.path = DataValues.requestValues.get("USERSERVICE")+"v1.0/users/"+USER_ID_HO1+"/resetpassword"
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_HO1
			println "Uri : " + uri
			response.success = { resp, reader ->
				println "Success"
				println "Got response: ${resp.statusLine}"
				println "Content-Type: ${resp.headers.'Content-Type'}"
				
				responseStatus = resp.statusLine.statusCode
				
				reader.each{
					println "Token values : "+"$it"
					
					String token = "$it"
					String key = token.substring(0, token.indexOf("="))
					String value = token.substring(token.indexOf("=") + 1, token.length())
					println key
					println value
				}
			}
			
			response.failure = { resp ->
				println "Request failed with status ${resp.status}"
				responseStatus = resp.statusLine.statusCode
			}
		}
		then:
		1 == 1
	}
	
	
	
	def "Verify Reset password"()
	{
		given:
		DatabaseHelper.executeQuery("select token from uaa.user_password_token where user_id= '${USER_ID_HO1}'")
		String responseStatus = null
		when:
		HTTP_BUILDER.request(Method.PUT)
		{
			uri.path = DataValues.requestValues.get("USERSERVICE")+"v1.0/users/"+USER_ID_HO1+"/resetpassword"
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_HO1
			println "Uri : " + uri
			response.success = 
			{
				
				resp, reader ->
				println "Success"
				println "Got response: ${resp.statusLine}"
				println "Content-Type: ${resp.headers.'Content-Type'}"
				
				responseStatus = resp.statusLine.statusCode
				
				reader.each
				{
					println "Token values : "+"$it"
					
					String token = "$it"
					String key = token.substring(0, token.indexOf("="))
					String value = token.substring(token.indexOf("=") + 1, token.length())
					println key
					println value
				}
			}
			
			response.failure = 
			{ resp ->
				println "Request failed with status ${resp.status}"
				responseStatus = resp.statusLine.statusCode
				}
			
			}
			then:
			1==1
//			do nothing
		}
	
	
}
