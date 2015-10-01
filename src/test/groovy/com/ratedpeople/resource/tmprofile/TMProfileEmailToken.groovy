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
import com.ratedpeople.service.utility.MatcherStringUtility;
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper

/**
 * @author shabana.khanam
 *
 */
class TMProfileEmailToken extends Specification{

	private TradesmanService tradesmanService = new TradesmanService();
	private TMProfileService tmProfileService = new TMProfileService()


	def "Regenerate Email validation Token"(){
		given:
		UserInfo user =  tradesmanService.createAndActivateDynamicUser()
		println "********************************"
		println "Test Running .... Regenerate Email validation Token"
		when:
		ResultInfo result = tmProfileService.updateEmailToken(user)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_200)
	}


	def "Validate Email with Token"(){
		given:
		UserInfo user =  tradesmanService.createAndActivateDynamicUser()
		println "********************************"
		println "Test Running ....  Validate Email with Token"
		tmProfileService.updateEmailToken(user)
		String queryReuslt = DatabaseHelper.select("select token from uaa.email_validation_token where user_id = '${user.getId()}'")
		String token = MatcherStringUtility.getMatch("token=(.*)}",queryReuslt)

		println "Token for email  is :"+token
		when:
		def json = new JsonBuilder()
		json {
			"userId" user.getId()
			"email" user.getUsername()
			"token" token
		}
		ResultInfo result = tmProfileService.validateEmailToken(user,json)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_200)
	}
}
