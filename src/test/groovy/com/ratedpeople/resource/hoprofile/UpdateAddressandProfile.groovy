/**
 * 
 */
package com.ratedpeople.resource.hoprofile

import groovy.json.JsonBuilder
import spock.lang.Specification
import com.ratedpeople.service.HOProfileService
import com.ratedpeople.service.UserService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
/**
 * @author shabana.khanam
 *
 */
class UpdateAddressandProfile extends Specification{
	
	protected static long RANDOM_MOBILE = Math.round(Math.random()*10000);

	private UserService userService = new UserService();
	private HOProfileService hoProfileService = new HOProfileService();

	def "Update Homeowner Profile"(){
		given :
			UserInfo user = userService.getActivateDynamicHO();
			def json = createProfile(user)

			println "Json is " +  json.toString()
			println "********************************"
			println "Test running ..  Update Homeowner Profile"
		when:
			ResultInfo result = hoProfileService.updateProfile(json,user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}
	
	private def createProfile(final UserInfo user){
		def json = new JsonBuilder()
		json{
			"userId" user.getId()
			"firstName" CommonVariable.DEFAULT_HO_FIRSTNAME + " Update"
			"lastName" CommonVariable.DEFAULT_HO_LASTNAME
			"phone" {
				"number" CommonVariable.DEFAULT_MOBILE_PREFIX  + (829300000+RANDOM_MOBILE)
			}
		}
		
		return json
	}
}
