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

	protected static long RANDOM_MOBILE = Math.round(Math.random()*1000000000);
	private static final String JOB_URI_PREFIX = CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/"
	
	def "setup"(){
		HTTP_BUILDER.handler.failure = { resp, reader ->
			[response:resp, reader:reader]
		}
		HTTP_BUILDER.handler.success = { resp, reader ->
			[response:resp, reader:reader]
		}
	}
	
	
   def "post a job Test"(){
	given:
		String  responseStatus
		
	when :
		def response = postajob(JOB_URI_PREFIX,createJsonPostaJob(""));
		def  resp = response['response']
		def reader = response['reader']
		responseStatus = resp.status
		println "response code :${resp.status}"
		reader.each{
			println "Response data: " + "$it"

			String data = "$it"
			
		}
		
	then:
		responseStatus == CommonVariable.STATUS_201
	
		
	}
	
	
	def "post a job with foul language"(){
		given:
		String  responseStatus
		
	when :
		def response = postajob(JOB_URI_PREFIX,createJsonPostaJob(" Abuse"));
		def  resp = response['response']
		def reader = response['reader']
		responseStatus = resp.status
		println "response code :${resp.status}"
		reader.each{
			println "Response data: " + "$it"
			String data = "$it"
		}
		
	then:
		responseStatus == CommonVariable.STATUS_400
	}
	
	
	
	def "Get Job List for HomeOwner"() {
		given:
		String responseStatus = null
		def response = postajob(JOB_URI_PREFIX,createJsonPostaJob("Get List"));
		def  resp = response['response']
		def reader = response['reader']
		responseStatus = resp.status
		println "response code :${resp.status}"
		reader.each{
			println "Response data: " + "$it"
			String data = "$it"
		}
		when:
		def map = HTTP_BUILDER.request(Method.GET, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_HO
			uri.path =  JOB_URI_PREFIX + USER_ID_DYNAMIC_HO + "/hojobs"
			uri.query = [
				jobStatus:'PENDING',
				offset:0,
				limit:10
				]
			requestContentType = ContentType.JSON
			println "uri job is : "+uri.path
			}
			resp = map['response']
			reader = map['reader']
	
			responseStatus = resp.status
			println "response code :${resp.status}"
			
			reader.each{
				println "Response data:for getting a  List of Jobs for HO " + "$it"
				String data = "$it"
			}
			
		then:
		responseStatus == CommonVariable.STATUS_200
	}
	
	
	
	def "Get Single Job Homeowner"(){
	given:
		String responseStatus = null
		def response = postajob(JOB_URI_PREFIX,createJsonPostaJob(""));
		def  resp = response['response']
		def reader = response['reader']
		responseStatus = resp.status
		println "response code :${resp.status}"
		reader.each{
			println "Response data: " + "$it"
			String data = "$it"
		}
		def getJobId = DatabaseHelper.select("select id from job.job where homeowner_user_id = '${USER_ID_DYNAMIC_HO}' limit 1 ")
		println "id of Job is :"+getJobId
		if (getJobId.startsWith("[{id")){
			getJobId = getJobId.replace("[{id=", "").replace("}]","")
			println "JobId is : " +getJobId
		}
	when:
		def map = HTTP_BUILDER.request(Method.GET, ContentType.JSON){
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_HO
			uri.path =  JOB_URI_PREFIX + USER_ID_DYNAMIC_HO + "/hojobs/"+getJobId
			requestContentType = ContentType.JSON
			println "uri job is : "+uri.path
			}
			resp = map['response']
			reader = map['reader']
	
			responseStatus = resp.status
			println "response code :${resp.status}"
			
			reader.each{
				println "Response data:for getting a  List of Jobs for HO " + "$it"
				String data = "$it"
			}
			
	then:
		responseStatus == CommonVariable.STATUS_200
		}
	
	
	
	def "Unique Homeowner Withdraw Job No Merchant Created"(){
		given:
				String responseStatus = null
				println "Unique Homeowner Withdraw Job No Merchant Created"
				def response = postajob(JOB_URI_PREFIX,createJsonPostaJob(""));
				def  resp = response['response']
				def reader = response['reader']
				responseStatus = resp.status
				println "response code :${resp.status}"
				reader.each{
					println "Response data: " + "$it"
					String data = "$it"
				}
				def getJobId = DatabaseHelper.select("select id from job.job where homeowner_user_id = '${USER_ID_DYNAMIC_HO}' limit 1 ")
				println "id of Job is :"+getJobId
				if (getJobId.startsWith("[{id")){
					getJobId = getJobId.replace("[{id=", "").replace("}]","")
					println "JobId is : " +getJobId
				}
		when:
		try{
				def map = HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
						headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_HO
						uri.path =  JOB_URI_PREFIX + USER_ID_DYNAMIC_HO + "/hojobs/"+getJobId+"/withdraw"
						requestContentType = ContentType.JSON
						println "uri job is : "+uri.path
				}
				resp = map['response']
				reader = map['reader']
				responseStatus = resp.status
				println "response code :${resp.status}"
				reader.each{
					println "Response data:for getting a  List of Jobs for HO " + "$it"
					String data = "$it"
				}
			}catch(java.net.ConnectException ex){
				ex.printStackTrace()
			}
		then:
			responseStatus == CommonVariable.STATUS_200

		}
	
	

	
	
}
