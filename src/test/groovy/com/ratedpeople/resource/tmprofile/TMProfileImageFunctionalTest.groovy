/**
 * 
 */
package com.ratedpeople.resource.tmprofile

import groovyx.net.http.ContentType
import groovyx.net.http.Method

import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.ByteArrayBody

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
class TMProfileImageFunctionalTest extends Specification{

	private static final String PROFILE_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"

	private static final String MATCH_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/match"

	private TradesmanService tradesmanService = new TradesmanService();
	private TMProfileService tmProfileService = new TMProfileService()

	def "Post a Image for Tradesman"(){
		given:
			UserInfo user =  tradesmanService.createAndActivateDynamicUser()
			def query = [
				imageType:CommonVariable.IMAGE_TYPE_PROFILE
			]
			println "********************************"
			println "Test Running .... Post a Image TM"
		when:
			ResultInfo result = tmProfileService.postImage(user,query)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_201)
	}

	def "Get a Image from Tradesman profile"(){
		given:
			UserInfo user =  tradesmanService.createAndActivateDynamicUser()
			def query = [
				imageType:CommonVariable.IMAGE_TYPE_PROFILE
			]
			tmProfileService.postImage(user,query)
			Thread.sleep(4000);
			println "********************************"
			println "Test Running .... Get Image to TM Profile"
		when:
			ResultInfo result = tmProfileService.getImage(user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}
}
