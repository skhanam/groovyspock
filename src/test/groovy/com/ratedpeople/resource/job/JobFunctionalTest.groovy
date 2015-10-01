/**
 * 
 */
package com.ratedpeople.resource.job
import groovy.json.JsonBuilder
import spock.lang.Specification
import com.ratedpeople.service.HomeownerService
import com.ratedpeople.service.JobService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper

/**
 * @author shabana.khanam
 *
 */
class JobFunctionalTest extends Specification{

	protected static long RANDOM_MOBILE = Math.round(Math.random()*1000000000);
	private static final String JOB_URI_PREFIX = CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/"
	
	private JobService jobService = new JobService();
	private HomeownerService homeownerService = new HomeownerService();
	
	private def createJsonPostaJob(final UserInfo user, final String addDescription){
		
		def json = new JsonBuilder()
		json {
			"tradeId" CommonVariable.DEFAULT_TRADE_ID
			"homeownerUserId" user.getId()
			"tradesmanUserId" CommonVariable.DEFAULT_TM_ID
			"title"	CommonVariable.DEFAULT_TITLE
			"description"  CommonVariable.DEFAULT_DESCRIPTION + addDescription
			"hoRate" CommonVariable.DEFAULT_HOURRATE
			"jobContactDetails" {
				"homeownerName" "hotest"
				"email" "test@gid.com"
				"mobilePhone" CommonVariable.DEFAULT_MOBILE_PREFIX  + RANDOM_MOBILE
				"line1" CommonVariable.DEFAULT_LINE1
				"city" CommonVariable.DEFAULT_CITY
				"postcode" CommonVariable.DEFAULT_POSTCODE
				"longitude" CommonVariable.DEFAULT_LONGITUDE
				"latitude" CommonVariable.DEFAULT_LATITUDE
			}
		}

		println "Json is   : ${json.toString()}"
		return json;
	}

	def "post a job Test"(){
		given:
			UserInfo user = homeownerService.createAndActivateDynamicUser();
			def jobJson = createJsonPostaJob(user,"")
		when:
			ResultInfo result = jobService.createJob(jobJson,user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_201)
	}

	def "post a job with foul language"(){
		given:
			UserInfo user = homeownerService.createAndActivateDynamicUser();
			def jobJson = createJsonPostaJob(user," Abuse")
		when:
			ResultInfo result = jobService.createJob(jobJson,user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_400)
	}

	def "Get Job List for HomeOwner"() {
		given:
			UserInfo user = homeownerService.createAndActivateDynamicUser();
			def jobJson = createJsonPostaJob(user,"")
			jobService.createJob(jobJson,user)
		when:
			ResultInfo result = jobService.getAllJobsForHo(user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
			println result.getBody()
	}

	def "Get Single Job Homeowner"(){
		given:
			UserInfo user = homeownerService.createAndActivateDynamicUser();
			def jobJson = createJsonPostaJob(user,"")
			jobService.createJob(jobJson,user)
			def jobId = DatabaseHelper.select("select id from job.job where homeowner_user_id = '${user.getId()}' limit 1 ")
			println "id of Job is :"+jobId
			if (jobId.startsWith("[{id")){
				jobId = jobId.replace("[{id=", "").replace("}]","")
				println "JobId is : " +jobId
			}
		when:
			ResultInfo result = jobService.getJobForHo(user,jobId)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	def "Unique Homeowner Withdraw Job No Merchant Created"(){
		given:
			UserInfo user = homeownerService.createAndActivateDynamicUser();
			def jobJson = createJsonPostaJob(user,"")
			jobService.createJob(jobJson,user)
			def jobId = DatabaseHelper.select("select id from job.job where homeowner_user_id = '${user.getId()}' limit 1 ")
			println "id of Job is :"+jobId
			if (jobId.startsWith("[{id")){
				jobId = jobId.replace("[{id=", "").replace("}]","")
				println "JobId is : " +jobId
			}
		when:
			ResultInfo result = jobService.withdrawJob(user,jobId)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}
}
