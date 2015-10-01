package com.ratedpeople.service

import com.ratedpeople.service.utility.HttpConnectionService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable

final class RatingService {
	
	private static final RATING_PREFIX = CommonVariable.RATING_SERVICE_PREFIX + "v1.0/users/"
	private static final HttpConnectionService http = new HttpConnectionService()
	
	public ResultInfo createRating(final def rating, final String jobId, final UserInfo userInfo){
		String url = RATING_PREFIX + userInfo.getId() + "/jobs/${jobId}/ratings"
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(), null, rating)
		
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Created"
		}
		
		return result;
	}
	
	public ResultInfo getRating(final String jobId, final UserInfo userInfo){
		String url = RATING_PREFIX + userInfo.getId() + "/jobs/${jobId}/ratings"
		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(), null)
		
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Ok"
		}
		
		return result;
	}
}



