/**
 * 
 */
package com.ratedpeople.payment.resource

import com.ratedpeople.user.resource.AbstractUserToken;
import com.ratedpeople.support.DataValues;
import groovy.json.*
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

/**
 * @author shabana.khanam
 *
 */
class TakePaymentFunctionalTest extends AbstractUserToken{
	
	/*
	def "test payment from user "(){	
		given:
			String responseStatus = null
		when:
			HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
				uri.path = DataValues.requestValues.get("PAYMENTSERVICE")+"v1.0/users/"+USER_ID_HO+"/jobs/1"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_HO
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

		}*/
	}
