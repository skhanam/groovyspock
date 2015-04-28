/**
 * 
 */
package com.ratedpeople.payment.resource

import com.ratedpeople.user.token.AbstractTradesman;

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.Method

import com.ratedpeople.support.DataValues
import com.ratedpeople.support.DatabaseHelper
import groovyx.net.http.HTTPBuilder


/**
 * @author shabana.khanam
 *
 */
class CreateBankFunctionalTest extends AbstractTradesman{

	private final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(DataValues.requestValues.get("URL"))
	
	def "Create bank account Success"(){	
		given:
				String responseStatus = null
				def json = new JsonBuilder()
				json {
					"userId" USER_ID_DYNAMIC_TM
					"beneficiaryName" DataValues.requestValues.get("BENEFICIARYNAME")
					"bankCode" DataValues.requestValues.get("BANKCODE")
					"accountNumber"  DataValues.requestValues.get("ACCOUNTNUMBER")
					"bankAccountType" DataValues.requestValues.get("BANKACCOUNTTYPE")
					"country" DataValues.requestValues.get("COUNTRY")
				}
		
				println "Json is " +  json.toString()
		when:
				HTTP_BUILDER.request(Method.POST, ContentType.JSON){
					uri.path = DataValues.requestValues.get("BILLINGSERVICE")+"v1.0/users/"+USER_ID_DYNAMIC_TM+"/bankdetails"
					println "uri create bank is : "+uri.path
					headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
					body = json.toString()
					requestContentType = ContentType.JSON
					
					println "Uri : " + uri
					response.success = { resp, reader ->
						println "Success"
						println "Got response: ${resp.statusLine}"
						println "Content-Type: ${resp.headers.'Content-Type'}"
						
						responseStatus = resp.statusLine.statusCode
						
						reader.each{
							println "Token values : "+"$it"
							
							String token = "$it"
							String key = token.substring(0, token.indexOf("="))
							String value = token.substring(token.indexOf("=") + 1, token.length())
							println key
							println value
						}
					}
					
					response.failure = { resp ->
						println "Request failed with status ${resp.status}"
						responseStatus = resp.statusLine.statusCode
					}
				}
		then:
				responseStatus == DataValues.requestValues.get("STATUS201")	
	}
	
	def "Create bank account Update"(){
		given:
				String responseStatus = null
				def json = new JsonBuilder()
				json {
					"userId" DataValues.requestValues.get("USERIDTM")
					"beneficiaryName" DataValues.requestValues.get("BENEFICIARYNAME")+"Some One"
					"bankCode" DataValues.requestValues.get("BANKCODE")
					"accountNumber"  DataValues.requestValues.get("ACCOUNTNUMBER")
					"bankAccountType" DataValues.requestValues.get("BANKACCOUNTTYPE")
					"country" DataValues.requestValues.get("COUNTRY")
					}
		
				println "Json is " +  json.toString()
		when:
				HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
					uri.path = DataValues.requestValues.get("BILLINGSERVICE")+"v1.0/users/"+USER_ID_DYNAMIC_TM+"/bankdetails"
					headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
					body = json.toString()
					requestContentType = ContentType.JSON
					
					println "Uri : " + uri
					response.success = { resp, reader ->
						println "Success"
						println "Got response: ${resp.statusLine}"
						println "Content-Type: ${resp.headers.'Content-Type'}"
						
						responseStatus = resp.statusLine.statusCode
						
						reader.each{
							println "Token values : "+"$it"
							
							String token = "$it"
							String key = token.substring(0, token.indexOf("="))
							String value = token.substring(token.indexOf("=") + 1, token.length())
							println key
							println value
						}
					}
					
					response.failure = { resp ->
						println "Request failed with status ${resp.status}"
						responseStatus = resp.statusLine.statusCode
					}
				}
		then:
				responseStatus == DataValues.requestValues.get("STATUS400")
		cleanup:
		DatabaseHelper.executeQuery("delete from billing.bank_details where user_id = '${USER_ID_DYNAMIC_TM}'")
		}

}
