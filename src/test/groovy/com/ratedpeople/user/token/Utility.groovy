/**
 * 
 */
package com.ratedpeople.user.token

/**
 * @author shabana.khanam
 *
 */
import groovy.json.*
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import spock.lang.Specification
import groovyx.net.http.ContentType

import com.ratedpeople.support.DataValues



class Utility  extends Specification{

	protected final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(DataValues.requestValues.get("URL"))
	protected static String USER_ID_DYNAMIC_HO
	
	

	
	
	public def createUser(def getUser,def uripath){
		
		String responseCode = null
		
		HTTP_BUILDER.handler.failure = { resp, reader ->
			[response:resp, reader:reader]
		}
		HTTP_BUILDER.handler.success = { resp, reader ->
			[response:resp, reader:reader]
		}
		def json = new JsonBuilder()
		json {
	
			"username" getUser
			"password" DataValues.requestValues.get("PASSWORD")
		}
		println "Json is " +  json.toString()
		println "********************************"
		println "Test Running ........  Create  User HO"
		println "uri path :"+uripath
		println "httpbuilder :"+HTTP_BUILDER
//		when:
		HTTP_BUILDER.request(Method.POST,ContentType.JSON){
			uri.path = uripath
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
		
		Utility.getUserId
//	then:
		responseCode == DataValues.requestValues.get("STATUS201")
	}
	
	
	
	
	
	
	
	
	
	public  def getUserId(String token)
	{
		HTTP_BUILDER.request(Method.GET){
			headers.Accept = 'application/json'
			headers.'Authorization' = "Bearer " + token
			uri.path = ME_URI
			
			println "Uri is " + uri
			
			response.success =
			{ resp, reader ->
				println "Success"
				responseCode = resp.statusLine.statusCode
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
						println "Admin Id  : "+ user
						
					}
					
				}
			}
			
			response.failure = { resp ->
				println "Request failed with status ${resp.status}"
				println resp.toString()
				println resp.statusLine.statusCode
			}
		}
//	then:
		assert responseCode == DataValues.requestValues.get("STATUS200")
	
	}
	

}
