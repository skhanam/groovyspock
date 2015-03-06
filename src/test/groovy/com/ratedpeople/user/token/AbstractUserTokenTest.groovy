/**
 * 
 */
package com.ratedpeople.user.token

import groovy.json.*
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.http.entity.ContentType
import spock.lang.Specification
import com.ratedpeople.common.support.DataValues;

/**
 * @author shabana.khanam
 *
 */
class AbstractUserTokenTest extends Specification {

	protected static String ACCESS_TOKEN
	protected static String REFRESH_TOKEN
	protected static String USER_ID
	protected final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(DataValues.requestValues.get("URL"))
	
	private static final GET_TOKEN_URI = DataValues.requestValues.get("AUTHSERVICE") + 'oauth/token'
	private static final ME_URI = DataValues.requestValues.get("USERSERVICE") +"v1.0/me"
	
	def setup() {
		given:
			String responseCode = null
		when:
			HTTP_BUILDER.request(Method.POST){
				headers.Accept = 'application/json'
				headers.'Authorization' = "Basic "+ "9ecc8459ea5f39f9da55cb4d71a70b5d1e0f0b80:1".bytes.encodeBase64().toString()
				uri.path = GET_TOKEN_URI
				uri.query = [
					grant_type: DataValues.requestValues.get("PASSWORD"),
					username: DataValues.requestValues.get("USERNAME"),
					password: DataValues.requestValues.get("PASSWORD"),
					scope: 'all'
				]
				
				println "Uri is " + uri
				
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					
					responseCode = resp.statusLine.statusCode
					
					reader.each{
						println "Token values : "+"$it"
			
						String tokentemp = "$it"
						if (tokentemp.startsWith("access_token")){
							ACCESS_TOKEN = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
							println "token  " + ACCESS_TOKEN
						}
						if (tokentemp.startsWith("refresh_token")){
							REFRESH_TOKEN = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
							println "refreshtoken  " + REFRESH_TOKEN
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
						println "Token values : "+"$it"
			
						String user = "$it"
						if (user.startsWith("userId")){
							USER_ID = user.replace("userId=", "")
						}
					}
				}
				
				response.'404' = {
					println 'Not found'
				}
			}
		then:
			responseCode == DataValues.requestValues.get("STATUS200")
			ACCESS_TOKEN != null
			REFRESH_TOKEN != null
			USER_ID != null
	}
}
