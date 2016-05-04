package com.clkio.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.clkio.schemas.common.Response;
import com.clkio.schemas.email.ConfirmEmailRequest;
import com.clkio.schemas.email.Email;
import com.clkio.schemas.email.InsertEmailRequest;
import com.clkio.web.constants.AppConstants;
import com.clkio.web.enums.ContentType;
import com.clkio.web.exception.BadRequestException;
import com.clkio.web.exception.NotAcceptableException;
import com.clkio.web.exception.RestException;
import com.clkio.ws.EmailPort;
import com.clkio.ws.ResponseException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EmailServlet extends CommonHttpServlet {

	private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;
	
	@Autowired
	private EmailPort service;
	
	@Override
	protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
		ContentType accept = null;
		StringWriter sw = new StringWriter();
		PrintWriter out = resp.getWriter();
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() );
			
			Email email = null;
			ContentType contentType = ContentType.parse( req.getHeader( "Content-Type" ) );
			if ( contentType == null ) throw new NotAcceptableException( "Header 'Content-Type' is mandatory and has to be either 'application/json' or 'application/xml'." );
			else if ( contentType.equals( ContentType.APPLICATION_JSON ) )
				email = new ObjectMapper().readValue( req.getReader(), Email.class );
			else if ( contentType.equals( ContentType.APPLICATION_XML ) )
				email = JAXB.unmarshal( req.getReader(), Email.class );
			else throw new IllegalStateException( "No valid value for header 'Content-Type'. contentType=[" + accept.getValue() + "]" );

			Response response = null;
			Matcher matcher = Pattern.compile( "^http.+\\/confirmations\\/?(.*)$" ).matcher( req.getRequestURL().toString() );
			if ( matcher.matches() ) {
				String confirmationCode = matcher.group( 1 );
				if ( StringUtils.isEmpty( confirmationCode ) ) throw new BadRequestException( "No valid value provided for 'confirmationCode'" );
				email.setConfirmationCode( URLDecoder.decode( confirmationCode, "UTF-8" ) );
				response = this.service.confirm( new ConfirmEmailRequest( email ) );
			} else
				response = this.service.insert( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new InsertEmailRequest( email ) );
			
			out.print( response.getMessage( accept ) );
			resp.setStatus( HttpServletResponse.SC_CREATED );
		} catch ( JsonParseException | JsonMappingException e ) {
			resp.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		} catch ( DataBindingException e ) {
			resp.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		} catch ( ResponseException e ) {
			resp.setStatus( e.getStatusCode() );
			out.println( e.getMessage( accept ) );
		} catch ( RestException e ) {
			resp.sendError( e.getStatusCode(), e.getMessage() );
		} catch ( Exception e ) {
			resp.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
			resp.resetBuffer();
		} finally {
			if ( sw != null ) sw.close();
			if ( out != null ) out.close();
		}
	}
	
}