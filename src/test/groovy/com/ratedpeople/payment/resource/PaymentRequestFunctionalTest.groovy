/**
 * 
 */
package com.ratedpeople.payment.resource

import com.ratedpeople.user.token.AbstractUserToken;
import groovy.json.JsonBuilder
import spock.lang.Specification;
import com.ratedpeople.support.DataValues;
import groovy.json.*
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method


/**
 * @author shabana.khanam
 *
 */
class PaymentRequestFunctionalTest extends AbstractUserToken{
	
	/*
	 *  Negative scenarios are not written as there are no validations in place 
	 *  will update once this is being put in place
	 */
	
	private long randomJobId = Math.round(Math.random()*1000);
	
	def "test request payment "(){	
		given:
			String responseStatus = null
			def json = new JsonBuilder()
			json {
				"fromUserId" USER_ID_HO
				"toUserId" USER_ID_TM
				"jobId" randomJobId
				"token" "83f1c8e83a004ebdb8c8c35362a688ff"
				"currency" DataValues.requestValues.get("CURRENCY")
				"skrillTransaction" DataValues.requestValues.get("SKRILLTRANSACTION")
				"amount" DataValues.requestValues.get("AMOUNT")
				"fromUserEmail" DataValues.requestValues.get("FROMUSEREMAIL")
				"ip" DataValues.requestValues.get("IP")
			
			}
			
			println "Json is " +  json.toString()
		when:
			HTTP_BUILDER.request(Method.POST, ContentType.JSON){
				uri.path = DataValues.requestValues.get("PAYMENTSERVICE")+"v1.0/users/${USER_ID_HO}/jobs/${randomJobId}"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_HO
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
				
				response.failure = { resp ->
					println "Request failed with status ${resp.status}"
					responseStatus = resp.statusLine.statusCode
				}
			}
		then:
			responseStatus == DataValues.requestValues.get("STATUS201")
		}
}
