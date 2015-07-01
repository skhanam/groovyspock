/**
 * 
 */
package com.ratedpeople.tradesman.profile.resource

import groovyx.net.http.HTTPBuilder;

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
class TMProfileFunctionalTest extends AbstractUserToken{

	private static final String PROFILE_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"
	
	private static final String MATCH_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/match"
	
	def "Get TradesMan Profile"() {
		given:
		String responseCode = null
		println "********************************"
		println "Test running ..  " +"Get TradesMan Profile"
		when:
			HTTP_BUILDER.request(Method.GET){
				headers.Accept = 'application/json'
				headers.'Authorization' = "Bearer " + ACCESS_TOKEN_TM
				uri.path = PROFILE_PREFIX + USER_ID_TM + "/profiles"
				println "Uri is " + uri
				response.success = { resp, reader ->
					println "Success"
					responseCode = resp.statusLine.statusCode
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					reader.each{
						println "Response data: " + "$it"
						String user = "$it"
						if (user.startsWith("userId")){
							user = user.replace("userId=", "")
							println "User values : " +user
						}
					}
				}
				
				response.failure = { resp, reader ->
					println "Request failed with status ${resp.status}"
					reader.each{ println "Error values : "+"$it" }
					responseStatus = resp.statusLine.statusCode
				}
			}

		then:
			responseCode == CommonVariable.STATUS_200
	}
	
	
	
	
	
	def "Update Tradesman Profile"(){
		given :
			String responseCode = null
			def json = new JsonBuilder()
			json{
				"userId" USER_ID_TM
				"firstName" CommonVariable.DEFAULT_TM_FIRSTNAME + " Update"
				"lastName" CommonVariable.DEFAULT_TM_LASTNAME
				"email" CommonVariable.DEFAULT_TM_USERNAME 
				"description" "This is a desc of more for the tm it needs to have more than 60 chars"
//				"listPhone" ([ 
//							{"number" CommonVariable.DEFAULT_MOBILE_PREFIX  + RANDOM_MOBILE
//							"phoneType" "MOBILE_PHONE"},
//							{"number" CommonVariable.DEFAULT_MOBILE_PREFIX  + RANDOM_MOBILE
//							 "phoneType" "OFFICE_PHONE"}
//						])
				"company"{
							"name" CommonVariable.DEFAULT_COMPANY_NAME
						 }
		
			}
			
			println "Json is " +  json.toString()
			println "********************************"
			println "Test running ..  Update Tradesman Profile"
		when:
			HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
				uri.path = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"+USER_ID_TM+"/profiles"
				println "uri.path   :"+uri.path
				println "Access Token TM : "+ ACCESS_TOKEN_TM
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
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
	
				response.failure = { resp, reader ->
					responseCode = resp.statusLine.statusCode
					println " stacktrace : "+reader.each{"$it"}
				}
			}
		then:
			responseCode == CommonVariable.STATUS_200
	}
	
	
	
	
}
