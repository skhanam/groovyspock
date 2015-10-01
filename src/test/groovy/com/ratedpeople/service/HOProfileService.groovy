package com.ratedpeople.service

import groovy.json.*
import com.ratedpeople.service.utility.HttpConnectionService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable

final class HOProfileService{

	private static final PROFILE_PREFIX = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/users/"
	private static final String EMAIL_POSTFIX = "@gid.com"

	private static HttpConnectionService http = new HttpConnectionService()

	public ResultInfo getAllProfiles(final UserInfo userInfo){
		String url = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/allprofiles"
		def query = [
			freeText: CommonVariable.DEFAULT_HO_FIRSTNAME,
			offset:0
		]

		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(),query)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}

		return result;
	}

	public ResultInfo createAddress(final def address, final UserInfo userInfo){
		String url = PROFILE_PREFIX + userInfo.getId() + "/addresses"
		ResultInfo result = http.callPostMethodWithAuthentication(url, userInfo.getToken(), address)

		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Created"
		}

		return result;
	}

	public ResultInfo updateAddress(final def address, final UserInfo userInfo){
		String url = PROFILE_PREFIX + userInfo.getId() + "/addresses"
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(), null, address)

		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}

		return result;
	}

	public ResultInfo updateProfile(final def profile, final UserInfo userInfo){
		String url = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/users/" +userInfo.getId() +"/profiles"
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(), null, profile)

		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}

		return result;
	}

	public ResultInfo getAddress(final UserInfo userInfo){
		String url = PROFILE_PREFIX + userInfo.getId() + "/addresses"
		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(), null)

		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}

		return result;
	}

	public ResultInfo getHomeownerProfile(final UserInfo userInfo){
		String url = PROFILE_PREFIX + userInfo.getId() + "/profiles"
		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(), null)

		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}else{
			throw new Exception("Failed " +result.getResponseCode())
		}

		return result;
	}

	public ResultInfo resetPin(final UserInfo userInfo, final String phoneId){
		String url = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/users/" +userInfo.getId() +"/phones/"+phoneId+"/resetpin"
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(), null, null)

		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}

		return result;
	}

	public ResultInfo verifyPin(final UserInfo userInfo, final String phoneId, final def phone){
		String url = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/users/" +userInfo.getId() +"/phones/"+phoneId+"/verify"
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(), null, phone)

		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}

		return result;
	}
}
