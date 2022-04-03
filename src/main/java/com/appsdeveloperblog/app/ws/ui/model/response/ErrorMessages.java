package com.appsdeveloperblog.app.ws.ui.model.response;

public enum ErrorMessages {
   
	MISSING_REQUIRED_FIELD("missing required field. check documentation la gundu"),
	RECORD_ALREADY_EXISTS("record alrd exists"),
	INTERNAL_SERVER_ERROR("internal server"),
	NO_REC("no rec"),
	AUTHENTICATION_FAILED("failed autho"),
	COULD_NOT_UPDATE_RECORD("cannot update la");
	
	private String errorMessage;
	
	ErrorMessages(String errorMessage)
	{
		this.errorMessage=errorMessage;
	}
	
	public String getErrorMessage()
	{
		return this.errorMessage;
	}
	
	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}
	
}
