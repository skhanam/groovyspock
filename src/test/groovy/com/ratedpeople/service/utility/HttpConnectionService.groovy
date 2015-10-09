package com.ratedpeople.service.utility
import groovy.json.*
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.ByteArrayBody

import com.ratedpeople.support.CommonVariable

final class HttpConnectionService {

	public static final HTTPBuilder HTTP_BUILDER = new HTTPBuilder(CommonVariable.SERVER_URL)

	private ResultInfo callPostMethodWithoutAuthentication(final String url, final def query, final def json){
		ResultInfo info = new ResultInfo()
		HTTP_BUILDER.ignoreSSLIssues();
		
		String dateLocal = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyy"))
		String base64String= "clientId=${CommonVariable.CLIENT_ID}&date=${dateLocal}&methodType=POST"
		println base64String
		def response = HTTP_BUILDER.request(Method.POST,ContentType.JSON) {
			uri.path = url
			headers.'Authentication' = new String(Base64.getEncoder().encode(base64String.getBytes()))
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

	private ResultInfo callPostMethodWithAuthentication(final String url, final String token, final def json){
		ResultInfo info = new ResultInfo()
		HTTP_BUILDER.ignoreSSLIssues();
		def response = HTTP_BUILDER.request(Method.POST,ContentType.JSON) {
			uri.path = url
			headers.'Authorization' = "Bearer "+ token
			body = json.toString()
			requestContentType = ContentType.JSON
			headers.Accept = ContentType.JSON
			println "Post user Uri : " + uri

			response.success = { resp, reader ->
				println "Success"
				info.responseCode = resp.statusLine.statusCode
				reader.each{
					String temp = "$it"
					info.setBody(info.getBody()+temp)
				}
			}
			response.failure = { resp, reader ->
				println "Fail"
				reader.each{
					String temp = "$it"
					info.setError(info.getError()+temp)
				}
				info.responseCode = resp.statusLine
			}
		}

		return info;
	}

	private ResultInfo callPostMethodWithAuthenticationAndImage(final String url, final String token, final def queryText){
		ResultInfo info = new ResultInfo()
		HTTP_BUILDER.ignoreSSLIssues();
		def response = HTTP_BUILDER.request(Method.POST,ContentType.JSON) {req ->
			requestContentType = 'multipart/form-data'
			uri.path = url
			uri.query = queryText
			headers.'Authorization' = "Bearer "+ token
			
			MultipartEntityBuilder entity = new MultipartEntityBuilder()
			def file = new File('src/test/resources/imageTest1.jpg');
			entity.addPart("file",new ByteArrayBody(file.getBytes(), 'src/test/resources/imageTest1.jpg'))
			req.entity = entity.build();
			println "Post user Uri : " + uri

			response.success = { resp, reader ->
				println "Success"
				info.responseCode = resp.statusLine.statusCode
				reader.each{
					String temp = "$it"
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

	private ResultInfo callDeleteMethodWithAuthentication(final String url, final String token){
		ResultInfo info = new ResultInfo()
		HTTP_BUILDER.ignoreSSLIssues();
		def response = HTTP_BUILDER.request(Method.DELETE,ContentType.JSON) {
			uri.path = url
			headers.'Authorization' = "Bearer "+ token
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

	private ResultInfo callGetToken(final String url, final def query, final def json){
		ResultInfo info = new ResultInfo()
		HTTP_BUILDER.ignoreSSLIssues();
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
				info.responseCode = resp.statusLine.statusCode
				reader.each{
					String temp = "$it"
					info.setBody(info.getBody()+temp)
				}
			}
			response.failure = { resp, reader ->
				println "Fail"
				reader.each{
					String temp = "$it"
					info.setError(info.getError()+temp)
				}
				info.responseCode = resp.statusLine
			}
		}

		return info
	}

	private ResultInfo callGetMethodWithAuthentication(final String url, final String token, final def query){
		ResultInfo info = new ResultInfo()
		HTTP_BUILDER.ignoreSSLIssues();
		HTTP_BUILDER.request(Method.GET){
			headers.Accept = 'application/json'
			headers.'Authorization' = "Bearer " + token
			uri.path = url
			uri.query = query
			println "Uri is " + uri

			response.success = { resp, reader ->
				println "Success"
				reader.each{
					String temp = "$it"
					info.setBody(info.getBody()+","+temp)
				}
				info.responseCode = resp.statusLine
			}
			response.failure = { resp, reader ->
				println "Fail"
				reader.each{
					String temp = "$it"
					info.setError(info.getError()+temp)
				}
				info.responseCode = resp.statusLine
			}
		}
		return info
	}

	private ResultInfo callGetMethodWithoutAuthentication(final String url, final def query){
		ResultInfo info = new ResultInfo()
		String dateLocal = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyy"))
		String base64String= "clientId=${CommonVariable.CLIENT_ID}&date=${dateLocal}&methodType=GET"
		println base64String
		
		HTTP_BUILDER.ignoreSSLIssues();
		HTTP_BUILDER.request(Method.GET,ContentType.JSON){
			headers.'Authentication' = new String(Base64.getEncoder().encode(base64String.getBytes()))
			uri.path = url
			uri.query = query
			println "Uri is " + uri

			response.success = { resp, reader ->
				println "Success"
				reader.each{
					String temp = "$it"
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

	private ResultInfo callPutMethodWithAuthentication(final String url, final String token, final def query, final def bodyText){	
		ResultInfo info = new ResultInfo()
		HTTP_BUILDER.ignoreSSLIssues();
		HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
			headers.Accept = 'application/json'
			headers.'Authorization' = "Bearer "+ token
			uri.path = url
			
			if(query!=null){
				uri.query = query
			}
			
			if(bodyText!=null){
				body = bodyText.toString()
			}
			
			println "Uri is " + uri
			response.success = { resp, reader ->
				println "Success"
				reader.each{
					String temp = "$it"
					info.setBody(info.getBody()+","+temp)
				}
				info.responseCode = resp.statusLine
			}
			
			response.failure = { resp, reader ->
				println "Fail"
				reader.each{
					String temp = "$it"
					info.setError(info.getError()+temp)
				}
				info.responseCode = resp.statusLine
			}
		}
		
		return info
	}
	
	private ResultInfo callPutMethodWithoutAuthentication(final String url, final def query, final def bodyText){
		ResultInfo info = new ResultInfo()
		String dateLocal = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyy"))
		String base64String= "clientId=${CommonVariable.CLIENT_ID}&date=${dateLocal}&methodType=PUT"
		println base64String
		HTTP_BUILDER.ignoreSSLIssues();
		HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
			headers.'Authentication' = new String(Base64.getEncoder().encode(base64String.getBytes()))
			uri.path = url
			if(query!=null){
				uri.query = query
			}
			
			if(bodyText!=null){
				body = bodyText.toString()
			}
			
			println "Uri is " + uri
			response.success = { resp, reader ->
				println "Success"
				reader.each{
					String temp = "$it"
					info.setBody(info.getBody()+","+temp)
				}
				info.responseCode = resp.statusLine
			}
			
			response.failure = { resp, reader ->
				println "Fail"
				reader.each{
					String temp = "$it"
					info.setError(info.getError()+temp)
				}
				info.responseCode = resp.statusLine
			}
		}
		
		return info
	}

}
