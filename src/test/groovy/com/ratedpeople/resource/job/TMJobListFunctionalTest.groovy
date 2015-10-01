/**
 * 
 */
package com.ratedpeople.resource.job

import groovy.json.JsonBuilder
import spock.lang.Specification
import com.ratedpeople.service.JobService
import com.ratedpeople.service.TradesmanService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper

/**
 * @author shabana.khanam
 *
 */
class TMJobListFunctionalTest extends Specification {

	protected static long RANDOM_MOBILE = Math.round(Math.random()*100000000);

	private JobService jobService = new JobService();
	private TradesmanService tradesmanService = new TradesmanService();

	def "Get Tradesman Job List"(){
		given:
			UserInfo user =  tradesmanService.createTradesmanUser()
			println "********************************"
			println "Test running ..  " +"Get Tradesman Job List"
			DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 5 WHERE id = 5")
		when:
			ResultInfo result = jobService.getAllJobsForTm(user)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	def "Get Unique Job Address HomeOwner"(){
		given:
			UserInfo user =  tradesmanService.createTradesmanUser()
			println "********************************"
			println "Test running ..  " +"Get Unique Job Address HomeOwner"
		when:
			String hoId= "2"
			ResultInfo result = jobService.getJobInfoForTm(user,hoId)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	/*def "Homeowner Withdraw Job"(){
		given:
		String responseStatus = null
		def getJobStatus = DatabaseHelper.executeQuery("select job_status_id from job.job WHERE id = 1")
		println "Get the Job status :"+getJobStatus
		if(getJobStatus.equals(true)){
			DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 1 WHERE id = 1")
		}
		when:
		try{
			HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_HO
				uri.path =  JOB_URI_PREFIX +USER_ID_HO+"/hojobs/"+1+"/withdraw"
				println "uri job is : "+uri.path
				requestContentType = ContentType.JSON
				println "Uri : " + uri
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseStatus = resp.statusLine.statusCode
					reader.each{
						println "Token values : "+"$it"

						String token = "$it"
						String key = token.substring(0, token.indexOf("="))
						String value = token.substring(token.indexOf("=") + 1, token.length())
						println key
						println value
					}
				}

				response.failure = { resp, reader ->
					println "Request failed with status ${resp.status}"
					println " stacktrace : "+reader.each{"$it"}
					responseStatus = resp.statusLine.statusCode
				}
			}
		}catch(java.net.ConnectException ex){
			ex.printStackTrace()
		}
		then:
		responseStatus == CommonVariable.STATUS_200
		cleanup:
		DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 1 WHERE id = 10")
	}*/

	def "Tradesman Accept Job"(){
		given:
			UserInfo user =  tradesmanService.createTradesmanUser()
			println "********************************"
			println "Test running ..  " +"Tradesman Accept Job"
		when:
			ResultInfo result = jobService.acceptJob(user,"2")
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
		cleanup:
			DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 2 WHERE id = 2")
	}

	def "Tradesman Start Job"(){
		given:
			UserInfo user =  tradesmanService.createTradesmanUser()
			println "********************************"
			println "Test running ..  " +"Tradesman Start Job"
		when:
			ResultInfo result = jobService.startJob(user,"3")
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
		cleanup:
			DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 3 WHERE id = 3")
	}

	def "Tradesman Pause Job"(){
		given:
			UserInfo user =  tradesmanService.createTradesmanUser()
			println "********************************"
			println "Test running ..  " +"Tradesman Pause Job"
			def json = new JsonBuilder()
			json {
				"jobId" 5
				"stopLatitude" "10.00"
				"stopLongitude" "11.00"
			}
		when:
			ResultInfo result = jobService.pauseJob(user,"5",json)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
		cleanup:
			DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 5 WHERE id = 5")
			DatabaseHelper.executeQuery("UPDATE job.job_tracking SET stop_ts = null  WHERE job_id = 5")
	}

	def "Tradesman Complete Job"(){
		given:
			UserInfo user =  tradesmanService.createTradesmanUser()
			println "********************************"
			println "Test running ..  " +"Tradesman Complete Job"
			def json = new JsonBuilder()
			json {
				"jobId" 5
				"stopLatitude" "10.00"
				"stopLongitude" "11.00"
			}
		when:
			ResultInfo result = jobService.completeJob(user,"5",json)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
		cleanup:
			DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 5 WHERE id = 5")
			DatabaseHelper.executeQuery("UPDATE job.job_tracking SET stop_ts = null  WHERE job_id = 5")
	}

	def "Tradesman Reject Job"(){
		given:
			UserInfo user =  tradesmanService.createTradesmanUser()
			println "********************************"
			println "Test running ..  " +"Tradesman Reject Job"
			def json = new JsonBuilder()
			json {
				"jobId" 2
				"rejectedBy" user.getId()
				"description" "I am not available hence i am rejecting the job sorry for inconvenience caused"
			}
			println "JSON is : "+json.toString()
		when:
			ResultInfo result = jobService.rejectJob(user,"2",json)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
		cleanup:
			DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 2 WHERE id = 2")
			DatabaseHelper.executeQuery("DELETE from job.job_rejected where job_id = 2")
	}

	def "Get Single Job for Tradesman"(){
		given:
			UserInfo user =  tradesmanService.createTradesmanUser()
			println "********************************"
			println "Test running ..  " +" Single Job for Tradesman"
		when:
			ResultInfo result = jobService.getJobForTm(user,"2")
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
	}

	def "Raise an Invoice by Tradesman"(){
		given:
			UserInfo user =  tradesmanService.createTradesmanUser()
			println "********************************"
			println "Test running ..  " +" Raise an Invoice by Tradesman"
			def json = new JsonBuilder()
			json {
				"jobId" '7'
				"hoursWorked" '1.5'
			}
			DatabaseHelper.executeQuery("DELETE from  payment.payment_transaction where job_id = 7")
		when:
			ResultInfo result = jobService.raiseInvoice(user,"7",json)
		then:
			result.getResponseCode().contains(CommonVariable.STATUS_200)
			cleanup:
			DatabaseHelper.executeQuery("UPDATE job.job SET job_status_id = 7 WHERE id = 7")
	}

	private def getWorkingLatitude(String additionalInfo){
		def json = new JsonBuilder()
		json {
			"jobId" additionalInfo
			"startLatitude" CommonVariable.DEFAULT_LONGITUDE
			"startLongitude" CommonVariable.DEFAULT_LATITUDE
		}
		return json;
	}
}
