/**
 * 
 */
package com.ratedpeople.billing.resource

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.Method
import spock.lang.Specification

import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper
/**
 * @author shabana.khanam
 *
 */
class CreateBankFunctionalTest extends Specification{

	private static final String BILLING_URI_PREFIX = CommonVariable.BILLING_SERVICE_PREFIX + "v1.0/users/"
	private static Integer RANDOM_BANK_ACCOUNT = (Math.random()*9000000)+10000000;
	//	private final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(CommonVariable.SERVER_URL)

	def "Create bank account Success"(){
		given:
		String responseStatus = null
		def json = new JsonBuilder()
		json {
			"userId" USER_ID_DYNAMIC_TM
			"beneficiaryName" CommonVariable.DEFAULT_BENEFICIARY_NAME
			"bankCode" CommonVariable.DEFAULT_BANKCODE
			"accountNumber"  RANDOM_BANK_ACCOUNT
			"bankAccountType" CommonVariable.DEFAULT_BANK_ACCOUNT_TYPE
			"country" CommonVariable.DEFAULT_BANK_COUNTRY
		}

		println "Json is " +  json.toString()
		when:
		HTTP_BUILDER.request(Method.POST, ContentType.JSON){
			uri.path =  BILLING_URI_PREFIX + USER_ID_DYNAMIC_TM + "/bankdetails"
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

			response.failure = { resp, reader ->
				println "Request failed with status ${resp.status}"
				println " stacktrace : "+reader.each{"$it"}
				responseStatus = resp.statusLine.statusCode
			}
		}
		then:
		responseStatus == CommonVariable.STATUS_201
	}


	def "Get bank account"(){
		given:
		String responseStatus = null

		when:
		HTTP_BUILDER.request(Method.GET, ContentType.JSON){
			uri.path = BILLING_URI_PREFIX + USER_ID_DYNAMIC_TM + "/bankdetails"
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_TM
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

			response.failure = { resp, reader ->
				println "Request failed with status ${resp.status}"
				println " stacktrace : "+reader.each{"$it"}
				responseStatus = resp.statusLine.statusCode
			}
		}
		then:
		responseStatus == CommonVariable.STATUS_200

	}




	def "Update bank account "(){
		given:
		String responseStatus = null
		def json = new JsonBuilder()
		json {
			"userId" USER_ID_DYNAMIC_TM
			"beneficiaryName" CommonVariable.DEFAULT_BENEFICIARY_NAME + " Update"
			"bankCode" CommonVariable.DEFAULT_BANKCODE
			"accountNumber"  RANDOM_BANK_ACCOUNT.toString()
			"bankAccountType" CommonVariable.DEFAULT_BANK_ACCOUNT_TYPE
			"country" CommonVariable.DEFAULT_BANK_COUNTRY
		}

		println "Json is " +  json.toString()
		when:
		HTTP_BUILDER.request(Method.PUT, ContentType.JSON){
			uri.path = BILLING_URI_PREFIX + USER_ID_DYNAMIC_TM + "/bankdetails"
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

			response.failure = { resp, reader ->
				println "Request failed with status ${resp.status}"
				println " stacktrace : "+reader.each{"$it"}
				responseStatus = resp.statusLine.statusCode
			}
		}
		then:
		responseStatus == CommonVariable.STATUS_200
		cleanup:
		DatabaseHelper.executeQuery("delete from billing.bank_details where user_id = '${USER_ID_DYNAMIC_TM}'")
	}





}
