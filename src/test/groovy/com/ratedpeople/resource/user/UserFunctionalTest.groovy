/**
 * 
 */
package com.ratedpeople.resource.user
import groovy.json.JsonBuilder
import spock.lang.Specification;

import com.ratedpeople.service.HomeownerService;
import com.ratedpeople.service.TMProfileService;
import com.ratedpeople.service.TradesmanService;
import com.ratedpeople.service.utility.MatcherStringUtility;
import com.ratedpeople.service.utility.ResultInfo;
import com.ratedpeople.service.utility.UserInfo;
import com.ratedpeople.support.CommonVariable;
import com.ratedpeople.support.DatabaseHelper;
/**
 * @author shabana.khanam
 *
 */

class UserFunctionalTest extends Specification {

	private static final String REGISTER_USER_HO_URI = CommonVariable.USER_SERVICE_PREFIX + "v1.0/homeowners/register"
	private static final String REGISTER_USER_TM_URI = CommonVariable.USER_SERVICE_PREFIX + "v1.0/tradesmen/register"

	private TradesmanService tradesmanService = new TradesmanService();
	private HomeownerService homeownerService = new HomeownerService();
	private TMProfileService tmProfileService = new TMProfileService()



	def "Get  User info by Id"(){
		given :
			UserInfo user =  homeownerService.getHoUser()
			println "********************************"
			println "Test running ..  Get user info Id"
		when:
			ResultInfo result = homeownerService.getHoInfo(user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}


	
	 def "Generate and Verify Reset password"(){
	 given:
		 UserInfo user =  homeownerService.createAndActivateDynamicUser()
		 homeownerService.getPasswordToken(user)
     when:
		 String queryReuslt = DatabaseHelper.select("select token from uaa.user_password_token where user_id= '${user.getId()}'")
		 String tokenPassword = MatcherStringUtility.getMatch("token=(.*)}",queryReuslt)
		 println "********************************"
		 println "Test Running ........  Verify Reset password"
		 def json = new JsonBuilder()
		 json {
		 "userId" user.getId()
		 "password" CommonVariable.DEFAULT_PASSWORD
		 "token" tokenPassword
		 }
		 ResultInfo result =  homeownerService.updatePassword(user,json)
	 then:
		 result.getResponseCode().contains(CommonVariable.STATUS_200)
	
	
	 }
	 
	 
	 def "Regenerate Email validation Token"(){
		 given:
			 UserInfo user =  homeownerService.createAndActivateDynamicUser()
			 println "********************************"
			 println "Test Running .... Regenerate Email validation Token"
		 when:
			 ResultInfo result = homeownerService.updateEmailToken(user)
		 then:
			 result.getResponseCode().contains(CommonVariable.STATUS_200)
	 }
 
	 def "Validate Email with Token"(){
		 given:
			 UserInfo user =  homeownerService.createAndActivateDynamicUser()
			 println "********************************"
			 println "Test Running ....  Validate Email with Token"
			 homeownerService.updateEmailToken(user)
			 String queryReuslt = DatabaseHelper.select("select token from uaa.email_validation_token where user_id = '${user.getId()}'")
			 String tokenEmail = MatcherStringUtility.getMatch("token=(.*)}",queryReuslt)
	 
			 println "Token for email  is :"+tokenEmail
		 when:
			 def json = new JsonBuilder()
			 json {
				 "userId" user.getId()
				 "email" user.getUsername()
				 "token" tokenEmail
			 }
			 ResultInfo result = homeownerService.validateEmailToken(user,json)
		 then:
			 result.getResponseCode().contains(CommonVariable.STATUS_200)
	 }
}
