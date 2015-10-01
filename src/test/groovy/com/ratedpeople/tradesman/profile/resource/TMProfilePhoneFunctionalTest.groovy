package com.ratedpeople.tradesman.profile.resource

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.Method
import spock.lang.Specification

import com.ratedpeople.service.TMProfileService
import com.ratedpeople.service.TradesmanService
import com.ratedpeople.service.utility.MatcherStringUtility;
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper
/**
 * 
 */


/**
 * @author shabana.khanam
 *
 */
class TMProfilePhoneFunctionalTest extends Specification{

	private TradesmanService tradesmanService = new TradesmanService();
	private TMProfileService tmProfileService = new TMProfileService()

	def "Add TM Phone"() {
		given :
		UserInfo user =  tradesmanService.createAndActivateDynamicUser()
		
		println "********************************"
		println "Test running ..  Add Tradesman Phone"
		def json = getPhone(user)
		println "Json is " +  json.toString()
		when:
		ResultInfo result = tmProfileService.createPhone(user, json)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_201)
	}



	def "Update TM Phone"() {
		given :
		UserInfo user =  tradesmanService.createAndActivateDynamicUser()
		def json = getPhone(user)
		tmProfileService.createPhone(user, json)
		def  queryReuslt = DatabaseHelper.select("select id from tmprofile.phone where updated_by = '${user.getId()}'")
		String phoneId = MatcherStringUtility.getMatch("id=(.*)}",queryReuslt)
		json = getPhone(user)
		println "Json is " +  json.toString()
		println "********************************"
		println "Test running ..  Update Tradesman Phone"

		when:
		ResultInfo result = tmProfileService.updatePhone(user,phoneId, json)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	private def getPhone(UserInfo user){

		def json = new JsonBuilder()
		json{
			"userId" user.getId()
			"number" CommonVariable.DEFAULT_LANDLINE_PREFIX  + (Math.round(Math.random()*100000000)+100000000);
			"phoneType" "EMERGENCY_PHONE"
		}
		return json;
	}
}
