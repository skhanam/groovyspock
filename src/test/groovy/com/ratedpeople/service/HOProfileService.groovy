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
class HOProfileService{

	private static final String URL_STATUS = "/status"

	private static final String REGISTER = "register"

	private static final String HOMEOWNER_URI_PREFIX = CommonVariable.USER_SERVICE_PREFIX + "v1.0/homeowners/"

	private static final String EMAIL_POSTFIX = "@gid.com"

	private  String ACCESS_TOKEN_DYNAMIC_HO
	//private  String REFRESH_TOKEN_DYNAMIC_HO
	private  String USER_ID_DYNAMIC_HO
	private  String ACCESS_TOKEN_ADMIN
	//private  String REFRESH_TOKEN_ADMIN

	private  String DYNAMIC_USER

	private static final PROFILE_PREFIX = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/users/"

	HttpConnectionService http = new HttpConnectionService()



	public def getAllProfiles(UserInfo userInfo){

		String url = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/allprofiles"
		def query = [
			freeText: CommonVariable.DEFAULT_HO_FIRSTNAME,
			offset:0
		]

		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(),query)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}
		return result;
	}

	public def createAddress(def address,UserInfo userInfo){

		String url = PROFILE_PREFIX + userInfo.getId() + "/addresses"
		ResultInfo result = http.callPostMethodWithAuthentication(url, userInfo.getToken(), address)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Created"
		}
		return result;
	}

	public def updateAddress(def address,UserInfo userInfo){

		String url = PROFILE_PREFIX + userInfo.getId() + "/addresses"
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(),null, address)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}

	public def updateProfile(def profile,UserInfo userInfo){

		String url = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/users/" +userInfo.getId() +"/profiles"
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(),null, profile)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}


	public def getAddress(UserInfo userInfo){

		String url = PROFILE_PREFIX + userInfo.getId() + "/addresses"
		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(), null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}
		return result;
	}

	public def getHomeownerProfile(UserInfo userInfo){

		String url = PROFILE_PREFIX + userInfo.getId() + "/profiles"
		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(), null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}else{
			throw new Exception("Failed " +result.getResponseCode())
		}
		return result;
	}

	public def resetPin(UserInfo userInfo,String phoneId){

		String url = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/users/" +userInfo.getId() +"/phones/"+phoneId+"/resetpin"
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(),null, null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}

	public def verifyPin(UserInfo userInfo,String phoneId,def phone){

		String url = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/users/" +userInfo.getId() +"/phones/"+phoneId+"/verify"
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(),null, phone)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}

}
