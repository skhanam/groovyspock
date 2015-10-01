/**
 * 
 */
package com.ratedpeople.resource.hoprofile

import groovy.json.JsonBuilder
import groovyx.net.http.HTTPBuilder
import spock.lang.Specification

import com.ratedpeople.service.HOProfileService
import com.ratedpeople.service.HomeownerService
import com.ratedpeople.service.utility.ResultInfo;
import com.ratedpeople.service.utility.UserInfo;
import com.ratedpeople.support.CommonVariable
/**
 * @author shabana.khanam
 *
 */
class HomeownerProfileFunctionalTest  extends Specification {

	private final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(CommonVariable.SERVER_URL)
	private static final PROFILE_PREFIX = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/users/"

	private HomeownerService homeownerService = new HomeownerService();
	private HOProfileService hoProfileService = new HOProfileService();

	def "Matching Free Text"(){
		given:
		UserInfo admin =homeownerService.getAdminUser()
		String responseCode = null
		println "********************************"
		println "Test Running ... Matching Free Text "
		when:
		ResultInfo result = hoProfileService.getAllProfiles(admin)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_200)
		
	}

	def "Add HO Address"() {
		given:
		def jsonAddress1 = getAddress("")
		UserInfo user = homeownerService.createAndActivateDynamicUser();
		println "Json is " +  jsonAddress1.toString()
		println "********************************"
		println "Test Running .... Add HO Address"
		when:
		ResultInfo result = hoProfileService.createAddress(jsonAddress1, user)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_201)
	}



	def "Update HO Address"(){
		given:
		String responseCode = null
		def jsonAddress1 = getAddress("")
		UserInfo user = homeownerService.createAndActivateDynamicUser();
		hoProfileService.createAddress(jsonAddress1, user)
		def jsonAddress2 = getAddress("Update")
		println "Json is " +  jsonAddress2.toString()
		println "********************************"
		println "Test Running .... Update Ho Address"
		when:
		ResultInfo result = hoProfileService.updateAddress(jsonAddress2, user)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	def "Get HO address"(){
		given:
		println "********************************"
		println "Test running ..  " +"Get HO address"
		def json = getAddress("")
		UserInfo user = homeownerService.createAndActivateDynamicUser();
		hoProfileService.createAddress(json, user)

		when:
		ResultInfo result = hoProfileService.getAddress(user)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_200)
	}
	
	

	def "Get HomeOwners Profile"() {
		given:
		println "********************************"
		println "Test running ..  " +"Get HomeOwners Profile"
		UserInfo user = homeownerService.createAndActivateDynamicUser();
		when:
		ResultInfo result = hoProfileService.getHomeownerProfile(user)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_200)
		
		
	}



	private def getAddress(String additionInfo){
		def json = new JsonBuilder()
		json {
			"postcode" CommonVariable.DEFAULT_POSTCODE
			"line1" CommonVariable.DEFAULT_LINE1+additionInfo
			"line2" CommonVariable.DEFAULT_LINE2+additionInfo
			"city"  CommonVariable.DEFAULT_CITY
			"country" CommonVariable.DEFAULT_COUNTRY
			"addressType"CommonVariable.ADDRESS_TYPE_HOME
		}
		return json;
	}
}
