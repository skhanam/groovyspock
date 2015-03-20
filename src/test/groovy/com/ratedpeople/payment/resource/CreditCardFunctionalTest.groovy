/**
 * 
 */
package com.ratedpeople.payment.resource

import groovy.json.*
import groovyx.net.http.ContentType
import groovyx.net.http.Method
import com.ratedpeople.support.DataValues
import com.ratedpeople.support.DatabaseHelper
import com.ratedpeople.user.token.AbstractUserToken
/**
 * @author shabana.khanam
 *
 */

class CreditCardFunctionalTest extends AbstractUserToken {
	
	private final String CREDIT_CARD_RESOURCE_URI = DataValues.requestValues.get("PAYMENTSERVICE")+"v1.0/users/" 
	
	def testCreditCardSuccess(){
		println "CreditCardFunctionalTest"
		println "Test 1 :  testCreditCardSuccess"
		
		given:
			String ccToken
			String responseStatus
		when:
			def response = postCreditCard(createJson());
			
			def resp = response['response']
			def reader = response['reader']
			
			responseStatus = resp.status
			println "*********************************"
			println "response code :${resp.status}"
			println "reader type: ${reader.get('cause')}"
			println "*********************************"
			
			reader.each{
				println "Response data: " + "$it"
	
				String data = "$it"
				if (data.startsWith("token")){
					ccToken = data.replace("token=", "")
				}
			}
		then:
			responseStatus == DataValues.requestValues.get("STATUS201")
		cleanup:	
			DatabaseHelper.executeQuery("delete from payment.credit_card where token = '${ccToken}'")
	}
					
	def testGetCardDetails(){
		println "Test 2 :  testGetCardDetails"

		given:
			String ccToken
			def response = postCreditCard(createJson())
			def resp = response['response']
			def reader = response['reader']
			
			reader.each{
				println "Response data: " + "$it"
	
				String data = "$it"
				if (data.startsWith("token")){
					ccToken = data.replace("token=", "")
				}
			}
			
			assert resp.status.toString() == DataValues.requestValues.get("STATUS201")
			println "Credit card created"
			
			String responseStatus
		when:
			def map = HTTP_BUILDER.request(Method.GET) {
				uri.path = CREDIT_CARD_RESOURCE_URI + USER_ID_TM +"/cards"
				headers.'Authorization' = "Bearer " + ACCESS_TOKEN_TM
				requestContentType = ContentType.JSON
				headers.Accept = ContentType.JSON
				
				println "Get credit card Uri: ${uri}" 
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
			responseStatus == DataValues.requestValues.get("STATUS200")
			getToken == ccToken
		cleanup:	
			DatabaseHelper.executeQuery("delete from payment.credit_card where token = '${ccToken}'")
	}
	
	def testCreateCreditCardValidation(){
		println "Test 3 :  testCreateCreditCardValidation"
		println "USER IS ${USER_ID_HO}"
		
		given:
			String responseStatus = null
			String errorMessage
			
			def json = new JsonBuilder()

			json {
				number creditCardNumber
				userId USER_ID_HO
				cvv cv2
				expiryYear ccExpiryYear
				expiryMonth ccExpiryMonth
				nameOnCard ccNameOnCard
				type cardType
			}
			
			println "Json is " +  json.toString()
		when:
			def response = postCreditCard(json)	
			def resp = response['response']
			def reader = response['reader']
			
			println "*********************************"
			println "response code :${response.status}"
			println "reader type: ${reader.get('cause')}"
			println "*********************************"
			errorMessage = reader.get('cause')
			responseStatus = resp.status.toString()
		then:
			responseStatus == status
			errorMessage == message
		where :
			creditCardNumber			| cv2		| ccExpiryYear | ccExpiryMonth | ccNameOnCard | cardType  | status 				| message
			"  "|DataValues.requestValues.get("CV2")|DataValues.requestValues.get("EXPIRYYEAR") |DataValues.requestValues.get("EXPIRYMONTH")|DataValues.requestValues.get("NAMEONCARD")|DataValues.requestValues.get("CARDTYPE")  ||DataValues.requestValues.get("STATUS400")||DataValues.requestValues.get("CREDITCARDVALIDATION")
			DataValues.requestValues.get("CREDITCARDNUMBER")| "" |DataValues.requestValues.get("EXPIRYYEAR")|DataValues.requestValues.get("EXPIRYMONTH")|DataValues.requestValues.get("NAMEONCARD")|DataValues.requestValues.get("CARDTYPE")||DataValues.requestValues.get("STATUS400")||DataValues.requestValues.get("CVVVALIDATION")
			DataValues.requestValues.get("CREDITCARDNUMBER")|DataValues.requestValues.get("CV2")|"  "		 |DataValues.requestValues.get("EXPIRYMONTH")|DataValues.requestValues.get("NAMEONCARD")|DataValues.requestValues.get("CARDTYPE")||DataValues.requestValues.get("STATUS400")||DataValues.requestValues.get("EXPIRYYEARVALIDATION")
			DataValues.requestValues.get("CREDITCARDNUMBER")|DataValues.requestValues.get("CV2")|DataValues.requestValues.get("EXPIRYYEAR")|"  " |DataValues.requestValues.get("NAMEONCARD")|DataValues.requestValues.get("CARDTYPE")||DataValues.requestValues.get("STATUS400")||DataValues.requestValues.get("EXPIRYMONTHVALIDATION")
			DataValues.requestValues.get("CREDITCARDNUMBER")|DataValues.requestValues.get("CV2")|DataValues.requestValues.get("EXPIRYYEAR")|DataValues.requestValues.get("EXPIRYMONTH")|"    "|DataValues.requestValues.get("CARDTYPE")||DataValues.requestValues.get("STATUS400")||DataValues.requestValues.get("NAMEONCARDVALIDATION")
			DataValues.requestValues.get("CREDITCARDNUMBER")|DataValues.requestValues.get("CV2")|DataValues.requestValues.get("EXPIRYYEAR")|DataValues.requestValues.get("EXPIRYMONTH")|DataValues.requestValues.get("NAMEONCARD")| "   "||DataValues.requestValues.get("STATUS400")||DataValues.requestValues.get("CARDTYPEVALIDATION")
	}
	
	def testCreateCreditCardWhenAlreadyExists(){
		println "Test 3 :  testCreateCreditCardValidation"
		println "USER IS ${USER_ID_HO}"
		
		given:
			String ccToken
			String errorMessage
			
			def response = postCreditCard(createJson())
			def resp = response['response']
			def reader = response['reader']
			
			reader.each{
				println "Response data: " + "$it"
	
				String data = "$it"
				if (data.startsWith("token")){
					ccToken = data.replace("token=", "")
				}
			}
			
			assert resp.status.toString() == DataValues.requestValues.get("STATUS201")
			println "Credit card created"
			
			String responseStatus
		when:
			response = postCreditCard(createJson())
			
			resp = response['response']
			reader = response['reader']

			responseStatus = resp.status.toString()
			errorMessage = reader.get('cause')
		then:
			responseStatus == DataValues.requestValues.get("STATUS409")
			errorMessage == DataValues.requestValues.get("CARDEXISTS")
		cleanup:	
			DatabaseHelper.executeQuery("delete from payment.credit_card where token = '${ccToken}'")			
	}
	
	private def createJson(){
		def json = new JsonBuilder()
		json {
			"number" DataValues.requestValues.get("CREDITCARDNUMBER")
			"userId" USER_ID_HO
			"cvv" DataValues.requestValues.get("CV2")
			"expiryYear" DataValues.requestValues.get("EXPIRYYEAR")
			"expiryMonth" DataValues.requestValues.get("EXPIRYMONTH")
			"nameOnCard" DataValues.requestValues.get("NAMEONCARD")
			"type" DataValues.requestValues.get("CARDTYPE")
		}
		
		println "Json is ${json.toString()}"
		return json;
	}
	
	private def postCreditCard(def json){
		def map = HTTP_BUILDER.request(Method.POST) {
			uri.path = CREDIT_CARD_RESOURCE_URI + USER_ID_HO +"/cards"
			headers.'Authorization' = "Bearer " + ACCESS_TOKEN_HO
			body = json.toString()
			requestContentType = ContentType.JSON
			headers.Accept = ContentType.JSON
			
			println "Post credit card Uri : " + uri
		}
		
		return map;
	}
}
