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

import com.clkio.schemas.profile.Profile;
import com.clkio.schemas.reason.ListManualEnteringReasonRequest;
import com.clkio.web.constants.AppConstants;
import com.clkio.web.enums.ContentType;
import com.clkio.web.exception.BadRequestException;
import com.clkio.web.exception.NotAcceptableException;
import com.clkio.web.exception.RestException;
import com.clkio.ws.ManualEnteringReasonPort;
import com.clkio.ws.ResponseException;

public class ReasonServlet extends CommonHttpServlet {

	private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;
	
	@Autowired
	private ManualEnteringReasonPort service;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ContentType accept = null;
		PrintWriter out = resp.getWriter();
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() );
			Matcher matcher = Pattern.compile( "^http.+\\/profiles\\/(\\d+)\\/reasons\\/?$" ).matcher( req.getRequestURL().toString() );
			if ( matcher.matches() ) {
				BigInteger profileId;
				try {
					profileId = new BigInteger( matcher.group( 1 ) );
				} catch ( NumberFormatException e) {
					throw new BadRequestException( "Invalid value provided for 'profileId'" );
				}
				out.print( this.service.list( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new ListManualEnteringReasonRequest( new Profile( profileId ) ) ).getMessage( accept ) );
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