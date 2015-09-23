/**
 * 
 */
package com.ratedpeople.rating.resource

import com.ratedpeople.user.resource.AbstractHomeowner;
import com.ratedpeople.user.resource.AbstractUserToken;

import groovy.json.JsonBuilder;

import com.ratedpeople.support.CommonVariable;

import groovyx.net.http.ContentType
import groovyx.net.http.Method

import com.ratedpeople.support.DatabaseHelper

import groovyx.net.http.HTTPBuilder

/**
 * @author Shabana
 *
 */
class RatingFunctionalTest extends  AbstractUserToken{
	
	
	def "Rating HO "(){
		given :
			String responseCode = null
			def json = new JsonBuilder()
			json{
				"jobId" "7"
				"ratingType" "HOMEOWNER"
				"rating" 2
				"fromUserId" "2"
				"toUserId" "1"
				"ratingComment"
				{
					"comment" "I am commenting because this person is awesome"
					 "fromUserId" "2" 
					 "toUserId" "1"
				}
			}
			
			println "Json is " +  json.toString()
			println "********************************"
			println "Test running ..  Rating HO"
		when:
			HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
				uri.path = CommonVariable.RATING_SERVICE_PREFIX + "v1.0/users/2/jobs/7/ratings"
				println "uri.path   :"+uri.path
				println "Access Token HO : "+ ACCESS_TOKEN_HO
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_HO
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
}



