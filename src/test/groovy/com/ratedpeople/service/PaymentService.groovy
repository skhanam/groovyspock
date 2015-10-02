package com.ratedpeople.service

import groovy.json.*
import com.ratedpeople.service.utility.HttpConnectionService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable

final class PaymentService{

	private final String PAYMENT_RESOURCE_URI = CommonVariable.PAYMENT_SERVICE_PREFIX + "v1.0/users/"

	private static HttpConnectionService http = new HttpConnectionService()

	public def postCreditCard(UserInfo userInfo,def creditCard){
		String url = PAYMENT_RESOURCE_URI + userInfo.getId() +"/cards"

		ResultInfo result = http.callPostMethodWithAuthentication(url, userInfo.getToken(),creditCard)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Ok"
		}

		return result;
	}

	public ResultInfo getCreditCard(UserInfo userInfo ){
		String url = PAYMENT_RESOURCE_URI + userInfo.getId() +"/cards"

		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}

		return result;
	}

	public ResultInfo getPaymentDetails(UserInfo userInfo,String jobId ){
		String url = PAYMENT_RESOURCE_URI + userInfo.getId() +"/jobs/"+jobId+"/payments"

		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}

		return result;
	}

	public ResultInfo updateBankDetails(def bank,UserInfo userInfo){
		String url = PAYMENT_RESOURCE_URI + userInfo.getId() + "/bankdetails"

		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(),null, bank)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}

		return result;
	}

	public ResultInfo preauth(UserInfo userInfo,def card){
		String url = PAYMENT_RESOURCE_URI + userInfo.getId() + "/preauth"

		ResultInfo result = http.callPostMethodWithAuthentication(url, userInfo.getToken(), card)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Created"
		}

		return result;
	}


	public ResultInfo postPayment(UserInfo userInfo,String jobId,def body){

		String url = PAYMENT_RESOURCE_URI + userInfo.getId() + "/jobs/"+jobId
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(),null, body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "UPdated"
		}

		return result;
	}

	public ResultInfo disputePayment(UserInfo userInfo,String hoId,String jobId,String pamyentId, def body){

		String url = PAYMENT_RESOURCE_URI + hoId + "/jobs/"+jobId+"/payments/"+pamyentId+"/dispute"
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(),null, body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}

		return result;
	}

	public ResultInfo approvePayment(UserInfo userInfo,String hoId,String jobId,String pamyentId, def body){

		String url = PAYMENT_RESOURCE_URI + hoId + "/jobs/"+jobId+"/payments/"+pamyentId+"/approve"
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(),null, body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}

		return result;
	}

	public ResultInfo addCommentDisputePayment(UserInfo userInfo,String hoId,String jobId,String pamyentId, def body){

		String url = PAYMENT_RESOURCE_URI + hoId + "/jobs/"+jobId+"/payments/"+pamyentId+"/dispute"
		ResultInfo result = http.callPostMethodWithAuthentication(url, userInfo.getToken(), body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}

		return result;
	}
}
