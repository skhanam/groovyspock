/**
 * 
 */
package com.ratedpeople.resource.notification

import groovy.json.JsonBuilder
import spock.lang.Specification

import com.ratedpeople.service.HOProfileService
import com.ratedpeople.service.HomeownerService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper
/**
 * @author shabana.khanam
 *
 */
class SmsPinTokenFunctionalTest extends Specification{

	private static String getpinToken

	private HomeownerService homeownerService = new HomeownerService();
	private HOProfileService hoProfileService = new HOProfileService();



	def testRegeneratePINToken(){
		println "****************************************************"
		println "SmsPinToken"
		println "Test Running ............. :  testRegeneratePINToken"
		given:
		UserInfo user = homeownerService.getHoUser()
		cleanDB(user)

		when:
		ResultInfo result = hoProfileService.resetPin(user, "2")
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	private cleanDB(UserInfo user) {
		def getphoneId = DatabaseHelper.select("select phone_id from hoprofile.ho_profile where user_id =  '${user.getId()}'")
		getpinToken = DatabaseHelper.select("select pin_token from hoprofile.phone where id IN ( select phone_id from hoprofile.ho_profile where user_id = '${user.getId()}')")
		if (getpinToken.startsWith("[{pin_token")){
			getpinToken = getpinToken.replace("[{pin_token=", "").replace("}]","")
			println "pin token is : " +getpinToken
		}
		DatabaseHelper.executeQuery("update hoprofile.phone set pin_generated_times= '0', pin_validation_times= '0' where pin_token = '${getpinToken}'")
	}


	def "testValidate pin"(){
		println "****************************************************"
		println "SmsPinToken"
		println "Test Running ............. :  testValidate pin"
		UserInfo user = homeownerService.getHoUser()
		given:
		String responseStatus = null
		try{
			def getphoneId = DatabaseHelper.select("select phone_id from hoprofile.ho_profile where user_id =  '${user.getId()}'")
			getpinToken = DatabaseHelper.select("select pin_token from hoprofile.phone where id IN ( select phone_id from hoprofile.ho_profile where user_id = '${user.getId()}')")
			if (getpinToken.startsWith("[{pin_token")){
				getpinToken = getpinToken.replace("[{pin_token=", "").replace("}]","")
				println "pin token is : " +getpinToken
			}
		}catch(Exception e){
			println e.getMessage()
		}
		def json = new JsonBuilder()
		json{
			"pinToken" getpinToken
			"phoneId" 2
		}
		println "Json is    : "+json.toString()
		when:
		ResultInfo result = hoProfileService.verifyPin(user, "2",json)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_200)
		cleanup:
		DatabaseHelper.executeQuery("update hoprofile.phone set pin_generated_times= '0' where pin_token = '${getpinToken}'")
	}
}
