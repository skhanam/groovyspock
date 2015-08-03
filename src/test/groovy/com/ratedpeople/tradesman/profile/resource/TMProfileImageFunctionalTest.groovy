/**
 * 
 */
package com.ratedpeople.tradesman.profile.resource

import com.ratedpeople.user.resource.AbstractUserToken;
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
class TMProfileImageFunctionalTest extends AbstractUserToken{

	private static final String PROFILE_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"
	
	private static final String MATCH_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/match"
	
	
	def "Post a Image for Tradesman"(){
		given:
			String responseCode = null
			println "********************************"
			println "Test Running .... Post a Image TM"
		when:
			responseCode = postImage()
		then:
			responseCode == CommonVariable.STATUS_201
	}

	 
	def "Get a Image from Tradesman profile"(){
		given:
			Thread.sleep(1000)
			String responseCode = null
			println "********************************"
			println "Test Running .... Post TM Image profile before getting a image"
			responseCode = postImage()
			Thread.sleep(3000);
		when:
			println "********************************"
			println "Test Running .... Get Image to TM Profile"
			HTTP_BUILDER.request(Method.GET,ContentType.JSON){
				uri.path = PROFILE_PREFIX + USER_ID_TM + "/images"
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
	
	private def postImage(){
		HTTP_BUILDER.request(Method.POST,ContentType.JSON){ req ->
			uri.path = PROFILE_PREFIX + USER_ID_TM + "/images"
			uri.query = [
				imageType:CommonVariable.IMAGE_TYPE_PROFILE
			]
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
			requestContentType = 'multipart/form-data'
			MultipartEntityBuilder entity = new MultipartEntityBuilder()
			
			def file = new File('src/test/resources/imageTest1.jpg');
			entity.addPart("file",new ByteArrayBody(file.getBytes(), 'src/test/resources/imageTest1.jpg'))
			req.entity = entity.build();
			println "Uri is " + uri
			
			response.success = { resp, reader ->
				println "Success"
				println "Got response: ${resp.statusLine}"
				println "Content-Type: ${resp.headers.'Content-Type'}"
				return resp.statusLine.statusCode
				reader.each{ "Results  : "+ "$it" }
			}
			
			response.failure = { resp, reader ->
				println " stacktrace : "+reader.each{"$it"}
				println 'Not found'
				return resp.statusLine.statusCode
			}
		}
	}
}
