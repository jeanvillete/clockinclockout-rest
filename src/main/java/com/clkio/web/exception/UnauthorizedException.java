package com.clkio.web.exception;

import javax.servlet.http.HttpServletResponse;

import com.clkio.web.constants.AppConstants;

public class UnauthorizedException extends RestException {
	
	private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;
	
	public enum HeaderAuthenticate {
		KEY( "WWW-Authenticate" ),
		VALUE( "Basic realm=\"required proper user and password\"" );
		
		private String message;
		private HeaderAuthenticate( String message ) {
			this.message = message;
		}
		public String getMessage() {
			return this.message;
		}
	}
	
	public UnauthorizedException() {
		super( "", HttpServletResponse.SC_UNAUTHORIZED );
	}

}
