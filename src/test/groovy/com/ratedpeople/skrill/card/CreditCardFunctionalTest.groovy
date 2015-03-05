/**
 * 
 */
package com.ratedpeople.skrill.card

import groovy.json.JsonBuilder
import groovyx.net.http.HTTPBuilder

import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.protocol.BasicHttpContext
import org.apache.http.protocol.HttpContext

import spock.lang.Specification



import groovyx.net.http.Method
import groovy.transform.Field;
import groovy.json.*

import com.ratedpeople.skrill.card.DataValues
/**
 * @author shabana.khanam
 *
 */




//@Stepwise
class CreditCardFunctionalTest extends AbstractGetUserTest {

	private static String URL = "http://localhost:8765/api/user/"
	private static String URLAUTHENTICATE = "http://localhost:8765/api/uaa/"
	public static String  gettoken = DataValues.requestValues.get("TOKEN").bytes.encodeBase64().toString()
	public static String USERNAME = "davide"
	
	
	
	
	def "Register a Merchant"(){
		
							setup:
							
							println "Hash map value   : Test 3 :  " + token
											
							when:
							
							def jsonString = new JsonBuilder()
							
							def jsonParams = jsonString rpUserId: rpUserId,merchant:  merchant,channel:  channel
							
							def map = http.request(DataValues.requestValues.get("URL")+DataValues.requestValues.get("PAYMENTSERVICE")+"v1.0/merchants",Method.POST, "application/json")
							{
								headers.'Authorization' =
								"Bearer "+token
								send('application/json',jsonParams )
							}
								
							def response = map['response']
							def reader = map['reader']
							
							then:
					
							String getResposecode = response.status
							assert getResposecode == status
							def responsemap = reader.each
							{
								println "Token values : "+"$it"
								
								String token = "$it"
								String key = token.substring(0, token.indexOf("="))
								String value = token.substring(token.indexOf("=") + 1, token.length())
								println key
								println value
								
								DataValues.responseToken.put(key,value)
		
							}
		
						where :   
					
						rpUserId | merchant | channel|access_token|status|message
						DataValues.requestValues.get("RPUSERID") | DataValues.requestValues.get("MERCHANT") | DataValues.requestValues.get("CHANNELID")|DataValues.responseToken.get("access_token")||DataValues.requestValues.get("STATUS200")||""
						DataValues.requestValues.get("RPUSERID") | DataValues.requestValues.get("MERCHANT") | DataValues.requestValues.get("CHANNELID")|DataValues.responseToken.get("access_token")||DataValues.requestValues.get("STATUS400")||DataValues.requestValues.get("MERCHANTEXISTS")
		
				}
	
	
	
	

	
	
	def "credit card creation success"(){
		
				setup:
					
						println "Hash map value   : Test 5 :  "
			
		
				when :
		
				def jsonString = new JsonBuilder()
				
				def jsonParams = jsonString number: creditCardNumber,userId:  userId,cvv:  cvv,
				expiryYear: expiryYear,expiryMonth:  expiryMonth,nameOnCard:  nameOnCard,type:  cardType
				
		
		
				def map = http.request("http://localhost:8765/api/payment/", Method.POST, "application/json") {
					uri.path = 'v1.0/users/'+userId+'/cards'
					headers.'Authorization' =
					"Bearer "+token
					send('application/json',jsonParams )
				}
		
				
			
				then:
				
				def response = map['response']
				def reader = map['reader']
				println "*********************************"
				println "response code :${response.status}"
				println "reader type: ${reader.get('cause')}"
				println "*********************************"

				String delims = "[-\\,]+"
				def messageValue = message.tokenize(delims)
				def range = messageValue
				println "range : "+range.size
				println "constant :"+messageValue
				String getResposecode = response.status
				assert getResposecode==status
				
				/*def trimrightspaces = reader.get('cause').trim()
				def watever = trimrightspaces.tokenize(delims)
				def rangeresponse = watever
				println "from Id :"+watever
				println"range of watever :"+rangeresponse.size

				
				watever.each{println "watever: $it"}
				messageValue.each{println "messageValue:$it"
				
				}
				assert range.size == rangeresponse.size
				*/	
				where :
		
				creditCardNumber	| userId			| cvv		| expiryYear | expiryMonth | nameOnCard | cardType  || status 				|| message
				DataValues.requestValues.get("CREDITCARDNUMBER")| DataValues.requestValues.get("USERID")| DataValues.requestValues.get("CV2")|DataValues.requestValues.get("EXPIRYYEAR")|DataValues.requestValues.get("EXPIRYMONTH")  |DataValues.requestValues.get("NAMEONCARD")  | DataValues.requestValues.get("CARDTYPE")||DataValues.requestValues.get("STATUS201")||""
				
		
			}
	
	
		def "Credit card creation and all validations"(){
			
					setup:
						
							println "Hash map value   : Test 6 :  " 
							
			
					when :
			
					def jsonString = new JsonBuilder()
					
					def jsonParams = jsonString number: creditCardNumber,userId:  userId,cvv:  cvv,
					expiryYear: expiryYear,expiryMonth:  expiryMonth,nameOnCard:  nameOnCard,type:  cardType
					
			
			
					def map = http.request("http://localhost:8765/api/payment/", Method.POST, "application/json") {
						uri.path = 'v1.0/users/1/cards'
						headers.'Authorization' =
						"Bearer "+token
						send('application/json',jsonParams )
					}
			
					
				
					then:
					
					def response = map['response']
					def reader = map['reader']
					println "*********************************"
					println "response code :${response.status}"
					println "reader type: ${reader.get('cause')}"
					println "*********************************"
	
					String delims = "[-\\,]+"
					def messageValue = message.tokenize(delims)
					def range = messageValue
					println "range : "+range.size
					println "constant :"+messageValue
					
					def trimrightspaces = reader.get('cause').trim()
					def watever = trimrightspaces.tokenize(delims)
					def rangeresponse = watever
					println "from Id :"+watever
					println"range of watever :"+rangeresponse.size
	
					String getResposecode = response.status
					assert getResposecode==status
					watever.each{println "watever: $it"}
					messageValue.each{println "messageValue:$it"}
					assert range.size == rangeresponse.size
					
					where :
			
					creditCardNumber	| userId			| cvv		| expiryYear | expiryMonth | nameOnCard | cardType  || status 				|| message
//					DataValues.requestValues.get("CREDITCARDNUMBER")    | DataValues.requestValues.get("USERID")| DataValues.requestValues.get("CV2")|DataValues.requestValues.get("EXPIRYYEAR")|DataValues.requestValues.get("EXPIRYMONTH")  |DataValues.requestValues.get("NAMEONCARD")  | DataValues.requestValues.get("CARDTYPE")||DataValues.requestValues.get("STATUS400")||""
					DataValues.requestValues.get("CREDITCARDNUMBER")    | DataValues.requestValues.get("USERID")| DataValues.requestValues.get("CV2")|DataValues.requestValues.get("EXPIRYYEAR")|DataValues.requestValues.get("EXPIRYMONTH")  |DataValues.requestValues.get("NAMEONCARD")  | DataValues.requestValues.get("CARDTYPE")||DataValues.requestValues.get("STATUS400")||DataValues.requestValues.get("CARDEXISTS")
					"  "| DataValues.requestValues.get("USERID")|DataValues.requestValues.get("CV2")|DataValues.requestValues.get("EXPIRYYEAR") |DataValues.requestValues.get("EXPIRYMONTH")|DataValues.requestValues.get("NAMEONCARD")|DataValues.requestValues.get("CARDTYPE")  ||DataValues.requestValues.get("STATUS400")||DataValues.requestValues.get("CREDITCARDVALIDATION")
					DataValues.requestValues.get("CREDITCARDNUMBER")|"  "|DataValues.requestValues.get("CV2")|DataValues.requestValues.get("EXPIRYYEAR")|DataValues.requestValues.get("EXPIRYMONTH")|DataValues.requestValues.get("NAMEONCARD")|DataValues.requestValues.get("CARDTYPE")||DataValues.requestValues.get("STATUS400")||DataValues.requestValues.get("USERIDVALIDATION")
					DataValues.requestValues.get("CREDITCARDNUMBER")|DataValues.requestValues.get("USERID")|"  "|DataValues.requestValues.get("EXPIRYYEAR")|DataValues.requestValues.get("EXPIRYMONTH")|DataValues.requestValues.get("NAMEONCARD")|DataValues.requestValues.get("CARDTYPE")||DataValues.requestValues.get("STATUS400")||DataValues.requestValues.get("CVVVALIDATION")
					DataValues.requestValues.get("CREDITCARDNUMBER")|DataValues.requestValues.get("USERID")|DataValues.requestValues.get("CV2")|"  "		 |DataValues.requestValues.get("EXPIRYMONTH")|DataValues.requestValues.get("NAMEONCARD")|DataValues.requestValues.get("CARDTYPE")||DataValues.requestValues.get("STATUS400")||DataValues.requestValues.get("EXPIRYYEARVALIDATION")
					DataValues.requestValues.get("CREDITCARDNUMBER")|DataValues.requestValues.get("USERID")|DataValues.requestValues.get("CV2")|DataValues.requestValues.get("EXPIRYYEAR")|"  " |DataValues.requestValues.get("NAMEONCARD")|DataValues.requestValues.get("CARDTYPE")||DataValues.requestValues.get("STATUS400")||DataValues.requestValues.get("EXPIRYMONTHVALIDATION")
					DataValues.requestValues.get("CREDITCARDNUMBER")|DataValues.requestValues.get("USERID")|DataValues.requestValues.get("CV2")|DataValues.requestValues.get("EXPIRYYEAR")|DataValues.requestValues.get("EXPIRYMONTH")|"    "|DataValues.requestValues.get("CARDTYPE")||DataValues.requestValues.get("STATUS400")||DataValues.requestValues.get("NAMEONCARDVALIDATION")
					DataValues.requestValues.get("CREDITCARDNUMBER")|DataValues.requestValues.get("USERID")|DataValues.requestValues.get("CV2")|DataValues.requestValues.get("EXPIRYYEAR")|DataValues.requestValues.get("EXPIRYMONTH")|DataValues.requestValues.get("NAMEONCARD")| "   "||DataValues.requestValues.get("STATUS400")||DataValues.requestValues.get("CARDTYPEVALIDATION")
			
				}
			
		def "Get Card details"()
		{
					setup:
									println "Hash map value   : Test 7 :  " + token
								
									println "you are in setup method "
									println "tokenised string  :"+gettoken
								
									when:
								
									println "Get Map values  : "
									def map = http.request(DataValues.requestValues.get("URL")+DataValues.requestValues.get("PAYMENTSERVICE")+"v1.0/users/1/cards",Method.GET, "application/json")
									{
								
										headers.'Authorization' =
												"Bearer "+token
									}
									def response = map['response']
									def reader = map['reader']
									then:
								
									String getResposecode = response.status
									assert getResposecode == status
									reader.each{println "$it"}
								
									where :
								
									username | password | status
									USERNAME | DataValues.requestValues.get("PASSWORD") | DataValues.requestValues.get("STATUS200")
			
		}
		
		
	
		
		

}
