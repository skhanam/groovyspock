/**
 * 
 */
package com.ratedpeople.integration.jobflow
import groovy.json.JsonBuilder
import spock.lang.Specification

import com.ratedpeople.service.HOProfileService
import com.ratedpeople.service.UserService
import com.ratedpeople.service.JobService
import com.ratedpeople.service.PaymentService
import com.ratedpeople.service.utility.MatcherStringUtility
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper
/**
 * @author shabana.khanam
 *
 */
class JobFlowIntegrationTest extends Specification{

	private static String getpinToken
	private static String phoneid


	private UserService userService = new UserService()
	private HOProfileService hoProfileService = new HOProfileService()
	private JobService jobService = new JobService()
	private PaymentService paymentService = new PaymentService()



	def "JobFlow Integration"(){
		given:
			UserInfo hoUser = userService.getPendingDynamicHO();
			println "********************************"
			println "Test Running .... JobFlow Integration"

		when:
			//post phone
			ResultInfo result = hoProfileService.createPhone(getJsonPhone(hoUser),hoUser)
			result.getResponseCode().contains(CommonVariable.STATUS_201)
			Thread.sleep(1000)
			String phoneId = getPhoneId(hoUser)
			def jsonPin = getJsonPin(hoUser,phoneId)
			//validate pin
			result = hoProfileService.verifyPin(hoUser, phoneId, jsonPin)
			result.getResponseCode().contains(CommonVariable.STATUS_200)
			Thread.sleep(1000)
			//refresh token
			result = userService.authToken(hoUser);
			//post job
			def jobJson = createJsonJob(hoUser,"")
			result = jobService.createJob(jobJson,hoUser)
			String jobId = MatcherStringUtility.getMatch("jobId=(.*)jobRejectedDescription",result.getBody())
			result.getResponseCode().contains(CommonVariable.STATUS_201)
			//post credit card
			Thread.sleep(1000)
			def cardJson =  createJsonCreditCard(hoUser)
			result = paymentService.postCreditCard(hoUser, cardJson)
			result = paymentService.getCreditCard(hoUser)
			String token = MatcherStringUtility.getMatch("token=(.*),type",result.getBody())
	
	
			UserInfo tm =  userService.getDefaultTM()
			def preauthJson =  createPreauth(hoUser,tm,jobId,token)
			result = paymentService.preauth(hoUser, preauthJson)
			result.getResponseCode().contains(CommonVariable.STATUS_200)
			Thread.sleep(1000)
			result = jobService.getJobForHo(hoUser,jobId)
	
			result.getResponseCode().contains(CommonVariable.STATUS_200)
			String status = MatcherStringUtility.getMatch("jobStatus=(.*),title",result.getBody())
		then:
			status=="REQUESTED"


	}


	def "JobFlow paid Integration "(){
		given:
			UserInfo hoUser = userService.getPendingDynamicHO();
			println "********************************"
			println "Test Running .... JobFlow Integration"

		when:
			//post phone
			ResultInfo result = hoProfileService.createPhone(getJsonPhone(hoUser),hoUser)
			result.getResponseCode().contains(CommonVariable.STATUS_201)
			Thread.sleep(1000)
			String phoneId = getPhoneId(hoUser)
			def jsonPin = getJsonPin(hoUser,phoneId)
			//validate pin
			result = hoProfileService.verifyPin(hoUser, phoneId, jsonPin)
			result.getResponseCode().contains(CommonVariable.STATUS_200)
			Thread.sleep(1000)
			//refresh token
			result = userService.authToken(hoUser);
			//post job
			def jobJson = createJsonJob(hoUser,"")
			result = jobService.createJob(jobJson,hoUser)
			String jobId = MatcherStringUtility.getMatch("jobId=(.*)jobRejectedDescription",result.getBody())
			result.getResponseCode().contains(CommonVariable.STATUS_201)
			//post credit card
			Thread.sleep(1000)
			def cardJson =  createJsonCreditCard(hoUser)
			result = paymentService.postCreditCard(hoUser, cardJson)
			result = paymentService.getCreditCard(hoUser)
			String token = MatcherStringUtility.getMatch("token=(.*),type",result.getBody())
	
	
			UserInfo tm =  userService.getDefaultTM()
			def preauthJson =  createPreauth(hoUser,tm,jobId,token)
			result = paymentService.preauth(hoUser, preauthJson)
			result.getResponseCode().contains(CommonVariable.STATUS_200)
			Thread.sleep(1000)
			result = jobService.getJobForHo(hoUser,jobId)
			result.getResponseCode().contains(CommonVariable.STATUS_200)
			String status = MatcherStringUtility.getMatch("jobStatus=(.*),title",result.getBody())
			status=="REQUESTED"
			//Tm accept job
			result = jobService.acceptJob(tm,jobId)
			result.getResponseCode().contains(CommonVariable.STATUS_200)
			result = jobService.startJob(tm,jobId,createJsonStartJob(jobId))
			result.getResponseCode().contains(CommonVariable.STATUS_200)
			result = jobService.completeJob(tm,jobId,createJsonStopJob(jobId))
			result.getResponseCode().contains(CommonVariable.STATUS_200)
			result = jobService.raiseInvoice(tm,jobId,createJsonInvoice(jobId))
			result.getResponseCode().contains(CommonVariable.STATUS_200)
			Thread.sleep(1000)
			//ho pay the job
			def json = new JsonBuilder()
			json { "ipAddress" CommonVariable.DEFAULT_IP }
			result = paymentService.postPayment(hoUser,jobId, json)
			result.getResponseCode().contains(CommonVariable.STATUS_200)
			Thread.sleep(1000)
			result = jobService.getJobForHo(hoUser,jobId)
			result.getResponseCode().contains(CommonVariable.STATUS_200)
			status = MatcherStringUtility.getMatch("jobStatus=(.*),title",result.getBody())
		
		then:
		status=="CLOSED"





	}



	private def getPhoneId(final UserInfo user){
		def queryResult1 = DatabaseHelper.select("select phone_id from hoprofile.ho_profile where user_id = '${user.getId()}'")
		String phoneId = MatcherStringUtility.getMatch("phone_id=(.*)}",queryResult1)
		return phoneId
	}

	private def getJsonPin(final UserInfo user,String phoneIdText){

		def queryResult2 = DatabaseHelper.select("select pin_token from hoprofile.phone where id IN ( select phone_id from hoprofile.ho_profile where user_id = '${user.getId()}')")
		String pin = MatcherStringUtility.getMatch("pin_token=(.*)}",queryResult2)
		println "pin mumber "+ pin
		def jsonPin = new JsonBuilder()
		jsonPin{
			"pinToken" pin
			"phoneId" phoneIdText
		}
		return jsonPin
	}

	private def getJsonPhone(final UserInfo user){
		long RANDOM_MOBILE = Math.round(Math.random()*10000);
		def json = new JsonBuilder()
		json{
			"userId" user.getId()
			"number" CommonVariable.DEFAULT_MOBILE_PREFIX  + (829300000+RANDOM_MOBILE)
		}

		return json
	}


	private def createJsonJob(final UserInfo user, final String addDescription){
		long RANDOM_MOBILE = Math.round(Math.random()*1000000000);
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



	private def createJsonCreditCard(UserInfo user){
		def json = new JsonBuilder()
		json {
			"number" (Math.round(Math.random()*100000000000000)+1000000000000000);
			"userId" user.getId()
			"cvv" CommonVariable.DEFAULT_CC_CVV
			"expiryYear" CommonVariable.DEFAULT_CC_EXPIRY_YEAR
			"expiryMonth" CommonVariable.DEFAULT_CC_EXPIRY_MONTH
			"nameOnCard" CommonVariable.DEFAULT_CC_NAME
			"type" CommonVariable.DEFAULT_CC_TYPE
		}

		println "Json is  CC :${json.toString()}"
		return json;
	}

	private def createPreauth(UserInfo ho,UserInfo tm,String jobIdString,String tokenString){

		def json = new JsonBuilder()
		json {
			"fromUserId" ho.getId()
			"toUserId" tm.getId()
			"jobId" jobIdString
			"ccToken" tokenString
			"currency" CommonVariable.DEFAULT_CURRENCY
			"skrillTransaction" ""
			"amount" CommonVariable.DEFAULT_AMOUNT
			"fromUserEmail" CommonVariable.DEFAULT_HO_USERNAME
			"ip" CommonVariable.DEFAULT_IP
		}
		println "Json is :${json.toString()}"
		return json;
	}

	private def createJsonStartJob(String job){
		def json = new JsonBuilder()
		json {
			"jobId" job
			"startLatitude" "10.00"
			"startLongitude" "11.00"
		}
		return json;
	}

	private def createJsonStopJob(String job){
		def json = new JsonBuilder()
		json {
			"jobId" job
			"stopLatitude" "10.00"
			"stopLongitude" "11.00"
		}
		return json;
	}

	private def createJsonInvoice(String job){
		def json = new JsonBuilder()
		json {
			"jobId" job
			"hoursWorked" '1.5'
		}
		return json;
	}
}
