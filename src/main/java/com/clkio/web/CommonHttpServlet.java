package com.clkio.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.clkio.web.constants.AppConstants;
import com.clkio.web.enums.ContentType;
import com.clkio.web.exception.NotAcceptableException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public abstract class CommonHttpServlet extends HttpServlet {

	private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;
	
	@Override
	public void init() throws ServletException {
		super.init();
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext( this, getServletContext() );
	}
	
	ObjectMapper getMarshaller( ContentType contentType ) throws NotAcceptableException {
		if ( contentType == null ) throw new NotAcceptableException( "Header 'Content-Type' is mandatory and has to be either 'application/json' or 'application/xml'." );
		else if ( contentType.equals( ContentType.APPLICATION_JSON ) )
			return new ObjectMapper();
		else if ( contentType.equals( ContentType.APPLICATION_XML ) )
			return new XmlMapper();
		else throw new IllegalStateException( "No valid value for header 'Content-Type'. contentType=[" + contentType.getValue() + "]" );
	}
	
}
