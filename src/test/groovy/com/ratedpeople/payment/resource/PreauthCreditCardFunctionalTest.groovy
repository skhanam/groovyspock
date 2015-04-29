/**
 * 
 */
package com.ratedpeople.payment.resource

import groovy.json.*
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DataValues
import com.ratedpeople.support.DatabaseHelper
import com.ratedpeople.user.resource.AbstractUserToken


/**
 * @author shabana.khanam
 *
 */
class PreauthCreditCardFunctionalTest extends AbstractUserToken{

	private static final long RANDOM_JOB_ID = Math.round(Math.random()*1000);
	
	def "test preauth credit card"(){

		given:
			String responseStatus = null
			def json = new JsonBuilder()
			json {
				"fromUserId" USER_ID_HO
				"toUserId" USER_ID_TM
				"jobId" RANDOM_JOB_ID
				"token" CommonVariable.DEFAULT_CC_TOKEN
				"currency" CommonVariable.DEFAULT_CURRENCY
				"skrillTransaction" ""
				"amount" CommonVariable.DEFAULT_AMOUNT
				"fromUserEmail" CommonVariable.DEFAULT_HO_USERNAME
				"ip" CommonVariable.DEFAULT_IP
			}
	
			println "Json is " +  json.toString()
		when:
			HTTP_BUILDER.request(Method.POST, ContentType.JSON){
				uri.path = CommonVariable.PAYMENT_SERVICE_PREFIX + "v1.0/users/"+USER_ID_HO+"/preauth"
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
	
				response.failure = { resp, reader ->
					println "Request failed with status ${resp.status}"
					reader.each{ println "Error values : "+"$it" }
					responseStatus = resp.statusLine.statusCode
				}
			}
		then:
			responseStatus == CommonVariable.STATUS_201
		cleanup:
			DatabaseHelper.executeQuery("delete from payment.preauth_transaction where from_user_id = '${USER_ID_HO}'")
	}
}
