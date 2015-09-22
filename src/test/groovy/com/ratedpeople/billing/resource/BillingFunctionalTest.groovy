/**
 * 
 */
package com.ratedpeople.billing.resourceimport com.ratedpeople.user.resource.AbstractUserToken;

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.Method

import com.ratedpeople.support.CommonVariable;
import com.ratedpeople.support.DatabaseHelper

import groovyx.net.http.HTTPBuilder


/**
 * @author Shabana
 *
 */



class BillingFunctionalTest extends AbstractUserToken{

	private static final String BILLING_URI_PREFIX = CommonVariable.BILLING_SERVICE_PREFIX + "v1.0/users/"

	def "Get list billing "(){
		given:
				String responseStatus = null
					
		when:
			HTTP_BUILDER.request(Method.GET, ContentType.JSON){
				uri.path = BILLING_URI_PREFIX +  "1/billingdetails"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_ADMIN
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
	


	def "Get details about billing "(){
		given:
				String responseStatus = null
				
					
		when:
			HTTP_BUILDER.request(Method.GET, ContentType.JSON){
				uri.path = BILLING_URI_PREFIX +  "1/billingdetails/4"
				
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_ADMIN
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
