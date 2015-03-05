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


		
		
		

}
