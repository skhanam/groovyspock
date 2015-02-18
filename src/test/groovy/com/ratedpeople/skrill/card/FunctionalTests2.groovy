package com.ratedpeople.skrill.card

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovyx.net.http.HttpResponseDecorator;
import groovyx.net.http.RESTClient
import spock.lang.Specification

class FunctionalTests2 extends Specification {
	
	def "test "() {
		setup:

		HTTPBuilder http = new HTTPBuilder()
		http.handler.failure = { resp, reader ->
			[response:resp, reader:reader]
		}
		http.handler.success = { resp, reader ->
			[response:resp, reader:reader]
		}
		
		when:
		String input1 ="{\"number\":\"4000000000000051\", \"userId\":\"34\",\"cvv\":\"123\",\"expiryYear\":\"2016\",\"expiryMonth\":\"01\",\"nameOnCard\":\"davide\",\"type\":\"visa\"}"
		def map = http.request("http://minerva.ratedcloud.net:8091/", Method.POST, "application/json") {
			uri.path = 'v1.0/register'
			send('application/json', input1)
		}

		def response = map['response']
		def reader = map['reader']
		println "reader type: ${reader.get('cause')}"
		
		then:
		assert response.status == 401
		assert reader.get('cause')=="Credit card already exists for this user"


	

	}
}