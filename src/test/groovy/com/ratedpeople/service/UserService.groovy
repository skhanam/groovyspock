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
class UserService{

	private static final String HOMEOWNER_URI_PREFIX = CommonVariable.USER_SERVICE_PREFIX + "v1.0/homeowners/"
	private static final String TRADESMAN_URI_PREFIX = CommonVariable.USER_SERVICE_PREFIX + "v1.0/tradesmen/"
	private static final String USER_URI_PREFIX = CommonVariable.USER_SERVICE_PREFIX + "v1.0/users/"
	private static final String USER_PROXY_URI_PREFIX = CommonVariable.USERPROXY_SERVICE_PREFIX + "v1.0/users/"
	private static final String EMAIL_POSTFIX = "@gid.com"

	private static final HttpConnectionService http = new HttpConnectionService()

	public UserInfo getActivateDynamicHO(){
		UserInfo user = getPendingDynamicHO();
		UserInfo admin = getDefaultAdmin();
		changeStatus(admin,user)
		authToken(user)
		return user;
	}

	public UserInfo getActivateDynamicTM(){
		UserInfo tm = getPendingDynamicTm();
		UserInfo admin = getDefaultAdmin();
		changeStatusTm(admin,tm)
		authToken(tm)
		Thread.sleep(400)
		return tm;
	}

	public UserInfo getPendingDynamicHO(){
		UserInfo user = createDynamicUser();
		authToken(user)
		getUserId(user)
		return user;
	}

	public UserInfo getPendingDynamicTm(){
		UserInfo user = createDynamicTm();
		authToken(user)
		getUserId(user)
		return user;
	}

	public UserInfo getDefaultAdmin(){
		UserInfo admin = createAdminUser();
		authToken(admin)
		return admin;
	}

	public UserInfo getDefaultHO(){
		UserInfo user = createDefaultUser();
		authToken(user)
		return user;
	}

	public UserInfo getDefaultTM(){
		UserInfo tm = createDefaultTMUser()
		authToken(tm)
		return tm;
	}

	private def createDynamicUser(){
		UserInfo user = new UserInfo();

		String DYNAMIC_USER = CommonVariable.HO_USER_PREFIX + System.currentTimeMillis() + EMAIL_POSTFIX

		def json = new JsonBuilder()
		json {
			"email" DYNAMIC_USER
			"password" CommonVariable.DEFAULT_PASSWORD
			"firstName" CommonVariable.DEFAULT_HO_FIRSTNAME
			"lastName"  CommonVariable.DEFAULT_HO_LASTNAME
		}
		user.setUsername(DYNAMIC_USER)
		user.setPassword(CommonVariable.DEFAULT_PASSWORD)
		println "Json is ${json.toString()}"


		ResultInfo result = http.callPostMethodWithoutAuthentication(HOMEOWNER_URI_PREFIX + "register", null, json)
		if(result.getResponseCode()==CommonVariable.STATUS_201){
			println "OK"
			return user
		}else{
			throw new Exception("Failed " +result.getResponseCode())
		}
	}

	private def createDynamicTm(){
		UserInfo user = new UserInfo();

		String DYNAMIC_USER = CommonVariable.TM_USER_PREFIX + System.currentTimeMillis() + EMAIL_POSTFIX

		def json = new JsonBuilder()
		json {
			"email" DYNAMIC_USER
			"password" CommonVariable.DEFAULT_PASSWORD
			"firstName" CommonVariable.DEFAULT_TM_FIRSTNAME
			"lastName"  CommonVariable.DEFAULT_TM_LASTNAME
		}
		user.setUsername(DYNAMIC_USER)
		user.setPassword(CommonVariable.DEFAULT_PASSWORD)
		println "Json is ${json.toString()}"


		ResultInfo result = http.callPostMethodWithoutAuthentication(TRADESMAN_URI_PREFIX + "register", null, json)
		if(result.getResponseCode()==CommonVariable.STATUS_201){
			println "OK"
			return user
		}else{
			throw new Exception("Failed " +result.getResponseCode())
		}
	}

	private def createAdminUser(){
		UserInfo user = new UserInfo();
		user.setUsername(CommonVariable.DEFAULT_ADMIN_USERNAME)
		user.setPassword(CommonVariable.DEFAULT_PASSWORD)
		return user
	}

	public UserInfo createDefaultUser(){
		UserInfo user = new UserInfo();
		user.setUsername(CommonVariable.DEFAULT_HO_USERNAME)
		user.setPassword(CommonVariable.DEFAULT_PASSWORD)
		user.setId("2")
		return user;
	}

	public UserInfo createDefaultTMUser(){
		UserInfo user = new UserInfo();
		user.setUsername(CommonVariable.DEFAULT_TM_USERNAME)
		user.setPassword(CommonVariable.DEFAULT_PASSWORD)
		user.setId("1")
		return user;
	}

	public void authToken(final UserInfo user){
		String token = null
		HttpConnectionService http = new HttpConnectionService()
		def query = [
			grant_type: CommonVariable.DEFAULT_PASSWORD,
			username: user.getUsername(),
			password: user.getPassword(),
			scope: 'all'
		]

		ResultInfo result = http.callGetToken(CommonVariable.DEFAULT_GET_TOKEN_URI,query,null)
		if(result.getResponseCode()==CommonVariable.STATUS_200){
			String ACCESS_TOKEN = MatcherStringUtility.getMatch("access_token=(.*)expires", result.getBody())
			println "Access Token: " + ACCESS_TOKEN
			user.setToken(ACCESS_TOKEN)
		}else{
			throw new Exception("AuthToken failed " +result.getResponseCode())
		}
	}

	private void getUserId(final UserInfo user) {
		HttpConnectionService http = new HttpConnectionService()

		ResultInfo result = http.callGetMethodWithAuthentication(CommonVariable.DEFAULT_ME_URI,user.getToken(),null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			String USER_ID = MatcherStringUtility.getMatch("userId=(.*),userName", result.getBody())
			println "User id : " + USER_ID
			user.setId(USER_ID)
		}else{
			throw new Exception("Error " +result.getResponseCode())
		}
	}


	private def changeStatus(final UserInfo admin,final UserInfo user) {
		String token = null
		def query = [
			status: CommonVariable.STATUS_ACTIVE
		]

		HttpConnectionService http = new HttpConnectionService()
		String url = HOMEOWNER_URI_PREFIX + user.getId() + "/status"
		ResultInfo result = http.callPutMethodWithAuthentication(url,admin.getToken(),query,null)

		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Status changed"
		}else{
			throw new Exception("AuthToken failed " +result.getResponseCode())
		}
	}

	private def changeStatusTm(final UserInfo admin,final UserInfo user) {
		String token = null
		def query = [
			status: CommonVariable.STATUS_ACTIVE
		]

		HttpConnectionService http = new HttpConnectionService()
		String url = TRADESMAN_URI_PREFIX + user.getId() + "/status"
		ResultInfo result = http.callPutMethodWithAuthentication(url,admin.getToken(),query,null)

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
	
	
	public ResultInfo getExistingHoInfo(UserInfo userInfo){
		String url = USER_PROXY_URI_PREFIX +"checkemail"
		def query = [
			email: CommonVariable.DEFAULT_HO_USERNAME
			]
		ResultInfo result = http.callGetMethodWithoutAuthentication(url,query)
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
