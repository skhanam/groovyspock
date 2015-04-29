/**
 * 
 */
package com.ratedpeople.homeowner.profile.resource

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import com.ratedpeople.support.DataValues
import com.ratedpeople.user.resource.AbstractUserToken
/**
 * @author shabana.khanam
 *
 */
class UpdateAddressandProfile extends AbstractUserToken{

	long randomMobile = Math.round(Math.random()*1000);
	private final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(DataValues.requestValues.get("URL"))
	def responseStatus = null;
	
	def "Update Homeowner Profile"(){
		given :
		String responseCode = null
		def json = new JsonBuilder()
		json{
			"userId" USER_ID_HO
			"firstName" DataValues.requestValues.get("FIRSTNAME")+"Update"
			"lastName" DataValues.requestValues.get("LASTNAME")
			"email"  DataValues.requestValues.get("USERNAME_HO")
			"phone" {
				"mobilePhone" DataValues.requestValues.get("PHONE")+randomMobile
			}
		}

		println "Json is " +  json.toString()
		println "********************************"
		println "Test running ..  Update Homeowner Profile"
		when:
		HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
			uri.path = DataValues.requestValues.get("PROFILESERVICE")+"v1.0/users/"+USER_ID_HO+"/profiles"
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
				reader.each{ "Results  : "+ "$it" }
			}

			response.failure = { resp, reader ->
				println "Request failed with status ${resp.status}"
				reader.each{ println "Error values : "+"$it" }
				responseStatus = resp.statusLine.statusCode
			}
		}
		then:
		responseCode == DataValues.requestValues.get("STATUS200")
	}
}
