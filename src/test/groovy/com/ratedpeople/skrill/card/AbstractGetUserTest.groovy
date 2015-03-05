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
class AbstractGetUserTest extends Specification {

	String key =""
	String token =""
	String refreshtoken =""
	String userId =""
	HTTPBuilder http = null
	def setup() {


		http = new HTTPBuilder()
		http.handler.failure = { resp, reader ->
			[response:resp, reader:reader]
		}
		http.handler.success = { resp, reader ->
			[response:resp, reader:reader]
		}
		def url = http.request(DataValues.requestValues.get("URL")+DataValues.requestValues.get("AUTHSERVICE")+"oauth/token?grant_type="+DataValues.requestValues.get("PASSWORD")+"&"+"username="+"davide"+"&"+"password="+DataValues.requestValues.get("PASSWORD")+"&scope=all",Method.POST, "application/json")
		{
			headers.'Authorization' =
					"Basic "+ gettoken
		}
		def response = url['response']
		def reader = url['reader']
		def responsemap = reader.each
		{
			println "Token values : "+"$it"

			String tokentemp = "$it"
			if (tokentemp.startsWith("access_token")){
				token = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
				println "token  " + token
			}
			if (tokentemp.startsWith("refresh_token")){
				refreshtoken = tokentemp.substring(tokentemp.indexOf("=") + 1, tokentemp.length())
				println "refreshtoken  " + refreshtoken
			}
		}
		
		def map = http.request(DataValues.requestValues.get("URL")+DataValues.requestValues.get("USERSERVICE")+"v1.0/me" ,Method.GET, "application/json")
		
				{
		
					headers.'Authorization' =
							"Bearer "+ token
				}
				def response2 = map['response']
				def reader2 = map['reader']
				then:
		
				String getResposecode = response2.status
				assert getResposecode ==  DataValues.requestValues.get("STATUS200")
				def responsemap2 = reader2.each
				{
					println "Token values : "+"$it"
					
					String user = "$it"
					if (user.startsWith("userId")){
					assert user == "userId=1"
					userId = user.replace("userId=", "")
					}
				}
	}






}
