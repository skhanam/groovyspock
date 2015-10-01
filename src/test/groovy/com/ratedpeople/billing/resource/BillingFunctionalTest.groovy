/**
 * 
 */
package com.ratedpeople.billing.resourceimport groovyx.net.http.ContentType
import groovyx.net.http.Method
import spock.lang.Specification

import com.ratedpeople.service.BillingService
import com.ratedpeople.service.TradesmanService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable


/**
 * @author Shabana
 *
 */



class BillingFunctionalTest extends Specification{



	private BillingService billingService = new BillingService();
	private TradesmanService tradesmanService = new TradesmanService();

	def "Get list billing "(){
		given:
		UserInfo user = tradesmanService.createTradesmanUser()
		when:
		ResultInfo result = billingService.getAllBillingsForTm(user)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	def "Get details about billing "(){
		given:

		UserInfo user = tradesmanService.createTradesmanUser()
		when:
		ResultInfo result = billingService.getSingleBillingDetailsForTm(user,"2")
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_200)
	}
}
