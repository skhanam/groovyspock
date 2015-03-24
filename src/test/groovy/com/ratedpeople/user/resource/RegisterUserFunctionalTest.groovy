/**
 * 
 */
package com.ratedpeople.user.resource

import groovy.json.*
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import spock.lang.Specification
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
	
	
	
}
