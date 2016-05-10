package com.clkio.web.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class CustomDispatcherFilter implements Filter {

	private Map< String, String > mappings = new HashMap< String, String >();
	
	@Override
	public void init( FilterConfig filterConfig ) throws ServletException {
		String customDispatcherForward = filterConfig.getInitParameter( "custom-dispatcher-forward" );
		
		if ( customDispatcherForward == null || ( customDispatcherForward = customDispatcherForward.trim() ).isEmpty() )
			throw new IllegalStateException( "No proper value provided for 'custom-dispatcher-forward'." );
		
		for ( String line : customDispatcherForward.split( "\\n" ) ) {
			String[] elements = line.split( "=>" );
			if ( elements.length != 2 ) throw new IllegalStateException( "Invalid pair value; line=[" + line + "]" );
			mappings.put( elements[0].trim(), elements[1].trim() );
		}
	}

	@Override
	public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException {
		String url = ( ( HttpServletRequest ) request ).getRequestURL().toString();
		for ( String pattern : mappings.keySet() )
			if ( Pattern.compile( pattern ).matcher( url ).matches() ) {
				request.getRequestDispatcher( url.replaceAll( pattern, mappings.get( pattern ) ) ).forward( request, response );
				return;
			}
		chain.doFilter( request, response );
	}

	@Override
	public void destroy() {
		mappings.clear();
	}

}
