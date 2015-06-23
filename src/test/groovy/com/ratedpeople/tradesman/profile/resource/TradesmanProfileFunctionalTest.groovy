/**
 * 
 */
package com.ratedpeople.tradesman.profile.resource

import groovyx.net.http.HTTPBuilder;

import com.ratedpeople.user.resource.AbstractTradesman;
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper
import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.ByteArrayBody
import org.apache.http.entity.mime.content.FileBody


/**
 * @author shabana.khanam
 *
 */
class TradesmanProfileFunctionalTest extends AbstractTradesman {

	private static final String PROFILE_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"
	
	private static final String MATCH_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/match"
	
	def "Get TradesMan Profile"() {
		given:
		String responseCode = null
		println "********************************"
		println "Test running ..  " +"Get TradesMan Profile"
		when:
			HTTP_BUILDER.request(Method.GET){
				headers.Accept = 'application/json'
				headers.'Authorization' = "Bearer " + ACCESS_TOKEN_DYNAMIC_TM
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_TM + "/profiles"
				println "Uri is " + uri
				response.success = { resp, reader ->
					println "Success"
					responseCode = resp.statusLine.statusCode
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					reader.each{
						println "Response data: " + "$it"
						String user = "$it"
						if (user.startsWith("userId")){
							user = user.replace("userId=", "")
							println "User values : " +user
						}
					}
				}
				
				response.failure = { resp, reader ->
					println "Request failed with status ${resp.status}"
					reader.each{ println "Error values : "+"$it" }
					responseStatus = resp.statusLine.statusCode
				}
			}

		then:
			responseCode == CommonVariable.STATUS_200
	}
	
	
	
	
	
	def "Update Tradesman Profile"(){
		given :
			String responseCode = null
			def json = new JsonBuilder()
			json{
				"userId" USER_ID_DYNAMIC_TM
				"firstName" CommonVariable.DEFAULT_TM_FIRSTNAME + " Update"
				"lastName" CommonVariable.DEFAULT_TM_LASTNAME
				"email" DYNAMIC_USER
				"description" "This is a desc of more for the tm it needs to have more than 60 chars"
				"listPhone" ([ 
							{"number" CommonVariable.DEFAULT_MOBILE_PREFIX  + RANDOM_MOBILE
							"phoneType" "MOBILE_PHONE"},
							{"number" CommonVariable.DEFAULT_MOBILE_PREFIX  + RANDOM_MOBILE
							 "phoneType" "OFFICE_PHONE"}
						])
				"company"{
							"name" CommonVariable.DEFAULT_COMPANY_NAME
						 }
		
			}
			
			println "Json is " +  json.toString()
			println "********************************"
			println "Test running ..  Update Tradesman Profile"
		when:
			HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
				uri.path = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"+USER_ID_DYNAMIC_TM+"/profiles"
				println "uri.path   :"+uri.path
				println "Access Token TM : "+ ACCESS_TOKEN_DYNAMIC_TM
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				body = json.toString()
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{
						"Results  : "+ "$it"
					}
				}
	
				response.failure = { resp, reader ->
					responseCode = resp.statusLine.statusCode
					println " stacktrace : "+reader.each{"$it"}
				}
			}
		then:
			responseCode == CommonVariable.STATUS_200
	}
	
	
	
	
	def "Add  Tradesman Trade"(){
		given :
			String responseCode = null
			def json = new JsonBuilder()
			json{
				"tradeId" "2"
				"rate" CommonVariable.DEFAULT_HOURRATE
			}
			
			println "Json is " +  json.toString()
			println "********************************"
			println "Test running ..  Add Tradesman Profile"
		when:
			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				uri.path = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"+USER_ID_DYNAMIC_TM+"/profiletrades"
				println "uri.path   :"+uri.path
				println "Access Token TM : "+ ACCESS_TOKEN_DYNAMIC_TM
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				body = json.toString()
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{
						"Results  : "+ "$it"
					}
				}
	
				response.failure = { resp, reader ->
					responseCode = resp.statusLine.statusCode
					println " stacktrace : "+reader.each{"$it"}
				}
			}
		then:
			responseCode == CommonVariable.STATUS_201
	}
	
	
	
	
	def "Update  Tradesman Trade"(){
		given :
			String responseCode = null
			def json = new JsonBuilder()
			json{
				"tradeId" "3"
				"rate" CommonVariable.DEFAULT_HOURRATE
			}
			
			println "Json is " +  json.toString()
			println "********************************"
			println "Test running ..  Update Tradesman Trade"
		
			def  getId = DatabaseHelper.select("select id from tmprofile.tm_profile_trade where updated_by =  '${USER_ID_DYNAMIC_TM}'")
			if (getId.startsWith("[{id=")){
				getId = getId.replace("[{id=", "").replace("}]","")
				println "Profile trade id : " +getId
			}
		when:
			HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
				uri.path = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"+USER_ID_DYNAMIC_TM+"/profiletrades/"+getId
				println "uri.path   :"+uri.path
				println "Access Token TM : "+ ACCESS_TOKEN_DYNAMIC_TM
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				body = json.toString()
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{
						"Results  : "+ "$it"
					}
				}
	
				response.failure = { resp, reader ->
					responseCode = resp.statusLine.statusCode
					println " stacktrace : "+reader.each{"$it"}
				}
			}
		then:
			responseCode == CommonVariable.STATUS_200
	}
	
	def "Get  Tradesman Trade"(){
		given :
			String responseCode = null
			println "********************************"
			println "Test running ..  Get Tradesman Trade"
		when:
			HTTP_BUILDER.request(Method.GET,ContentType.JSON){
				uri.path = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"+USER_ID_DYNAMIC_TM+"/profiletrades"
				println "uri.path   :"+uri.path
				println "Access Token TM : "+ ACCESS_TOKEN_DYNAMIC_TM
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{
						"Results  : "+ "$it"
					}
				}
	
				response.failure = { resp, reader ->
					responseCode = resp.statusLine.statusCode
					println " stacktrace : "+reader.each{"$it"}
				}
			}
		then:
			responseCode == CommonVariable.STATUS_200
	}
	
	
	def "Delete  Tradesman Trade"(){
		given :
			String responseCode = null
			println "********************************"
			println "Test running ..  Delete Tradesman Trade"
		
			def  getId = DatabaseHelper.select("select id from tmprofile.tm_profile_trade where updated_by =  '${USER_ID_DYNAMIC_TM}'")
			if (getId.startsWith("[{id=")){
				getId = getId.replace("[{id=", "").replace("}]","")
				println "Profile trade id : " +getId
			}
		when:
			HTTP_BUILDER.request(Method.DELETE,ContentType.JSON){
				uri.path = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"+USER_ID_DYNAMIC_TM+"/profiletrades/"+getId
				println "uri.path   :"+uri.path
				println "Access Token TM : "+ ACCESS_TOKEN_DYNAMIC_TM
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
//				body = json.toString()
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{
						"Results  : "+ "$it"
					}
				}
	
				response.failure = { resp, reader ->
					responseCode = resp.statusLine.statusCode
					println " stacktrace : "+reader.each{"$it"}
				}
			}
		then:
			responseCode == CommonVariable.STATUS_200
	}
	
	
	
	def "Add TM Address"() {
		given:
			String responseCode = null
			def json = getAddress("")
			println "Json is " +  json.toString()
			println "********************************"
			println "Test Running .... Add TM Address"
		when:
			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_TM + "/addresses"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				body = json.toString()
				requestContentType = ContentType.JSON
				println "Uri is " + uri
	
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{ "Results  : "+ "$it" }
				}
	
				response.failure = { resp, reader ->
					println " stacktrace : "+reader.each{"$it"}
					println 'Not found'
					responseCode = resp.statusLine.statusCode
				}
			}
		then:
			responseCode == CommonVariable.STATUS_201
	}
	
	
	
	
	
	def "Get Matching tradesman"(){
		given:
		String responseCode = null
		println "********************************"
		println "Test running ..  " +"Get Match tradesman"
		when:
			   HTTP_BUILDER.request(Method.GET,ContentType.JSON){
				headers.Accept = 'application/json'
				uri.path = MATCH_PREFIX
				uri.query = [
					longitude: CommonVariable.DEFAULT_LONGITUDE	,
					latitude: CommonVariable.DEFAULT_LATITUDE ,
					tradeId:CommonVariable.DEFAULT_TRADE_ID	
					]
				println "Uri is " + uri
	
				response.success = { resp, reader ->
					println "Success"
					responseCode = resp.statusLine.statusCode
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					reader.each{
					println "Response data: " + "$it"
					String user = "$it"
					if (user.startsWith("userId")){
							user = user.replace("userId=", "")
							println "User values : " +user
						}
					}
				}
				response.failure = { resp, reader ->
				println "Request failed with status ${resp.status}"
				reader.each{ println "Error values : "+"$it" }
				responseStatus = resp.statusLine.statusCode
				}
			}
			
		then:
			responseCode == CommonVariable.STATUS_200
	}


	
	def "Get TM address"(){
		given:
		def json = getAddress("")
		String responseCode = null
		println "********************************"
		println "Test running ..  " +"Get HO address"
		when:
			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_TM + "/addresses"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				body = json.toString()
				requestContentType = ContentType.JSON
				println "Uri is " + uri
	
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{ "Results  : "+ "$it" }
				}
	
				response.failure = { resp, reader ->
					println " stacktrace : "+reader.each{"$it"}
					println 'Not found'
					responseCode = resp.statusLine.statusCode
				}
			}
	
			HTTP_BUILDER.request(Method.GET){
				headers.Accept = 'application/json'
				headers.'Authorization' = "Bearer " + ACCESS_TOKEN_DYNAMIC_TM
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_TM + "/addresses"
	
				println "Uri is " + uri
	
				response.success = { resp, reader ->
					println "Success"
					responseCode = resp.statusLine.statusCode
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
	
					reader.each{
						println "Response data: " + "$it"
	
						String user = "$it"
						if (user.startsWith("userId")){
							user = user.replace("userId=", "")
							println "User values : " +user
						}
					}
				}
				
				response.failure = { resp, reader ->
					println "Request failed with status ${resp.status}"
					reader.each{ println "Error values : "+"$it" }
					responseStatus = resp.statusLine.statusCode
				}
			}
			
		then:
			responseCode == CommonVariable.STATUS_200
	}
	
	
	
	def "Update TM Address"(){
		given:
			String responseCode = null
			def json = getAddress("Update")
			println "Json is " +  json.toString()
			println "********************************"
			println "Test Running .... Add TM Address"
			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_TM + "/addresses"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				body = json.toString()
				requestContentType = ContentType.JSON
				println "Uri is " + uri
	
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{ "Results  : "+ "$it" }
				}
	
				response.failure = { resp, reader ->
					println " stacktrace : "+reader.each{"$it"}
					println 'Not found'
					responseCode = resp.statusLine.statusCode
				}
			}
			def  getaddressID = DatabaseHelper.select("select id from tmprofile.address where updated_by =  '${USER_ID_DYNAMIC_TM}'")
			if (getaddressID.startsWith("[{id=")){
				getaddressID = getaddressID.replace("[{id=", "").replace("}]","")
				println "Address id : " +getaddressID
			}
			println "********************************"
			println "Test Running .... Update TM Address"
		when:
			HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_TM + "/addresses/"+getaddressID
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				body = json.toString()
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{ "Results  : "+ "$it" }
				}
				response.failure = { resp, reader ->
					println " stacktrace : "+reader.each{"$it"}
					println 'Not found'
					responseCode = resp.statusLine.statusCode
				}
			}
		then:
			responseCode == CommonVariable.STATUS_200
	}

	
	
	def "Post a Tradesman Workingarea"(){
		given:
			String responseCode = null
			def json = getWorkingarea(0)
			println "Json is " +  json.toString()
			println "********************************"
			println "Test Running .... Post TM Workingarea"
		when:
			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_TM + "/workingarea"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				body = json.toString()
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{ "Results  : "+ "$it" }
				}
				response.failure = { resp, reader ->
					println " stacktrace : "+reader.each{"$it"}
					println 'Not found'
					responseCode = resp.statusLine.statusCode
				}
			}
		then:
			responseCode == CommonVariable.STATUS_201
	}
	
	
	
	def "Update a Tradesman Workingarea"(){
		given:
			String responseCode = null
			def json = getWorkingarea(1)
			println "Json is " +  json.toString()
			println "********************************"
			println "Test Running .... Add TM Workingarea"
			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_TM + "/workingarea"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				body = json.toString()
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{ "Results  : "+ "$it" }
				}
				response.failure = { resp, reader ->
					println " stacktrace : "+reader.each{"$it"}
					println 'Not found'
					responseCode = resp.statusLine.statusCode
				}
			}
			def  getworkingID = DatabaseHelper.select("select id from tmprofile.working_area where updated_by =  '${USER_ID_DYNAMIC_TM}'")
			if (getworkingID.startsWith("[{id=")){
				getworkingID = getworkingID.replace("[{id=", "").replace("}]","")
				println "Address id : " +getworkingID
			}
			def jsonOne = new JsonBuilder()
			jsonOne {
				"id" getworkingID
				"longitude" CommonVariable.DEFAULT_LONGITUDE
				"latitude" CommonVariable.DEFAULT_LATITUDE
				"radius" CommonVariable.DEFAULT_RADIUS
			}
		when:
			println "********************************"
			println "Test Running .... Update TM Workingarea"
			HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_TM + "/workingarea/"+getworkingID
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				body = jsonOne.toString()
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{ "Results  : "+ "$it" }
				}
				response.failure = { resp, reader ->
					println " stacktrace : "+reader.each{"$it"}
					println 'Not found'
					responseCode = resp.statusLine.statusCode
				}
			}
		then:
			responseCode == CommonVariable.STATUS_200
	}
	
	def "Get a Tradesman Workingarea"(){
		given:
			String responseCode = null
			def json = getWorkingarea(0)
			println "Json is " +  json.toString()
			println "********************************"
			println "Test Running .... Add TM Workingarea"
			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_TM + "/workingarea"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				body = json.toString()
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{ "Results  : "+ "$it" }
				}
				response.failure = { resp, reader ->
					println " stacktrace : "+reader.each{"$it"}
					println 'Not found'
					responseCode = resp.statusLine.statusCode
				}
			}
		when:
		println "********************************"
		println "Test Running .... Get TM Workingarea"
			HTTP_BUILDER.request(Method.GET,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_TM + "/workingarea"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{ "Results  : "+ "$it" }
				}
				response.failure = { resp, reader ->
					println " stacktrace : "+reader.each{"$it"}
					println 'Not found'
					responseCode = resp.statusLine.statusCode
				}
			}
		then:
			responseCode == CommonVariable.STATUS_200
	}
	

	def "Post a Image for Tradesman"(){
		given:
			String responseCode = null
			println "********************************"
			println "Test Running .... Post a Image TM"
		when:

			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				req -> 
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_TM + "/images"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				requestContentType = 'multipart/form-data'
				MultipartEntityBuilder entity = new MultipartEntityBuilder()
				def file = new File('src/test/resources/imageTest1.jpg');
				entity.addPart("file",new ByteArrayBody(file.getBytes(), 'src/test/resources/imageTest1.jpg'))
				req.entity = entity.build();
				println "Uri is " + uri
				response.success = 
				{ resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{ "Results  : "+ "$it" }
				}
				response.failure = 
				{ resp, reader ->
					println " stacktrace : "+reader.each{"$it"}
					println 'Not found'
					responseCode = resp.statusLine.statusCode
				}
			}
		then:
			responseCode == CommonVariable.STATUS_201
	}

	 
	def "Get a Image from Tradesman profile"(){
		given:
			Thread.sleep(1000)
			String responseCode = null
			println "********************************"
			println "Test Running .... Post TM Image profile"
			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				req ->
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_TM + "/images"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				requestContentType = 'multipart/form-data'
				MultipartEntityBuilder entity = new MultipartEntityBuilder()
				def file = new File('src/test/resources/imageTest1.jpg');
				entity.addPart("file",new ByteArrayBody(file.getBytes(), 'src/test/resources/imageTest1.jpg'))
				req.entity = entity.build();
				println "Uri is " + uri
				response.success =
				{ resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{ "Results  : "+ "$it" }
				}
				response.failure =
				{ resp, reader ->
					println " stacktrace : "+reader.each{"$it"}
					println 'Not found'
					responseCode = resp.statusLine.statusCode
				}
			}
			Thread.sleep(3000);
		when:
		println "********************************"
		println "Test Running .... Get Image to TM Profile"
			HTTP_BUILDER.request(Method.GET,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_TM + "/images"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{ "Results  : "+ "$it" }
				}
				response.failure = { resp, reader ->
					println " stacktrace : "+reader.each{"$it"}
					println 'Not found'
					responseCode = resp.statusLine.statusCode
				}
			}
		then:
			responseCode == CommonVariable.STATUS_200
	}
	
	
	
	
	
	def "Update a Tradesman Availability"(){
	given:
			String responseCode = null
			def json = new JsonBuilder()
			json {
				"reacheableFlag" "true"
				}
			println "Json is " +  json.toString()
    when:
		println "********************************"
		println "Test Running .... Update Tradesman Availability"
			HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
				uri.path = PROFILE_PREFIX +USER_ID_DYNAMIC_TM  + "/availability/"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				body = json.toString()
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{ "Results  : "+ "$it" }
				}
				response.failure = { resp, reader ->
					println " stacktrace : "+reader.each{"$it"}
					println 'Not found'
					responseCode = resp.statusLine.statusCode
				}
			}
		then:
			responseCode == CommonVariable.STATUS_200
	}
	
	
	def "Get a Tradesman Availability"(){
	given:
			String responseCode = null
			
	when:
		println "********************************"
		println "Test Running .... Update TM Workingarea"
			HTTP_BUILDER.request(Method.GET,ContentType.JSON){
				uri.path = PROFILE_PREFIX +USER_ID_DYNAMIC_TM+"/availability/"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					println "Content-Type: ${resp.headers.'Content-Type'}"
					responseCode = resp.statusLine.statusCode
					reader.each{ "Results  : "+ "$it" }
				}
				response.failure = { resp, reader ->
					println " stacktrace : "+reader.each{"$it"}
					println 'Not found'
					responseCode = resp.statusLine.statusCode
				}
			}
		then:
			responseCode == CommonVariable.STATUS_200
	}
	
	
	
	
	
	def "Regenerate Email validation Token"(){
			given:
					String responseCode = null
					
		    when:
				println "********************************"
				println "Test Running .... Regenerate Email validation Token"
					HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
						uri.path = PROFILE_PREFIX +USER_ID_DYNAMIC_TM  + "/email/resettoken"
						headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
						requestContentType = ContentType.JSON
						println "Uri is " + uri
						response.success = { resp, reader ->
							println "Success"
							println "Got response: ${resp.statusLine}"
							println "Content-Type: ${resp.headers.'Content-Type'}"
							responseCode = resp.statusLine.statusCode
							reader.each{ "Results  : "+ "$it" }
						}
						response.failure = { resp, reader ->
							println " stacktrace : "+reader.each{"$it"}
							println 'Not found'
							responseCode = resp.statusLine.statusCode
						}
					}
				then:
					responseCode == CommonVariable.STATUS_200
	}
	
	
	def "Validate Email with Token"(){
		given:
				String responseCode = null
				def getToken = DatabaseHelper.select("select token from tmprofile.email_validation_token where user_id = '${USER_ID_DYNAMIC_TM}'")
				println "Token for email  is :"+getToken
				if (getToken.startsWith("[{token")){
					getToken = getToken.replace("[{token=", "").replace("}]","")
					println "Token is  is : " +getToken
				}
				
		when:
			println "********************************"
			println "Test Running ....  Validate Email with Token"
				HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
					uri.path = PROFILE_PREFIX +USER_ID_DYNAMIC_TM  + "/email/verify"
					uri.query = [
						token:getToken
						]
					headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
					requestContentType = ContentType.JSON
					println "Uri is " + uri
					response.success = { resp, reader ->
						println "Success"
						println "Got response: ${resp.statusLine}"
						println "Content-Type: ${resp.headers.'Content-Type'}"
						responseCode = resp.statusLine.statusCode
						reader.each{ "Results  : "+ "$it" }
					}
					response.failure = { resp, reader ->
						println " stacktrace : "+reader.each{"$it"}
						println 'Not found'
						responseCode = resp.statusLine.statusCode
					}
				}
			then:
				responseCode == CommonVariable.STATUS_200
 }
	
	
	private def getAddress(String additionalInfo){
		def json = new JsonBuilder()
		json {
			"postcode" CommonVariable.DEFAULT_POSTCODE
			"line1" CommonVariable.DEFAULT_LINE1+additionalInfo
			"line2" CommonVariable.DEFAULT_LINE2+additionalInfo
			"city"  CommonVariable.DEFAULT_CITY
			"country" CommonVariable.DEFAULT_COUNTRY
		}
		return json;
	}
	
	private def getWorkingarea(int additionalInfo){
		def json = new JsonBuilder()
		json {
			"longitude" CommonVariable.DEFAULT_LONGITUDE
			"latitude" CommonVariable.DEFAULT_LATITUDE
			"radius" CommonVariable.DEFAULT_RADIUS+additionalInfo
		}
		return json;
	}
	
}
