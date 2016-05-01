package com.clkio.web.enums;

import org.springframework.util.StringUtils;

public enum ContentType {

	APPLICATION_XML( "application/xml" ),
	APPLICATION_JSON( "application/json" );
	
	private String value;
	
	private ContentType( String headerAccept ) {
		this.value = headerAccept;
	}
	
	public static ContentType parse( String headerValue ) {
		if ( !StringUtils.hasText( headerValue ) )
			return null;
		headerValue = headerValue.trim();
		for ( String accept : headerValue.split( ";" ) ) {
			accept = accept.trim();
			if ( accept.equals( APPLICATION_JSON.getValue() ) ) return APPLICATION_JSON;
			if ( accept.equals( APPLICATION_XML.getValue() ) ) return APPLICATION_XML;
		}
		return null;
	}
	
	public String getValue() {
		return this.value;
	}
	
}
