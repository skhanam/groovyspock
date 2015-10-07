package com.ratedpeople.resource.billing
import spock.lang.Specification

import com.ratedpeople.service.BillingService
import com.ratedpeople.service.UserService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable

class BillingFunctionalTest extends Specification{
	private BillingService billingService = new BillingService();
	private UserService userService = new UserService();

	def "Get list billing "(){
		given:
			UserInfo user = userService.getDefaultTM()
		when:
			ResultInfo result = billingService.getAllBillingsForTm(user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	def "Get details about billing "(){
		given:
			UserInfo user = userService.getDefaultTM()
		when:
			ResultInfo result = billingService.getSingleBillingDetailsForTm(user,"2")
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}
}
