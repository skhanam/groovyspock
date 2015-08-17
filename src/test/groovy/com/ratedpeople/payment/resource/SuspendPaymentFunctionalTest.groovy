/**
 * 
 */
package com.ratedpeople.payment.resource

/**
 * @author shabana.khanam
 *
 */
/**
 * 
 */
import com.ratedpeople.user.resource.AbstractTradesman;
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.user.resource.AbstractUserToken
import com.ratedpeople.support.DatabaseHelper
import groovy.json.*
import groovyx.net.http.ContentType


class SuspendPaymentFunctionalTest extends AbstractTradesman{
	
	
	
	def "test suspend payment "(){
		given:
			String responseStatus = null
			def json = new JsonBuilder()
			json {
						"paymentTransactionId" "1"
						"jobId" "8"
						"homeownerUserId" 2
						"comment" "This is a payment that needs to be stopped"
			}
			
			println "Json is " +  json.toString()
			def getSkrillID1 = DatabaseHelper.executeQuery("select skrill_transaction from  payment.payment_transaction WHERE job_id=8")
			println "Skrill transaction Id : " + getSkrillID1
			if(getSkrillID1.equals(true)){
				DatabaseHelper.executeQuery("UPDATE payment.payment_transaction SET skrill_transaction='', payment_status_id='1' WHERE job_id=8")
			}

		when:
		try{
			HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
				uri.path = CommonVariable.PAYMENT_SERVICE_PREFIX + "v1.0/users/2/jobs/8/payments/1/dispute"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_ADMIN
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
			 DatabaseHelper.executeQuery("Delete from payment.payment_dispute_comment where payment_transaction_id = 1");
		
	}
	
	
	
	def "Test Add Comment to disputed payment"(){
			given:
				String responseStatus = null
				def json = new JsonBuilder()
				json {
							"paymentTransactionId" "1"
							"jobId" "8"
							"homeownerUserId" 2
							"comment" "This is a payment that needs to be stopped Update"
				}
				
				println "Json is " +  json.toString()
				def getSkrillID1 = DatabaseHelper.executeQuery("select skrill_transaction from  payment.payment_transaction WHERE job_id=8")
				println "Skrill transaction Id : " + getSkrillID1
				if(getSkrillID1.equals(true)){
					DatabaseHelper.executeQuery("UPDATE payment.payment_transaction SET skrill_transaction='', payment_status_id='3' WHERE job_id=8 and id=1")
				}
	
			when:
			try{
				HTTP_BUILDER.request(Method.POST, ContentType.JSON){
					uri.path = CommonVariable.PAYMENT_SERVICE_PREFIX + "v1.0/users/2/jobs/8/payments/1/dispute"
					headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_ADMIN
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
				responseStatus == CommonVariable.STATUS_201
			cleanup:
				Thread.sleep(3000)
				DatabaseHelper.executeQuery("UPDATE payment.payment_transaction SET skrill_transaction='', payment_status_id=1 WHERE job_id=8")
				 def getSkrillID = DatabaseHelper.executeQuery("select skrill_transaction from  payment.payment_transaction WHERE job_id=8")
				 println "Skrill transaction Id : " + getSkrillID
				 DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id ='8' WHERE id = 8")
				 DatabaseHelper.executeQuery("Delete from payment.payment_dispute_comment where payment_transaction_id = 1");
	
	}
	
	
	
	def "Test Approve a disputed payment"(){
		given:
			String responseStatus = null
			def json = new JsonBuilder()
			json {
						"paymentTransactionId" "1"
						"jobId" "8"
						"homeownerUserId" 2
						"comment" "This is a payment that needs to be stopped Update"
			}
			
			println "Json is " +  json.toString()
			def getSkrillID1 = DatabaseHelper.executeQuery("select skrill_transaction from  payment.payment_transaction WHERE job_id=8")
			println "Skrill transaction Id : " + getSkrillID1
			if(getSkrillID1.equals(true)){
				DatabaseHelper.executeQuery("UPDATE payment.payment_transaction SET skrill_transaction='', payment_status_id='3' WHERE job_id=8 and id=1" )
			}
//			def getTransactionID = DatabaseHelper.executeQuery("select skrill_transaction from  payment.payment_transaction WHERE job_id=8")
//			println "payment transaction Id : " +getTransactionID

		when:
		try{
			HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
				uri.path = CommonVariable.PAYMENT_SERVICE_PREFIX + "v1.0/users/2/jobs/8/payments/1/approve"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_ADMIN
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
			 DatabaseHelper.executeQuery("Delete from payment.payment_dispute_comment where payment_transaction_id = 1");

}
	
	
	
	def "test get  payment details from job "(){
		given:
			String responseStatus = null
		when:
		try{
			HTTP_BUILDER.request(Method.GET, ContentType.JSON){
				uri.path = CommonVariable.PAYMENT_SERVICE_PREFIX + "v1.0/users/2/jobs/8/payments"
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
					reader.each{ println "Error values : "+"$it" }
					responseStatus = resp.statusLine.statusCode
				}
			}
			}catch(java.net.ConnectException ex){
				ex.printStackTrace()
		}
		then:
			responseStatus == CommonVariable.STATUS_200
		
	}
	

}

