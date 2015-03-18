/**
 * 
 */
package com.ratedpeople.user.token

import groovy.json.*
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import spock.lang.Specification
import com.ratedpeople.support.DataValues

/**
 * @author shabana.khanam
 *
 */
class AbstractUserToken extends Specification {

	protected static String ACCESS_TOKEN
	protected static String REFRESH_TOKEN
	protected static String USER_ID
	protected final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(DataValues.requestValues.get("URL"))
	
	private static final GET_TOKEN_URI = DataValues.requestValues.get("AUTHSERVICE") + 'oauth/token'
	private static final ME_URI = DataValues.requestValues.get("USERSERVICE") +"v1.0/me"
	
	def "some tests"() {
		given:
			String responseCode = null
			
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
//					username: DataValues.requestValues.get("USERNAME"),
					username: userName,
//					password:DataValues.requestValues.get("PASSWORD") ,
					password:passWord,
					scope: 'all'
				]
				
//				println "username is : "+username
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
							ACCESS_TOKEN = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
							println "Access Token: " + ACCESS_TOKEN
						}
						if (tokentemp.startsWith("refresh_token")){
							REFRESH_TOKEN = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
							println "Refresh Token: " + REFRESH_TOKEN
						}
					}
				}
				
				response.'404' = {
					println 'Not found'
				}
			}
			
			HTTP_BUILDER.request(Method.GET){
				headers.Accept = 'application/json'
				headers.'Authorization' = "Bearer " + ACCESS_TOKEN
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
							USER_ID = user.replace("userId=", "")
						}
					}
				}
				
				response.failure = { resp ->
					println "Request failed with status ${resp.status}"
					println resp.toString()
					println resp.statusLine.statusCode
				}
			}
		then:
			assert responseCode == DataValues.requestValues.get("STATUS200")
			ACCESS_TOKEN != null
			REFRESH_TOKEN != null
			USER_ID != null
			
		where:
		
		userName|passWord
		
		DataValues.requestValues.get("USERNAME")|DataValues.requestValues.get("PASSWORD")
		DataValues.requestValues.get("USERNAME_HO")|DataValues.requestValues.get("PASSWORD")
			
	}
	
	
}
