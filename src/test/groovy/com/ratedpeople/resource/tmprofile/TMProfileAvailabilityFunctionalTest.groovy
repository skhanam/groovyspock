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
class TMProfileAvailabilityFunctionalTest extends Specification{

	private UserService userService = new UserService();
	private TMProfileService tmProfileService = new TMProfileService()



	def "Update a Tradesman Availability"(){
		given:
			UserInfo user =  userService.getActivateDynamicTM()
			def json = new JsonBuilder()
			json { "reacheable" "true" }
			println "********************************"
			println "Test Running .... Update Tradesman Availability"
			println "Json is " +  json.toString()
		when:
			ResultInfo result = tmProfileService.updateAvailability(user,json)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	def "Get a Tradesman Availability"(){
		given:
			UserInfo user =  userService.getActivateDynamicTM()
			println "********************************"
			println "Test Running .... Get Tradesman Availability"
			def json = new JsonBuilder()
			json { "reacheable" "true" }
			tmProfileService.updateAvailability(user,json)
		when:
			ResultInfo result = tmProfileService.getAvailability(user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}
}
