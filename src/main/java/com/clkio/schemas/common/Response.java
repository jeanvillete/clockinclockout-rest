
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
import com.clkio.schemas.timecard.GetTimeCardResponse;
import com.clkio.schemas.timecard.GetTotalTimeMonthlyResponse;
import com.clkio.schemas.timecard.GetTotalTimeResponse;
import com.clkio.web.enums.ContentType;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;


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
    ListEmailResponse.class,
    GetTotalTimeResponse.class,
    GetTotalTimeMonthlyResponse.class,
    ListManualEnteringReasonResponse.class,
    LoginResponse.class,
    GetTimeCardResponse.class,
    ListAdjustingResponse.class,
    ListProfileResponse.class,
    ConfirmResetPasswordResponse.class
})
public class Response {

    @XmlElement(required = true)
    protected String message;

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
		if ( accept == null ) return "";
		else if ( accept.equals( ContentType.APPLICATION_JSON ) ) {
			StringWriter sw = new StringWriter();
			try {
				JsonGenerator jGenerator = new JsonFactory().createGenerator( sw );
				jGenerator.writeStartObject();
				jGenerator.writeStringField( "message", this.getMessage() );
				jGenerator.writeEndObject();
				jGenerator.close();
			} catch ( IOException e ) { }
			return sw.toString();
		} else if ( accept.equals( ContentType.APPLICATION_XML ) ) {
			throw new IllegalStateException("Method not implemented yet!");
		} else throw new IllegalArgumentException( "Invalid value for 'accept' argument." );
	}
    
}