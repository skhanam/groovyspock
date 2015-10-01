/**
 * 
 */
package com.ratedpeople.resource.payment

import groovy.json.*
import spock.lang.Specification

import com.ratedpeople.service.HomeownerService
import com.ratedpeople.service.PaymentService
import com.ratedpeople.service.TradesmanService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper


/**
 * @author shabana.khanam
 *
 */
class PreauthCreditCardFunctionalTest extends Specification{


	private HomeownerService homeownerService = new HomeownerService();
	private PaymentService paymentService = new PaymentService()
	private TradesmanService tradesmanService = new TradesmanService()

	private static final long RANDOM_JOB_ID = Math.round(Math.random()*1000);

	def "test preauth credit card"(){

		given:

		UserInfo ho =  homeownerService.getHoUser()
		UserInfo tm =  tradesmanService.createTradesmanUser()

		def json = new JsonBuilder()
		json {
			"fromUserId" ho.getId()
			"toUserId" tm.getId()
			"jobId" RANDOM_JOB_ID
			"ccToken" CommonVariable.DEFAULT_CC_TOKEN
			"currency" CommonVariable.DEFAULT_CURRENCY
			"skrillTransaction" ""
			"amount" CommonVariable.DEFAULT_AMOUNT
			"fromUserEmail" CommonVariable.DEFAULT_HO_USERNAME
			"ip" CommonVariable.DEFAULT_IP
		}

		println "Json is " +  json.toString()
		when:
		ResultInfo result = paymentService.preauth(ho, json)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_201)
		cleanup:
		DatabaseHelper.executeQuery("delete from payment.preauth_transaction where from_user_id = '${ho.getId()}'")
	}
}
