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
final class TMProfileService{

	private static final String PROFILE_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"
	private static final String MATCH_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/match"

	private static HttpConnectionService http = new HttpConnectionService()

	public ResultInfo getAllProfiles(UserInfo userInfo){
		String url = PROFILE_PREFIX + "v1.0/allprofiles"
		def query = [
			freeText: CommonVariable.DEFAULT_TM_FIRSTNAME,
			offset:0
		]

		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(),query)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}

		return result;
	}

	public ResultInfo getMatchingTm(def query){
		String url = MATCH_PREFIX

		ResultInfo result = http.callGetMethodWithoutAuthentication(url, query)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}
		return result;
	}

	public ResultInfo addTrade(UserInfo userInfo,def trade){
		String url = PROFILE_PREFIX + userInfo.getId() + "/profiletrades"

		ResultInfo result = http.callPostMethodWithAuthentication(url, userInfo.getToken(),trade)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Created"
		}

		return result;
	}

	public ResultInfo updateTrade(UserInfo userInfo,String profileTradeId,def trade){
		String url = PROFILE_PREFIX + userInfo.getId() + "/profiletrades/"+profileTradeId

		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(),null, trade)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}

		return result;
	}

	public ResultInfo getTrade(UserInfo userInfo){
		String url = PROFILE_PREFIX + userInfo.getId() + "/profiletrades/"

		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}

		return result;
	}

	

	public ResultInfo getTmProfile(UserInfo userInfo){
		String url = PROFILE_PREFIX + userInfo.getId()+"/profiles"
		
		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}
		
		return result;
	}

	public ResultInfo updateTmProfile(UserInfo userInfo,def tmprofile){
		String url = PROFILE_PREFIX + userInfo.getId() + "/profiles"
		
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(),null, tmprofile)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		
		return result;
	}


	public ResultInfo deleteTrade(UserInfo userInfo,String profileTradeId){
		String url = PROFILE_PREFIX + userInfo.getId() + "/profiletrades/"+profileTradeId
		
		ResultInfo result = http.callDeleteMethodWithAuthentication(url, userInfo.getToken())
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Deleted"
		}
		
		return result;
	}

	public ResultInfo createAddress(def address,UserInfo userInfo){
		String url = PROFILE_PREFIX + userInfo.getId() + "/addresses"
		
		ResultInfo result = http.callPostMethodWithAuthentication(url, userInfo.getToken(), address)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Created"
		}
		
		return result;
	}

	public ResultInfo updateAddress(def address,String addressId,UserInfo userInfo){
		String url = PROFILE_PREFIX + userInfo.getId() + "/addresses/"+addressId
		
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(),null, address)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		
		return result;
	}

	public ResultInfo getAddress(UserInfo userInfo){
		String url = PROFILE_PREFIX + userInfo.getId() + "/addresses"
		
		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(), null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}
		
		return result;
	}

	public ResultInfo updateAvailability(UserInfo userInfo,def body){
		String url = PROFILE_PREFIX + userInfo.getId() + "/availability"
		
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(),null, body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		
		return result;
	}

	public ResultInfo getAvailability(UserInfo userInfo ){

		String url = PROFILE_PREFIX + userInfo.getId() + "/availability"
		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "OK"
		}
		return result;
	}

	public ResultInfo updateEmailToken(UserInfo userInfo){
		String url = PROFILE_PREFIX + userInfo.getId() + "/email/token"
		
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(),null, null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		
		return result;
	}

	public ResultInfo validateEmailToken(UserInfo userInfo,def body){
		String url = PROFILE_PREFIX + userInfo.getId() + "/email/verify"
		
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(),null, body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		
		return result;
	}

	public ResultInfo postImage(UserInfo userInfo,def query){
		String url = PROFILE_PREFIX + userInfo.getId() + "/images"
		
		ResultInfo result = http.callPostMethodWithAuthenticationAndImage(url, userInfo.getToken(), query)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Created"
		}
		
		return result;
	}

	public ResultInfo getImage(UserInfo userInfo){
		String url = PROFILE_PREFIX + userInfo.getId() + "/images"
		
		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(), null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "OK"
		}
		
		return result;
	}

	public ResultInfo createPhone(UserInfo userInfo,def phone){
		String url = PROFILE_PREFIX + userInfo.getId() + "/phones"
		
		ResultInfo result = http.callPostMethodWithAuthentication(url, userInfo.getToken(), phone)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Created"
		}
		
		return result;
	}

	public ResultInfo updatePhone(UserInfo userInfo,String phoneId,def phone){
		String url = PROFILE_PREFIX + userInfo.getId() + "/phones/"+phoneId
		
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(),null, phone)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		
		return result;
	}

	public ResultInfo getPhone(UserInfo userInfo,String phoneId){
		String url = PROFILE_PREFIX + userInfo.getId() + "/phones/"+phoneId
		
		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(), null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}
		
		return result;
	}

	public ResultInfo createWorkingArea(UserInfo userInfo,def workingarea){
		String url = PROFILE_PREFIX + userInfo.getId() + "/workingareas"
		
		ResultInfo result = http.callPostMethodWithAuthentication(url, userInfo.getToken(), workingarea)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Created"
		}
		
		return result;
	}

	public ResultInfo updateWorkingarea(UserInfo userInfo,String workingAreaId,def workingarea){
		String url = PROFILE_PREFIX + userInfo.getId() + "/workingareas/"+workingAreaId
		
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(),null, workingarea)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		
		return result;
	}

	public ResultInfo getWorkingarea(UserInfo userInfo,String workingareaId){
		String url = PROFILE_PREFIX + userInfo.getId() + "/workingareas/"+workingareaId
		
		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(), null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}
		
		return result;
	}
}
