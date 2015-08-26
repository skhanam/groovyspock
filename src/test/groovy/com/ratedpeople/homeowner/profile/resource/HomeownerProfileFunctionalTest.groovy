/**
 * 
 */
package com.ratedpeople.homeowner.profile.resource

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.commons.lang.RandomStringUtils
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper
import com.ratedpeople.user.resource.AbstractHomeowner
/**
 * @author shabana.khanam
 *
 */
class HomeownerProfileFunctionalTest  extends AbstractHomeowner{
	
	private final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(CommonVariable.SERVER_URL)
	private static final PROFILE_PREFIX = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/users/"
	
	def "setupSpec"(){
		Thread.sleep(3000);
	}
	
	def "Matching Free Text"(){
		given:
			
			String responseCode = null
			println "********************************"
			println "Test Running ... Matching Free Text "
		when:
			HTTP_BUILDER.request(Method.GET){
				headers.Accept = 'application/json'
				headers.'Authorization' = "Bearer " + ACCESS_TOKEN_ADMIN
				println "Admin Token is : "+ACCESS_TOKEN_ADMIN
				uri.path = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/allprofiles"
	
				println "Uri is " + uri
				uri.query = [
					freeText: CommonVariable.DEFAULT_HO_FIRSTNAME,
					offset:0
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
					}
				}
				response.failure = { resp ->
					println "Request failed with status ${resp.status}"
					println resp.toString()
					println resp.statusLine.statusCode
					println " stacktrace : "+reader.each{"$it"}
					responseCode = resp.statusLine.statusCode
				}
			}
			then:
				responseCode == CommonVariable.STATUS_200
	}

	def "Add HO Address"() {
		given:
			String responseCode = null
			def json = getAddress("")
			println "Json is " +  json.toString()
			println "********************************"
			println "Test Running .... Add HO Address"
		when:
			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_HO + "/addresses"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_HO
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


	
	def "Update HO Address"(){
		given:
			String responseCode = null
			def json = getAddress("Update")
			
			println "Json is " +  json.toString()
			println "********************************"
			println "Test Running .... Update Ho Address"
		when:
			HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_HO + "/addresses"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_HO
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

	def "Get HO address"(){
		given:
		def json = getAddress("")
		String responseCode = null
		println "********************************"
		println "Test running ..  " +"Get HO address"
		when:
			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_HO + "/addresses"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_HO
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
				headers.'Authorization' = "Bearer " + ACCESS_TOKEN_DYNAMIC_HO
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_HO + "/addresses"
	
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

	def "Get HomeOwners Profile"() {
		given:
		String responseCode = null
		println "********************************"
		println "Test running ..  " +"Get HomeOwners Profile"
		when:
			HTTP_BUILDER.request(Method.GET){
				headers.Accept = 'application/json'
				headers.'Authorization' = "Bearer " + ACCESS_TOKEN_DYNAMIC_HO
				uri.path = PROFILE_PREFIX + USER_ID_DYNAMIC_HO + "/profiles"
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
		cleanup:
			def  getpHoneID = DatabaseHelper.select("select phone_id from hoprofile.ho_profile where user_id =  '${USER_ID_DYNAMIC_HO}'")
			def getaddressId = DatabaseHelper.select("select address_id from hoprofile.ho_profile where user_id =  '${USER_ID_DYNAMIC_HO}'")
			if (getpHoneID.startsWith("[{phone_id")){
				getpHoneID = getpHoneID.replace("[{phone_id=", "").replace("}]","")
				println "Phone Id : " +getpHoneID
			}
			println "Address Id : " +getaddressId
			if (getaddressId.startsWith("[{address_id")){
				getaddressId = getaddressId.replace("[{address_id=", "").replace("}]","")
				println "Address Id : " +getaddressId
			}
			try{
				DatabaseHelper.executeQuery("delete from hoprofile.ho_profile where user_id = '${USER_ID_DYNAMIC_HO}'")
				DatabaseHelper.executeQuery("delete from hoprofile.phone where id = '$getpHoneID'")
				DatabaseHelper.executeQuery("delete from hoprofile.address where id = '$getaddressId'")
			}catch(Exception e){
				println e.getMessage()
			}
	}
	
	
	private def getrandomString(){
		RandomStringUtils generateString = new RandomStringUtils();
		String randomString = generateString.random(3);
		println "random string generated : "+randomString
		return randomString
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
