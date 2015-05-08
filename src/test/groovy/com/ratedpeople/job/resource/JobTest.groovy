/**
 * 
 */
package com.ratedpeople.job.resource

import com.ratedpeople.user.resource.AbstractHomeowner;

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.Method

import com.ratedpeople.support.CommonVariable;
import com.ratedpeople.support.DatabaseHelper

import groovyx.net.http.HTTPBuilder

/**
 * @author shabana.khanam
 *
 */
class JobTest extends AbstractHomeowner{

	private final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(CommonVariable.SERVER_URL)
	protected static long RANDOM_MOBILE = Math.round(Math.random()*100000000);
	private static final String JOB_URI_PREFIX = CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/"
	
	
	def "post a job"(){
		given:
				String responseStatus = null
				def json = new JsonBuilder()
				json {
					"homeownerUserId" USER_ID_DYNAMIC_HO
					"tradesmanUserId" CommonVariable.DEFAULT_TM_ID
					"name" CommonVariable.NAME
					"description"  CommonVariable.DEFAULT_DESCRIPTION
					"hourRate" CommonVariable.DEFAULT_HOURRATE
					"jobContactDetails" {
						"email" "test@gid.com"
						"mobilePhone" CommonVariable.DEFAULT_MOBILE_PREFIX  + RANDOM_MOBILE
						"line1" CommonVariable.DEFAULT_LINE1
						"city" CommonVariable.DEFAULT_CITY
						"postcode" CommonVariable.DEFAULT_POSTCODE
						
					}
				}
		
				println "Json is " +  json.toString()
		when:
				HTTP_BUILDER.request(Method.POST, ContentType.JSON){
					uri.path =  JOB_URI_PREFIX + USER_ID_DYNAMIC_HO + "/jobs"
					println "uri job is : "+uri.path
					headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_HO
					body = json.toString()
					requestContentType = ContentType.JSON
					
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
					
					response.failure = { resp, reader -> 
						println "Request failed with status ${resp.status}"
						println " stacktrace : "+reader.each{"$it"}
						responseStatus = resp.statusLine.statusCode
					}
				}
		then:
				responseStatus == CommonVariable.STATUS_201
	}
	
	
	
	def "post a job with foul language"(){
		given:
				String responseStatus = null
				def json = new JsonBuilder()
				json {
					"homeownerUserId" USER_ID_DYNAMIC_HO
					"tradesmanUserId" CommonVariable.DEFAULT_TM_ID
					"name" CommonVariable.NAME
					"description"  CommonVariable.DEFAULT_DESCRIPTION +"conman"
					"hourRate" CommonVariable.DEFAULT_HOURRATE
					"jobContactDetails" {
						"email" "test@gid.com"
						"mobilePhone" CommonVariable.DEFAULT_MOBILE_PREFIX  + RANDOM_MOBILE
						"line1" CommonVariable.DEFAULT_LINE1
						"city" CommonVariable.DEFAULT_CITY
						"postcode" CommonVariable.DEFAULT_POSTCODE
						
					}
				}
		
				println "Json is " +  json.toString()
		when:
				HTTP_BUILDER.request(Method.POST, ContentType.JSON){
					uri.path =  JOB_URI_PREFIX + USER_ID_DYNAMIC_HO + "/jobs"
					println "uri job is : "+uri.path
					headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_HO
					body = json.toString()
					requestContentType = ContentType.JSON
					
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
					
					response.failure = { resp, reader ->
						println "Request failed with status ${resp.status}"
						println " stacktrace : "+reader.each{"$it"}
						responseStatus = resp.statusLine.statusCode
					}
				}
		then:
				responseStatus == CommonVariable.STATUS_400
	}
	
	
	
	def "Get Job List for HomeOwner"() {
		given:
		String responseStatus = null
		when:
		HTTP_BUILDER.request(Method.GET, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_HO
			uri.path =  JOB_URI_PREFIX + USER_ID_DYNAMIC_HO + "/hojobs"
			uri.query = [
				jobStatus:'REQUESTED',
				offset:0,
				limit:10
				]
			println "uri job is : "+uri.path

			requestContentType = ContentType.JSON
			
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
			
			response.failure = { resp, reader ->
				println "Request failed with status ${resp.status}"
				println " stacktrace : "+reader.each{"$it"}
				responseStatus = resp.statusLine.statusCode
			}
		}
		then:
		responseStatus == CommonVariable.STATUS_200
	}
	
	
	
	def "Get Single Job List Homeowner"(){
		given:
		String responseStatus = null
		when:
		HTTP_BUILDER.request(Method.GET, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_HO
			uri.path =  JOB_URI_PREFIX +USER_ID_DYNAMIC_HO+"/hojobs/1"
			println "uri job is : "+uri.path
			requestContentType = ContentType.JSON
			
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
			
			response.failure = { resp, reader ->
				println "Request failed with status ${resp.status}"
				println " stacktrace : "+reader.each{"$it"}
				responseStatus = resp.statusLine.statusCode
			}
		}
		then:
		responseStatus == CommonVariable.STATUS_201
		}
	
}
