/**
 * 
 */
package com.ratedpeople.service

import groovy.json.*

import com.ratedpeople.service.utility.HttpConnectionService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
/**
 * @author shabana.khanam
 *
 */
class PaymentService{

	private final String CREDIT_CARD_RESOURCE_URI = CommonVariable.PAYMENT_SERVICE_PREFIX + "v1.0/users/"

	HttpConnectionService http = new HttpConnectionService()

	private def postCreditCard(UserInfo userInfo,def creditCard){

		String url = CREDIT_CARD_RESOURCE_URI + userInfo.getId() +"/cards"

		ResultInfo result = http.callPostMethodWithAuthentication(url, userInfo.getToken(),creditCard)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Ok"
		}
		return result;
	}

	private def getCreditCard(UserInfo userInfo ){

		String url = CREDIT_CARD_RESOURCE_URI + userInfo.getId() +"/cards"

		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}else{
			throw new Exception("Failed " +result.getResponseCode())
		}
		return result;
	}

	private def updateBankDetails(def bank,UserInfo userInfo){

		String url = BILLING_URI_PREFIX + userInfo.getId() + "/bankdetails"
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(),null, bank)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}
}
