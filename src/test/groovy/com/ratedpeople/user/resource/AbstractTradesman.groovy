/**
 * 
 */
package com.ratedpeople.user.resource

import groovy.json.*
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import spock.lang.Specification
import com.ratedpeople.support.CommonVariable

/**
 * @author shabana.khanam
 *
 */
class AbstractTradesman extends Specification{
	
	public static final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(CommonVariable.SERVER_URL)
	
	private static final String REGISTER_USER_TM_URI = CommonVariable.USER_SERVICE_PREFIX + "v1.0/tradesmen/register"
	private static final STATUS_URI = CommonVariable.USER_SERVICE_PREFIX + "v1.0/tradesmen/"
	protected static long RANDOM_MOBILE = Math.round(Math.random()*100000000);
	
	protected static String ACCESS_TOKEN_DYNAMIC_TM
	protected static String REFRESH_TOKEN_DYNAMIC_TM
	protected static String USER_ID_DYNAMIC_TM
	protected static String ACCESS_TOKEN_ADMIN
	protected static String REFRESH_TOKEN_ADMIN
	protected static String DYNAMIC_USER
		
	def "setupSpec"(){
		String responseStatus
		
		def response = createUser(createJsonUser());
		
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
	
		}
		
		authToken()
		getUserId()
		getAdmin()
		changeStatus()
		authToken()
	}				
	
	private static def createJsonUser(){
		def json = new JsonBuilder()
		DYNAMIC_USER = CommonVariable.TM_USER_PREFIX + System.currentTimeMillis()+"@gid.com"
		json {
			"email" DYNAMIC_USER
			"password" CommonVariable.DEFAULT_PASSWORD
			"firstName" "tmprofile"
			"lastName"  "Aws"
			"phoneNumber" CommonVariable.DEFAULT_MOBILE_PREFIX + RANDOM_MOBILE
		}
		
		println "Json is ${json.toString()}"
		return json;
	}

	private static def createUser(def json){
		def map = HTTP_BUILDER.request(Method.POST,ContentType.JSON) {
			headers.'Authorization' = "Basic "+ CommonVariable.DEFAULT_CLIENT_CREDENTIAL.bytes.encodeBase64().toString()
			uri.path = REGISTER_USER_TM_URI
			body = json.toString()
			requestContentType = ContentType.JSON
			headers.Accept = ContentType.JSON
  			println "Post user Uri : " + uri
		}
		return map;
	}

	private static def authToken(){
		String token = null
		HTTP_BUILDER.request(Method.POST){
			headers.Accept = 'application/json'
			headers.'Authorization' = "Basic "+ CommonVariable.DEFAULT_CLIENT_CREDENTIAL.bytes.encodeBase64().toString()
			uri.path = CommonVariable.DEFAULT_GET_TOKEN_URI
			uri.query = [
				grant_type: CommonVariable.DEFAULT_PASSWORD,
				username: DYNAMIC_USER,
				password:CommonVariable.DEFAULT_PASSWORD ,
				scope: 'all'
			]
			
			println "Uri is " + uri
			
			response.success = { resp, reader ->
				println "Success"
				println "Got response: ${resp.statusLine}"
				println "Content-Type: ${resp.headers.'Content-Type'}"
				reader.each {
					println "Response data: "+"$it"
		
					String tokentemp = "$it"
					if (tokentemp.startsWith("access_token"))
					{
						token = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
							ACCESS_TOKEN_DYNAMIC_TM = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
							println "Access Token Dynamic HO : " + ACCESS_TOKEN_DYNAMIC_TM
						}
					if (tokentemp.startsWith("refresh_token"))
					{
						REFRESH_TOKEN_DYNAMIC_TM = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
							println "Refresh Token Dynamic HO : " + REFRESH_TOKEN_DYNAMIC_TM
					}
					if (tokentemp.startsWith("user_id"))
					{
						tokentemp = tokentemp.replace("user_id=", "")
						println "User values : " +tokentemp
						USER_ID_DYNAMIC_TM = tokentemp
						println "Dynamic Id second iteration  : "+ USER_ID_DYNAMIC_TM
						
					}
					
				}
			}
			
			response.failure = { resp, reader -> 
				println " stacktrace : "+reader.each{"$it"}
			}
		}
	}
	
	
	
	private static def getUserId()
	{
		HTTP_BUILDER.request(Method.GET){
			headers.Accept = 'application/json'
			headers.'Authorization' = "Bearer " + ACCESS_TOKEN_DYNAMIC_TM
			uri.path = CommonVariable.DEFAULT_ME_URI
			
			println "Uri is " + uri
			
			response.success =
			{ resp, reader ->
				println "Success"
				println "Got response: ${resp.statusLine}"
				println "Content-Type: ${resp.headers.'Content-Type'}"
				
				reader.each
				{
					println "Response data: " + "$it"
		
					String user = "$it"
					if (user.startsWith("userId"))
					{
						user = user.replace("userId=", "")
						println "User values : " +user
						USER_ID_DYNAMIC_TM = user
						println "Dynamic Id  : "+ user
						
					}
					
				}
			}
			
			response.failure = { resp, reader->
				println "Request failed with status ${resp.status}"
				println resp.toString()
				println resp.statusLine.statusCode
				println " stacktrace : "+reader.each{"$it"}
			}
		}
	
	}
	
	
	private static def getAdmin(){
		String token = null
		HTTP_BUILDER.request(Method.POST){
			headers.Accept = 'application/json'
			headers.'Authorization' = "Basic "+ CommonVariable.DEFAULT_CLIENT_CREDENTIAL.bytes.encodeBase64().toString()
			uri.path = CommonVariable.DEFAULT_GET_TOKEN_URI
			uri.query = [
				grant_type: CommonVariable.DEFAULT_PASSWORD,
				username: CommonVariable.DEFAULT_ADMIN_USERNAME,
				password:CommonVariable.DEFAULT_PASSWORD ,
				scope: 'all'
			]
			println "Uri is " + uri
			response.success =
			{ resp, reader ->
				println "Success"
				println "Got response: ${resp.statusLine}"
				println "Content-Type: ${resp.headers.'Content-Type'}"
				reader.each
				{
					println "Response data: "+"$it"
		
					String tokentemp = "$it"
					if (tokentemp.startsWith("access_token"))
					{
						token = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
							ACCESS_TOKEN_ADMIN = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
							println "Access Token Admin : " + ACCESS_TOKEN_ADMIN
						}
					if (tokentemp.startsWith("refresh_token"))
					{
						REFRESH_TOKEN_ADMIN = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
							println "Refresh Token Admin : " + REFRESH_TOKEN_ADMIN
					}
				}
			}
			
			
			response.failure = { resp, reader->
				println "Request failed with status ${resp.status}"
				println resp.toString()
				println resp.statusLine.statusCode
				println " stacktrace : "+reader.each{"$it"}
			}
		}
		
	}

   private static def changeStatus()  {
	   String token = null
	   HTTP_BUILDER.request(Method.PUT){
		   headers.Accept = 'application/json'
		   headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_ADMIN
		   uri.path = STATUS_URI+USER_ID_DYNAMIC_TM+"/status"
		   uri.query = [
			   status: CommonVariable.STATUS_ACTIVE,
			   ]
		   println "Uri is " + uri
		   
		   response.success = { resp, reader ->
			   println "Success"
			   println "Got response: ${resp.statusLine}"
			   println "Content-Type: ${resp.headers.'Content-Type'}"
			   reader.each
			   {
				   println "Response data: status change "+"$it"
	   		   }
		   }
		   
		   response.failure = { resp, reader->
				println "Request failed with status ${resp.status}"
				println resp.toString()
				println resp.statusLine.statusCode
				println " stacktrace : "+reader.each{"$it"}
			}
	   }
   }
}
