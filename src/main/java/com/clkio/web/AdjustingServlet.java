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

import com.clkio.schemas.adjusting.Adjusting;
import com.clkio.schemas.adjusting.InsertAdjustingRequest;
import com.clkio.schemas.adjusting.ListAdjustingRequest;
import com.clkio.schemas.adjusting.UpdateAdjustingRequest;
import com.clkio.schemas.common.Response;
import com.clkio.schemas.profile.Profile;
import com.clkio.web.constants.AppConstants;
import com.clkio.web.enums.ContentType;
import com.clkio.web.exception.BadRequestException;
import com.clkio.web.exception.NotAcceptableException;
import com.clkio.ws.AdjustingPort;
import com.clkio.ws.ResponseException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AdjustingServlet extends CommonHttpServlet {

	private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;
	
	@Autowired
	private AdjustingPort service;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ContentType accept = null;
		PrintWriter out = resp.getWriter();
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() );

			Response response = null;
			Matcher matcher = Pattern.compile( "^http.+\\/profile\\/(\\d+)\\/adjustings\\/?$" ).matcher( req.getRequestURL().toString() );
			if ( matcher.matches() ) {
				BigInteger profileId;
				try {
					profileId = new BigInteger( matcher.group( 1 ) );
				} catch ( NumberFormatException e) {
					throw new BadRequestException( "Invalid value provided for 'profileId'" );
				}
				response = this.service.list( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new ListAdjustingRequest( new Profile( profileId ) ) );
			} else throw new BadRequestException();
			
			out.print( response.getMessage( accept ) );
			resp.setStatus( HttpServletResponse.SC_OK );
		} catch ( DataBindingException e ) {
			resp.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		} catch ( ResponseException e ) {
			resp.setStatus( e.getStatusCode() );
			out.println( e.getMessage( accept ) );
		} catch ( NotAcceptableException e ) {
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

			Adjusting adjusting = null;
			ContentType contentType = ContentType.parse( req.getHeader( "Content-Type" ) );
			if ( contentType == null ) throw new NotAcceptableException( "Header 'Content-Type' is mandatory and has to be either 'application/json' or 'application/xml'." );
			else if ( contentType.equals( ContentType.APPLICATION_JSON ) )
				adjusting = new ObjectMapper().readValue( req.getReader(), Adjusting.class );
			else if ( contentType.equals( ContentType.APPLICATION_XML ) )
				adjusting = JAXB.unmarshal( req.getReader(), Adjusting.class );
			else throw new IllegalStateException( "No valid value for header 'Content-Type'. contentType=[" + accept.getValue() + "]" );
			
			Response response = null;
			Matcher matcher = Pattern.compile( "^http.+\\/profile\\/(\\d+)\\/adjustings\\/?$" ).matcher( req.getRequestURL().toString() );
			if ( matcher.matches() ) {
				BigInteger profileId;
				try {
					profileId = new BigInteger( matcher.group( 1 ) );
				} catch ( NumberFormatException e) {
					throw new BadRequestException( "Invalid value provided for 'profileId'" );
				}
				adjusting.setProfile( new Profile( profileId ) );
				response = this.service.insert( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new InsertAdjustingRequest( adjusting ) );
			} else throw new BadRequestException();
			
			out.print( response.getMessage( accept ) );
			resp.setStatus( HttpServletResponse.SC_CREATED );
		} catch ( JsonParseException | JsonMappingException e ) {
			resp.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		} catch ( DataBindingException e ) {
			resp.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		} catch ( ResponseException e ) {
			resp.setStatus( e.getStatusCode() );
			out.println( e.getMessage( accept ) );
		} catch ( NotAcceptableException e ) {
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

			Adjusting adjusting = null;
			ContentType contentType = ContentType.parse( req.getHeader( "Content-Type" ) );
			if ( contentType == null ) throw new NotAcceptableException( "Header 'Content-Type' is mandatory and has to be either 'application/json' or 'application/xml'." );
			else if ( contentType.equals( ContentType.APPLICATION_JSON ) )
				adjusting = new ObjectMapper().readValue( req.getReader(), Adjusting.class );
			else if ( contentType.equals( ContentType.APPLICATION_XML ) )
				adjusting = JAXB.unmarshal( req.getReader(), Adjusting.class );
			else throw new IllegalStateException( "No valid value for header 'Content-Type'. contentType=[" + accept.getValue() + "]" );
			
			Response response = null;
			Matcher matcher = Pattern.compile( "^http.+\\/profile\\/(\\d+)\\/adjustings\\/(\\d+)\\/?$" ).matcher( req.getRequestURL().toString() );
			if ( matcher.matches() ) {
				BigInteger profileId, adjustingId;
				try {
					profileId = new BigInteger( matcher.group( 1 ) );
					adjustingId = new BigInteger( matcher.group( 2 ) );
				} catch ( NumberFormatException e) {
					throw new BadRequestException( "Invalid value provided for 'profileId' and/or 'adjustingId'" );
				}
				adjusting.setId( adjustingId );
				adjusting.setProfile( new Profile( profileId ) );
				response = this.service.update( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new UpdateAdjustingRequest( adjusting ) );
			} else throw new BadRequestException();
			
			out.print( response.getMessage( accept ) );
			resp.setStatus( HttpServletResponse.SC_OK );
		} catch ( JsonParseException | JsonMappingException e ) {
			resp.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		} catch ( DataBindingException e ) {
			resp.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		} catch ( ResponseException e ) {
			resp.setStatus( e.getStatusCode() );
			out.println( e.getMessage( accept ) );
		} catch ( NotAcceptableException e ) {
			resp.sendError( e.getStatusCode(), e.getMessage() );
		} catch ( Exception e ) {
			resp.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
			resp.resetBuffer();
		} finally {
			if ( out != null ) out.close();
		}
	}
}