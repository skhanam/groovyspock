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
class CreateInvoiceFunctionalTest extends AbstractUserToken{
	
	/*
	 *  Negative scenarios are not written as there are no validations in place 
	 *  will update once this is being put in place
	 */
	
	def "test create invoice "(){
		
		given:
		String responseStatus = null
		def json = new JsonBuilder()
		json {
			"fromUserId" DataValues.requestValues.get("FROMUSERID")
			"toUserId" DataValues.requestValues.get("TOUSERID")
			"jobId" DataValues.requestValues.get("JOBID")
			"token" "528d5b2a1ed64d87854d2920dd7f5560"
			"currency" DataValues.requestValues.get("CURRENCY")
			"skrillTransaction" DataValues.requestValues.get("SKRILLTRANSACTION")
			"amount" DataValues.requestValues.get("AMOUNT")
			"fromUserEmail" DataValues.requestValues.get("FROMUSEREMAIL")
			"ip" DataValues.requestValues.get("IP")
		
		}
		
		println "Json is " +  json.toString()
	when:
		HTTP_BUILDER.request(Method.POST, ContentType.JSON){
			uri.path = DataValues.requestValues.get("PAYMENTSERVICE")+"v1.0/users/1/jobs/1"
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN
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
		responseStatus == DataValues.requestValues.get("STATUS200")
//		where:
//			status
//			DataValues.requestValues.get("STATUS200")

	}

	
	
	
	
	
}
