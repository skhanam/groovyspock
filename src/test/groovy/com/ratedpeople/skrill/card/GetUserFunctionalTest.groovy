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
class GetUserFunctionalTest extends AbstractGetUserTest {

	public static String  gettoken = DataValues.requestValues.get("TOKEN").bytes.encodeBase64().toString()
	



	def "Get User Id"(){

		setup:
		println "Hash map value   : Test 1 :  " + token
		println "tokenised string  :"+gettoken

		when:

		println "Get Map values  : "
		def map = http.request(DataValues.requestValues.get("URL")+DataValues.requestValues.get("USERSERVICE")+"v1.0/me" ,Method.GET, "application/json")

		{

			headers.'Authorization' =
					"Bearer "+ token
		}
		def response = map['response']
		def reader = map['reader']
		then:

		String getResposecode = response.status
		assert getResposecode ==  DataValues.requestValues.get("STATUS200")
		def responsemap = reader.each
		{
			println "Token values : "+"$it"

			String user = "$it"
			assert user == "userId=1"

		}


	}





	def "Refresh access token for registered user"(){

		setup:
		println "Hash map value   : Test 8 :  " + token
		println "you are in setup method "
		println "tokenised string  :"+gettoken

		when:

		println "Get Map values  : "
		def map = http.request(DataValues.requestValues.get("URL")+DataValues.requestValues.get("AUTHSERVICE")+"/oauth/token?grant_type=refresh_token&refresh_token="+refreshtoken,Method.POST, "application/json")
		{

			headers.'Authorization' =
					"Basic "+ gettoken
		}
		def response = map['response']
		def reader = map['reader']
		then:

		String getResposecode = response.status
		assert getResposecode ==  DataValues.requestValues.get("STATUS200")
		reader.each{
			println "$it"
			String info = "$it"
			if (info.startsWith("access_token")){
				assert info.endsWith(token) == false
			}

		}




	}






}
