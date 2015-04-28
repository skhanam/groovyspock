/**
 * 
 */
package com.ratedpeople.user.resource

import groovy.json.*
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import spock.lang.Specification
import groovyx.net.http.ContentType

import com.ratedpeople.support.DataValues
/**
 * @author shabana.khanam
 *
 */
class AbstractHomeowner  extends Specification{
	
	long randomMobile = Math.round(Math.random()*1000);
	private static final String REGISTER_USER_HO_URI = DataValues.requestValues.get("USERSERVICE")+"v1.0/homeowners/register"
	private final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(DataValues.requestValues.get("URL"))
	private static final GET_TOKEN_URI = DataValues.requestValues.get("AUTHSERVICE") + 'oauth/token'
	protected static String ACCESS_TOKEN_DYNAMIC_HO
	protected static String REFRESH_TOKEN_DYNAMIC_HO
	protected static String USER_ID_DYNAMIC_HO
	protected static String ACCESS_TOKEN_ADMIN
	protected static String REFRESH_TOKEN_ADMIN
	private static final STATUS_URI = DataValues.requestValues.get("USERSERVICE")+"v1.0/homeowners/"
	protected static String DYNAMIC_USER
	
	private static final ME_URI = DataValues.requestValues.get("USERSERVICE") +"v1.0/me"
	
	
	def "setup"(){
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
	
	
	
	private def createJsonUser(){
				def json = new JsonBuilder()
				DYNAMIC_USER = DataValues.requestValues.get("HOUSER")+System.currentTimeMillis()+"@gid.com"
				json {
						"email" DYNAMIC_USER
						"password" DataValues.requestValues.get("PASSWORD")
						"firstName" "hoprofile"
						"lastName"  "Aws"
						"phoneNumber" DataValues.requestValues.get("PHONE")+randomMobile
						
				}
				
				println "Json is ${json.toString()}"
				return json;
		}
	
	
		private def createUser(def json)
		{
			def map = HTTP_BUILDER.request(Method.POST,ContentType.JSON) {
				uri.path = REGISTER_USER_HO_URI
				body = json.toString()
				requestContentType = ContentType.JSON
				headers.Accept = ContentType.JSON
	  			println "Post user Uri : " + uri
			}
			return map;
		}
	

	
	private def authToken(){
		String token = null
		HTTP_BUILDER.request(Method.POST){
			headers.Accept = 'application/json'
			headers.'Authorization' = "Basic "+ DataValues.requestValues.get("CLIENT_ID").bytes.encodeBase64().toString()
			uri.path = GET_TOKEN_URI
			uri.query = [
				grant_type: DataValues.requestValues.get("PASSWORD"),
				username: DYNAMIC_USER,
				password:DataValues.requestValues.get("PASSWORD") ,
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
							ACCESS_TOKEN_DYNAMIC_HO = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
							println "Access Token Dynamic HO : " + ACCESS_TOKEN_DYNAMIC_HO
						}
					if (tokentemp.startsWith("refresh_token"))
					{
						REFRESH_TOKEN_DYNAMIC_HO = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
							println "Refresh Token Dynamic HO : " + REFRESH_TOKEN_DYNAMIC_HO
					}
					if (tokentemp.startsWith("user_id"))
					{
						tokentemp = tokentemp.replace("user_id=", "")
						println "User values : " +tokentemp
						USER_ID_DYNAMIC_HO = tokentemp
						println "Dynamic Id second iteration  : "+ USER_ID_DYNAMIC_HO
						
					}
					
				}
			}
			
			
			response.'404' = {
				println 'Not found'
			}
		}
	}
	
	
	
	private def getUserId()
	{
		HTTP_BUILDER.request(Method.GET){
			headers.Accept = 'application/json'
			headers.'Authorization' = "Bearer " + ACCESS_TOKEN_DYNAMIC_HO
			uri.path = ME_URI
			
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
						USER_ID_DYNAMIC_HO = user
						println "Dynamic Id  : "+ user
						
					}
					
				}
			}
			
			response.failure = { resp ->
				println "Request failed with status ${resp.status}"
				println resp.toString()
				println resp.statusLine.statusCode
			}
		}
	
	}
	
	
	private def getAdmin(){
		String token = null
		HTTP_BUILDER.request(Method.POST){
			headers.Accept = 'application/json'
			headers.'Authorization' = "Basic "+ DataValues.requestValues.get("CLIENT_ID").bytes.encodeBase64().toString()
			uri.path = GET_TOKEN_URI
			uri.query = [
				grant_type: DataValues.requestValues.get("PASSWORD"),
				username: DataValues.requestValues.get("ADMIN_USER"),
				password:DataValues.requestValues.get("PASSWORD") ,
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
			
			
			response.'404' = {
				println 'Not found'
			}
		}
		
	}

   private def changeStatus()
   {
	   String token = null
	   HTTP_BUILDER.request(Method.PUT){
		   headers.Accept = 'application/json'
		   headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_ADMIN
		   uri.path = STATUS_URI+USER_ID_DYNAMIC_HO+"/status"
		   uri.query = [
			   status: DataValues.requestValues.get("HOSTATUS1"),
			   ]
		   println "Uri is " + uri
		   response.success =
		   { resp, reader ->
			   println "Success"
			   println "Got response: ${resp.statusLine}"
			   println "Content-Type: ${resp.headers.'Content-Type'}"
			   reader.each
			   {
				   println "Response data: status change "+"$it"
	   		   }
		   }
		   response.'404' = {
			   println 'Not found'
		   }
	   }
   }
   
   
}
