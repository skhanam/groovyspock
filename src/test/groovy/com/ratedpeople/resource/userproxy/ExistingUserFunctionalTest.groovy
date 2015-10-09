/**
 * 
 */
package com.ratedpeople.resource.userproxy

import spock.lang.Specification;

import com.ratedpeople.service.UserService
import com.ratedpeople.service.TMProfileService
import com.ratedpeople.service.utility.MatcherStringUtility
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper

/**
 * @author Shaba
 *
 */
class ExistingUserFunctionalTest extends Specification {

	private UserService userService = new UserService();
	private TMProfileService tmProfileService = new TMProfileService()



	def "Get Existing User"(){
		given :
		UserInfo user =  userService.getDefaultHO()
		println "********************************"
		println "Test running ..  Get user info Id"
		when:
		ResultInfo result = userService.getExistingHoInfo(user)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_200)
		String originUser = MatcherStringUtility.getMatch("userOrigin=(.*),userRef",result.getBody())
		originUser=="GID"
	}
}
