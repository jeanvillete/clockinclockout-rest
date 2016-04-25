package com.clkio.web.exception;

public abstract class RestException extends Exception {
	
	private int statusCode;
	
	public RestException( String message, int statusCode ) {
		super( message );
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
