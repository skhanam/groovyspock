/**
 * 
 */
package com.ratedpeople.payment.resource

import groovy.json.*
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import com.ratedpeople.support.DataValues
import com.ratedpeople.support.DatabaseHelper
import com.ratedpeople.user.resource.AbstractUserToken


/**
 * @author shabana.khanam
 *
 */
class PreauthCreditCardFunctionalTest extends AbstractUserToken{

	private final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(DataValues.requestValues.get("URL"))


	def "ho test preauth"(){
	}

	def "test preauth credit card"(){

		given:
		String responseStatus = null
		def json = new JsonBuilder()
		json {
			"fromUserId" USER_ID_HO
			"toUserId" USER_ID_TM
			"jobId" DataValues.requestValues.get("JOBID")
			"token" DataValues.requestValues.get("CCTOKEN")
			"currency" DataValues.requestValues.get("CURRENCY")
			"skrillTransaction" DataValues.requestValues.get("SKRILLTRANSACTION")
			"amount" DataValues.requestValues.get("AMOUNT")
			"fromUserEmail" DataValues.requestValues.get("FROMUSEREMAIL")
			"ip" DataValues.requestValues.get("IP")
		}

		println "Json is " +  json.toString()
		when:
		HTTP_BUILDER.request(Method.POST, ContentType.JSON){
			uri.path = DataValues.requestValues.get("PAYMENTSERVICE")+"v1.0/users/"+USER_ID_HO+"/preauth"
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
		responseStatus == DataValues.requestValues.get("STATUS201")
		cleanup:
		DatabaseHelper.executeQuery("delete from payment.preauth_transaction where from_user_id = '${USER_ID_HO}'")
	}
}
