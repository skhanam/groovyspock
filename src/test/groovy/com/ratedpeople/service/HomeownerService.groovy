/**
 * 
 */
package com.ratedpeople.service

import groovy.json.*

import com.ratedpeople.service.utility.HttpConnectionService
import com.ratedpeople.service.utility.MatcherStringUtility
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
/**
 * @author shabana.khanam
 *
 */
class HomeownerService{

	private static final String HOMEOWNER_URI_PREFIX = CommonVariable.USER_SERVICE_PREFIX + "v1.0/homeowners/"
	private static final String USER_URI_PREFIX = CommonVariable.USER_SERVICE_PREFIX + "v1.0/users/"
	private static final String EMAIL_POSTFIX = "@gid.com"

	private String ACCESS_TOKEN_DYNAMIC_HO
	private String USER_ID_DYNAMIC_HO
	private String ACCESS_TOKEN_ADMIN
	private String DYNAMIC_USER

	private static final HttpConnectionService http = new HttpConnectionService()

	public UserInfo createAndActivateDynamicUser(){
		createDynamicUser();
		authToken()
		getUserId()
		getAdminToken()
		changeStatus()
		authToken()

		UserInfo user = new UserInfo();
		user.setId(USER_ID_DYNAMIC_HO);
		user.setToken(ACCESS_TOKEN_DYNAMIC_HO)
		user.setUsername(DYNAMIC_USER);

		return user;
	}

	public UserInfo getAdminUser(){
		UserInfo user = new UserInfo();
		getAdminToken()

		user.setToken(ACCESS_TOKEN_ADMIN)
		user.setUsername(CommonVariable.DEFAULT_ADMIN_USERNAME);
		return user;
	}

	public UserInfo getHoUser(){
		UserInfo user = new UserInfo();
		authTokenHo()
		user.setId("2")
		user.setToken(ACCESS_TOKEN_DYNAMIC_HO)
		user.setUsername(CommonVariable.DEFAULT_HO_USERNAME);
		return user;
	}

	private def createDynamicUser(){
		DYNAMIC_USER = CommonVariable.HO_USER_PREFIX + System.currentTimeMillis() + EMAIL_POSTFIX

		def json = new JsonBuilder()
		json {
			"email" DYNAMIC_USER
			"password" CommonVariable.DEFAULT_PASSWORD
			"firstName" CommonVariable.DEFAULT_HO_FIRSTNAME
			"lastName"  CommonVariable.DEFAULT_HO_LASTNAME
		}
		println "Json is ${json.toString()}"


		ResultInfo result = http.callPostMethodWithoutAuthentication(HOMEOWNER_URI_PREFIX + "register", null, json)
		if(result.getResponseCode()==CommonVariable.STATUS_201){
			println "OK"
		}else{
			throw new Exception("Failed " +result.getResponseCode())
		}
	}

	private def authToken(){
		String token = null
		HttpConnectionService http = new HttpConnectionService()
		def query = [
			grant_type: CommonVariable.DEFAULT_PASSWORD,
			username: DYNAMIC_USER,
			password: CommonVariable.DEFAULT_PASSWORD ,
			scope: 'all'
		]

		ResultInfo result = http.callGetToken(CommonVariable.DEFAULT_GET_TOKEN_URI,query,null)
		if(result.getResponseCode()==CommonVariable.STATUS_200){
			ACCESS_TOKEN_DYNAMIC_HO = MatcherStringUtility.getMatch("access_token=(.*)expires", result.getBody())
			println "Access Token: " + ACCESS_TOKEN_DYNAMIC_HO
		}else{
			throw new Exception("AuthToken failed " +result.getResponseCode())
		}
	}

	private def authTokenHo(){
		String token = null
		HttpConnectionService http = new HttpConnectionService()
		def query = [
			grant_type: CommonVariable.DEFAULT_PASSWORD,
			username: CommonVariable.DEFAULT_HO_USERNAME,
			password: CommonVariable.DEFAULT_PASSWORD ,
			scope: 'all'
		]

		ResultInfo result = http.callGetToken(CommonVariable.DEFAULT_GET_TOKEN_URI,query,null)
		if(result.getResponseCode()==CommonVariable.STATUS_200){
			ACCESS_TOKEN_DYNAMIC_HO = MatcherStringUtility.getMatch("access_token=(.*)expires", result.getBody())
			println "Access Token: " + ACCESS_TOKEN_DYNAMIC_HO
		}else{
			throw new Exception("AuthToken failed " +result.getResponseCode())
		}
	}

	private def getUserId() {
		HttpConnectionService http = new HttpConnectionService()

		ResultInfo result = http.callGetMethodWithAuthentication(CommonVariable.DEFAULT_ME_URI,ACCESS_TOKEN_DYNAMIC_HO,null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			USER_ID_DYNAMIC_HO = MatcherStringUtility.getMatch("userId=(.*),userName", result.getBody())
			println "User id : " + USER_ID_DYNAMIC_HO
		}else{
			throw new Exception("Error " +result.getResponseCode())
		}
	}


	private def getAdminToken(){
		String token = null
		def query = [
			grant_type: CommonVariable.DEFAULT_PASSWORD,
			username: CommonVariable.DEFAULT_ADMIN_USERNAME,
			password: CommonVariable.DEFAULT_PASSWORD,
			scope: 'all'
		]

		HttpConnectionService http = new HttpConnectionService()
		ResultInfo result = http.callGetToken(CommonVariable.DEFAULT_GET_TOKEN_URI, query, null)

		if(result.getResponseCode()==CommonVariable.STATUS_200){
			ACCESS_TOKEN_ADMIN = MatcherStringUtility.getMatch("access_token=(.*)expires", result.getBody())
			println "Access Token: " + ACCESS_TOKEN_ADMIN
		}else{
			throw new Exception("AuthToken failed " +result.getResponseCode())
		}
	}

	private def changeStatus() {
		String token = null
		def query = [
			status: CommonVariable.STATUS_ACTIVE
		]

		HttpConnectionService http = new HttpConnectionService()
		String url = HOMEOWNER_URI_PREFIX + USER_ID_DYNAMIC_HO + "/status"
		ResultInfo result = http.callPutMethodWithAuthentication(url,ACCESS_TOKEN_ADMIN,query,null)

		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Status changed"
		}else{
			throw new Exception("AuthToken failed " +result.getResponseCode())
		}
	}


	public def getHoInfo(UserInfo userInfo){
		String url = USER_URI_PREFIX + userInfo.getId()

		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}

		return result;
	}
	
	public def getPasswordToken(UserInfo userInfo){
		String url =  USER_URI_PREFIX + userInfo.getId() + "/password/token"

		ResultInfo result = http.callPostMethodWithoutAuthentication(url,null,null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Ok"
		}

		return result;
	}
	
	public def updatePassword(UserInfo userInfo,def body){
		String url =  USER_URI_PREFIX + userInfo.getId() + "/password/reset"

		ResultInfo result = http.callPutMethodWithoutAuthentication(url,null,body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}

		return result;
	}
	
	
	public ResultInfo updateEmailToken(UserInfo userInfo){
		String url = USER_URI_PREFIX + userInfo.getId() + "/email/token"
		
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(),null, null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		
		return result;
	}

	public ResultInfo validateEmailToken(UserInfo userInfo,def body){
		String url = USER_URI_PREFIX + userInfo.getId() + "/email/verify"
		
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(),null, body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		
		return result;
	}

	
	
	
}
