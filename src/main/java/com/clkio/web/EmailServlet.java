package com.clkio.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DataBindingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.clkio.schemas.common.Response;
import com.clkio.schemas.email.ConfirmEmailRequest;
import com.clkio.schemas.email.DeleteEmailRequest;
import com.clkio.schemas.email.Email;
import com.clkio.schemas.email.InsertEmailRequest;
import com.clkio.schemas.email.ListEmailRequest;
import com.clkio.schemas.email.SetEmailAsPrimaryRequest;
import com.clkio.web.constants.AppConstants;
import com.clkio.web.enums.ContentType;
import com.clkio.web.exception.BadRequestException;
import com.clkio.web.exception.NotAcceptableException;
import com.clkio.web.exception.RestException;
import com.clkio.ws.EmailPort;
import com.clkio.ws.ResponseException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class EmailServlet extends CommonHttpServlet {

	private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;
	
	@Autowired
	private EmailPort service;
	
	@Override
	protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
		ContentType accept = null;
		PrintWriter out = null;
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() + "; charset=UTF-8" );
			out = resp.getWriter();
			
			ContentType contentType = ContentType.parse( req.getHeader( "Content-Type" ) );
			Email email = this.getMarshaller( contentType ).readValue( req.getReader(), Email.class );

			Response response = null;
			int statusCode;
			Matcher matcher = Pattern.compile( "^http.+\\/confirmations\\/?(.*)$" ).matcher( req.getRequestURL().toString() );
			if ( matcher.matches() ) {
				String confirmationCode = matcher.group( 1 );
				if ( StringUtils.isEmpty( confirmationCode ) ) throw new BadRequestException( "No valid value provided for 'confirmationCode'" );
				email.setConfirmationCode( URLDecoder.decode( confirmationCode, "UTF-8" ) );
				response = this.service.confirm( new ConfirmEmailRequest( email ) );
				statusCode = HttpServletResponse.SC_OK;
			} else {
				response = this.service.insert( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new InsertEmailRequest( email ) );
				statusCode = HttpServletResponse.SC_CREATED;
			}
			
			out.print( response.getMessage( accept ) );
			resp.setStatus( statusCode );
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
			Email email = this.getMarshaller( contentType ).readValue( req.getReader(), Email.class );

			if ( !email.isPrimary() )
				throw new BadRequestException( "This service is aimed to set an 'email' record as primary, so this property has to be 'true'." );
			
			Matcher matcher = Pattern.compile( "^http.+\\/emails\\/(\\d+)$" ).matcher( req.getRequestURL().toString() );
			if ( matcher.matches() ) {
				try {
					email.setId( new BigInteger( matcher.group( 1 ) ) );
				} catch ( NumberFormatException e ) {
					throw new BadRequestException();
				}
			} else throw new BadRequestException();
			
			out.print( this.service.setEmailAsPrimary( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new SetEmailAsPrimaryRequest( email ) ).getMessage( accept ) );
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
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ContentType accept = null;
		PrintWriter out = null;
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() + "; charset=UTF-8" );
			out = resp.getWriter();
			
			Email email = null;
			Matcher matcher = Pattern.compile( "^http.+\\/emails\\/(\\d+)$" ).matcher( req.getRequestURL().toString() );
			if ( matcher.matches() ) {
				try {
					email = new Email( new BigInteger( matcher.group( 1 ) ) );
				} catch ( NumberFormatException e ) {
					throw new BadRequestException();
				}
			} else throw new BadRequestException();
			
			out.print( this.service.delete( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new DeleteEmailRequest( email ) ).getMessage( accept ) );
			resp.setStatus( HttpServletResponse.SC_OK );
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
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ContentType accept = null;
		PrintWriter out = null;
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() + "; charset=UTF-8" );
			out = resp.getWriter();
			
			out.print( this.service.list( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new ListEmailRequest() ).getMessage( accept ) );
			resp.setStatus( HttpServletResponse.SC_OK );
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