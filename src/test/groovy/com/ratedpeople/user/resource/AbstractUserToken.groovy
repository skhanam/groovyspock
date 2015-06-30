/**
 * 
 */
package com.ratedpeople.user.resource

import groovy.json.*
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import spock.lang.Specification
import com.ratedpeople.support.CommonVariable

/**
 * @author shabana.khanam
 *
 */
class AbstractUserToken extends Specification {

	public static final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(CommonVariable.SERVER_URL)
	
	protected static String ACCESS_TOKEN_TM
	protected static String REFRESH_TOKEN_TM
	protected static String USER_ID_TM
	protected static String ACCESS_TOKEN_HO
	protected static String REFRESH_TOKEN_HO
	protected static String USER_ID_HO
	protected static String ACCESS_TOKEN_ADMIN
	protected static String REFRESH_TOKEN_ADMIN
	protected static String USER_ID_ADMIN
	protected static long RANDOM_MOBILE = Math.round(Math.random()*100000000)+100000000;
	
	
	def "setupSpec"(){
	
		String [] userName = new String[2];
		userName[0] = CommonVariable.DEFAULT_TM_USERNAME
		userName[1]	= CommonVariable.DEFAULT_HO_USERNAME

			String responseCode = null
			String token = null
			
			HTTP_BUILDER.handler.failure = { resp, reader ->
				[response:resp, reader:reader]
			}
			HTTP_BUILDER.handler.success = { resp, reader ->
				[response:resp, reader:reader]
			}

		for(int i=0; i<2;i++){
			HTTP_BUILDER.request(Method.POST){
				headers.Accept = 'application/json'
				headers.'Authorization' = "Basic "+ CommonVariable.DEFAULT_CLIENT_CREDENTIAL.bytes.encodeBase64().toString()
				uri.path = CommonVariable.DEFAULT_GET_TOKEN_URI
				uri.query = [
					grant_type: CommonVariable.DEFAULT_PASSWORD,
					username: userName[i],
					password: CommonVariable.DEFAULT_PASSWORD ,
					scope: 'all'
				]
				
				println "username : "+ userName[i]
				println "Uri is " + uri
				
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					
					responseCode = resp.statusLine.statusCode
					
					reader.each{
						println "Response data: "+"$it"
			
						String tokentemp = "$it"
						if (tokentemp.startsWith("access_token")){
							println "userName"+userName[i]
							println " :" + userName.equals(CommonVariable.DEFAULT_TM_USERNAME)
							token = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
							
							if(userName[i].equals(CommonVariable.DEFAULT_TM_USERNAME)){
								ACCESS_TOKEN_TM = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
								println "Access Token TM: " + ACCESS_TOKEN_TM
							}else {
								ACCESS_TOKEN_HO = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
								println "Access Token HO: " + ACCESS_TOKEN_HO
							}
							
						}
						if (tokentemp.startsWith("refresh_token")){
							
							if(userName[i].equals(CommonVariable.DEFAULT_TM_USERNAME)){
								REFRESH_TOKEN_TM = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
								println "Refresh Token TM: " + REFRESH_TOKEN_TM
							}else {
								REFRESH_TOKEN_HO = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
								println "Refresh Token HO: " + REFRESH_TOKEN_HO
							}
						}
					}
				}
				
				response.failure = { resp, reader -> 
					println " stacktrace : "+reader.each{"$it"}
				}
			}
			
			HTTP_BUILDER.request(Method.GET){
				headers.Accept = 'application/json'
				headers.'Authorization' = "Bearer " + token
				uri.path = CommonVariable.DEFAULT_ME_URI
				
				println "Uri is " + uri
				
				response.success = { resp, reader ->
					println "Success"
					responseCode = resp.statusLine.statusCode
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					
					reader.each{
						println "Response data: " + "$it"
			
						String user = "$it"
						if (user.startsWith("user_id")){
							user = user.replace("user_id=", "")
							println "User values : " +user
							if(userName[i].equals(CommonVariable.DEFAULT_TM_USERNAME)){
								USER_ID_TM = user
								println "User ID TM: " + USER_ID_TM
							}else {
								USER_ID_HO = user
								println "User ID HO: " + USER_ID_HO
							}		
						}
						
					}
				}
				
				response.failure = { resp, reader ->
					println "Request failed with status ${resp.status}"
					println resp.toString()
					println " stacktrace : "+reader.each{"$it"}
					println resp.statusLine.statusCode
				}
			}

			assert responseCode == CommonVariable.STATUS_200
			println "ACCESS_TOKEN_TM  :"+ ACCESS_TOKEN_TM
			assert ACCESS_TOKEN_TM != null
			println "REFRESH_TOKEN_TM  :"+REFRESH_TOKEN_TM
			assert REFRESH_TOKEN_TM != null
			println "USER_ID_TM  :"+USER_ID_TM
			assert USER_ID_TM != null
		}	
	}
}

