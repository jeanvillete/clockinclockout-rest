package com.clkio.web;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.clkio.schemas.resetpassword.ConfirmResetPasswordRequest;
import com.clkio.schemas.resetpassword.RequestResetPasswordRequest;
import com.clkio.schemas.resetpassword.ResetPasswordRequest;
import com.clkio.schemas.user.User;
import com.clkio.web.constants.AppConstants;
import com.clkio.web.enums.ContentType;
import com.clkio.web.exception.BadRequestException;
import com.clkio.web.exception.NotAcceptableException;
import com.clkio.web.exception.RestException;
import com.clkio.ws.ResetPasswordPort;
import com.clkio.ws.ResponseException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PasswordServlet extends CommonHttpServlet {

	private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;
	
	@Autowired
	private ResetPasswordPort service;
	
	@Override
	protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
		ContentType accept = null;
		PrintWriter out = resp.getWriter();
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() );

			User user = null;
			ContentType contentType = ContentType.parse( req.getHeader( "Content-Type" ) );
			if ( contentType == null ) throw new NotAcceptableException( "Header 'Content-Type' is mandatory and has to be either 'application/json' or 'application/xml'." );
			else if ( contentType.equals( ContentType.APPLICATION_JSON ) )
				user = new ObjectMapper().readValue( req.getReader(), User.class );
			else if ( contentType.equals( ContentType.APPLICATION_XML ) )
				user = JAXB.unmarshal( req.getReader(), User.class );
			else throw new IllegalStateException( "No valid value for header 'Content-Type'. contentType=[" + accept.getValue() + "]" );
			
			out.print( this.service.requestResetPassword( new RequestResetPasswordRequest( user ) ).getMessage( accept ) );
			resp.setStatus( HttpServletResponse.SC_CREATED );
		} catch ( JsonParseException | JsonMappingException e ) {
			resp.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		} catch ( DataBindingException e ) {
			resp.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		} catch ( ResponseException e ) {
			resp.setStatus( e.getStatusCode() );
			out.print( e.getMessage( accept ) );
		} catch ( RestException e ) {
			resp.sendError( e.getStatusCode(), e.getMessage() );
		} catch ( Exception e ) {
			resp.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
			resp.resetBuffer();
		} finally {
			if ( out != null ) out.close();
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ContentType accept = null;
		PrintWriter out = resp.getWriter();
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() );

			User user = null;
			ContentType contentType = ContentType.parse( req.getHeader( "Content-Type" ) );
			if ( contentType == null ) throw new NotAcceptableException( "Header 'Content-Type' is mandatory and has to be either 'application/json' or 'application/xml'." );
			else if ( contentType.equals( ContentType.APPLICATION_JSON ) )
				user = new ObjectMapper().readValue( req.getReader(), User.class );
			else if ( contentType.equals( ContentType.APPLICATION_XML ) )
				user = JAXB.unmarshal( req.getReader(), User.class );
			else throw new IllegalStateException( "No valid value for header 'Content-Type'. contentType=[" + accept.getValue() + "]" );
			
			Response response = null;
			Matcher matcher = null;
			if ( ( matcher = Pattern.compile( "^http.+\\/requestings\\/?(.*)$" ).matcher( req.getRequestURL().toString() ) ).matches() ) {
				String requestCode = matcher.group( 1 );
				if ( StringUtils.isEmpty( requestCode ) ) throw new BadRequestException( "No valid value provided for 'requestCode'" );
				response = this.service.confirmResetPassword( new ConfirmResetPasswordRequest( URLDecoder.decode( requestCode, "UTF-8" ), user ) );
			} else if ( ( matcher = Pattern.compile( "^http.+\\/confirmations\\/?(.*)$" ).matcher( req.getRequestURL().toString() ) ).matches() ) {
				String confirmationCode = matcher.group( 1 );
				if ( StringUtils.isEmpty( confirmationCode ) ) throw new BadRequestException( "No valid value provided for 'confirmationCode'" );
				response = this.service.resetPassword( new ResetPasswordRequest( URLDecoder.decode( confirmationCode, "UTF-8" ), user, user.getPassword() ) );
			} else throw new BadRequestException();
			
			out.print( response.getMessage( accept ) );
			resp.setStatus( HttpServletResponse.SC_OK );
		} catch ( JsonParseException | JsonMappingException e ) {
			resp.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		} catch ( DataBindingException e ) {
			resp.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		} catch ( ResponseException e ) {
			resp.setStatus( e.getStatusCode() );
			out.print( e.getMessage( accept ) );
		} catch ( RestException e ) {
			resp.sendError( e.getStatusCode(), e.getMessage() );
		} catch ( Exception e ) {
			resp.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
			resp.resetBuffer();
		} finally {
			if ( out != null ) out.close();
		}
	}
}