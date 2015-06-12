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
class TMJobListTest extends AbstractUserToken {
	
	protected static long RANDOM_MOBILE = Math.round(Math.random()*100000000);
	private static final String JOB_URI_PREFIX = CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/"
	
	def "Get Tradesman Job List"(){
	given:
	String responseStatus = null
	println "********************************"
	println "Test running ..  " +"Get Tradesman Job List"
	DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 5 WHERE id = 5")
	when:
	try{
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
	
	} catch(java.net.ConnectException ex){
		ex.printStackTrace()
	}
	then:
	responseStatus == CommonVariable.STATUS_200
	}
	
	
	
	
	def "Get Unique Job Address HomeOwner"(){
		given:
		String responseStatus = null
		println "********************************"
		println "Test running ..  " +"Get Unique Job Address HomeOwner"
		when:
		try{
		HTTP_BUILDER.request(Method.GET, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_HO
			uri.path =  JOB_URI_PREFIX +"2/contactdetails"
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
		
		} catch(java.net.ConnectException ex){
			ex.printStackTrace()
		}
		then:
		responseStatus == CommonVariable.STATUS_200
}
	
	
	
	def "Homeowner Withdraw Job"(){
		given:
		String responseStatus = null
		def getJobStatus = DatabaseHelper.executeQuery("select job_status_id from job.job WHERE id = 1")
		println "Get the Job status :"+getJobStatus 
		if(getJobStatus.equals(true)){
			DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 1 WHERE id = 1")
		}
		when:
		try{
		HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_HO
			uri.path =  JOB_URI_PREFIX +USER_ID_HO+"/hojobs/"+1+"/withdraw"
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
		}}catch(java.net.ConnectException ex){
		ex.printStackTrace()
		}
		then:
		responseStatus == CommonVariable.STATUS_200
		cleanup:
		DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 1 WHERE id = 10")
		}
	
	
	
	def "Tradesman Accept Job"(){
		given:
		String responseSta1tus = null

		when:
		try{
		HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
			uri.path =  JOB_URI_PREFIX +USER_ID_TM+"/tmjobs/"+2+"/schedule"
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
		}}catch(java.net.ConnectException ex){
		ex.printStackTrace()
	}
		then:
		responseStatus == CommonVariable.STATUS_200
		cleanup:
		DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 2 WHERE id = 2")
		}
	
	
	def "Tradesman Start Job"(){
		given:
		String responseStatus = null
		def json = getWorkingLatitude("3")
		when:
		try{
		HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
			body = json.toString()
			uri.path =  JOB_URI_PREFIX +USER_ID_TM+"/tmjobs/"+3+"/start"
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
		}catch(java.net.ConnectException ex){
		ex.printStackTrace()
	}
		then:
		responseStatus == CommonVariable.STATUS_200
		cleanup:
		DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 3 WHERE id = 3")
	}
	
	
	
	
	def "Tradesman Pause Job"(){
		given:
		String responseStatus = null
		def json = new JsonBuilder()
		json {
			"jobId" 5
			"stopLatitude" "10.00"
			"stopLongitude" "11.00"
		}

		when:
		try{
		HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
			body = json.toString()
			uri.path =  JOB_URI_PREFIX +USER_ID_TM+"/tmjobs/"+5+"/pause"
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
		}}catch(java.net.ConnectException ex){
		ex.printStackTrace()
	}
		then:
		responseStatus == CommonVariable.STATUS_200
		cleanup:
		DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 5 WHERE id = 5")
		DatabaseHelper.executeQuery("UPDATE job.job_tracking SET stop_ts = null  WHERE job_id = 5")
	}
	
	
	
	
	
	
	def "Tradesman Complete Job"(){
		given:
		String responseStatus = null

		def json = new JsonBuilder()
		json {
			"jobId" 5
			"stopLatitude" "10.00"
			"stopLongitude" "11.00"
		}
		when:
		try{
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
		}}catch(java.net.ConnectException ex){
		ex.printStackTrace()
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
		def json = new JsonBuilder()
		json {
			"jobId" 2
			"rejectedBy" USER_ID_TM
			"description" "I am not available hence i am rejecting the job sorry for inconvenience caused"
		}
		println "JSON is : "+json.toString()
		when:
		try{
		HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
			uri.path =  JOB_URI_PREFIX +USER_ID_TM+"/tmjobs/"+2+"/reject"
			println "uri job is : "+uri.path
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
		} }catch(java.net.ConnectException ex){
		ex.printStackTrace()
		}
		then:
		responseStatus == CommonVariable.STATUS_200
		cleanup:
		DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 2 WHERE id = 2")
		DatabaseHelper.executeQuery("DELETE from job.job_rejected where job_id = 2")
	
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
		def json = new JsonBuilder()
		json {
			"jobId" '7'
			"hoursWorked" '1.5'
		}
		when:try{
		HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
			uri.path =  JOB_URI_PREFIX +USER_ID_TM+"/tmjobs/"+7+"/invoice"
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
		}catch(java.net.ConnectException ex){
		ex.printStackTrace()
	}
		then:
		responseStatus == CommonVariable.STATUS_200
		cleanup:
		DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 7 WHERE id = 7")
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
