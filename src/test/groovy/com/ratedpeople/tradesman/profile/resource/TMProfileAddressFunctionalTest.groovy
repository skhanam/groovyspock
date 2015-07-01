

/**
 * 
 */
package com.ratedpeople.tradesman.profile.resource


import com.ratedpeople.user.resource.AbstractUserToken;
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

	
	def "Add TM Address"() {
		given:
			String responseCode = null
			def json = getAddress("")
			println "Json is " +  json.toString()
			println "********************************"
			println "Test Running .... Add TM Address"
			def  getId = DatabaseHelper.select("select id from tmprofile.address where tm_profile_id=1 ")
			if(getId != null){
				println "Profile Address id : " +getId
				if (getId.startsWith("[{id=")){
					getId = getId.replace("[{id=", "").replace("}]","")
				println "Profile Address id : " +getId
				DatabaseHelper.executeQuery("delete from tmprofile.address where tm_profile_id =  1")}
			}
			
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
		then:
			responseCode == CommonVariable.STATUS_201
//		cleanup:
//		DatabaseHelper.executeQuery("delete from tmprofile.address where tm_profile_id =  1")
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
			def  getaddressID = DatabaseHelper.select("select id from tmprofile.address where updated_by =  '${USER_ID_TM}'")
			if (getaddressID.startsWith("[{id=")){
				getaddressID = getaddressID.replace("[{id=", "").replace("}]","")
				println "Address id : " +getaddressID
			}
			println "********************************"
			println "Test Running .... Update TM Address"
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
//		cleanup:
//		DatabaseHelper.executeQuery("delete from tmprofile.address where tm_profile_id =  1")
		
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

}
