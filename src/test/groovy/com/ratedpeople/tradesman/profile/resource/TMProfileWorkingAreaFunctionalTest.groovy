

/**
 * 
 */
package com.ratedpeople.tradesman.profile.resource

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.Method
import spock.lang.Specification

import com.ratedpeople.service.TMProfileService
import com.ratedpeople.service.TradesmanService
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

	private TradesmanService tradesmanService = new TradesmanService();
	private TMProfileService tmProfileService = new TMProfileService()


	def "Post a Tradesman Workingarea"(){
		given :
		UserInfo user =  tradesmanService.createAndActivateDynamicUser()
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
		UserInfo user =  tradesmanService.createAndActivateDynamicUser()
		def json = getWorkingarea(0)
		println "Json is " +  json.toString()
		println "********************************"
		println "Test Running .... Update TM Workingarea"
		tmProfileService.createWorkingArea(user, json)
		when:
		def  queryReuslt = DatabaseHelper.select("select id from tmprofile.working_area where updated_by =  '${user.getId()}'")
		String workingID = MatcherStringUtility.getMatch("id=(.*)}",queryReuslt)
		def jsonOne = new JsonBuilder()
		jsonOne {
			"id" workingID
			"longitude" CommonVariable.DEFAULT_LONGITUDE
			"latitude" CommonVariable.DEFAULT_LATITUDE
			"radius" CommonVariable.DEFAULT_RADIUS
		}
		
		ResultInfo result = tmProfileService.updateWorkingarea(user, workingID,json)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_200)
		
	}

	def "Get a Tradesman Workingarea"(){
		given :
		UserInfo user =  tradesmanService.createAndActivateDynamicUser()
		def json = getWorkingarea(0)
		println "Json is " +  json.toString()
		println "********************************"
		println "Test Running .... Update TM Workingarea"
		tmProfileService.createWorkingArea(user, json)
		when:
		def  queryReuslt = DatabaseHelper.select("select id from tmprofile.working_area where updated_by =  '${user.getId()}'")
		String workingID = MatcherStringUtility.getMatch("id=(.*)}",queryReuslt)
		ResultInfo result = tmProfileService.getWorkingarea(user, workingID)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_200)
	}


	private def getWorkingarea(int additionalInfo){
		def json = new JsonBuilder()
		json {
			"longitude" CommonVariable.DEFAULT_LONGITUDE
			"latitude" CommonVariable.DEFAULT_LATITUDE
			"radius" CommonVariable.DEFAULT_RADIUS+additionalInfo
		}
		return json;
	}
}
