package com.clkio.web.exception;

import javax.servlet.http.HttpServletResponse;

import com.clkio.web.constants.AppConstants;

public class BadRequestException extends RestException {
	
	private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

	public BadRequestException( String message ) {
		super( message, HttpServletResponse.SC_BAD_REQUEST );
	}

}
