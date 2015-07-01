/**
 * 
 */
package com.ratedpeople.tradesman.profile.resource


import com.ratedpeople.user.resource.AbstractTradesman;
import com.ratedpeople.user.resource.AbstractUserToken;
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.ByteArrayBody
import org.apache.http.entity.mime.content.FileBody

/**
 * @author shabana.khanam
 *
 */
class TMProfileAvailabilityFunctionalTest extends AbstractUserToken{

	private static final String PROFILE_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"
	
	private static final String MATCH_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/match"

	
	
	def "Update a Tradesman Availability"(){
		given:
				String responseCode = null
				def json = new JsonBuilder()
				json {
					"reacheableFlag" "true"
					}
				println "Json is " +  json.toString()
		when:
			println "********************************"
			println "Test Running .... Update Tradesman Availability"
				HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
					uri.path = PROFILE_PREFIX +USER_ID_TM  + "/availability/"
					headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
					body = json.toString()
					requestContentType = ContentType.JSON
					println "Uri is " + uri
					response.success = { resp, reader ->
						println "Success"
						println "Got response: ${resp.statusLine}"
						println "Content-Type: ${resp.headers.'Content-Type'}"
						responseCode = resp.statusLine.statusCode
						reader.each{ "Results  : "+ "$it" }
					}
					response.failure = { resp, reader ->
						println " stacktrace : "+reader.each{"$it"}
						println 'Not found'
						responseCode = resp.statusLine.statusCode
					}
				}
			then:
				responseCode == CommonVariable.STATUS_200
		}
		
		
		def "Get a Tradesman Availability"(){
		given:
				String responseCode = null
				
		when:
			println "********************************"
			println "Test Running .... Update TM Workingarea"
				HTTP_BUILDER.request(Method.GET,ContentType.JSON){
					uri.path = PROFILE_PREFIX +USER_ID_TM+"/availability/"
					headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
					requestContentType = ContentType.JSON
					println "Uri is " + uri
					response.success = { resp, reader ->
						println "Success"
						println "Got response: ${resp.statusLine}"
						println "Content-Type: ${resp.headers.'Content-Type'}"
						responseCode = resp.statusLine.statusCode
						reader.each{ "Results  : "+ "$it" }
					}
					response.failure = { resp, reader ->
						println " stacktrace : "+reader.each{"$it"}
						println 'Not found'
						responseCode = resp.statusLine.statusCode
					}
				}
			then:
				responseCode == CommonVariable.STATUS_200
		}
		
		
		
	
	
	
	
	
	
	
	
	
	
}
