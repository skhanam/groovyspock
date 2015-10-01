/**
 * 
 */
package com.ratedpeople.payment.resource

import groovy.json.*
import groovyx.net.http.ContentType
import groovyx.net.http.Method
import spock.lang.Specification

import com.ratedpeople.service.HomeownerService
import com.ratedpeople.service.PaymentService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper
/**
 * @author shabana.khanam
 *
 */

class CreditCardFunctionalTest extends Specification {

	private HomeownerService homeownerService = new HomeownerService();
	private PaymentService paymentService = new PaymentService()

	def testCreditCardSuccess() {
		println "CreditCardFunctionalTest"
		println "Test 1 :  testCreditCardSuccess"
		given:
		UserInfo user =  homeownerService.createAndActivateDynamicUser()
		
		when:
		def json = createJsonCreditCard(user);
		ResultInfo result = paymentService.postCreditCard(user, json)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_201)
	}


	def testGetCardDetails(){
		println "CreditCardFunctionalTest"
		println "Test 1 :  testCreditCardSuccess"
		
		given:
		UserInfo user =  homeownerService.createAndActivateDynamicUser()
		def json = createJsonCreditCard(user);
		paymentService.postCreditCard(user, json)
		when:
		ResultInfo result = paymentService.getCreditCard(user)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_200)
		
		}

	/*def testCreateCreditCardValidation(){
		given:
		UserInfo user =  homeownerService.createAndActivateDynamicUser()
		println "Test 3 :  testCreateCreditCardValidation"
		println "User is : "+ user.getId()
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
		ResultInfo result = paymentService.postCreditCard(user, json)
		
		println "Credit card fetched"
		String delims = "[-\\,]+"
		def aspectResultList = result.getResponseCode().tokenize(delims)
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
*/
	def testCreateCreditCardWhenAlreadyExists(){
		
		
		
		given:
		UserInfo user =  homeownerService.createAndActivateDynamicUser()
		println "Test 3 :  testCreateCreditCardValidation"
		println "USER IS ${user.getId()}"
		def json = createJsonCreditCard(user);
		paymentService.postCreditCard(user, json)
		given:
		when:
		ResultInfo result = paymentService.postCreditCard(user, json)
		then:
		result.getResponseCode().contains(CommonVariable.STATUS_409)
		
	}


	public def createJsonCreditCard(UserInfo user){
		def json = new JsonBuilder()
		json {
			"number" (Math.round(Math.random()*100000000000000)+1000000000000000);
			"userId" user.getId()
			"cvv" CommonVariable.DEFAULT_CC_CVV
			"expiryYear" CommonVariable.DEFAULT_CC_EXPIRY_YEAR
			"expiryMonth" CommonVariable.DEFAULT_CC_EXPIRY_MONTH
			"nameOnCard" CommonVariable.DEFAULT_CC_NAME
			"type" CommonVariable.DEFAULT_CC_TYPE
		}

		println "Json is  CC :${json.toString()}"
		return json;
	}
}
