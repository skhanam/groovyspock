/**
 * 
 */
package com.ratedpeople.user.resource

import com.ratedpeople.user.token.AbstractHomeowner;
import com.ratedpeople.user.token.AbstractUserToken;

import groovyx.net.http.HTTPBuilder;

import com.ratedpeople.support.DataValues
/**
 * @author shabana.khanam
 *
 */
class SmsPinTokenTest extends AbstractUserToken{
	
	private final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(DataValues.requestValues.get("URL"))
	
	def testRegeneratePINToken(){
		println "SmsPinToken"
		println "Test 1 :  testRegeneratePINToken"
		
		given:
		String responseStatus = null
		
		when:
		HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
						uri.path = DataValues.requestValues.get("PROFILESERVICE")+"v1.0/users/"+USER_ID_HO+"/generatepin"
						println "uri path : "+uri.path
						headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_HO
//						body = json.toString()
						requestContentType = ContentType.JSON
						println "Uri is " + uri
						
						response.success = { resp, reader ->
							println "Success"
							println "Got response: ${resp.statusLine}"
							println "Content-Type: ${resp.headers.'Content-Type'}"
							responseCode = resp.statusLine.statusCode
							reader.each{
								"Results  : "+ "$it"
							}
						}
			
						response.failure = {
							resp, reader -> println " stacktrace : "+reader.each{"$it"}
							println 'Not found'
						}
					}
				then:
					responseStatus == DataValues.requestValues.get("STATUS200")
		
	}
	
	
	

}
