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
class LocationService{

	private static final String POSTCODE_PREFIX = CommonVariable.LOCATION_SERVICE_PREFIX + "v1.0/postcodes/"

	HttpConnectionService http = new HttpConnectionService()

	private def getPostcode(String postcode){

		String url = POSTCODE_PREFIX +postcode

		ResultInfo result = http.callGetMethod(url, null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}
		return result;
	}

	private def getAddressInfo(String postcode){

		String url = POSTCODE_PREFIX +postcode+"/addresses"

		ResultInfo result = http.callGetMethod(url, null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}
		return result;
	}

	private def updateArea(String postcode,def body){

		String url =  POSTCODE_PREFIX + postcode
		ResultInfo result = http.callPutMethod(url,null,body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}
}
