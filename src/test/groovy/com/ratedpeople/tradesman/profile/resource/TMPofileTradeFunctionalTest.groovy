/**
 * 
 */
package com.ratedpeople.tradesman.profile.resourceimport com.ratedpeople.user.resource.AbstractUserToken;
import groovyx.net.http.HTTPBuilder;

import com.ratedpeople.user.resource.AbstractTradesman;
import com.ratedpeople.user.resource.AbstractUserToken;
import com.ratedpeople.support.CommonVariable
import com.ratedpeople.support.DatabaseHelper

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.ByteArrayBody
import org.apache.http.entity.mime.content.FileBody


/**
 * @author shabana.khanam
 *
 */
class TMPofileTradeFunctionalTest  extends AbstractUserToken{

	private static final String PROFILE_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"
	
	private static final String MATCH_PREFIX = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/match"
		
	
	
	
	def "Add  Tradesman Profile Trade"()
    {
		given :
			String responseCode = null
			def  getTradeId = DatabaseHelper.select("select trade_id from tmprofile.tm_profile_trade where tm_profile_id =  1 limit 1")
//			println "Trade id : " +getTradeId
			
//			if (getTradeId.startsWith("[{trade_id=")){
//				getTradeId = getTradeId.replace("[{trade_id=", "").replace("}]","")
//				println "Profile trade id : " +getTradeId
//			}
//			DatabaseHelper.executeQuery("delete from tmprofile.tm_profile_trade where tm_profile_id =  1 and trade_id=1")
//			DatabaseHelper.select("select trade_id from tmprofile.tm_profile_trade where tm_profile_id =  1 limit 1")
//			def json = new JsonBuilder()
//			json{
//				"tradeId" "1"
//				"rate" CommonVariable.DEFAULT_HOURRATE
//			}
			def json = getTrade(1,10.00)
			println "Json is " +  json.toString()
			println "********************************"
			println "Test running ..  Add Tradesman Profile"
		when:
			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				uri.path = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"+USER_ID_TM+"/profiletrades"
				println "uri.path   :"+uri.path
				println "Access Token TM : "+ ACCESS_TOKEN_TM
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
				body = json.toString();
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
	
				response.failure = { resp, reader ->
					responseCode = resp.statusLine.statusCode
					println " stacktrace : "+reader.each{"$it"}
				}
			}
		then:
			responseCode == CommonVariable.STATUS_201
		cleanup:
			DatabaseHelper.executeQuery("delete from tmprofile.tm_profile_trade where tm_profile_id =  1 and trade_id=1")
	}
	
	
	
	
	def "Update  Tradesman Trade"(){
		given :
			String responseCode = null
			def json = getTrade(3,10.00)
//			json{
//				"tradeId" "3"
//				"rate" CommonVariable.DEFAULT_HOURRATE
//			}
//			
			println "Json is " +  json.toString()
			println "********************************"
			println "Test running ..  Update Tradesman Trade"
		
			def  getId = DatabaseHelper.select("select id from tmprofile.tm_profile_trade where updated_by=1  limit 1")
//			println "Profile trade id : " +getId
			if(getId != null)
			{
				DatabaseHelper.executeQuery("delete from tmprofile.tm_profile_trade where updated_by = 1")
//					if (getId.startsWith("[{id=")){
//						getId = getId.replace("[{id=", "").replace("}]","")
//						println "Profile trade id : " +getId
//					}
			}
			println "Test you are posting a trade before Post ......"

			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				uri.path = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"+USER_ID_TM+"/profiletrades"
				println "uri.path   :"+uri.path
				println "Access Token TM : "+ ACCESS_TOKEN_TM
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
				body = json.toString();
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
	
				response.failure = { resp, reader ->
					responseCode = resp.statusLine.statusCode
					println " stacktrace : "+reader.each{"$it"}
				}
			}
			def  getProfiletradeId = DatabaseHelper.select("select id from tmprofile.tm_profile_trade where updated_by=1  limit 1")
			if (getProfiletradeId.startsWith("[{id=")){
				getProfiletradeId = getProfiletradeId.replace("[{id=", "").replace("}]","")
				println "Profile trade id : " +getProfiletradeId
			}
		when:
		println "Test you have posted a trade before upgrade ......"
		def json1 = getTrade(3,15.00)
			HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
				uri.path = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"+USER_ID_TM+"/profiletrades/"+getProfiletradeId
				println "uri.path   :"+uri.path
				println "Access Token TM : "+ ACCESS_TOKEN_TM
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
				body = json1.toString()
				
				println "Json Body : "+body
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
	
				response.failure = { resp, reader ->
					responseCode = resp.statusLine.statusCode
					println " stacktrace : "+reader.each{"$it"}
				}
			}
		then:
			responseCode == CommonVariable.STATUS_200
//		cleanup:
//		DatabaseHelper.executeQuery("Update tmprofile.tm_profile_trade set trade_id = 2 where trade_id = 3")
	}
	
	
	
	
	def "Get  Tradesman Trade"(){
		given :
			String responseCode = null
			println "********************************"
			println "Test running ..  Get Tradesman Trade"
		when:
			HTTP_BUILDER.request(Method.GET,ContentType.JSON){
				uri.path = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"+USER_ID_TM+"/profiletrades"
				println "uri.path   :"+uri.path
				println "Access Token TM : "+ ACCESS_TOKEN_TM
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
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
	
				response.failure = { resp, reader ->
					responseCode = resp.statusLine.statusCode
					println " stacktrace : "+reader.each{"$it"}
				}
			}
		then:
			responseCode == CommonVariable.STATUS_200
	}
	
	
	def "Delete  Tradesman Trade"(){
		given :
			String responseCode = null
			println "********************************"
			println "Test running ..  Delete Tradesman Trade"
		
			def  getId = DatabaseHelper.select("select id from tmprofile.tm_profile_trade where tm_profile_id =  '${USER_ID_TM}' limit 1")
			if (getId.startsWith("[{id=")){
				getId = getId.replace("[{id=", "").replace("}]","")
				println "TM Profile id : " +getId
			}
			def  gettradeId = DatabaseHelper.select("select trade_id from tmprofile.tm_profile_trade where tm_profile_id =  '${USER_ID_TM}' limit 1")
			if (gettradeId.startsWith("[{trade_id=")){
				gettradeId = getId.replace("[{trade_id=", "").replace("}]","")
				println "Profile trade id : " +gettradeId
			}
			def json = new JsonBuilder()
			json{
				"tradeId" gettradeId
				"rate" CommonVariable.DEFAULT_HOURRATE
			}
			println "Json String is :"+json.toString()
			
		when:
			HTTP_BUILDER.request(Method.DELETE,ContentType.JSON){
				uri.path = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"+USER_ID_TM+"/profiletrades/"+gettradeId
				println "uri.path   :"+uri.path
				println "Access Token TM : "+ ACCESS_TOKEN_TM
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM

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
	
				response.failure = { resp, reader ->
					responseCode = resp.statusLine.statusCode
					println " stacktrace : "+reader.each{"$it"}
				}
			}
		then:
			responseCode == CommonVariable.STATUS_200
		cleanup:
		println "Are you cleaning at all ........ "
			HTTP_BUILDER.request(Method.POST,ContentType.JSON){
				uri.path = CommonVariable.TMPROFILE_SERVICE_PREFIX + "v1.0/users/"+USER_ID_TM+"/profiletrades"
				println "uri.path   :"+uri.path
				println "Access Token TM : "+ ACCESS_TOKEN_TM
				headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_TM
				body = getTrade(1,10.00);
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
	
				response.failure = { resp, reader ->
					responseCode = resp.statusLine.statusCode
					println " stacktrace : "+reader.each{"$it"}
				}
			}
	}
	
	
	private def getTrade(int trade,float defaultrate){
		def json = new JsonBuilder()
		json {
				"tradeId" trade
				"rate" defaultrate
		}
		return json;
	}
	
}
