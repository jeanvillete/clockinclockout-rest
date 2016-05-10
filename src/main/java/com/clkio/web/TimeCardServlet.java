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
import org.springframework.util.StringUtils;

import com.clkio.schemas.clockinclockout.Clockinclockout;
import com.clkio.schemas.common.Response;
import com.clkio.schemas.manualentering.ManualEntering;
import com.clkio.schemas.profile.Profile;
import com.clkio.schemas.timecard.GetTimeCardRequest;
import com.clkio.schemas.timecard.GetTotalTimeMonthlyRequest;
import com.clkio.schemas.timecard.GetTotalTimeRequest;
import com.clkio.schemas.timecard.InsertClockinClockoutRequest;
import com.clkio.schemas.timecard.InsertManualEnteringRequest;
import com.clkio.schemas.timecard.PunchClockRequest;
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

public class TimeCardServlet extends CommonHttpServlet {

	private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;
	
	@Autowired
	private TimeCardPort service;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ContentType accept = null;
		PrintWriter out = resp.getWriter();
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() );
			
			Response response = null;
			Matcher matcher = null;
			if ( ( matcher = Pattern.compile( "^.+\\/timecard\\/(\\d{4}\\-\\d{2})?\\/?profiles\\/(\\d+)\\/?$" ).matcher( req.getRequestURL().toString() ) ).matches() ) {
				String yearMonth = matcher.group( 1 );
				BigInteger profileId;
				try {
					profileId = new BigInteger( matcher.group( 2 ) );
				} catch ( NumberFormatException e) {
					throw new BadRequestException( "Invalid value provided for 'profileId'" );
				}
				response = this.service.getTimeCard( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new GetTimeCardRequest( new Profile( profileId ), yearMonth ) );
			} else if ( ( matcher = Pattern.compile( "^.+\\/timecard\\/totaltime\\/(\\d{4}\\-\\d{2})?\\/?profiles\\/(\\d+)\\/?$" ).matcher( req.getRequestURL().toString() ) ).matches() ) {
				String yearMonth = matcher.group( 1 );
				BigInteger profileId;
				try {
					profileId = new BigInteger( matcher.group( 2 ) );
				} catch ( NumberFormatException e) {
					throw new BadRequestException( "Invalid value provided for 'profileId'" );
				}
				if ( StringUtils.hasText( yearMonth ) )
					response = this.service.getTotalTimeMonthly( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new GetTotalTimeMonthlyRequest( new Profile( profileId ), yearMonth ) );
				else 
					response = this.service.getTotalTime( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new GetTotalTimeRequest( new Profile( profileId ) ) );
			} else throw new BadRequestException();
			
			out.print( response.getMessage( accept ) );
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
	protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
		ContentType accept = null;
		PrintWriter out = resp.getWriter();
		try {
			accept = ContentType.parse( req.getHeader( "Accept" ) );
			if ( accept == null ) throw new NotAcceptableException( "Header 'Accept' is mandatory and has to be either 'application/json' or 'application/xml'." );
			resp.setContentType( accept.getValue() );
			ContentType contentType = ContentType.parse( req.getHeader( "Content-Type" ) );
			if ( contentType == null )
				throw new NotAcceptableException( "Header 'Content-Type' is mandatory and has to be either 'application/json' or 'application/xml'." );
			else if ( !contentType.equals( ContentType.APPLICATION_JSON ) && !contentType.equals( ContentType.APPLICATION_XML ) )
				throw new IllegalStateException( "No valid value for header 'Content-Type'. contentType=[" + accept.getValue() + "]" );
			
			Response response = null;
			Matcher matcher = null;
			if ( ( matcher = Pattern.compile( "^.+\\/timecard\\/profiles\\/(\\d+)\\/?$" ).matcher( req.getRequestURL().toString() ) ).matches() ) {
				PunchClockRequest punchClockRequest = contentType.equals( ContentType.APPLICATION_JSON ) ?
						new ObjectMapper().readValue( req.getReader(), PunchClockRequest.class ) :
							JAXB.unmarshal( req.getReader(), PunchClockRequest.class );
				try {
					punchClockRequest.setProfile( new Profile( new BigInteger( matcher.group( 1 ) ) ) );
				} catch ( NumberFormatException e) {
					throw new BadRequestException( "Invalid value provided for 'profileId'" );
				}
				response = this.service.punchClock( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), punchClockRequest );
			} else if ( ( matcher = Pattern.compile( "^.+\\/timecard\\/clockinclockout\\/profiles\\/(\\d+)\\/?$" ).matcher( req.getRequestURL().toString() ) ).matches() ) {
				Clockinclockout clockinClockout = null;
				if ( contentType.equals( ContentType.APPLICATION_JSON ) )
					clockinClockout = new ObjectMapper().readValue( req.getReader(), Clockinclockout.class );
				else if ( contentType.equals( ContentType.APPLICATION_XML ) )
					clockinClockout = JAXB.unmarshal( req.getReader(), Clockinclockout.class );
				try {
					response = this.service.insertClockinClockout( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new InsertClockinClockoutRequest( new Profile( new BigInteger( matcher.group( 1 ) ) ), clockinClockout ) );
				} catch ( NumberFormatException e) {
					throw new BadRequestException( "Invalid value provided for 'profileId'" );
				}
			} else if ( ( matcher = Pattern.compile( "^.+\\/timecard\\/manualentering\\/profiles\\/(\\d+)\\/?$" ).matcher( req.getRequestURL().toString() ) ).matches() ) {
				ManualEntering manualEntering = null;
				if ( contentType.equals( ContentType.APPLICATION_JSON ) )
					manualEntering = new ObjectMapper().readValue( req.getReader(), ManualEntering.class );
				else if ( contentType.equals( ContentType.APPLICATION_XML ) )
					manualEntering = JAXB.unmarshal( req.getReader(), ManualEntering.class );
				try {
					response = this.service.insertManualEntering( req.getHeader( AppConstants.CLKIO_LOGIN_CODE ), new InsertManualEnteringRequest( new Profile( new BigInteger( matcher.group( 1 ) ) ), manualEntering ) );
				} catch ( NumberFormatException e) {
					throw new BadRequestException( "Invalid value provided for 'profileId'" );
				}
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