package com.clkio.web.exception;

import javax.servlet.http.HttpServletResponse;

public class NotAcceptableException extends RestException {

	public NotAcceptableException( String message ) {
		super( message, HttpServletResponse.SC_NOT_ACCEPTABLE );
	}

}
