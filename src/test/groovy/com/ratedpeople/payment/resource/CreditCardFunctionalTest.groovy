/**
 * 
 */
package com.ratedpeople.payment.resource

import groovy.json.*
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import com.ratedpeople.support.DataValues
import com.ratedpeople.support.DatabaseHelper
import com.ratedpeople.user.resource.AbstractHomeowner;
/**
 * @author shabana.khanam
 *
 */

class CreditCardFunctionalTest extends AbstractHomeowner {

	private final String CREDIT_CARD_RESOURCE_URI = DataValues.requestValues.get("PAYMENTSERVICE")+"v1.0/users/"
	private HTTPBuilder HTTP_BUILDER2 = new HTTPBuilder(DataValues.requestValues.get("URL"))
	def "setup"(){

		HTTP_BUILDER2.handler.failure = { resp, reader ->
			[response:resp, reader:reader]
		}
		HTTP_BUILDER2.handler.success = { resp, reader ->
			[response:resp, reader:reader]
		}
	}

	def testCreditCardSuccess() {
		println "CreditCardFunctionalTest"
		println "Test 1 :  testCreditCardSuccess"

		given:
		String ccToken
		String responseStatus

		when:
		def response = postCreditCard(createJsonCreditCard());

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
		responseStatus == DataValues.requestValues.get("STATUS201")
		cleanup:
		DatabaseHelper.executeQuery("delete from payment.credit_card where token = '${ccToken}'")
	}


	def testGetCardDetails(){
		println "Test 2 :  testGetCardDetails"

		given:
		def response = postCreditCard(createJsonCreditCard());
		String ccToken
		response = getCreditCard()
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

		assert resp.status.toString() == DataValues.requestValues.get("STATUS200")
		println "Credit card fetched"

		String responseStatus
		when:
		def map = HTTP_BUILDER2.request(Method.GET) {
			uri.path = CREDIT_CARD_RESOURCE_URI + USER_ID_DYNAMIC_HO +"/cards"
			headers.'Authorization' = "Bearer " + ACCESS_TOKEN_DYNAMIC_HO
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
		//		println "USER IS ${USER_ID_HO}"
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
		def response = postCreditCard(json)
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

		/*
		 def range = aspectResult
		 println "range : "+range.size
		 println "constant :"+aspectResult
		 def watever = trimrightspaces.tokenize(delims)
		 def rangeresponse = watever
		 println "from Id :"+watever
		 println"range of watever :"+rangeresponse.size
		 watever.each{println "watever: $it"}
		 aspectResult.each{println "messageValue:$it"}
		 */
		then:
		for(String aspectResult:aspectResultList){
			resultFound.contains(aspectResult)

		}
		//responseStatus == status



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
		println "USER IS ${USER_ID_DYNAMIC_HO}"

		given:
		String ccToken
		String errorMessage

		def response = postCreditCard(createJsonCreditCard())
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
		response = postCreditCard(createJsonCreditCard())

		resp = response['response']
		reader = response['reader']

		responseStatus = resp.status.toString()
		errorMessage = reader.get('message')
		println "error Message : "+ errorMessage
		println "From DataMap : "+ DataValues.requestValues.get("CARDEXISTS")
		then:
		responseStatus == DataValues.requestValues.get("STATUS409")
		cleanup:
		DatabaseHelper.executeQuery("delete from payment.credit_card where token = '${ccToken}'")
	}

	private def createJsonCreditCard(){
		def json = new JsonBuilder()
		json {
			"number" DataValues.requestValues.get("CREDITCARDNUMBER")
			"userId" USER_ID_DYNAMIC_HO
			"cvv" DataValues.requestValues.get("CV2")
			"expiryYear" DataValues.requestValues.get("EXPIRYYEAR")
			"expiryMonth" DataValues.requestValues.get("EXPIRYMONTH")
			"nameOnCard" DataValues.requestValues.get("NAMEONCARD")
			"type" DataValues.requestValues.get("CARDTYPE")
		}

		println "Json is  CC :${json.toString()}"
		return json;
	}

	private def postCreditCard(def json){
		println "Access token for HO in CC :"+ACCESS_TOKEN_DYNAMIC_HO
		def map = HTTP_BUILDER2.request(Method.POST) {
			uri.path = CREDIT_CARD_RESOURCE_URI + USER_ID_DYNAMIC_HO +"/cards"
			headers.'Authorization' = "Bearer " + ACCESS_TOKEN_DYNAMIC_HO
			body = json.toString()
			requestContentType = ContentType.JSON
			headers.Accept = ContentType.JSON

			println "Post credit card Uri : " + uri
		}
		println "Map is : "+map
		return map;
	}


	private def getCreditCard(){
		def map = HTTP_BUILDER2.request(Method.GET) {
			uri.path = CREDIT_CARD_RESOURCE_URI + USER_ID_DYNAMIC_HO +"/cards"
			headers.'Authorization' = "Bearer " + ACCESS_TOKEN_DYNAMIC_HO
			requestContentType = ContentType.JSON
			headers.Accept = ContentType.JSON

			println "Get credit card Uri : " + uri
		}

		return map;
	}
}
