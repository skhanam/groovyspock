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
class TMProfileService{


	HttpConnectionService http = new HttpConnectionService()

	private static final String PROFILE_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"

	private static final String MATCH_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/match"


	public def getAllProfiles(UserInfo userInfo){

		String url = PROFILE_PREFIX + "v1.0/allprofiles"
		def query = [
			freeText: CommonVariable.DEFAULT_TM_FIRSTNAME,
			offset:0
		]

		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(),query)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}
		return result;
	}

	public def getMatchingTm(def query){

		String url = MATCH_PREFIX


		ResultInfo result = http.callGetMethod(url,query)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}
		return result;
	}



	public def addTrade(UserInfo userInfo,def trade){

		String url = PROFILE_PREFIX + userInfo.getId() + "/profiletrades"
		ResultInfo result = http.callPostMethodWithAuthentication(url, userInfo.getToken(),trade)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Created"
		}
		return result;
	}

	public def updateTrade(UserInfo userInfo,String profileTradeId,def trade){

		String url = PROFILE_PREFIX + userInfo.getId() + "/profiletrades/"+profileTradeId
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(),null, trade)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}

	public def getTrade(UserInfo userInfo){

		String url = PROFILE_PREFIX + userInfo.getId() + "/profiletrades/"
		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}
		return result;
	}

	public def getTmInfo(UserInfo userInfo){

		String url = PROFILE_PREFIX + userInfo.getId()
		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}
		return result;
	}

	public def getTmProfile(UserInfo userInfo){

		String url = PROFILE_PREFIX + userInfo.getId()+"/profiles"
		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}
		return result;
	}

	public def updateTmProfile(UserInfo userInfo,def tmprofile){

		String url = PROFILE_PREFIX + userInfo.getId() + "/profiles"
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(),null, tmprofile)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}


	public def deleteTrade(UserInfo userInfo,String profileTradeId){

		String url = PROFILE_PREFIX + userInfo.getId() + "/profiletrades/"+profileTradeId
		ResultInfo result = http.callDeleteMethodWithAuthentication(url, userInfo.getToken())
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Deleted"
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

	public def updateAddress(def address,String addressId,UserInfo userInfo){

		String url = PROFILE_PREFIX + userInfo.getId() + "/addresses/"+addressId
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(),null, address)
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

	public def updateAvailability(UserInfo userInfo,def body){

		String url = PROFILE_PREFIX + userInfo.getId() + "/availability"
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(),null, body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}

	public def getAvailability(UserInfo userInfo ){

		String url = PROFILE_PREFIX + userInfo.getId() + "/availability"
		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "OK"
		}
		return result;
	}

	public def updateEmailToken(UserInfo userInfo){

		String url = PROFILE_PREFIX + userInfo.getId() + "/email/token"
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(),null, null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}

	public def validateEmailToken(UserInfo userInfo,def body){

		String url = PROFILE_PREFIX + userInfo.getId() + "/email/verify"
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(),null, body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}

	public def postImage(UserInfo userInfo,def query){

		String url = PROFILE_PREFIX + userInfo.getId() + "/images"
		ResultInfo result = http.callPostMethodWithAuthenticationAndImage(url, userInfo.getToken(), query)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Created"
		}
		return result;
	}

	public def getImage(UserInfo userInfo){

		String url = PROFILE_PREFIX + userInfo.getId() + "/images"
		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(), null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "OK"
		}
		return result;
	}


	public def createPhone(UserInfo userInfo,def phone){

		String url = PROFILE_PREFIX + userInfo.getId() + "/phones"
		ResultInfo result = http.callPostMethodWithAuthentication(url, userInfo.getToken(), phone)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Created"
		}
		return result;
	}

	public def updatePhone(UserInfo userInfo,String phoneId,def phone){

		String url = PROFILE_PREFIX + userInfo.getId() + "/phones/"+phoneId
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(),null, phone)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}

	public def getPhone(UserInfo userInfo,String phoneId){

		String url = PROFILE_PREFIX + userInfo.getId() + "/phones/"+phoneId
		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(), null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}
		return result;
	}


	public def createWorkingArea(UserInfo userInfo,def workingarea){

		String url = PROFILE_PREFIX + userInfo.getId() + "/workingareas"
		ResultInfo result = http.callPostMethodWithAuthentication(url, userInfo.getToken(), workingarea)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Created"
		}
		return result;
	}

	public def updateWorkingarea(UserInfo userInfo,String workingAreaId,def workingarea){
		String url = PROFILE_PREFIX + userInfo.getId() + "/workingareas/"+workingAreaId
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(),null, workingarea)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}

	public def getWorkingarea(UserInfo userInfo,String workingareaId){

		String url = PROFILE_PREFIX + userInfo.getId() + "/workingareas/"+workingareaId
		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(), null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}
		return result;
	}
}
