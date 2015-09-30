package com.ratedpeople.service.utility

import groovyx.net.http.HTTPBuilder;
import groovy.json.*
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import com.ratedpeople.support.CommonVariable


class HttpConnectionService {

	public static final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(CommonVariable.SERVER_URL)

	private def callPostMethodWithoutAuthentication(String url,def query, def json){
		ResultInfo info = new ResultInfo()
		def response = HTTP_BUILDER.request(Method.POST,ContentType.JSON) {
			uri.path = url
			uri.query = query
			body = json.toString()
			requestContentType = ContentType.JSON
			headers.Accept = ContentType.JSON
			println "Post user Uri : " + uri


			response.success = { resp, reader ->
				println "Success"

				//info.responseCode = resp.statusLine.statusCode
				info.responseCode = resp.statusLine.statusCode
				reader.each{
					//"Results  : "+ "$it"
					String temp = "$it"
					//println temp
					info.setBody(info.getBody()+temp)
				}
			}
			response.failure = { resp, reader ->
				println "Fail"
				reader.each{
					//"Results  : "+ "$it"
					String temp = "$it"
					info.setError(info.getError()+temp)
				}
				info.responseCode = resp.statusLine
			}
		}

		return info;
	}
	
	
	private def callPostMethodWithAuthentication(String url,String token, def json){
		ResultInfo info = new ResultInfo()
		def response = HTTP_BUILDER.request(Method.POST,ContentType.JSON) {
			uri.path = url
			headers.'Authorization' = "Bearer "+ token
			body = json.toString()
			requestContentType = ContentType.JSON
			headers.Accept = ContentType.JSON
			println "Post user Uri : " + uri


			response.success = { resp, reader ->
				println "Success"

				//info.responseCode = resp.statusLine.statusCode
				info.responseCode = resp.statusLine.statusCode
				reader.each{
					//"Results  : "+ "$it"
					String temp = "$it"
					//println temp
					info.setBody(info.getBody()+temp)
				}
			}
			response.failure = { resp, reader ->
				println "Fail"
				reader.each{
					//"Results  : "+ "$it"
					String temp = "$it"
					info.setError(info.getError()+temp)
				}
				info.responseCode = resp.statusLine
			}
		}

		return info;
	}

	private def callGetToken(String url,def query, def json){
		ResultInfo info = new ResultInfo()
		def response = HTTP_BUILDER.request(Method.POST,ContentType.JSON) {
			headers.Accept = 'application/json'
			headers.'Authorization' = "Basic "+ CommonVariable.DEFAULT_CLIENT_CREDENTIAL.bytes.encodeBase64().toString()
			uri.path = url
			uri.query = query
			body = json.toString()
			requestContentType = ContentType.JSON
			headers.Accept = ContentType.JSON
			println "Post user Uri : " + uri

			response.success = { resp, reader ->
				println "Success"

				//info.responseCode = resp.statusLine.statusCode
				info.responseCode = resp.statusLine.statusCode
				reader.each{
					//"Results  : "+ "$it"
					String temp = "$it"
					//println temp
					info.setBody(info.getBody()+temp)
				}
			}
			response.failure = { resp, reader ->
				println "Fail"
				reader.each{
					//"Results  : "+ "$it"
					String temp = "$it"
					info.setError(info.getError()+temp)
				}
				info.responseCode = resp.statusLine
			}
		}


		return info
	}

	private def callGetMethodWithAuthorization(String url,String token, def query){
		ResultInfo info = new ResultInfo()

		HTTP_BUILDER.request(Method.GET){
			headers.Accept = 'application/json'
			headers.'Authorization' = "Bearer " + token
			uri.path = url
			uri.query = query
			println "Uri is " + uri

			response.success = { resp, reader ->
				println "Success"
				reader.each{
					//"Results  : "+ "$it"
					String temp = "$it"
					//println temp
					info.setBody(info.getBody()+","+temp)
				}
				info.responseCode = resp.statusLine
			}
			response.failure = { resp, reader ->
				println "Fail"
				reader.each{
					//"Results  : "+ "$it"
					String temp = "$it"
					info.setError(info.getError()+temp)
				}
				info.responseCode = resp.statusLine
			}
		}
		return info
	}

	private def callPutMethodWithAuthorization(String url,String token, def queryText,def bodyText){
		
		ResultInfo info = new ResultInfo()

		HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
			headers.Accept = 'application/json'
			headers.'Authorization' = "Bearer "+ token
			uri.path = url
			if(queryText!=null){
				uri.query = queryText
			}
			if(bodyText!=null){
				body = bodyText.toString()
			}
			println "Uri is " + uri
			response.success = { resp, reader ->
				println "Success"
				reader.each{
					//"Results  : "+ "$it"
					String temp = "$it"
					//println temp
					info.setBody(info.getBody()+","+temp)
				}
				info.responseCode = resp.statusLine
			}
			response.failure = { resp, reader ->
				println "Fail"
				reader.each{
					//"Results  : "+ "$it"
					String temp = "$it"
					info.setError(info.getError()+temp)
				}
				info.responseCode = resp.statusLine
			}
		}
		return info
	}
	
	
	
}
