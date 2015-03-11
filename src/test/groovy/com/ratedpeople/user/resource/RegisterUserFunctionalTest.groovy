/**
 * 
 */
package com.ratedpeople.user.resource

import groovy.json.*
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import spock.lang.Specification
import com.ratedpeople.support.DataValues
/**
 * @author shabana.khanam
 *
 */

class RegisterUserFunctionalTest extends Specification {

	public static String  gettoken = DataValues.requestValues.get("TOKEN").bytes.encodeBase64().toString()
	public static String USERNAME = DataValues.requestValues.get("USERNAME")+System.currentTimeMillis();
	
	private static final String REGISTER_USER_URI = DataValues.requestValues.get("USERSERVICE")+"v1.0/user/register"
	private final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(DataValues.requestValues.get("URL"))
	
	def "Create  User"(){
		given:
			String responseCode = null
		when:
			HTTP_BUILDER.request(Method.POST){
				headers.Accept = 'application/json'
				uri.path = REGISTER_USER_URI
				uri.query = [
					username: DataValues.requestValues.get("USERNAME_NEW") + new Date(),
					password: DataValues.requestValues.get("PASSWORD"),
				]
				
				println "Uri is " + uri
				
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
				}

				response.'404' = {
					println 'Not found'
				}
			}
		then:
			responseCode == DataValues.requestValues.get("STATUS200")
	}
}
