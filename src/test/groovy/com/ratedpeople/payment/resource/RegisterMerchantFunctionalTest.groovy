package com.ratedpeople.payment.resource;

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.Method
import com.ratedpeople.support.DataValues
import com.ratedpeople.user.token.AbstractUserToken


class RegisterMerchantFunctionalTest extends AbstractUserToken{
	
	private static final String REGISTER_MERCHANT_URI = DataValues.requestValues.get("PAYMENTSERVICE")+"v1.0/merchants"
	
	def testRegisterMerchant(){			
		given:
			String responseStatus = null
			def json = new JsonBuilder()
			json {
				"rpUserId" DataValues.requestValues.get("RPUSERID")
				"merchant" DataValues.requestValues.get("MERCHANT")
				"channel" DataValues.requestValues.get("CHANNELID")
			}
			
			println "Json is " +  json.toString()
		when:			
			HTTP_BUILDER.request(Method.POST, ContentType.JSON){
				uri.path = REGISTER_MERCHANT_URI
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN
				body = json.toString()
				requestContentType = ContentType.JSON
				
				println "Uri : " + uri
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					
					responseStatus = resp.statusLine.statusCode
				}
				
				response.failure = { resp ->
					println "Request failed with status ${resp.status}"
					responseStatus = resp.statusLine.statusCode
				}	
			}
		then:
			responseStatus == status
		where:   
			status										|message
			DataValues.requestValues.get("STATUS200")	|""
			DataValues.requestValues.get("STATUS400")	|DataValues.requestValues.get("MERCHANTEXISTS")
	}
}
