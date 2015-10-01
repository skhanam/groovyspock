/**
 * 
 */
package com.ratedpeople.payment.resource

import groovy.json.*
import groovyx.net.http.ContentType
import groovyx.net.http.Method
import spock.lang.Specification

import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper


/**
 * @author shabana.khanam
 *
 */
class PaymentRequestFunctionalTest extends Specification{

	/*
	 *  Negative scenarios are not written as there are no validations in place 
	 *  will update once this is being put in place
	 */

	private static final long RANDOM_JOB_ID = Math.round(Math.random()*1000);

	def "test request payment "(){
		given:
		String responseStatus = null
		def json = new JsonBuilder()
		json { "ipAddress" CommonVariable.DEFAULT_IP }

		println "Json is " +  json.toString()
		def getSkrillID1 = DatabaseHelper.executeQuery("select skrill_transaction from  payment.payment_transaction WHERE job_id=8")
		println "Skrill transaction Id : " + getSkrillID1
		if(getSkrillID1.equals(true)){
			DatabaseHelper.executeQuery("UPDATE payment.payment_transaction SET skrill_transaction='', payment_status_id='1' WHERE job_id=8")
		}

		when:
		try{
			HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
				uri.path = CommonVariable.PAYMENT_SERVICE_PREFIX + "v1.0/users/${USER_ID_HO}/jobs/"+8
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
		}catch(java.net.ConnectException ex){
			ex.printStackTrace()
		}
		then:
		responseStatus == CommonVariable.STATUS_200
		cleanup:
		Thread.sleep(3000)
		DatabaseHelper.executeQuery("UPDATE payment.payment_transaction SET skrill_transaction='', payment_status_id='1' WHERE job_id=8")
		def getSkrillID = DatabaseHelper.executeQuery("select skrill_transaction from  payment.payment_transaction WHERE job_id=8")
		println "Skrill transaction Id : " + getSkrillID
		DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id ='8' WHERE id = 8")
		Thread.sleep(2000);
	}
}

