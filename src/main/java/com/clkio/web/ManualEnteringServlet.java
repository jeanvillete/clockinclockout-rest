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

import com.clkio.schemas.manualentering.ManualEntering;
import com.clkio.schemas.profile.Profile;
import com.clkio.schemas.timecard.DeleteManualEnteringRequest;
import com.clkio.schemas.timecard.InsertManualEnteringRequest;
import com.clkio.schemas.timecard.UpdateManualEnteringRequest;
import com.clkio.web.constants.AppConstants;
import com.clkio.web.enums.ContentType;
import com.clkio.web.exception.BadRequestException;
import com.clkio.web.exception.NotAcceptableException;
import com.clkio.web.exception.RestException;
import com.clkio.ws.ResponseException;
import com.clkio.ws.TimeCardPort;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ManualEnteringServlet extends CommonHttpServlet {

	private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;
	
	@Autowired
	private TimeCardPort service;
	
	@Override
	protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
		ContentType accept = null;
		PrintWriter out = resp.getWriter();
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() );
			
			ManualEntering manualEntering = null;
			ContentType contentType = ContentType.parse( req.getHeader( "Content-Type" ) );
			if ( contentType == null ) throw new NotAcceptableException( "Header 'Content-Type' is mandatory and has to be either 'application/json' or 'application/xml'." );
			else if ( contentType.equals( ContentType.APPLICATION_JSON ) )
				manualEntering = new ObjectMapper().readValue( req.getReader(), ManualEntering.class );
			else if ( contentType.equals( ContentType.APPLICATION_XML ) )
				manualEntering = JAXB.unmarshal( req.getReader(), ManualEntering.class );
			else throw new IllegalStateException( "No valid value for header 'Content-Type'. contentType=[" + accept.getValue() + "]" );
			
			Matcher matcher = null;
			if ( ( matcher = Pattern.compile( "^.+\\/manualentering\\/profiles\\/(\\d+)\\/?$" ).matcher( req.getRequestURL().toString() ) ).matches() ) {
				Profile profile = null;
				try {
					profile = new Profile( new BigInteger( matcher.group( 1 ) ) );
				} catch ( NumberFormatException e) {
					throw new BadRequestException( "Invalid value provided for 'profileId'" );
				}
				out.print( this.service.insertManualEntering( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new InsertManualEnteringRequest( profile, manualEntering ) ).getMessage( accept ) );
				resp.setStatus( HttpServletResponse.SC_CREATED );
			} else throw new BadRequestException();
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
			
			ManualEntering manualEntering = null;
			ContentType contentType = ContentType.parse( req.getHeader( "Content-Type" ) );
			if ( contentType == null ) throw new NotAcceptableException( "Header 'Content-Type' is mandatory and has to be either 'application/json' or 'application/xml'." );
			else if ( contentType.equals( ContentType.APPLICATION_JSON ) )
				manualEntering = new ObjectMapper().readValue( req.getReader(), ManualEntering.class );
			else if ( contentType.equals( ContentType.APPLICATION_XML ) )
				manualEntering = JAXB.unmarshal( req.getReader(), ManualEntering.class );
			else throw new IllegalStateException( "No valid value for header 'Content-Type'. contentType=[" + accept.getValue() + "]" );
			
			Matcher matcher = null;
			if ( ( matcher = Pattern.compile( "^.+\\/manualentering\\/(\\d+)\\/profiles\\/(\\d+)\\/?$" ).matcher( req.getRequestURL().toString() ) ).matches() ) {
				Profile profile = null;
				try {
					manualEntering.setId( new BigInteger( matcher.group( 1 ) ) );
					profile = new Profile( new BigInteger( matcher.group( 2 ) ) );
				} catch ( NumberFormatException e) {
					throw new BadRequestException( "Invalid value provided for 'manualenteringId' or 'profileId'" );
				}
				out.print( this.service.updateManualEntering( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new UpdateManualEnteringRequest( profile, manualEntering ) ).getMessage( accept ) );
				resp.setStatus( HttpServletResponse.SC_OK );
			} else throw new BadRequestException();
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
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ContentType accept = null;
		PrintWriter out = resp.getWriter();
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() );
			
			Matcher matcher = null;
			if ( ( matcher = Pattern.compile( "^.+\\/manualentering\\/(\\d+)\\/profiles\\/(\\d+)\\/?$" ).matcher( req.getRequestURL().toString() ) ).matches() ) {
				ManualEntering manualEntering = null;
				Profile profile = null;
				try {
					manualEntering = new ManualEntering( new BigInteger( matcher.group( 1 ) ) );
					profile = new Profile( new BigInteger( matcher.group( 2 ) ) );
				} catch ( NumberFormatException e) {
					throw new BadRequestException( "Invalid value provided for 'manualenteringId' or 'profileId'" );
				}
				out.print( this.service.deleteManualEntering( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new DeleteManualEnteringRequest( profile, manualEntering ) ).getMessage( accept ) );
				resp.setStatus( HttpServletResponse.SC_OK );
			} else throw new BadRequestException();
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