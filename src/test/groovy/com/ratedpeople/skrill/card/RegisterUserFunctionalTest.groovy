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
class RegisterUserFunctionalTest extends Specification {

	public static String  gettoken = DataValues.requestValues.get("TOKEN").bytes.encodeBase64().toString()
	public static String USERNAME = DataValues.requestValues.get("USERNAME")+System.currentTimeMillis();
	
	def "Create  User"(){
			
					setup:
					println "Hash map value   : Test 1 :  " + DataValues.responseToken
					HTTPBuilder http = new HTTPBuilder()
					http.handler.failure = { resp, reader ->
						[response:resp, reader:reader]
					}
					http.handler.success = { resp, reader ->
						[response:resp, reader:reader]
					}
					println "you are in setup method "
					println "tokenised string  :"+gettoken
					println "User name :" + username
					when:
					def map = http.request(DataValues.requestValues.get("URL")+DataValues.requestValues.get("USERSERVICE")+"v1.0/user/register?username="+USERNAME+"&"+"password="+DataValues.requestValues.get("PASSWORD"),Method.POST, "application/json")
					{
						headers.'Authorization' =
								"Basic "+ gettoken
					}
					def response = map['response']
					def reader = map['reader']
					then:
			
					String getResposecode = response.status
					assert getResposecode == status
			
					where :
			
					username | password | status
					USERNAME | DataValues.requestValues.get("PASSWORD") | DataValues.requestValues.get("STATUS200")

	}


	def "Get token for registered user"(){

					setup:
					
					println "Hash map value   : Test 2 :  " + DataValues.responseToken
					HTTPBuilder http = new HTTPBuilder()
					http.handler.failure = { resp, reader ->
						[response:resp, reader:reader]
					}
					http.handler.success = { resp, reader ->
						[response:resp, reader:reader]
					}
					println "you are in setup method 2"
					println "tokenised string  :"+gettoken
			
			
					when:
					
					def map = http.request(DataValues.requestValues.get("URL")+DataValues.requestValues.get("AUTHSERVICE")+"oauth/token?grant_type="+DataValues.requestValues.get("PASSWORD")+"&"+"username="+USERNAME+"&"+"password="+DataValues.requestValues.get("PASSWORD")+"&scope=all",Method.POST, "application/json")
					{
						headers.'Authorization' =
								"Basic "+ gettoken
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
			
				username | password | status
				USERNAME | DataValues.requestValues.get("PASSWORD") | DataValues.requestValues.get("STATUS200")

		}
	
	
	
	
	def "Register a Merchant"(){
		
							setup:
							
							println "Hash map value   : Test 3 :  " + DataValues.responseToken
							HTTPBuilder http = new HTTPBuilder()
							http.handler.failure = { resp, reader ->
								[response:resp, reader:reader]
							}
							http.handler.success = { resp, reader ->
								[response:resp, reader:reader]
							}

					
							when:
							
							
							def jsonString = new JsonBuilder()
							
							def jsonParams = jsonString rpUserId: rpUserId,merchant:  merchant,channel:  channel
							
							def map = http.request(DataValues.requestValues.get("URL")+DataValues.requestValues.get("PAYMENTSERVICE")+"v1.0/merchants",Method.POST, "application/json")
							{
								headers.'Authorization' =
								"Bearer "+DataValues.responseToken.get("access_token")
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
	
	
	
	def "credit card creation"(){
		
				setup:
					
						println "Hash map value   : Test 4 :  "
						HTTPBuilder http = new HTTPBuilder()
						http.handler.failure = { resp, reader ->
							[response:resp, reader:reader]
						}
						http.handler.success = { resp, reader ->
							[response:resp, reader:reader]
						}
		
				when :
		
				def jsonString = new JsonBuilder()
				
				def jsonParams = jsonString number: creditCardNumber,userId:  userId,cvv:  cvv,
				expiryYear: expiryYear,expiryMonth:  expiryMonth,nameOnCard:  nameOnCard,type:  cardType
				
		
		
				def map = http.request(DataValues.requestValues.get("URL")+DataValues.requestValues.get("PAYMENTSERVICE"), Method.POST, "application/json") {
					uri.path = 'v1.0/users/1/cards'
					headers.'Authorization' =
					"Bearer "+DataValues.responseToken.get("access_token")
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
				messageValue.each{println "messageValue:$it"
					
				}
				assert range.size == rangeresponse.size
				
				where :
		
				creditCardNumber	| userId			| cvv		| expiryYear | expiryMonth | nameOnCard | cardType  || status 				|| message
				DataValues.requestValues.get("CREDITCARDNUMBER")| DataValues.requestValues.get("USERID")| DataValues.requestValues.get("CV2")|DataValues.requestValues.get("EXPIRYYEAR")|DataValues.requestValues.get("EXPIRYMONTH")  |DataValues.requestValues.get("NAMEONCARD")  | DataValues.requestValues.get("CARDTYPE")||DataValues.requestValues.get("STATUS400")||""
				
		
	}
	
	
		def "Credit card creation and all validations"(){
			
					setup:
						
							println "Hash map value   : Test 5 :  " 
							HTTPBuilder http = new HTTPBuilder()
							http.handler.failure = { resp, reader ->
								[response:resp, reader:reader]
							}
							http.handler.success = { resp, reader ->
								[response:resp, reader:reader]
							}
			
					when :
			
					def jsonString = new JsonBuilder()
					
					def jsonParams = jsonString number: creditCardNumber,userId:  userId,cvv:  cvv,
					expiryYear: expiryYear,expiryMonth:  expiryMonth,nameOnCard:  nameOnCard,type:  cardType
					
			
			
					def map = http.request(DataValues.requestValues.get("URL")+DataValues.requestValues.get("PAYMENTSERVICE"), Method.POST, "application/json") {
						uri.path = 'v1.0/users/1/cards'
						headers.'Authorization' =
						"Bearer "+DataValues.responseToken.get("access_token")
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
					DataValues.requestValues.get("CREDITCARDNUMBER")    | DataValues.requestValues.get("USERID")| DataValues.requestValues.get("CV2")|DataValues.requestValues.get("EXPIRYYEAR")|DataValues.requestValues.get("EXPIRYMONTH")  |DataValues.requestValues.get("NAMEONCARD")  | DataValues.requestValues.get("CARDTYPE")||DataValues.requestValues.get("STATUS400")||""
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
									println "Hash map value   : Test 6 :  " + DataValues.responseToken
									HTTPBuilder http = new HTTPBuilder()
									http.handler.failure = { resp, reader ->
										[response:resp, reader:reader]
									}
									http.handler.success = { resp, reader ->
										[response:resp, reader:reader]
									}
									println "you are in setup method "
									println "tokenised string  :"+gettoken
								
									when:
								
									println "Get Map values  : "
									def map = http.request(DataValues.requestValues.get("URL")+DataValues.requestValues.get("PAYMENTSERVICE")+"v1.0/users/1/cards",Method.GET, "application/json")
									{
								
										headers.'Authorization' =
												"Bearer "+DataValues.responseToken.get("access_token")
									}
									def response = map['response']
									def reader = map['reader']
									then:
								
									String getResposecode = response.status
									assert getResposecode == status
//									reader.each{println "$it"}
								
									where :
								
									username | password | status
									USERNAME | DataValues.requestValues.get("PASSWORD") | DataValues.requestValues.get("STATUS200")
			
		}
		
		
		def "Refresh access token for registered user"(){
			
									setup:
									println "Hash map value   : Test 7 :  " + DataValues.responseToken
									HTTPBuilder http = new HTTPBuilder()
									http.handler.failure = { resp, reader ->
										[response:resp, reader:reader]
									}
									http.handler.success = { resp, reader ->
										[response:resp, reader:reader]
									}
									println "you are in setup method "
									println "tokenised string  :"+gettoken
								
									when:
								
									println "Get Map values  : "
									def map = http.request(DataValues.requestValues.get("URL")+DataValues.requestValues.get("AUTHSERVICE")+"oauth/token?grant_type=refresh_token&refresh_token="+DataValues.responseToken.get("refresh_token"),Method.POST, "application/json")
									{
								
										headers.'Authorization' =
												"Basic "+ gettoken
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
