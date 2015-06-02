/**
 * 
 */
package com.ratedpeople.location.resource

import spock.lang.Ignore;
import spock.lang.IgnoreRest;
import spock.lang.Specification;
import junit.textui.TestRunner;

import com.ratedpeople.user.resource.AbstractUserToken;

import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder;
import groovyx.net.http.Method

import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper

import groovy.json.JsonBuilder

/**
 * @author shabana.khanam
 *
 */

//@Ignore
class LocationServiceTest extends Specification {
	
	private static final String POSTCODE_PREFIX = CommonVariable.LOCATION_SERVICE_PREFIX + "v1.0/postcodes/"
	
	public static final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(CommonVariable.SERVER_URL)
	
					def setupSpec()
					{
						println "You are in setup "
						HTTP_BUILDER.handler.failure = { resp, reader ->
							[response:resp, reader:reader]
						}
						HTTP_BUILDER.handler.success = { resp, reader ->
							[response:resp, reader:reader]
						}
					}
	
	

					def "Get Postcode Information"()
					{
						given :
						String responseCode = null
						println "********************************"
						println "Test running ..  " +"Get Postcode Information"
						when:
						try{
							   HTTP_BUILDER.request(Method.GET,ContentType.JSON){
								headers.Accept = 'application/json'
								
								uri.path = POSTCODE_PREFIX+CommonVariable.DEFAULT_POSTCODE
								println "Uri is " + uri
								response.success = { resp, reader ->
									println "Success"
									println "Got response: ${resp.statusLine}"
									responseCode = resp.statusLine.statusCode
									println "Got response: ${resp.statusLine}"
									println "Content-Type: ${resp.headers.'Content-Type'}"
									reader.each{
									println "Response data: " + "$it"
										}
								}
								response.failure = { resp, reader ->
								println "Request failed with status ${resp.status}"
								reader.each{ println "Error values : "+"$it" }
								responseCode = resp.statusLine.statusCode
								}
							}
					}catch (java.net.ConnectException ex){
							ex.printStackTrace()
						}	
					then:
							responseCode == CommonVariable.STATUS_200
					
					}
					
					
					
					def "Get Address Information"()
					{
						given :
						String responseCode = null
						println "********************************"
						println "Test running ..  " +"Get Address Information"
						when:
						try{
							   HTTP_BUILDER.request(Method.GET,ContentType.JSON){
								headers.Accept = 'application/json'
								uri.path = POSTCODE_PREFIX+CommonVariable.DEFAULT_POSTCODE+"/addresses"
								println "Uri is " + uri
					
								response.success = { resp, reader ->
									println "Success"
									responseCode = resp.statusLine.statusCode
									println "Got response: ${resp.statusLine}"
									println "Content-Type: ${resp.headers.'Content-Type'}"
									reader.each{
									println "Response data: " + "$it"
										}
								}
								response.failure = { resp, reader ->
								println "Request failed with status ${resp.status}"
								reader.each{ println "Error values : "+"$it" }
								responseCode = resp.statusLine.statusCode
								}
							}
						}catch (java.net.ConnectException ex){
							ex.printStackTrace()
						}	
						then:
							responseCode == CommonVariable.STATUS_200
					
					}
					
					
					
					@Ignore
					def "Update Area Postcode"(){
						given :
							String responseCode = null
							def json = new JsonBuilder()
							json{
								"postcode" "SE18NW"
								"listEmail" {
								"email" "davide83@tin.it"
											 }
								}
							println "Json is " +  json.toString()
							println "********************************"
							println "Test running ..  Update Homeowner Profile"
						when:
						try
						{
							HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
								uri.path = POSTCODE_PREFIX+CommonVariable.DEFAULT_POSTCODE
								println "uri.path   :"+uri.path
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
					}catch (java.net.ConnectException ex){
					ex.printStackTrace()
				}
						then:
							responseCode == CommonVariable.STATUS_200
					}
}
