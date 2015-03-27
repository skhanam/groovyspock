/**
 * 
 */
package com.ratedpeople.user.resource

import com.ratedpeople.user.token.AbstractUserToken;

import groovy.json.JsonBuilder
import com.ratedpeople.support.DataValues
import groovyx.net.http.ContentType
import groovyx.net.http.Method
import com.ratedpeople.support.DatabaseHelper
import java.util.Random
import org.apache.commons.lang.RandomStringUtils;
/**
 * @author shabana.khanam
 *
 */
class ProfileHomeownerFunctionalTest  extends AbstractUserToken{
	
	
	long randomMobile = Math.round(Math.random()*1000);
	private static final ME_URI = DataValues.requestValues.get("PROFILESERVICE") +"v1.0/users/"

	
	def "Create Homeowner Profile"()
	{
	
						given:
							String responseCode = null
							def json = new JsonBuilder()
							json {
								"userId" DataValues.requestValues.get("USERIDHO")
								"firstName" DataValues.requestValues.get("FIRSTNAME")
								"lastName" DataValues.requestValues.get("LASTNAME")
								"email"  DataValues.requestValues.get("USERNAME_HO")
								"phone" {
									"mobilePhone" DataValues.requestValues.get("PHONE")+randomMobile
								}
								
							}
							println "Json is " +  json.toString()
							println "********************************"
							println "Test Running .... Create Homeowner Profile"
							
						when:
							HTTP_BUILDER.request(Method.POST,ContentType.JSON){
								uri.path = DataValues.requestValues.get("PROFILESERVICE")+"v1.0/users/"+DataValues.requestValues.get("USERIDHO")+"/hoprofiles"
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
				
								response.failure = {
									resp, reader -> println " stacktrace : "+reader.each{"$it"}
									println 'Not found'
								}
							}
						then:
							responseCode == DataValues.requestValues.get("STATUS201")
	}


	def "Matching Free Text"(){
						given:
							String responseCode = null
							
							HTTP_BUILDER.handler.failure = { resp, reader ->
								[response:resp, reader:reader]
							}
							HTTP_BUILDER.handler.success = { resp, reader ->
								[response:resp, reader:reader]
							}
							println "********************************"
							println "Test Running ... Matching Free Text " 
					when:
							HTTP_BUILDER.request(Method.GET){
							headers.Accept = 'application/json'
							headers.'Authorization' = "Bearer " + ACCESS_TOKEN_ADMIN
							uri.path = DataValues.requestValues.get("PROFILESERVICE")+"v1.0/allhoprofiles"
							println "Uri is " + uri
							uri.query = [
								freeText: DataValues.requestValues.get("FIRSTNAME"),
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
							}
						}
						then:
						responseCode == DataValues.requestValues.get("STATUS200")
							
		}
	
	
	private def getrandomString(){
		RandomStringUtils generateString = new RandomStringUtils();
		String randomString = generateString.random(3);
		println "random string generated : "+randomString 
		return randomString
	}
	
			
	def "Update Homeowner Profile"()
	{
	
				given:
					String responseCode = null
					def json = new JsonBuilder()
					json {
						"userId" DataValues.requestValues.get("USERIDHO")
						"firstName" DataValues.requestValues.get("FIRSTNAME")+"Update"
						"lastName" DataValues.requestValues.get("LASTNAME")
						"email"  DataValues.requestValues.get("USERNAME_HO")
						"phone" {
							"mobilePhone" DataValues.requestValues.get("PHONE")+randomMobile
						}
						
					}
					println "Json is " +  json.toString()
					println "********************************"
					println "Test running ..  " +"Update Homeowner Profile"
					
				when:
					HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
						uri.path = DataValues.requestValues.get("PROFILESERVICE")+"v1.0/users/"+DataValues.requestValues.get("USERIDHO")+"/hoprofiles"
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
			
						response.failure = {
							resp, reader -> println " stacktrace : "+reader.each{"$it"}
							println 'Not found'
						}
					}
				then:
					responseCode == DataValues.requestValues.get("STATUS200")
	}
			
	
	def "Add HO Address"()
	{
						given:
						String responseCode = null
						def json = new JsonBuilder()
						json {
							"postcode" DataValues.requestValues.get("POSTCODE")
							"line1" DataValues.requestValues.get("LINE1")
							"line2" DataValues.requestValues.get("LINE2")
							"city"  DataValues.requestValues.get("CITY")
							"country" DataValues.requestValues.get("COUNTRY")
							}
						println "Json is " +  json.toString()
						println "********************************"
						println "Test Running .... Add HO Address"
						
					when:
						HTTP_BUILDER.request(Method.POST,ContentType.JSON){
							uri.path = DataValues.requestValues.get("PROFILESERVICE")+"v1.0/users/"+DataValues.requestValues.get("USERIDHO")+"/hoaddress"
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
				
							response.failure = {
								resp, reader -> println " stacktrace : "+reader.each{"$it"}
								println 'Not found'
							}
						}
					then:
						responseCode == DataValues.requestValues.get("STATUS201")
	}

	
	def "Get HO address"(){
				given:
							String responseCode = null
							
							HTTP_BUILDER.handler.failure = { resp, reader ->
								[response:resp, reader:reader]
							}
							HTTP_BUILDER.handler.success = { resp, reader ->
								[response:resp, reader:reader]
							}
							println "********************************"
							println "Test running ..  " +"Get HO address"
					when:
							HTTP_BUILDER.request(Method.GET){
							headers.Accept = 'application/json'
							headers.'Authorization' = "Bearer " + ACCESS_TOKEN_HO
							uri.path = ME_URI+DataValues.requestValues.get("USERIDHO")+"/hoaddress"
							
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
							
							response.failure = { resp ->
								println "Request failed with status ${resp.status}"
								println resp.toString()
								println resp.statusLine.statusCode
							}
						}
						then:
						responseCode == DataValues.requestValues.get("STATUS200")
					
	}
	
	
	def "Update Ho Address"(){
		
		given:
		String responseCode = null
		def json = new JsonBuilder()
		json {
			"postcode" DataValues.requestValues.get("POSTCODE")
			"line1" DataValues.requestValues.get("LINE1")+"Update"
			"line2" DataValues.requestValues.get("LINE2")+"Update"
			"city"  DataValues.requestValues.get("CITY")
			"country" DataValues.requestValues.get("COUNTRY")
			}
		println "Json is " +  json.toString()
		println "********************************"
		println "Test Running .... Update Ho Address"
		
	when:
		HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
			uri.path = DataValues.requestValues.get("PROFILESERVICE")+"v1.0/users/"+DataValues.requestValues.get("USERIDHO")+"/hoaddress"
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

			response.failure = {
				resp, reader -> println " stacktrace : "+reader.each{"$it"}
				println 'Not found'
			}
		}
	then:
		responseCode == DataValues.requestValues.get("STATUS200")
	}
	
	
	def "Get HomeOwners Profile"()
	{
			
					given:
							String responseCode = null
							
							HTTP_BUILDER.handler.failure = { resp, reader ->
								[response:resp, reader:reader]
							}
							HTTP_BUILDER.handler.success = { resp, reader ->
								[response:resp, reader:reader]
							}
							println "********************************"
							println "Test running ..  " +"Get HomeOwners Profile"
					when:
							HTTP_BUILDER.request(Method.GET){
							headers.Accept = 'application/json'
							headers.'Authorization' = "Bearer " + ACCESS_TOKEN_HO
							uri.path = ME_URI+DataValues.requestValues.get("USERIDHO")+"/hoprofiles"
							
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
							
							response.failure = { resp ->
								println "Request failed with status ${resp.status}"
								println resp.toString()
								println resp.statusLine.statusCode
							}
						}
						then:
						responseCode == DataValues.requestValues.get("STATUS200")
						cleanup:
						def  getUserID = DatabaseHelper.select("select phone_id from profile.ho_profile where user_id =  '${DataValues.requestValues.get("USERIDHO")}'")
						
						if (getUserID.startsWith("[{phone_id")){
							getUserID = getUserID.replace("[{phone_id=", "").replace("}]","")
							println "Phone Id : " +getUserID
						}
						try{
						DatabaseHelper.executeQuery("delete from profile.ho_profile where user_id = '${DataValues.requestValues.get("USERIDHO")}'")
						DatabaseHelper.executeQuery("delete from profile.phone where id = '$a'")
						
						}catch(Exception e){
								
								println e.getMessage()
							}
			
		}
		

	
}
