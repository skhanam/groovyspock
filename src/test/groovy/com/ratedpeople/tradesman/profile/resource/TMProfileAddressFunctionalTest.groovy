

/**
 * 
 */
package com.ratedpeople.tradesman.profile.resource



import com.ratedpeople.user.resource.AbstractUserToken;
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
class TMProfileAddressFunctionalTest extends AbstractUserToken{

	private static final String PROFILE_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"
	
	private static final String MATCH_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/match"

	private static final String ALLPROFILE_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/allprofiles"
	
	def "Add TM Address"() {
		given:
			String responseCode = null
			def json = getAddress("")
			println "Json is " +  json.toString()
			println "********************************"
			println "Test Running .... Add TM Address"
		when:
			responseCode = postAddress(json)
		then:
			responseCode == CommonVariable.STATUS_201
		cleanup:
			DatabaseHelper.executeQuery("delete from tmprofile.address where tm_profile_id = 1 and address_type = '${CommonVariable.ADDRESS_TYPE_BUSINESS}'")
	}
	
	
	def "Get List Matching tradesman"(){
		given:
			String responseCode = null
			println "********************************"
			println "Test running ..  " +"Get List Matching tradesman"
		 
		when:
			   HTTP_BUILDER.request(Method.GET,ContentType.JSON){
				headers.Accept = 'application/json'
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_ADMIN
				uri.path = ALLPROFILE_PREFIX
				uri.query = [
					freeText: "test",
					page: 0,
					limit:2
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
				responseCode = resp.statusLine.statusCode
				}
			}
			
		then:
			responseCode == CommonVariable.STATUS_200
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
				uri.path = PROFILE_PREFIX + USER_ID_TM + "/addresses"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
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
				headers.'Authorization' = "Bearer " + ACCESS_TOKEN_TM
				uri.path = PROFILE_PREFIX + USER_ID_TM + "/addresses"
	
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
			postAddress(json)
			
			def  getaddressID = DatabaseHelper.select("select id from tmprofile.address where address_type = '${CommonVariable.ADDRESS_TYPE_BUSINESS}'")
			if (getaddressID.startsWith("[{id=")){
				getaddressID = getaddressID.replace("[{id=", "").replace("}]","")
				println "Address id : " +getaddressID
			}
			println "********************************"
			println "Test Running .... Update TM Address"
			json.getContent().put("addressId", getaddressID)
		when:
			HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_TM + "/addresses/"+getaddressID
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
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
		cleanup:
			DatabaseHelper.executeQuery("delete from tmprofile.address where tm_profile_id = 1 and address_type = '${CommonVariable.ADDRESS_TYPE_BUSINESS}'")
	}
	
	private def postAddress(JsonBuilder json){
		HTTP_BUILDER.request(Method.POST,ContentType.JSON){
			uri.path = PROFILE_PREFIX + USER_ID_TM + "/addresses"
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
			body = json.toString()
			requestContentType = ContentType.JSON
			println "Uri is " + uri

			response.success = { resp, reader ->
				println "Success"
				println "Got response: ${resp.statusLine}"
				println "Content-Type: ${resp.headers.'Content-Type'}"
				reader.each{ "Results  : "+ "$it" }
				return resp.statusLine.statusCode
			}

			response.failure = { resp, reader ->
				println " stacktrace : "+reader.each{"$it"}
				println 'Not found'
				return resp.statusLine.statusCode
			}
		}
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
