/**
 * 
 */
package com.ratedpeople.resource.rating
import groovy.json.*
import spock.lang.Specification;


/**
 * @author Shabana
 *
 */
class RatingFunctionalTest extends  Specification{

/*
	def "Rating HO "(){
		given :
		def  getratingVal = DatabaseHelper.select("select rating from rating.rating where job_id=7")
		if (getratingVal.startsWith("[{rating")){
			getratingVal = getratingVal.replace("[{rating=", "").replace("}]","")
			println "rating  value : " +getratingVal
			if(getratingVal != null ){
				DatabaseHelper.executeQuery("UPDATE rating.rating SET rating = null  WHERE job_id = 7")
				println "You are cleaning up rating for job Id 7"
			}
		}
		String responseCode = null
		def json = new JsonBuilder()
		json{
			"jobId" "7"
			"ratingType" "HOMEOWNER"
			"rating" 2
			"fromUserId" "2"
			"toUserId" "1"
			"ratingComment" {
				"comment" "I am commenting because this person is awesome"
				"fromUserId" "2"
				"toUserId" "1"
			}
		}

		println "Json is " +  json.toString()
		println "********************************"
		println "Test running ..  Rating HO"
		when:
		HTTP_BUILDER.request(Method.PUT,ContentType.JSON){
			uri.path = CommonVariable.RATING_SERVICE_PREFIX + "v1.0/users/2/jobs/7/ratings"
			println "uri.path   :"+uri.path
			println "Access Token HO : "+ ACCESS_TOKEN_HO
			headers.'Authorization' = "Bearer "+ ACCESS_TOKEN_HO
			body = json.toString()

			requestContentType = ContentType.JSON
			println "Uri is " + uri

			response.success = { resp, reader ->
				println "Success"
				println "Got response: ${resp.statusLine}"
				println "Content-Type: ${resp.headers.'Content-Type'}"
				responseCode = resp.statusLine.statusCode
				reader.each{ "Results  : "+ "$it" }
			}

			response.failure = { resp, reader ->
				responseCode = resp.statusLine.statusCode
				println " stacktrace : "+reader.each{"$it"}
			}
		}
		then:
		responseCode == CommonVariable.STATUS_200
	}*/
}



