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
final class BillingService{

	private static final String BILLING_URI_PREFIX = CommonVariable.BILLING_SERVICE_PREFIX + "v1.0/users/"

	private static final HttpConnectionService http = new HttpConnectionService()

	public ResultInfo getAllBillingsForTm(final UserInfo userInfo){
		String url = BILLING_URI_PREFIX + userInfo.getId() +"/billingdetails"

		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}else{
			throw new Exception("Failed " +result.getResponseCode())
		}
		
		return result;
	}

	public ResultInfo getSingleBillingDetailsForTm(final UserInfo userInfo, final String billingId){
		String url = BILLING_URI_PREFIX + userInfo.getId() +"/billingdetails/"+billingId

		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}else{
			throw new Exception("Failed " +result.getResponseCode())
		}
		
		return result;
	}

	public def createBankDetails(final def bank, final UserInfo userInfo){
		String url = BILLING_URI_PREFIX + userInfo.getId() + "/bankdetails"
		ResultInfo result = http.callPostMethodWithAuthentication(url, userInfo.getToken(), bank)
		
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Created"
		}else{
			throw new Exception("Failed " +result.getResponseCode())
		}
		
		return result;
	}

	public ResultInfo getBankDetails(final UserInfo userInfo ){
		String url = BILLING_URI_PREFIX + userInfo.getId() +"/bankdetails"

		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}else{
			throw new Exception("Failed " +result.getResponseCode())
		}
		
		return result;
	}

	public ResultInfo updateBankDetails(final def bank, final UserInfo userInfo){
		String url = BILLING_URI_PREFIX + userInfo.getId() + "/bankdetails"
		
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(), null, bank)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}else{
			throw new Exception("Failed " +result.getResponseCode())
		}
		
		return result;
	}
}
