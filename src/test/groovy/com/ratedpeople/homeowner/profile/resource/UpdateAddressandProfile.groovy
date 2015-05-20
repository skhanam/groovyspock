/**
 * 
 */
package com.ratedpeople.homeowner.profile.resource

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.user.resource.AbstractUserToken
/**
 * @author shabana.khanam
 *
 */
class UpdateAddressandProfile extends AbstractUserToken{

	private final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(CommonVariable.SERVER_URL)
	protected static long RANDOM_MOBILE = Math.round(Math.random()*10000);
	
	def "Update Homeowner Profile"(){	
		given :		
			String responseCode = null
			def json = new JsonBuilder()
			json{
				"userId" USER_ID_HO
				"firstName" CommonVariable.DEFAULT_HO_FIRSTNAME + " Update"
				"lastName" CommonVariable.DEFAULT_HO_LASTNAME
				"email" CommonVariable.DEFAULT_HO_USERNAME
				"phone" {
					"mobilePhone" CommonVariable.DEFAULT_MOBILE_PREFIX  + (829300000+RANDOM_MOBILE)
				} 
		
			}	
			
			println "Json is " +  json.toString()
			println "********************************"
			println "Test running ..  Update Homeowner Profile"
		when:
			HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
				uri.path = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/users/" + USER_ID_HO + "/profiles"
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
