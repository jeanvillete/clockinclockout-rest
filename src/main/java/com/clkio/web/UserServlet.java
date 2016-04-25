package com.clkio.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clkio.schemas.user.InsertUserRequest;
import com.clkio.schemas.user.User;
import com.clkio.web.enums.ContentType;
import com.clkio.web.exception.NotAcceptableException;
import com.clkio.ws.ResponseException;
import com.clkio.ws.UserPort;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings( "serial" )
public class UserServlet extends CommonHttpServlet {

	@Override
	protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
		ContentType accept = null;
		StringWriter sw = new StringWriter();
		PrintWriter out = resp.getWriter();
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			ContentType contentType = ContentType.parse( req.getHeader( "Content-Type" ) );
			User user = null;
			if ( contentType.equals( ContentType.APPLICATION_JSON ) ) {
				ObjectMapper om = new ObjectMapper();
				user = om.readValue( this.readBody( req ), User.class );
			} else if ( contentType.equals( ContentType.APPLICATION_XML ) ) {
				throw new RuntimeException("Method not implemented yet.");
			} else throw new IllegalStateException( "No valid value for header 'Content-Type'. contentType=[" + accept.getValue() + "]" );
			out.print( this.getService( req, UserPort.class ).insert( new InsertUserRequest( user ) ).getMessage( accept ) );
			resp.setHeader( "Content-Type", accept.getValue() );
			resp.setStatus( HttpServletResponse.SC_CREATED );
		} catch ( JsonParseException | JsonMappingException e ) {
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
			if ( sw != null ) sw.close();
			if ( out != null ) out.close();
		}
	}
	
}