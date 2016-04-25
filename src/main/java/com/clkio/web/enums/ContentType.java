package com.clkio.web.enums;

import org.springframework.util.StringUtils;

import com.clkio.web.exception.NotAcceptableException;

public enum ContentType {

	APPLICATION_XML( "application/xml" ),
	APPLICATION_JSON( "application/json" );
	
	private String value;
	
	private ContentType( String headerAccept ) {
		this.value = headerAccept;
	}
	
	public static ContentType parse( String headerValue ) throws NotAcceptableException {
		final String exceptionMsg = "Header '" + headerValue + "' is not valid, it has to be either 'application/json' or 'application/xml'.";
		if ( !StringUtils.hasText( headerValue ) )
			throw new NotAcceptableException( exceptionMsg );
		headerValue = headerValue.trim();
		for ( String accept : headerValue.split( ";" ) ) {
			accept = accept.trim();
			if ( accept.equals( APPLICATION_JSON.getValue() ) ) return APPLICATION_JSON;
			if ( accept.equals( APPLICATION_XML.getValue() ) ) return APPLICATION_XML;
		}
		throw new NotAcceptableException( exceptionMsg );
	}
	
	public String getValue() {
		return this.value;
	}
	
}
