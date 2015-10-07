/**
 * 
 */
package com.ratedpeople.resource.tmprofileimport groovy.json.JsonBuilder
import spock.lang.Specification

import com.ratedpeople.service.UserService
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
class TMPofileTradeFunctionalTest  extends Specification{

	
	private UserService userService = new UserService();
	private TMProfileService tmProfileService = new TMProfileService()

	def "Add  Tradesman Profile Trade"() {
		given :
			UserInfo user =  userService.getActivateDynamicTM()
			def json = getTrade(1,10.00)
			println "Json is " +  json.toString()
			println "********************************"
			println "Test running ..  Add Tradesman Profile"
		when:
			ResultInfo result = tmProfileService.addTrade(user, json)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_201)
	}

	def "Update  Tradesman Trade"(){
		given :
			UserInfo user =  userService.getActivateDynamicTM()
			def json = getTrade(1,10.00)
			tmProfileService.addTrade(user, json)
			
			println "Json is " +  json.toString()
			println "********************************"
			println "Test running ..  Update Tradesman Trade"

			String queryReuslt = DatabaseHelper.select("select id from tmprofile.tm_profile_trade where updated_by= '${user.getId()}'  limit 1")
			String tradeProfileId =MatcherStringUtility.getMatch("id=(.*)}",queryReuslt)
			def json1 = getTrade(1,15.00)
		when:
			ResultInfo result = tmProfileService.updateTrade(user,tradeProfileId,json1)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	def "Get  Tradesman Trade"(){
		given :
			UserInfo user =  userService.getActivateDynamicTM()
			def json = getTrade(1,10.00)
			tmProfileService.addTrade(user, json)
			println "********************************"
			println "Test running ..  Get Tradesman Trade"
		when:
			ResultInfo result = tmProfileService.getTrade(user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}
	
	
	
	def "Delete  Tradesman Trade"(){
		given :
			UserInfo user = userService.getActivateDynamicTM()
			def json = getTrade(1,10.00)
			tmProfileService.addTrade(user, json)
			
			println "********************************"
			println "Test running ..  Delete Tradesman Trade"
			
			String queryReuslt = DatabaseHelper.select("select id from tmprofile.tm_profile_trade where updated_by= '${user.getId()}'  limit 1")
			String tradeProfileId =MatcherStringUtility.getMatch("id=(.*)}",queryReuslt)
		when:
			ResultInfo result = tmProfileService.deleteTrade(user,tradeProfileId)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	private def getTrade(int trade,float defaultrate){
		def json = new JsonBuilder()
		json {
			"tradeId" trade
			"rate" defaultrate
		}
		return json;
	}
}
