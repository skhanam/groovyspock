/**
 * 
 */
package com.ratedpeople.service

import groovy.json.*

import com.ratedpeople.service.utility.HttpConnectionService
import com.ratedpeople.service.utility.ResultInfo
import com.ratedpeople.service.utility.UserInfo
import com.ratedpeople.support.CommonVariable
/**
 * @author shabana.khanam
 *
 */
class JobService{

	private static final String URL_STATUS = "/status"

	private static final String REGISTER = "register"

	private static final String HOMEOWNER_URI_PREFIX = CommonVariable.USER_SERVICE_PREFIX + "v1.0/homeowners/"

	private static final String EMAIL_POSTFIX = "@gid.com"

	private  String ACCESS_TOKEN_DYNAMIC_HO
	//private  String REFRESH_TOKEN_DYNAMIC_HO
	private  String USER_ID_DYNAMIC_HO
	private  String ACCESS_TOKEN_ADMIN
	//private  String REFRESH_TOKEN_ADMIN

	private  String DYNAMIC_USER

	private static final PROFILE_PREFIX = CommonVariable.HOPROFILE_SERVICE_PREFIX + "v1.0/users/"

	HttpConnectionService http = new HttpConnectionService()





	private def createJob(def job,UserInfo userInfo){

		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/jobs"
		ResultInfo result = http.callPostMethodWithAuthentication(url, userInfo.getToken(), job)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Created"
		}
		return result;
	}


	private def getAllJobsForHo(UserInfo userInfo){

		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/hojobs"

		def query = [
			jobStatus:'PENDING',
			offset:0,
			limit:10
		]
		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(), query)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "OK"
		}
		return result;
	}

	private def getJobForHo(UserInfo userInfo,String jobId){

		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/hojobs/"+jobId


		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(), null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "OK"
		}
		return result;
	}

	private def getJobForTm(UserInfo userInfo,String jobId){

		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/tmjobs/"+jobId

		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(), null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "OK"
		}
		return result;
	}





	private def withdrawJob(UserInfo userInfo,String jobId){

		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/hojobs/"+jobId+"/withdraw"
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(), null,null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}

	private def acceptJob(UserInfo userInfo,String jobId){

		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/tmjobs/"+jobId+"/schedule"
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(), null,null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}

	private def startJob(UserInfo userInfo,String jobId){

		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/tmjobs/"+jobId+"/start"
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(), null,null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}

	private def pauseJob(UserInfo userInfo,String jobId,def body){

		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/tmjobs/"+jobId+"/pause"
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(), null,body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}

	private def completeJob(UserInfo userInfo,String jobId,def body){

		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/tmjobs/"+jobId+"/complete"
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(), null,body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}

	private def rejectJob(UserInfo userInfo,String jobId,def body){

		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/tmjobs/"+jobId+"/reject"
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(), null,body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}

	private def raiseInvoice(UserInfo userInfo,String jobId,def body){

		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/tmjobs/"+jobId+"/invoice"
		ResultInfo result = http.callPutMethodWithAuthorization(url, userInfo.getToken(), null,body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		return result;
	}

	private def getAllJobsForTm(UserInfo userInfo){

		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/tmjobs"

		def query = [
			jobStatus:'REQUESTED',
			offset:0,
			limit:10
		]
		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(), query)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "OK"
		}
		return result;
	}

	private def getJobInfoForTm(UserInfo userInfo,String hoId){

		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + hoId + "/contactdetails"

		ResultInfo result = http.callGetMethodWithAuthorization(url, userInfo.getToken(), null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "OK"
		}
		return result;
	}



}
