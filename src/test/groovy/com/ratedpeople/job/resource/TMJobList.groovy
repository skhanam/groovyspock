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
//		def jobId = DatabaseHelper.select("select id  from job.job where job_status_id = 1 limit 1")
//		if (jobId.startsWith("[{id")){
//			jobId = jobId.replace("[{id=", "").replace("}]","")
//			println ("Job Id is : " +jobId)
//		}
		when:
		HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
//			uri.path =  JOB_URI_PREFIX +USER_ID_TM+"/tmjobs/"+jobId+"/status"
			uri.path =  JOB_URI_PREFIX +USER_ID_TM+"/tmjobs/"+1+"/status"
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
		cleanup:
		DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 1 WHERE id = 1")
		}
	
	
	def "Tradesman Start Job"(){
		given:
		String responseStatus = null
//		def jobId = DatabaseHelper.select("select id  from job.job where job_status_id = 2 limit 1")
//		if (jobId.startsWith("[{id")){
//			jobId = jobId.replace("[{id=", "").replace("}]","")
//			println ("Job Id is : " +jobId)
//		}
		def json = getWorkingLatitude("2")
		when:
		HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
			body = json.toString()
			uri.path =  JOB_URI_PREFIX +USER_ID_TM+"/tmjobs/"+2+"/start"
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
		cleanup:
		DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 2 WHERE id = 2")
	}
	
	
	
	
	def "Tradesman Pause Job"(){
		given:
		String responseStatus = null
//		def jobrefId = DatabaseHelper.select("select id  from job.job where job_status_id = 4 limit 1")
//		if (jobrefId.startsWith("[{id")){
//			jobrefId = jobrefId.replace("[{id=", "").replace("}]","")
//			println ("Job Id is : " +jobrefId)
//		}
		def json = new JsonBuilder()
		json {
			"jobId" 4
			"stopLatitude" "10.00"
			"stopLongitude" "11.00"
		}
//		def json = getWorkingLatitude("4")
		when:
		HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
			body = json.toString()
			uri.path =  JOB_URI_PREFIX +USER_ID_TM+"/tmjobs/"+4+"/pause"
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
		cleanup:
		DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 4 WHERE id = 4")
		DatabaseHelper.executeQuery("UPDATE job.job_tracking SET stop_ts = null  WHERE job_id = 4")
	}
	
	
	
	
	
	
	def "Tradesman Complete Job"(){
		given:
		String responseStatus = null
//		def jobrefId = DatabaseHelper.select("select id  from job.job where job_status_id = 5 limit 1")
//		if (jobrefId.startsWith("[{id")){
//			jobrefId = jobrefId.replace("[{id=", "").replace("}]","")
//			println ("Job Id is : " +jobrefId)
//		}
		def json = new JsonBuilder()
		json {
			"jobId" 5
			"stopLatitude" "10.00"
			"stopLongitude" "11.00"
		}
		when:
		HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
			body = json.toString()
			uri.path =  JOB_URI_PREFIX +USER_ID_TM+"/tmjobs/"+5+"/complete"
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
		cleanup:
		DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 5 WHERE id = 5")
		DatabaseHelper.executeQuery("UPDATE job.job_tracking SET stop_ts = null  WHERE job_id = 5")
	}


	def "Tradesman Reject Job"(){
		given:
		String responseStatus = null
//		def jobRejectId = DatabaseHelper.select("select id  from job.job where job_status_id = 1 limit 1")
//		if (jobRejectId.startsWith("[{id")){
//			jobRejectId = jobRejectId.replace("[{id=", "").replace("}]","")
//			println ("Job Id is : " +jobRejectId)
//		}
		def json = new JsonBuilder()
		json {
			"jobId" 3
			"rejectedBy" USER_ID_TM
			"description" "I am not available hence i am rejecting the job sorry for inconvenience caused"
		}
		when:
		HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
			uri.path =  JOB_URI_PREFIX +USER_ID_TM+"/tmjobs/"+3+"/status"
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
		responseStatus == CommonVariable.STATUS_200
//		cleanup:
//		DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 1 WHERE id = 3")
		}

	
	def "Get Single Job List Tradesman"(){
		given:
		String responseStatus = null
		def jobId = DatabaseHelper.select("select id  from job.job where job_status_id = 1 limit 1")
		if (jobId.startsWith("[{id")){
			jobId = jobId.replace("[{id=", "").replace("}]","")
			println ("Job Id is : " +jobId)
		}
		
		when:
		HTTP_BUILDER.request(Method.GET, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
			uri.path =  JOB_URI_PREFIX +USER_ID_TM+"/tmjobs/"+jobId
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
	

	def "Raise an Invoice by Tradesman"(){
		given:
		String responseStatus = null
		def jobRaiseInvoice = DatabaseHelper.select("select id  from job.job where job_status_id = 6 limit 1")
		if (jobRaiseInvoice.startsWith("[{id")){
			jobRaiseInvoice = jobRaiseInvoice.replace("[{id=", "").replace("}]","")
			println ("Job Id is : " +jobRaiseInvoice)
		}
		def json = new JsonBuilder()
		json {
			"jobId" jobRaiseInvoice
			"hoursWorked" '1.5'
		}
		when:
		HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
			uri.path =  JOB_URI_PREFIX +USER_ID_TM+"/tmjobs/"+jobRaiseInvoice+"/invoice"
			println "uri job is : "+uri.path
			requestContentType = ContentType.JSON
			body = json.toString()
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

	
	
	
	private def getWorkingLatitude(String additionalInfo){
		def json = new JsonBuilder()
		json {
			"jobId" additionalInfo
			"startLatitude" CommonVariable.DEFAULT_LONGITUDE
			"startLongitude" CommonVariable.DEFAULT_LATITUDE
			
		}
		return json;
	}

	
}
