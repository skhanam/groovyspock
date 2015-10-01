/**
 * 
 */
package com.ratedpeople.resource.payment

import groovy.json.*
import groovyx.net.http.ContentType
import groovyx.net.http.Method
import spock.lang.Specification

import com.ratedpeople.service.HomeownerService;
import com.ratedpeople.service.PaymentService;
import com.ratedpeople.service.utility.ResultInfo;
import com.ratedpeople.service.utility.UserInfo;
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper


/**
 * @author shabana.khanam
 *
 */
class PaymentRequestFunctionalTest extends Specification{

	private HomeownerService homeownerService = new HomeownerService();
	private PaymentService paymentService = new PaymentService()

	private static final long RANDOM_JOB_ID = Math.round(Math.random()*1000);

	def "test request payment "(){
		given:
		UserInfo user =  homeownerService.getHoUser()
		def json = new JsonBuilder()
		json { "ipAddress" CommonVariable.DEFAULT_IP }

		println "Json is " +  json.toString()
		def getSkrillID1 = DatabaseHelper.executeQuery("select skrill_transaction from  payment.payment_transaction WHERE job_id=8")
		println "Skrill transaction Id : " + getSkrillID1
		if(getSkrillID1.equals(true)){
			DatabaseHelper.executeQuery("UPDATE payment.payment_transaction SET skrill_transaction='', payment_status_id='1' WHERE job_id=8")
		}
		
		when:
			
			ResultInfo result = paymentService.postPayment(user,"8", json)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
		
		cleanup:
		Thread.sleep(3000)
		DatabaseHelper.executeQuery("UPDATE payment.payment_transaction SET skrill_transaction='', payment_status_id='1' WHERE job_id=8")
		def getSkrillID = DatabaseHelper.executeQuery("select skrill_transaction from  payment.payment_transaction WHERE job_id=8")
		println "Skrill transaction Id : " + getSkrillID
		DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id ='8' WHERE id = 8")
		
	}
}

