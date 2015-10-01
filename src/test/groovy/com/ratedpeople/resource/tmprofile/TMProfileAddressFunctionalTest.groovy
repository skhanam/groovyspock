

/**
 * 
 */
package com.ratedpeople.resource.tmprofile



import groovy.json.JsonBuilder
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
class TMProfileAddressFunctionalTest extends Specification{

	private TradesmanService tradesmanService = new TradesmanService();
	private TMProfileService tmProfileService = new TMProfileService()

	def "Add TM Address"() {
		given:
			UserInfo user =  tradesmanService.createAndActivateDynamicUser()
			def json = getAddress("")
			println "Json is " +  json.toString()
			println "********************************"
			println "Test Running .... Add TM Address"
		when:
			ResultInfo result = tmProfileService.createAddress(json,user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_201)
	}

	def "Get List Matching tradesman"(){
		given:
			UserInfo user =  tradesmanService.createAdminUser()
			println "********************************"
			println "Test running ..  " +"Get List Matching tradesman"
		when:
			ResultInfo result = tmProfileService.getAllProfiles(user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	def "Get Matching tradesman"(){
		given:
			String responseCode = null
			println "********************************"
			println "Test running ..  " +"Get Match tradesman"
			def query = [
				longitude: CommonVariable.DEFAULT_LONGITUDE	,
				latitude: CommonVariable.DEFAULT_LATITUDE ,
				tradeId:CommonVariable.DEFAULT_TRADE_ID
			]
		when:
			ResultInfo result = tmProfileService.getMatchingTm(query)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	def "Get TM address"(){
		given:
			UserInfo user =  tradesmanService.createAndActivateDynamicUser()
			def json = getAddress("")
			tmProfileService.createAddress(json,user)
			println "********************************"
			println "Test running ..  " +"Get TM address"
		when:
			ResultInfo result = tmProfileService.getAddress(user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	def "Update TM Address"(){
		given:
			UserInfo user =  tradesmanService.createAndActivateDynamicUser()
			def json = getAddress("")
			tmProfileService.createAddress(json,user)
			def json2 = getAddress("Update")
			String  queryReuslt = DatabaseHelper.select("select id from tmprofile.address where updated_by = '${user.getId()}'")
			String addressId = MatcherStringUtility.getMatch("id=(.*)}",queryReuslt)
			println "********************************"
			println "Test Running .... Update TM Address"
			println "Test Running .... Add TM Address"
			json2.getContent().put("addressId", addressId)
		when:
			ResultInfo result = tmProfileService.updateAddress(json2, addressId,user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	private def getAddress(String additionalInfo){
		def json = new JsonBuilder()
		json {
			"addressType" CommonVariable.ADDRESS_TYPE_BUSINESS
			"postcode" CommonVariable.DEFAULT_POSTCODE
			"line1" CommonVariable.DEFAULT_LINE1+additionalInfo
			"line2" CommonVariable.DEFAULT_LINE2+additionalInfo
			"city"  CommonVariable.DEFAULT_CITY
			"country" CommonVariable.DEFAULT_COUNTRY
		}
		return json;
	}
}
