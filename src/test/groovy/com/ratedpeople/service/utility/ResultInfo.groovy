package com.ratedpeople.service.utility

class ResultInfo {

	
	
	private String body ="";
	private String error  ="";
	private String responseCode  ="";
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
	@Override
	public String toString() {
		return "ResultInfo [body=" + body + ", error=" + error + ", responseCode=" + responseCode + "]";
	}
	 
}
