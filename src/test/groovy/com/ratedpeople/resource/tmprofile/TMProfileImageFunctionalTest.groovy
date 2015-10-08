/**
 * 
 */
package com.ratedpeople.resource.tmprofile

import spock.lang.Specification

import com.ratedpeople.service.UserService
import com.ratedpeople.service.TMProfileService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable

/**
 * @author shabana.khanam
 *
 */
class TMProfileImageFunctionalTest extends Specification{

	private static final String PROFILE_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"

	private static final String MATCH_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/match"

	private UserService userService = new UserService();
	private TMProfileService tmProfileService = new TMProfileService()

	def "Post a Image for Tradesman"(){
		given:
			UserInfo user =  userService.getActivateDynamicTM()
			def query = [
				imageType:CommonVariable.IMAGE_TYPE_PROFILE
			]
			println "********************************"
			println "Test Running .... Post a Image TM"
		when:
			ResultInfo result = tmProfileService.postImage(user,query)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_201)
	}

	def "Get a Image from Tradesman profile"(){
		given:
			UserInfo user =  userService.getActivateDynamicTM()
			def query = [
				imageType:CommonVariable.IMAGE_TYPE_PROFILE
			]
			tmProfileService.postImage(user,query)
			
			println "********************************"
			println "Test Running .... Get Image to TM Profile"
		when:
			boolean imageExist = false
			ResultInfo result = null
			int retry = 0;
			while(retry <3){
				Thread.sleep(2000);
				result = tmProfileService.getImage(user)
				imageExist=checkResult(result,CommonVariable.STATUS_200)
				if(imageExist==true) break;
				retry++;
			
			}
			
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}
	
	private boolean checkResult(ResultInfo result,String code) {
		if(result.getResponseCode().contains(code)!=true) {
			println result.getResponseCode()+" "+result.getError() +" "+result.getBody()
			return false;
		} 
		return true;
	}
}
