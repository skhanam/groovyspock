/**
 * 
 */
package com.ratedpeople.resource.tmprofile


import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.Method
import spock.lang.Specification

import com.ratedpeople.service.TMProfileService
import com.ratedpeople.service.TradesmanService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable

/**
 * @author shabana.khanam
 *
 */
class TMProfileAvailabilityFunctionalTest extends Specification{

	private TradesmanService tradesmanService = new TradesmanService();
	private TMProfileService tmProfileService = new TMProfileService()



	def "Update a Tradesman Availability"(){
		given:
			UserInfo user =  tradesmanService.createAndActivateDynamicUser()
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
			UserInfo user =  tradesmanService.createAndActivateDynamicUser()
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
