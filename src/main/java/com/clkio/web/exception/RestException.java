package com.clkio.web.exception;

import com.clkio.web.constants.AppConstants;

public abstract class RestException extends Exception {
	
	private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;
	
	private int statusCode;
	
	public RestException( String message, int statusCode ) {
		super( message );
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
