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
class BillingService{

	private static final String BILLING_URI_PREFIX = CommonVariable.BILLING_SERVICE_PREFIX + "v1.0/users/"

	HttpConnectionService http = new HttpConnectionService()

	public def getAllBillingsForTm(UserInfo userInfo){

		String url = BILLING_URI_PREFIX + userInfo.getId() +"/billingdetails"

		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}else{
			throw new Exception("Failed " +result.getResponseCode())
		}
		return result;
	}

	public def getSingleBillingDetailsForTm(UserInfo userInfo,String billingId){

		String url = BILLING_URI_PREFIX + userInfo.getId() +"/billingdetails/"+billingId

		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}else{
			throw new Exception("Failed " +result.getResponseCode())
		}
		return result;
	}


	public def createBankDetails(def bank,UserInfo userInfo){

		String url = BILLING_URI_PREFIX + userInfo.getId() + "/bankdetails"
		ResultInfo result = http.callPostMethodWithAuthentication(url, userInfo.getToken(), bank)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Created"
		}else{
			throw new Exception("Failed " +result.getResponseCode())
		}
		return result;
	}



	public def getBankDetails(UserInfo userInfo ){

		String url = BILLING_URI_PREFIX + userInfo.getId() +"/bankdetails"

		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}else{
			throw new Exception("Failed " +result.getResponseCode())
		}
		return result;
	}

	public def updateBankDetails(def bank,UserInfo userInfo){

		String url = BILLING_URI_PREFIX + userInfo.getId() + "/bankdetails"
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(),null, bank)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}else{
			throw new Exception("Failed " +result.getResponseCode())
		}
		return result;
	}
}
