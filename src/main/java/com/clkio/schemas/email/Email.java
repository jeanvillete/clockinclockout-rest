
package com.clkio.schemas.email;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for email complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="email">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="emailAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="confirmationCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="confirmed" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="primary" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "email", propOrder = {
    "id",
    "emailAddress",
    "confirmationCode",
    "confirmed",
    "primary"
})
public class Email {

    @XmlElement(required = true)
    protected BigInteger id;
    @XmlElement(required = true)
    protected String emailAddress;
    @XmlElement(required = true)
    protected String confirmationCode;
    protected boolean confirmed;
    protected boolean primary;

    public Email() {
    	super();
    }
    
    public Email( String emailAddress, String confirmationCode ) {
		super();
		this.emailAddress = emailAddress;
		this.confirmationCode = confirmationCode;
	}

	/**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setId(BigInteger value) {
        this.id = value;
    }

    /**
     * Gets the value of the emailAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the value of the emailAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAddress(String value) {
        this.emailAddress = value;
    }

    /**
     * Gets the value of the confirmationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfirmationCode() {
        return confirmationCode;
    }

    /**
     * Sets the value of the confirmationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfirmationCode(String value) {
        this.confirmationCode = value;
    }

    /**
     * Gets the value of the confirmed property.
     * 
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Sets the value of the confirmed property.
     * 
     */
    public void setConfirmed(boolean value) {
        this.confirmed = value;
    }

    /**
     * Gets the value of the primary property.
     * 
     */
    public boolean isPrimary() {
        return primary;
    }

    /**
     * Sets the value of the primary property.
     * 
     */
    public void setPrimary(boolean value) {
        this.primary = value;
    }

}
