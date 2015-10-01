/**
 * 
 */
package com.ratedpeople.resource.rating

import groovy.json.JsonBuilder
import spock.lang.Specification
import com.ratedpeople.service.HomeownerService
import com.ratedpeople.service.RatingService
import com.ratedpeople.service.utility.MatcherStringUtility
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper

class RatingFunctionalTest extends Specification {

	private HomeownerService homeownerService = new HomeownerService()
	private RatingService ratingService = new RatingService()

	def "test add HO Rating"(){
		given :
			final UserInfo user = homeownerService.getHoUser();
			final def json = getRatingObject(user)
			println "Json is " +  json.toString()
			println "********************************"
			println "Test running ..  Rating HO"
		when:
			ResultInfo result = ratingService.createRating(json, CommonVariable.DEFAULT_INVOICED_JOB_ID, user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
		cleanup:
			String queryReuslt = DatabaseHelper.select("select id from rating.rating WHERE job_id = ${CommonVariable.DEFAULT_INVOICED_JOB_ID}")
			String ratingId = MatcherStringUtility.getMatch("id=(.*)}",queryReuslt)
			
			println "You are cleaning up rating for job Id 8"
			DatabaseHelper.executeQuery("UPDATE rating.rating SET rating = null  WHERE job_id = ${CommonVariable.DEFAULT_INVOICED_JOB_ID}")
			DatabaseHelper.executeQuery("UPDATE job.job SET rated = 'F'  WHERE id = ${CommonVariable.DEFAULT_INVOICED_JOB_ID}")
			DatabaseHelper.executeQuery("DELETE FROM rating.rating_comment WHERE rating_id = ${ratingId}")
	}
	
	def "test get Rating"(){
		given :
			final UserInfo user = homeownerService.getHoUser();
		when:
			ResultInfo result = ratingService.getRating(CommonVariable.DEFAULT_CLOSED_JOB_ID, user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}
	
	private def getRatingObject(final UserInfo user){
		def json = new JsonBuilder()
		json{
			"jobId" CommonVariable.DEFAULT_INVOICED_JOB_ID
			"ratingType" "HOMEOWNER"
			"rating" 2
			"fromUserId" user.getId()
			"toUserId" "1"
			"ratingComment" {
				"comment" "I am commenting because this person is awesome"
				"fromUserId" user.getId()
				"toUserId" "1"
			}
		}
		
		return json;
	}
}



