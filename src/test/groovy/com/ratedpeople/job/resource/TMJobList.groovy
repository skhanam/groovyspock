/**
 * 
 */
package com.ratedpeople.job.resource

import com.ratedpeople.user.resource.AbstractTradesman;
import com.ratedpeople.user.resource.AbstractUserToken;

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
class TMJobList extends AbstractUserToken {
	
	private final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(CommonVariable.SERVER_URL)
	protected static long RANDOM_MOBILE = Math.round(Math.random()*100000000);
	private static final String JOB_URI_PREFIX = CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/"
	
	def "Get Tradesman Job List"(){
	given:
	String responseStatus = null
	when:
	HTTP_BUILDER.request(Method.GET, ContentType.JSON){
		headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
		uri.path =  JOB_URI_PREFIX +USER_ID_TM+"/tmjobs"
		println "uri job is : "+uri.path
		uri.query = [
			jobStatus:'REQUESTED',
			offset: 0,
			limit:10
			]
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
	
	
	
	
	def "Tradesman Accept Job"(){
		given:
		String responseStatus = null
		def jobId = DatabaseHelper.select("select id  from job.job where job_status_id = 1 limit 1")
		if (jobId.startsWith("[{id")){
			jobId = jobId.replace("[{id=", "").replace("}]","")
			println ("Job Id is : " +jobId)
		}
		when:
		HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
			uri.path =  JOB_URI_PREFIX +USER_ID_TM+"/tmjobs/"+jobId+"/status"
			println "uri job is : "+uri.path
			uri.query = [
				jobStatus:'SCHEDULED'
				]
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
	
	
	
	
	def "Tradesman Reject Job"(){
		given:
		String responseStatus = null
		def jobRejectId = DatabaseHelper.select("select id  from job.job where job_status_id = 1 limit 1")
		if (jobRejectId.startsWith("[{id")){
			jobRejectId = jobRejectId.replace("[{id=", "").replace("}]","")
			println ("Job Id is : " +jobRejectId)
		}
		def json = new JsonBuilder()
		json {
			"jobId" jobRejectId
			"rejectedBy" USER_ID_TM
			"description" "I am not available"
		}
		when:
		HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
			uri.path =  JOB_URI_PREFIX +USER_ID_TM+"/tmjobs/"+jobRejectId+"/status"
			println "uri job is : "+uri.path
			body = json.toString()
			uri.query = [
				jobStatus:'REJECTED'
				]
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
	
	
	
	
	
	
	
	def "Get Single Job List Tradesman"(){
		given:
		String responseStatus = null
		when:
		HTTP_BUILDER.request(Method.GET, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
			uri.path =  JOB_URI_PREFIX +USER_ID_TM+"/tmjobs/1"
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
	
}
