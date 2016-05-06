package com.clkio.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

import org.springframework.beans.factory.annotation.Autowired;

import com.clkio.schemas.profile.InsertProfileRequest;
import com.clkio.schemas.profile.ListProfileRequest;
import com.clkio.schemas.profile.Profile;
import com.clkio.schemas.profile.UpdateProfileRequest;
import com.clkio.web.constants.AppConstants;
import com.clkio.web.enums.ContentType;
import com.clkio.web.exception.BadRequestException;
import com.clkio.web.exception.NotAcceptableException;
import com.clkio.web.exception.RestException;
import com.clkio.ws.ProfilePort;
import com.clkio.ws.ResponseException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProfileServlet extends CommonHttpServlet {

	private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;
	
	@Autowired
	private ProfilePort service;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ContentType accept = null;
		PrintWriter out = resp.getWriter();
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() );
			out.print( this.service.list( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new ListProfileRequest() ).getMessage( accept ) );
			resp.setStatus( HttpServletResponse.SC_OK );
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
			if ( out != null ) out.close();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ContentType accept = null;
		PrintWriter out = resp.getWriter();
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() );

			Profile profile = null;
			ContentType contentType = ContentType.parse( req.getHeader( "Content-Type" ) );
			if ( contentType == null ) throw new NotAcceptableException( "Header 'Content-Type' is mandatory and has to be either 'application/json' or 'application/xml'." );
			else if ( contentType.equals( ContentType.APPLICATION_JSON ) )
				profile = new ObjectMapper().readValue( req.getReader(), Profile.class );
			else if ( contentType.equals( ContentType.APPLICATION_XML ) )
				profile = JAXB.unmarshal( req.getReader(), Profile.class );
			else throw new IllegalStateException( "No valid value for header 'Content-Type'. contentType=[" + accept.getValue() + "]" );
			
			out.print( this.service.insert( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new InsertProfileRequest( profile ) ).getMessage( accept ) );
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

			Profile profile = null;
			ContentType contentType = ContentType.parse( req.getHeader( "Content-Type" ) );
			if ( contentType == null ) throw new NotAcceptableException( "Header 'Content-Type' is mandatory and has to be either 'application/json' or 'application/xml'." );
			else if ( contentType.equals( ContentType.APPLICATION_JSON ) )
				profile = new ObjectMapper().readValue( req.getReader(), Profile.class );
			else if ( contentType.equals( ContentType.APPLICATION_XML ) )
				profile = JAXB.unmarshal( req.getReader(), Profile.class );
			else throw new IllegalStateException( "No valid value for header 'Content-Type'. contentType=[" + accept.getValue() + "]" );
			
			Matcher matcher = Pattern.compile( "^http.+\\/profiles\\/(\\d+)\\/?$" ).matcher( req.getRequestURL().toString() );
			if ( matcher.matches() ) {
				try {
					profile.setId( new BigInteger( matcher.group( 1 ) ) );
				} catch ( NumberFormatException e) {
					throw new BadRequestException( "Invalid value provided for 'profileId'" );
				}
			} else throw new BadRequestException();
			
			out.print( this.service.update( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new UpdateProfileRequest( profile ) ).getMessage( accept ) );
			resp.setStatus( HttpServletResponse.SC_OK );
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
			if ( out != null ) out.close();
		}
	}
}