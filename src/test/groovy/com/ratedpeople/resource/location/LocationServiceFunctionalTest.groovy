/**
 * 
 */
package com.ratedpeople.resource.location

import groovy.json.JsonBuilder
import spock.lang.Specification
import com.ratedpeople.service.LocationService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.support.CommonVariable

/**
 * @author shabana.khanam
 *
 */

class LocationServiceFunctionalTest extends Specification {

	private LocationService locationService = new LocationService();

	def "Get Postcode Information"() {
		given :
			println "********************************"
			println "Test running ..  " +"Get Postcode Information"
		when:
			ResultInfo result = locationService.getPostcode(CommonVariable.DEFAULT_POSTCODE)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	def "Get Invalid Postcode Information"() {
		given :
			println "********************************"
			println "Test running ..  " +"Get Postcode Information"
		when:
			ResultInfo result = locationService.getPostcode(CommonVariable.OUT_OF_AREA_POSTCODE)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_400)
	}

	def "Get Address Information"() {
		given :
			println "********************************"
			println "Test running ..  " +"Get Address Information"
		when:
			ResultInfo result = locationService.getAddressInfo(CommonVariable.DEFAULT_POSTCODE)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	//					@Ignore
	def "Update Area Postcode"(){
		given :
			def json = new JsonBuilder()
			json{
				"postcode" CommonVariable.OUT_OF_AREA_POSTCODE
				"listEmail"([
					{  "email" "davide83@tin.it" }
				])
			}
			println "Json is " +  json.toString()
			println "********************************"
			println "Test running ..  Update Homeowner Profile"
		when:
			ResultInfo result = locationService.updateArea(CommonVariable.OUT_OF_AREA_POSTCODE,json)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}
}
