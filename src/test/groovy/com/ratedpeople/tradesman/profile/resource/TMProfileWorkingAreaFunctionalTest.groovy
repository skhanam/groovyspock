

/**
 * 
 */
package com.ratedpeople.tradesman.profile.resource

import com.ratedpeople.user.resource.AbstractUserToken;
import com.ratedpeople.user.resource.AbstractUserToken;
import com.ratedpeople.support.CommonVariable;
import com.ratedpeople.support.DatabaseHelper;
import com.ratedpeople.user.resource.AbstractUserToken;

import groovy.json.JsonBuilder;
import groovyx.net.http.ContentType;
import groovyx.net.http.HTTPBuilder;
import groovyx.net.http.Method;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;

/**
 * @author shabana.khanam
 *
 */
class TMProfileWorkingAreaFunctionalTest extends AbstractUserToken{

	private static final String PROFILE_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"
	
	private static final String MATCH_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/match"

	def "Post a Tradesman Workingarea"(){
		given:
			String responseCode = null
			def json = getWorkingarea(0)
			println "Json is " +  json.toString()
			println "********************************"
			println "Test Running .... Post TM Workingarea"
			def  getId = DatabaseHelper.select("select id from tmprofile.working_area where updated_by=1 ")
			if(getId != null){
				println "Profile working area id : " +getId
				if (getId.startsWith("[{id=")){
					getId = getId.replace("[{id=", "").replace("}]","")
				println "Profile Address id : " +getId
				DatabaseHelper.executeQuery("update tmprofile.tm_profile set working_area_id = null  where user_id =  1")
				DatabaseHelper.executeQuery("delete from tmprofile.working_area where updated_by =  1")
				}
			}
			
		when:
			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_TM + "/workingarea"
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
	}
	
	
	
	def "Update a Tradesman Workingarea"(){
		given:
			String responseCode = null
			def json = getWorkingarea(1)
			println "Json is " +  json.toString()
			println "********************************"
			println "Test Running .... Add TM Workingarea"
			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_TM + "/workingarea"
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
			def  getworkingID = DatabaseHelper.select("select id from tmprofile.working_area where updated_by =  '${USER_ID_TM}'")
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
				uri.path = PROFILE_PREFIX + USER_ID_TM + "/workingarea/"+getworkingID
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
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
				uri.path = PROFILE_PREFIX + USER_ID_TM + "/workingarea"
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
		when:
		println "********************************"
		println "Test Running .... Get TM Workingarea"
			HTTP_BUILDER.request(Method.GET,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_TM + "/workingarea"
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
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
