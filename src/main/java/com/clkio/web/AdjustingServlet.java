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

import org.springframework.beans.factory.annotation.Autowired;

import com.clkio.schemas.adjusting.Adjusting;
import com.clkio.schemas.adjusting.DeleteAdjustingRequest;
import com.clkio.schemas.adjusting.InsertAdjustingRequest;
import com.clkio.schemas.adjusting.ListAdjustingRequest;
import com.clkio.schemas.adjusting.UpdateAdjustingRequest;
import com.clkio.schemas.profile.Profile;
import com.clkio.web.constants.AppConstants;
import com.clkio.web.enums.ContentType;
import com.clkio.web.exception.BadRequestException;
import com.clkio.web.exception.NotAcceptableException;
import com.clkio.web.exception.RestException;
import com.clkio.ws.AdjustingPort;
import com.clkio.ws.ResponseException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class AdjustingServlet extends CommonHttpServlet {

	private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;
	
	@Autowired
	private AdjustingPort service;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ContentType accept = null;
		PrintWriter out = null;
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() + "; charset=UTF-8" );
			out = resp.getWriter();
			
			Matcher matcher = Pattern.compile( "^http.+\\/adjustings\\/profiles\\/(\\d+)\\/?$" ).matcher( req.getRequestURL().toString() );
			if ( matcher.matches() ) {
				BigInteger profileId;
				try {
					profileId = new BigInteger( matcher.group( 1 ) );
				} catch ( NumberFormatException e) {
					throw new BadRequestException( "Invalid value provided for 'profileId'" );
				}
				out.print( this.service.list( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new ListAdjustingRequest( new Profile( profileId ) ) ).getMessage( accept ) );
				resp.setStatus( HttpServletResponse.SC_OK );
			} else throw new BadRequestException();
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
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ContentType accept = null;
		PrintWriter out = null;
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() + "; charset=UTF-8" );
			out = resp.getWriter();

			ContentType contentType = ContentType.parse( req.getHeader( "Content-Type" ) );
			Adjusting adjusting = this.getMarshaller( contentType ).readValue( req.getReader(), Adjusting.class );
			
			Matcher matcher = Pattern.compile( "^http.+\\/adjustings\\/profiles\\/(\\d+)\\/?$" ).matcher( req.getRequestURL().toString() );
			if ( matcher.matches() ) {
				try {
					adjusting.setProfile( new Profile( new BigInteger( matcher.group( 1 ) ) ) );
				} catch ( NumberFormatException e) {
					throw new BadRequestException( "Invalid value provided for 'profileId'" );
				}
				out.print( this.service.insert( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new InsertAdjustingRequest( adjusting ) ).getMessage( accept ) );
				resp.setStatus( HttpServletResponse.SC_CREATED );
			} else throw new BadRequestException();
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
		PrintWriter out = null;
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() + "; charset=UTF-8" );
			out = resp.getWriter();

			ContentType contentType = ContentType.parse( req.getHeader( "Content-Type" ) );
			Adjusting adjusting = this.getMarshaller( contentType ).readValue( req.getReader(), Adjusting.class );
			
			Matcher matcher = Pattern.compile( "^http.+\\/adjustings\\/(\\d+)\\/profiles\\/(\\d+)\\/?$" ).matcher( req.getRequestURL().toString() );
			if ( matcher.matches() ) {
				try {
					adjusting.setProfile( new Profile( new BigInteger( matcher.group( 2 ) ) ) );
					adjusting.setId( new BigInteger( matcher.group( 1 ) ) );
				} catch ( NumberFormatException e) {
					throw new BadRequestException( "Invalid value provided for 'profileId' and/or 'adjustingId'" );
				}
				out.print( this.service.update( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new UpdateAdjustingRequest( adjusting ) ).getMessage( accept ) );
				resp.setStatus( HttpServletResponse.SC_OK );
			} else throw new BadRequestException();
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
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ContentType accept = null;
		PrintWriter out = null;
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() + "; charset=UTF-8" );
			out = resp.getWriter();
			
			Adjusting adjusting = null;
			Matcher matcher = Pattern.compile( "^http.+\\/adjustings\\/(\\d+)\\/profiles\\/(\\d+)\\/?$" ).matcher( req.getRequestURL().toString() );
			if ( matcher.matches() ) {
				try {
					adjusting = new Adjusting( new BigInteger( matcher.group( 1 ) ) );
					adjusting.setProfile( new Profile( new BigInteger( matcher.group( 2 ) ) ) );
				} catch ( NumberFormatException e) {
					throw new BadRequestException( "Invalid value provided for 'profileId' and/or 'adjustingId'" );
				}
				out.print( this.service.delete( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new DeleteAdjustingRequest( adjusting ) ).getMessage( accept ) );
				resp.setStatus( HttpServletResponse.SC_OK );
			} else throw new BadRequestException();
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