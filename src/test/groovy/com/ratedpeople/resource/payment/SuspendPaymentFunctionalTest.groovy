/**
 * 
 */
package com.ratedpeople.resource.payment
import groovy.json.JsonBuilder
import spock.lang.Specification

import com.ratedpeople.service.UserService
import com.ratedpeople.service.PaymentService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper

/**
 * @author shabana.khanam
 *
 */


class SuspendPaymentFunctionalTest extends Specification{

	private UserService userService = new UserService();
	private PaymentService paymentService = new PaymentService()
	

	def "test suspend payment "(){
	
		given:
			UserInfo admin =  userService.getDefaultAdmin()
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
		
			ResultInfo result = paymentService.disputePayment(admin,"2","8","1", json)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
		
		
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
			UserInfo admin =  userService.getDefaultAdmin()
			def json = new JsonBuilder()
			json {
				"paymentTransactionId" "1"
				"jobId" "8"
				"homeownerUserId" 2
				"comment" "This is a payment that needs to be stopped Update"
			}
	
			println "Json is " +  json.toString()
			DatabaseHelper.executeQuery("UPDATE payment.payment_transaction SET skrill_transaction='', payment_status_id='3' WHERE job_id=8 and id=1")
			

		when:
			ResultInfo result = paymentService.addCommentDisputePayment(admin,"2","8","1", json)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_201)
		cleanup:
			Thread.sleep(3000)
			DatabaseHelper.executeQuery("UPDATE payment.payment_transaction SET skrill_transaction='', payment_status_id=1 WHERE id=1")
			DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id ='8' WHERE id = 8")
			DatabaseHelper.executeQuery("Delete from payment.payment_dispute_comment where payment_transaction_id = 1");
	}



	def "Test Approve a disputed payment"(){
		given:
		UserInfo admin =  userService.getDefaultAdmin()
		def json = new JsonBuilder()
		json {
			"paymentTransactionId" "1"
			"jobId" "8"
			"homeownerUserId" 2
			"comment" "This is a payment is ok and can go ahead"
		}

		println "Json is " +  json.toString()
		DatabaseHelper.executeQuery("UPDATE payment.payment_transaction SET skrill_transaction='', payment_status_id='3' WHERE job_id=8 and id=1" )
		
	
		
		when:
			ResultInfo result = paymentService.approvePayment(admin,"2","8","1", json)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
		cleanup:
			Thread.sleep(3000)
			DatabaseHelper.executeQuery("UPDATE payment.payment_transaction SET skrill_transaction='', payment_status_id='1' WHERE job_id=8")
			DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id ='8' WHERE id = 8")
			DatabaseHelper.executeQuery("Delete from payment.payment_dispute_comment where payment_transaction_id = 1");

	}



	def "test get  payment details from job "(){
		given:
			UserInfo ho =  userService.getDefaultHO()
		when:
			ResultInfo result = paymentService.getPaymentDetails(ho, "8")
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
		

	}


}

