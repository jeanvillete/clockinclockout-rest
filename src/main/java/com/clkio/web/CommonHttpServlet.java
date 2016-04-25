package com.clkio.web;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.clkio.ws.ResponseException;

public abstract class CommonHttpServlet extends HttpServlet {

	< E > E getService( HttpServletRequest req, Class< E > target ) throws ResponseException {
		ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext( req.getServletContext() );
		return applicationContext.getBean( target );
	}
	
	String readBody( HttpServletRequest req ) throws IOException {
		StringBuilder body = new StringBuilder();
		BufferedReader reader = req.getReader();
		String line;
		while ( ( line = reader.readLine() ) != null )
			body.append( line );
		return body.toString();
	}
}
