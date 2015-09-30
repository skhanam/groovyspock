/**
 * 
 */
package com.ratedpeople.homeowner.profile.resource

import groovy.json.JsonBuilder
import spock.lang.Specification

import com.ratedpeople.service.HOProfileService
import com.ratedpeople.service.HomeownerService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
/**
 * @author shabana.khanam
 *
 */
class UpdateAddressandProfile extends Specification{


	protected static long RANDOM_MOBILE = Math.round(Math.random()*10000);

	private HomeownerService homeownerService = new HomeownerService();
	private HOProfileService hoProfileService = new HOProfileService();


	def "Update Homeowner Profile"(){
		given :
		UserInfo user = homeownerService.createAndActivateDynamicUser();

		String responseCode = null
		def json = new JsonBuilder()
		json{
			"userId" user.getId()
			"firstName" CommonVariable.DEFAULT_HO_FIRSTNAME + " Update"
			"lastName" CommonVariable.DEFAULT_HO_LASTNAME
			"phone" {
				"number" CommonVariable.DEFAULT_MOBILE_PREFIX  + (829300000+RANDOM_MOBILE)
			}
		}

		println "Json is " +  json.toString()
		println "********************************"
		println "Test running ..  Update Homeowner Profile"
		when:
		ResultInfo result = hoProfileService.updateProfile(json,user)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_200)
	}
}
