package com.ratedpeople.payment.resource;

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.Method
import com.ratedpeople.support.DataValues
import com.ratedpeople.support.DatabaseHelper
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
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
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
			responseStatus == DataValues.requestValues.get("STATUS201")
		cleanup:
			DatabaseHelper.executeQuery("delete from payment.merchant where channel = '${DataValues.requestValues.get("CHANNELID")}'")
	
	}
	
	def testRegisterMerchantWhenAleadyExists(){
		given:
			String responseStatus = null
			String errorMessage
			
			def json = new JsonBuilder()
			json {
				"rpUserId" DataValues.requestValues.get("RPUSERID")
				"merchant" DataValues.requestValues.get("MERCHANT")
				"channel" DataValues.requestValues.get("CHANNELID")
			}
			
			println "Json is " +  json.toString()
		when:
			def response = registerMerchant(json)
			def resp = response['response']
			def reader = response['reader']
			response = registerMerchant(json)
			resp = response['response']
			reader = response['reader']
			
			responseStatus = resp.status.toString()
			errorMessage = reader.get('cause')
			println errorMessage
		then:
			responseStatus == DataValues.requestValues.get("STATUS409")
			
		cleanup:
			DatabaseHelper.executeQuery("delete from payment.merchant where channel = '${DataValues.requestValues.get("CHANNELID")}'")
	}
	
	private def registerMerchant(def json){
		def response = HTTP_BUILDER.request(Method.POST, ContentType.JSON){
			uri.path = REGISTER_MERCHANT_URI
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
			body = json.toString()
			requestContentType = ContentType.JSON
			
			println "Registering merchant 1 Uri : " + uri
		}
		
		return response;
	}
}
