package com.ratedpeople.resource.billing

import groovy.json.JsonBuilder
import spock.lang.Specification

import com.ratedpeople.service.BillingService
import com.ratedpeople.service.UserService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper

class CreateBankFunctionalTest extends Specification{

	
	private UserService userService = new UserService();
	private BillingService billingService = new BillingService();
	

	def "Create bank account Success"(){
		given:
			UserInfo user = userService.getActivateDynamicTM()
			def jsonBank1 = getBank(user,"")
		when:
			ResultInfo result = billingService.createBankDetails(jsonBank1,user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_201)
	}

	def "Get bank account"(){
		given:
			UserInfo user = userService.getActivateDynamicTM()
			def jsonBank1 = getBank(user,"")
			billingService.createBankDetails(jsonBank1,user)
		when:
			ResultInfo result = billingService.getBankDetails(user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	def "Update bank account "(){
		given:
			UserInfo user = userService.getActivateDynamicTM()
			def jsonBank1 = getBank(user,"")
			billingService.createBankDetails(jsonBank1,user)
			def jsonBank2 = getBank(user,"update")

			println "Json is " +  jsonBank2.toString()
		when:
			ResultInfo result = billingService.updateBankDetails(jsonBank2, user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
		cleanup:
			DatabaseHelper.executeQuery("delete from billing.bank_details where user_id = '${user.getId()}'")
	}

	private def getBank(UserInfo user,String additionInfo){
		def json = new JsonBuilder()
		Integer RANDOM_BANK_ACCOUNT = (Math.random()*9000000)+10000000;
		json {
			"userId"  user.getId()
			"beneficiaryName" CommonVariable.DEFAULT_BENEFICIARY_NAME + " additionInfo"
			"bankCode" CommonVariable.DEFAULT_BANKCODE
			"accountNumber"  RANDOM_BANK_ACCOUNT.toString()
			"bankAccountType" CommonVariable.DEFAULT_BANK_ACCOUNT_TYPE
			"country" CommonVariable.DEFAULT_BANK_COUNTRY
		}
		return json;
	}
}
