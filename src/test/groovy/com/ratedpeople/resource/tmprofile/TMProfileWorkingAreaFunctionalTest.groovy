

/**
 * 
 */
package com.ratedpeople.resource.tmprofile

import groovy.json.JsonBuilder
import spock.lang.Specification

import com.ratedpeople.service.UserService;
import com.ratedpeople.service.TMProfileService
import com.ratedpeople.service.utility.MatcherStringUtility
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper

/**
 * @author shabana.khanam
 *
 */
class TMProfileWorkingAreaFunctionalTest extends Specification{

	private UserService userService = new UserService()
	private TMProfileService tmProfileService = new TMProfileService()

	def "Post a Tradesman Workingarea"(){
		given :
			UserInfo user =  userService.getActivateDynamicTM()
			def json = getWorkingarea(0)
			println "Json is " +  json.toString()
			println "********************************"
			println "Test Running .... Post TM Workingarea"
		when:
			ResultInfo result = tmProfileService.createWorkingArea(user, json)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_201)
	}

	def "Update a Tradesman Workingarea"(){
		given :
			UserInfo user =   userService.getActivateDynamicTM()
			def json = getWorkingarea(0)
			println "Json is " +  json.toString()
			println "********************************"
			println "Test Running .... Update TM Workingarea"
			tmProfileService.createWorkingArea(user, json)
		when:
			def  queryReuslt = DatabaseHelper.select("select id from tmprofile.working_area where updated_by =  '${user.getId()}'")
			String workingId = MatcherStringUtility.getMatch("id=(.*)}",queryReuslt)
			def jsonOne = new JsonBuilder()
			jsonOne {
				"id" workingId
				"longitude" CommonVariable.DEFAULT_LONGITUDE
				"latitude" CommonVariable.DEFAULT_LATITUDE
				"radius" CommonVariable.DEFAULT_RADIUS
			}
		
			ResultInfo result = tmProfileService.updateWorkingarea(user, workingId,json)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	def "Get a Tradesman Workingarea"(){
		given :
			UserInfo user =  userService.getActivateDynamicTM()
			def json = getWorkingarea(0)
			println "Json is " +  json.toString()
			println "********************************"
			println "Test Running .... Update TM Workingarea"
			tmProfileService.createWorkingArea(user, json)
		when:
			ResultInfo result = tmProfileService.getWorkingarea(user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}
	
	
	private def getWorkingarea(final int additionalInfo){
		def json = new JsonBuilder()
		json {
			"longitude" CommonVariable.DEFAULT_LONGITUDE
			"latitude" CommonVariable.DEFAULT_LATITUDE
			"radius" CommonVariable.DEFAULT_RADIUS+additionalInfo
		}
		return json;
	}
}
