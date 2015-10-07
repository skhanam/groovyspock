/**
 * 
 */
package com.ratedpeople.resource.tmprofile

import groovy.json.JsonBuilder
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
class TMProfileFunctionalTest extends Specification{

	private UserService userService = new UserService();
	private TMProfileService tmProfileService = new TMProfileService()

	def "Get TradesMan Profile"() {
		given:
			UserInfo user =  userService.getDefaultTM()
			println "********************************"
			println "Test running ..  " +"Get TradesMan Profile"
		when:
			ResultInfo result = tmProfileService.getTmProfile(user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	def "Update Tradesman Profile"(){
		given:
			UserInfo user =   userService.getDefaultTM()
			def json = new JsonBuilder()
			json{
				"userId" user.getId()
				"firstName" CommonVariable.DEFAULT_TM_FIRSTNAME + " Update"
				"lastName" CommonVariable.DEFAULT_TM_LASTNAME
				"email" CommonVariable.DEFAULT_TM_USERNAME
				"description" "This is a desc of more for the tm it needs to have more than 60 chars"
				"company"{ "name" CommonVariable.DEFAULT_COMPANY_NAME }
	
			}
	
			println "Json is " +  json.toString()
			println "********************************"
			println "Test running ..  Update Tradesman Profile"
		when:
			ResultInfo result = tmProfileService.updateTmProfile(user,json)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}
}
