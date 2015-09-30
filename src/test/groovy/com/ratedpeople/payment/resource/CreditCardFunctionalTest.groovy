/**
 * 
 */
package com.ratedpeople.payment.resource

import groovy.json.*
import groovyx.net.http.ContentType
import groovyx.net.http.Method

import com.ratedpeople.service.HomeownerService;
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper
/**
 * @author shabana.khanam
 *
 */

class CreditCardFunctionalTest extends HomeownerService {

	private final String CREDIT_CARD_RESOURCE_URI = CommonVariable.PAYMENT_SERVICE_PREFIX + "v1.0/users/"

	def "setup"(){
		HTTP_BUILDER.handler.failure = { resp, reader ->
			[response:resp, reader:reader]
		}
		HTTP_BUILDER.handler.success = { resp, reader ->
			[response:resp, reader:reader]
		}
	}

	def testCreditCardSuccess() {
		println "CreditCardFunctionalTest"
		println "Test 1 :  testCreditCardSuccess"

		given:
			String responseStatus
			String ccToken
		when:
			def response = postCreditCard(CREDIT_CARD_RESOURCE_URI,createJsonCreditCard());
	//CREDIT_CARD_RESOURCE_URI,def uricc,
			println "RESPONSE : "+response
			def resp = response['response']
			def reader = response['reader']
	
			responseStatus = resp.status
			println "*********************************"
			println "response code :${resp.status}"
			println "reader type: ${reader.get('message')}"
			println "*********************************"
	
			reader.each{
				println "Response data: " + "$it"
	
				String data = "$it"
				if (data.startsWith("token")){
					ccToken = data.replace("token=", "")
				}
			}
		then:
			responseStatus == CommonVariable.STATUS_201
		cleanup:
			DatabaseHelper.executeQuery("delete from payment.credit_card where token = '${ccToken}'")
	}


	def testGetCardDetails(){
		println "Test 2 :  testGetCardDetails"

		given:
			def response = postCreditCard(CREDIT_CARD_RESOURCE_URI,createJsonCreditCard());
			String ccToken
			response = getCreditCard(CREDIT_CARD_RESOURCE_URI)
			def resp = response['response']
			def reader = response['reader']
	
			reader.each{
				println "Response data: " + "$it"
				String data = "$it"
				if (data.startsWith("token")){
					ccToken = data.replace("token=", "")
					println "ccToken : "+ccToken
				}
			}
	
			assert resp.status.toString() == CommonVariable.STATUS_200
			println "Credit card fetched"

			String responseStatus
		when:
			def map = HTTP_BUILDER.request(Method.GET) {
				uri.path = CREDIT_CARD_RESOURCE_URI + USER_ID_DYNAMIC_HO + "/cards"
				headers.'Authorization' = "Bearer " + ACCESS_TOKEN_DYNAMIC_HO
				requestContentType = ContentType.JSON
				headers.Accept = ContentType.JSON
				println "Get credit card Uri: "+ '${uri}'
			}
	
			resp = map['response']
			reader = map['reader']
	
			responseStatus = resp.status.toString()
	
			def getToken
			reader.each{
				println "Response data: " + "$it"
	
				String data = "$it"
				if (data.startsWith("token")){
					getToken = data.replace("token=", "")
				}
			}
		then:
			responseStatus == CommonVariable.STATUS_200
			getToken == ccToken
		cleanup:
			DatabaseHelper.executeQuery("delete from payment.credit_card where token = '${ccToken}'")
	}

	def testCreateCreditCardValidation(){
		println "Test 3 :  testCreateCreditCardValidation"
		println "User is : "+ USER_ID_DYNAMIC_HO

		given:
			String responseStatus = null
			String errorMessage
	
			def json = new JsonBuilder()
	
			json {
				number creditCardNumber
				userId USER_ID_DYNAMIC_HO
				cvv cv2
				expiryYear ccExpiryYear
				expiryMonth ccExpiryMonth
				nameOnCard ccNameOnCard
				type cardType
			}
	
			println "Json is " +  json.toString()
		when:
			def response = postCreditCard(CREDIT_CARD_RESOURCE_URI,json)
			def resp = response['response']
			def reader = response['reader']
	
			println "*********************************"
			println "response code :${response.status}"
			println "reader type: ${reader.get('message')}"
			println "*********************************"
	
			println "Credit card fetched"
			String delims = "[-\\,]+"
			def aspectResultList = message.tokenize(delims)
			def resultFound = reader.get('message').trim()
	
			responseStatus = resp.status.toString()

		then:
			for(String aspectResult:aspectResultList){
				resultFound.contains(aspectResult)
			}
		where :
			creditCardNumber			| cv2		| ccExpiryYear | ccExpiryMonth | ccNameOnCard | cardType  | status 				| message
			"  "|CommonVariable.DEFAULT_CC_CVV|CommonVariable.DEFAULT_CC_EXPIRY_YEAR |CommonVariable.DEFAULT_CC_EXPIRY_MONTH|CommonVariable.DEFAULT_CC_NAME|CommonVariable.DEFAULT_CC_TYPE  ||CommonVariable.STATUS_400||CommonVariable.CC_VALIDATION
			CommonVariable.DEFAULT_CC_NUMBER| "" |CommonVariable.DEFAULT_CC_EXPIRY_YEAR|CommonVariable.DEFAULT_CC_EXPIRY_MONTH|CommonVariable.DEFAULT_CC_NAME|CommonVariable.DEFAULT_CC_TYPE||CommonVariable.STATUS_400||CommonVariable.CVV_VALIDATION
			CommonVariable.DEFAULT_CC_NUMBER|CommonVariable.DEFAULT_CC_CVV|"  "		 |CommonVariable.DEFAULT_CC_EXPIRY_MONTH|CommonVariable.DEFAULT_CC_NAME|CommonVariable.DEFAULT_CC_TYPE||CommonVariable.STATUS_400||CommonVariable.EXPIRY_YEAR_VALIDATION
			CommonVariable.DEFAULT_CC_NUMBER|CommonVariable.DEFAULT_CC_CVV|CommonVariable.DEFAULT_CC_EXPIRY_YEAR|"  " |CommonVariable.DEFAULT_CC_NAME|CommonVariable.DEFAULT_CC_TYPE||CommonVariable.STATUS_400||CommonVariable.EXPIRY_MONTH_VALIDATION
			CommonVariable.DEFAULT_CC_NUMBER|CommonVariable.DEFAULT_CC_CVV|CommonVariable.DEFAULT_CC_EXPIRY_YEAR|CommonVariable.DEFAULT_CC_EXPIRY_MONTH|"    "|CommonVariable.DEFAULT_CC_TYPE||CommonVariable.STATUS_400||CommonVariable.CC_NAME_VALIDATION
			CommonVariable.DEFAULT_CC_NUMBER|CommonVariable.DEFAULT_CC_CVV|CommonVariable.DEFAULT_CC_EXPIRY_YEAR|CommonVariable.DEFAULT_CC_EXPIRY_MONTH|CommonVariable.DEFAULT_CC_NAME| "   "||CommonVariable.STATUS_400||CommonVariable.CARD_TYPE_VALIDATION
	}

	def testCreateCreditCardWhenAlreadyExists(){
		println "Test 3 :  testCreateCreditCardValidation"
		println "USER IS ${USER_ID_DYNAMIC_HO}"

		given:
			String ccToken
			String errorMessage
			DatabaseHelper.executeQuery("delete from payment.credit_card where token = '${ccToken}'")
			def response = postCreditCard(CREDIT_CARD_RESOURCE_URI,createJsonCreditCard())
			def resp = response['response']
			def reader = response['reader']
	
			reader.each{
				println "Response data: " + "$it"
	
				String data = "$it"
				if (data.startsWith("token")){
					ccToken = data.replace("token=", "")
				}
			}
	
			assert resp.status.toString() == CommonVariable.STATUS_201
			println "Credit card created"
	
			String responseStatus
		when:
			response = postCreditCard(CREDIT_CARD_RESOURCE_URI,createJsonCreditCard())
	
			resp = response['response']
			reader = response['reader']
	
			responseStatus = resp.status.toString()
			errorMessage = reader.get('message')
			println "error Message : "+ errorMessage
			println "From DataMap : "+ CommonVariable.CARD_ALREADY_EXISTS
		then:
			responseStatus == CommonVariable.STATUS_409
		cleanup:
			DatabaseHelper.executeQuery("delete from payment.credit_card where token = '${ccToken}'")
	}


	
	
}
