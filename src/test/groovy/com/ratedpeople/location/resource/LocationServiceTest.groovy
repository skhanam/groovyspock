/**
 * 
 */
package com.ratedpeople.location.resource

import com.ratedpeople.user.resource.AbstractUserToken;
import groovyx.net.http.ContentType
import groovyx.net.http.Method
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper
import groovy.json.JsonBuilder

/**
 * @author shabana.khanam
 *
 */
class LocationServiceTest extends AbstractUserToken{
	
	private static final String POSTCODE_PREFIX = CommonVariable.LOCATION_SERVICE_PREFIX + "v1.0/postcode/"
	
					def "Get Postcode Information"()
					{
						given :
						String responseCode = null
						println "********************************"
						println "Test running ..  " +"Get Postcode Information"
						when:
							   HTTP_BUILDER.request(Method.GET,ContentType.JSON){
								headers.Accept = 'application/json'
								uri.path = POSTCODE_PREFIX+CommonVariable.DEFAULT_POSTCODE
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
					
					
					
					
					def "Update Area Postcode"(){
						given :
							String responseCode = null
							def json = new JsonBuilder()
							json{
								"postcode" "BN11A"
								"listEmail" {
								"email" "davide83@tin.it"
											 }
								}
							println "Json is " +  json.toString()
							println "********************************"
							println "Test running ..  Update Homeowner Profile"
						when:
							HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
								uri.path = POSTCODE_PREFIX+CommonVariable.DEFAULT_POSTCODE
								println "uri.path   :"+uri.path
//								println "Access Token HO : "+ ACCESS_TOKEN_HO
//								headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_HO
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
