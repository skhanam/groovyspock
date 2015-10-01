/**
 * 
 */
package com.ratedpeople.resource.user

import groovy.json.*
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import com.ratedpeople.service.HomeownerService;
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper

import groovyx.net.http.ContentType
/**
 * @author shabana.khanam
 *
 */

class ResetPasswordFunctionalTest extends HomeownerService {
	
	private static final String REGISTER_USER_HO_URI = CommonVariable.USER_SERVICE_PREFIX + "v1.0/homeowners/register"
	private static final String REGISTER_USER_TM_URI = CommonVariable.USER_SERVICE_PREFIX + "v1.0/tradesmen/register"
	
	private static String ACCESS_TOKEN

	/*
		def "Generate and Verify Reset password"(){
			given:
				String responseStatus = null
				println "********************************"
				println "Test Running ........  Reset password"
			when:
				HTTP_BUILDER.request(Method.POST){
					headers.Accept = 'application/json'
					uri.path = CommonVariable.USER_SERVICE_PREFIX + "v1.0/users/" + USER_ID_DYNAMIC_HO + "/password/token"
					
					println "Uri : " + uri
					
					response.success = { resp, reader ->
						println "Success"
						println "Got response: ${resp.statusLine}"
						println "Content-Type: ${resp.headers.'Content-Type'}"
						
						responseStatus = resp.statusLine.statusCode
		
					}
					
					response.failure = { 
						resp, reader -> println " stacktrace : "+reader.each{"$it"}
						println 'Not found'
					}
				}
						
				try{
					ACCESS_TOKEN = DatabaseHelper.select("select token from uaa.user_password_token where user_id= '${USER_ID_DYNAMIC_HO}'")
					println "********************************"
					println "Printing Token in Verify Reset Password  :"+ACCESS_TOKEN
					if (ACCESS_TOKEN.startsWith("[{token")){
						ACCESS_TOKEN = ACCESS_TOKEN.replace("[{token=", "").replace("}]","")
						println "token is : " +ACCESS_TOKEN
					}
				}catch(Exception e){
					println e.getMessage()
				}
			
				println "********************************"
				println "Test Running ........  Verify Reset password"
			
				
				def json = new JsonBuilder()
				json {
					"userId" USER_ID_DYNAMIC_HO
					"password" CommonVariable.DEFAULT_PASSWORD
					"token" ACCESS_TOKEN
				}
				
				HTTP_BUILDER.request(Method.PUT){
					headers.Accept = 'application/json'
					uri.path = CommonVariable.USER_SERVICE_PREFIX + "v1.0/users/" + USER_ID_DYNAMIC_HO + "/password/reset"
					body = json.toString()
					requestContentType = ContentType.JSON
					
					
					println "Uri : " + uri
					
					response.success = {
						resp, reader ->
						println "Success"
						println "Got response: ${resp.statusLine}"
						println "Content-Type: ${resp.headers.'Content-Type'}"
						
						responseStatus = resp.statusLine.statusCode
					}
					responseStatus == CommonVariable.STATUS_201
				}
			then:
				responseStatus == CommonVariable.STATUS_200
		}*/
}
