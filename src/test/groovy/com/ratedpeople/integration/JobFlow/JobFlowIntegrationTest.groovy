/**
 * 
 */
package com.ratedpeople.integration.JobFlow

import com.ratedpeople.homeowner.profile.resource.HomeownerProfileFunctionalTest;
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper
import com.ratedpeople.user.resource.AbstractHomeowner;

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
/**
 * @author shabana.khanam
 *
 */
class JobFlowIntegrationTest extends AbstractHomeowner{

	private static String getpinToken
	private static final String JOB_URI_PREFIX = CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/"
	
	def "JobFlow Integration"(){
		given :
			validatePIN();
			String responseStatus = null
			def json = new JsonBuilder()
			json{ "pinToken" getpinToken }
			println "Json is    : "+json.toString()
			
		when:
			HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
				uri.path = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/users/" + USER_ID_DYNAMIC_HO + "/validatepin"
				println "uri path : "+uri.path
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_DYNAMIC_HO
				body = json.toString();
				requestContentType = ContentType.JSON
				println "Uri is " + uri
				response.success = { resp, reader ->
					println "Success"
					println "Got response: ${resp.statusLine}"
					responseStatus = resp.statusLine.statusCode
					println "Content-Type: ${resp.headers.'Content-Type'}"
					reader.each{ "Results  : "+ "$it" }
				}
	
				response.failure = { resp, reader ->
					println "Request failed with status ${resp.status}"
					reader.each{ println "Error values : "+"$it" }
					responseStatus = resp.statusLine.statusCode
				}
			}
		then:
			responseStatus ==  CommonVariable.STATUS_200
			Thread.sleep(3000);
			getToken();
			postajob(JOB_URI_PREFIX,createJsonPostaJob(""));
		
	}
	

	private def validatePIN(){
		def getphoneId = DatabaseHelper.select("select phone_id from hoprofile.ho_profile where user_id =  '${USER_ID_DYNAMIC_HO}'")
		getpinToken = DatabaseHelper.select("select pin_token from hoprofile.phone where id IN ( select phone_id from hoprofile.ho_profile where user_id = '${USER_ID_DYNAMIC_HO}')")
		if (getpinToken.startsWith("[{pin_token")){
			getpinToken = getpinToken.replace("[{pin_token=", "").replace("}]","")
			println "pin token is : " +getpinToken
		}
	}
	
	private def getToken(){
		authToken();	
	}
	
	
		
}
