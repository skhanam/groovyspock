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
final class JobService{

	private static HttpConnectionService http = new HttpConnectionService()

	public ResultInfo createJob(def job,UserInfo userInfo){
		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/jobs"
		
		ResultInfo result = http.callPostMethodWithAuthentication(url, userInfo.getToken(), job)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_201)){
			println "Created"
		}

		return result;
	}


	public ResultInfo getAllJobsForHo(UserInfo userInfo){
		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/hojobs"

		def query = [
			jobStatus:'PENDING',
			offset:0,
			limit:10
		]
		
		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(), query)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "OK"
		}
		
		return result;
	}

	public ResultInfo getJobForHo(UserInfo userInfo,String jobId){
		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/hojobs/"+jobId

		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(), null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "OK"
		}
		
		return result;
	}

	public ResultInfo getJobForTm(UserInfo userInfo,String jobId){
		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/tmjobs/"+jobId

		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(), null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "OK"
		}
		
		return result;
	}

	public ResultInfo withdrawJob(UserInfo userInfo,String jobId){
		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/hojobs/"+jobId+"/withdraw"
		
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(), null,null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		
		return result;
	}

	public ResultInfo acceptJob(UserInfo userInfo,String jobId){
		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/tmjobs/"+jobId+"/schedule"
		
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(), null,null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		
		return result;
	}

	public ResultInfo startJob(UserInfo userInfo,String jobId){
		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/tmjobs/"+jobId+"/start"
		
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(), null,null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		
		return result;
	}

	public ResultInfo pauseJob(UserInfo userInfo,String jobId,def body){
		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/tmjobs/"+jobId+"/pause"
		
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(), null,body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		
		return result;
	}

	public ResultInfo completeJob(UserInfo userInfo,String jobId,def body){
		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/tmjobs/"+jobId+"/complete"
		
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(), null,body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		
		return result;
	}

	public ResultInfo rejectJob(UserInfo userInfo,String jobId,def body){
		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/tmjobs/"+jobId+"/reject"
		
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(), null,body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		
		return result;
	}

	public ResultInfo raiseInvoice(UserInfo userInfo,String jobId,def body){
		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/tmjobs/"+jobId+"/invoice"
		
		ResultInfo result = http.callPutMethodWithAuthentication(url, userInfo.getToken(), null,body)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "Updated"
		}
		
		return result;
	}

	public ResultInfo getAllJobsForTm(UserInfo userInfo){
		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + userInfo.getId() + "/tmjobs"

		def query = [
			jobStatus:'REQUESTED',
			offset:0,
			limit:10
		]
		
		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(), query)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "OK"
		}
		
		return result;
	}

	public ResultInfo getJobInfoForTm(UserInfo userInfo,String hoId){
		String url =  CommonVariable.JOB_SERVICE_PREFIX + "v1.0/users/" + hoId + "/contactdetails"

		ResultInfo result = http.callGetMethodWithAuthentication(url, userInfo.getToken(), null)
		if(result.getResponseCode().toString().contains(CommonVariable.STATUS_200)){
			println "OK"
		}
		
		return result;
	}
}
