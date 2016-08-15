
package com.clkio.schemas.common;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.clkio.schemas.adjusting.ListAdjustingResponse;
import com.clkio.schemas.email.ListEmailResponse;
import com.clkio.schemas.login.LoginResponse;
import com.clkio.schemas.profile.ListProfileResponse;
import com.clkio.schemas.reason.ListManualEnteringReasonResponse;
import com.clkio.schemas.resetpassword.ConfirmResetPasswordResponse;
import com.clkio.schemas.timecard.TimeCardResponse;
import com.clkio.web.enums.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;


/**
 * <p>Java class for response complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="response">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "response", propOrder = {
    "message"
})
@XmlSeeAlso({
    TimeCardResponse.class,
    ListEmailResponse.class,
    ListManualEnteringReasonResponse.class,
    LoginResponse.class,
    ListAdjustingResponse.class,
    ListProfileResponse.class,
    ConfirmResetPasswordResponse.class,
    ResponseCreated.class
})
public class Response {

    @XmlElement(required = true)
    protected String message;

    public Response() {
    }
    
    public Response( String message ) {
		super();
		this.message = message;
	}

	/**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    public String getMessage( ContentType accept ) {
		ObjectMapper om = null;
    	if ( accept == null ) throw new IllegalArgumentException( "Argument 'accept' is mandatory." );
		else if ( accept.equals( ContentType.APPLICATION_JSON ) )
			om = new ObjectMapper();
		else if ( accept.equals( ContentType.APPLICATION_XML ) )
			om = new XmlMapper();
		else throw new IllegalArgumentException( "Invalid value for 'accept' argument." );
		
    	StringWriter sw = null;
    	try {
    		sw = new StringWriter();
			om.writeValue( sw, this );
			return sw.toString();
		} catch ( IOException e ) {
			throw new RuntimeException( "Impossible marshall content.", e );
		} finally {
			try {
				if ( sw != null ) sw.close();
			} catch ( IOException e ) {
				throw new RuntimeException( e );
			}
		}
	}
}
