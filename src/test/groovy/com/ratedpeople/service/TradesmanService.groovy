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
final class TradesmanService{

	private static final String URL_STATUS = "/status"
	private static final String EMAIL_POSTFIX = "@gid.com"
	private static final String REGISTER_USER_TM_URI = CommonVariable.USER_SERVICE_PREFIX + "v1.0/tradesmen/register"
	private static final STATUS_URI = CommonVariable.USER_SERVICE_PREFIX + "v1.0/tradesmen/"

	private  String ACCESS_TOKEN_DYNAMIC_TM
	private  String USER_ID_DYNAMIC_TM
	private  String ACCESS_TOKEN_ADMIN
	private  String DYNAMIC_USER

	private static HttpConnectionService http = new HttpConnectionService()

	public UserInfo createAndActivateDynamicUser(){
		createDynamicUser();
		authToken()
		getUserId()
		getAdminToken()
		changeStatus()
		authToken()

		UserInfo user = new UserInfo();
		user.setId(USER_ID_DYNAMIC_TM);
		user.setToken(ACCESS_TOKEN_DYNAMIC_TM)
		user.setUsername(DYNAMIC_USER);
		return user;
	}

	public UserInfo createAdminUser(){
		UserInfo user = new UserInfo();
		getAdminToken()

		user.setToken(ACCESS_TOKEN_ADMIN)
		user.setUsername(CommonVariable.DEFAULT_ADMIN_USERNAME);
		return user;
	}

	public UserInfo createTradesmanUser(){
		UserInfo user = new UserInfo();
		authTokenTm()
		user.setId("1")
		user.setToken(ACCESS_TOKEN_DYNAMIC_TM)
		user.setUsername(CommonVariable.DEFAULT_TM_USERNAME);
		return user;
	}

	private def createDynamicUser(){
		DYNAMIC_USER = CommonVariable.TM_USER_PREFIX + System.currentTimeMillis() + EMAIL_POSTFIX

		def json = new JsonBuilder()
		json {
			"email" DYNAMIC_USER
			"password" CommonVariable.DEFAULT_PASSWORD
			"firstName" "tmprofile"
			"lastName"  "Aws"
		}
		println "Json is ${json.toString()}"


		ResultInfo result = http.callPostMethodWithoutAuthentication(REGISTER_USER_TM_URI,null,json)
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
			ACCESS_TOKEN_DYNAMIC_TM = MatcherStringUtility.getMatch("access_token=(.*)expires", result.getBody())
			println "Access Token: " + ACCESS_TOKEN_DYNAMIC_TM
		}else{
			throw new Exception("AuthToken failed " +result.getResponseCode())
		}
	}

	private def authTokenTm(){
		String token = null
		HttpConnectionService http = new HttpConnectionService()
		def query = [
			grant_type: CommonVariable.DEFAULT_PASSWORD,
			username: CommonVariable.DEFAULT_TM_USERNAME,
			password: CommonVariable.DEFAULT_PASSWORD ,
			scope: 'all'
		]
		ResultInfo result = http.callGetToken(CommonVariable.DEFAULT_GET_TOKEN_URI,query,null)
		if(result.getResponseCode()==CommonVariable.STATUS_200){
			ACCESS_TOKEN_DYNAMIC_TM = MatcherStringUtility.getMatch("access_token=(.*)expires", result.getBody())
			println "Access Token: " + ACCESS_TOKEN_DYNAMIC_TM
		}else{
			throw new Exception("AuthToken failed " +result.getResponseCode())
		}
	}

	private def getUserId() {
		HttpConnectionService http = new HttpConnectionService()

		ResultInfo result = http.callGetMethodWithAuthentication(CommonVariable.DEFAULT_ME_URI,ACCESS_TOKEN_DYNAMIC_TM,null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			USER_ID_DYNAMIC_TM = MatcherStringUtility.getMatch("userId=(.*),userName", result.getBody())
			println "User id : " + USER_ID_DYNAMIC_TM
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
		ResultInfo result = http.callGetToken(CommonVariable.DEFAULT_GET_TOKEN_URI,query,null)
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
		String url = STATUS_URI + USER_ID_DYNAMIC_TM + URL_STATUS
		ResultInfo result = http.callPutMethodWithAuthentication(url,ACCESS_TOKEN_ADMIN,query,null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Status changed"
		}else{
			throw new Exception("AuthToken failed " +result.getResponseCode())
		}
	}
}
