package com.clkio.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DataBindingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import com.clkio.schemas.login.DoLoginRequest;
import com.clkio.schemas.user.User;
import com.clkio.web.constants.AppConstants;
import com.clkio.web.enums.ContentType;
import com.clkio.web.exception.NotAcceptableException;
import com.clkio.web.exception.RestException;
import com.clkio.web.exception.UnauthorizedException;
import com.clkio.web.exception.UnauthorizedException.HeaderAuthenticate;
import com.clkio.ws.LoginPort;
import com.clkio.ws.ResponseException;

public class LoginServlet extends CommonHttpServlet {

	private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;
	
	@Autowired
	private LoginPort service;
	
	@Override
	protected void doGet( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
		ContentType accept = null;
		PrintWriter out = resp.getWriter();
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() );
			
			String authorization = req.getHeader( "Authorization" );
			if ( !StringUtils.hasText( authorization ) ) throw new UnauthorizedException();
			
			Matcher matcher = Pattern.compile( "^Basic\\s(.*)$" ).matcher( authorization.trim() );
			if ( !matcher.matches() ) throw new UnauthorizedException();
			
			String based64 = matcher.group( 1 );
			if ( !StringUtils.hasText( based64 ) ) throw new UnauthorizedException();
			based64 = new String( Base64Utils.decode( based64.getBytes() ) );
			
			String[] userAndPass = based64.split( ":" );
			if ( userAndPass == null || userAndPass.length != 2 ) throw new UnauthorizedException();
			User user = new User( userAndPass[0], userAndPass[1] );

			out.print( this.service.doLogin( new DoLoginRequest( user ) ).getMessage( accept ) );
			resp.setStatus( HttpServletResponse.SC_OK );
		} catch ( DataBindingException e ) {
			resp.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		} catch ( ResponseException e ) {
			resp.setHeader( HeaderAuthenticate.KEY.getMessage(), HeaderAuthenticate.VALUE.getMessage() );
			resp.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
			out.println( e.getMessage( accept ) );
		} catch ( UnauthorizedException e ) {
			resp.setHeader( HeaderAuthenticate.KEY.getMessage(), HeaderAuthenticate.VALUE.getMessage() );
			resp.setStatus( e.getStatusCode() );
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