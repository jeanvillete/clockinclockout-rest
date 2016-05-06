
package com.clkio.schemas.adjusting;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deleteAdjustingRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deleteAdjustingRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="adjusting" type="{http://schemas.clkio.com/adjusting}adjusting"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteAdjustingRequest", propOrder = {
    "adjusting"
})
public class DeleteAdjustingRequest {

    @XmlElement(required = true)
    protected Adjusting adjusting;

    public DeleteAdjustingRequest() {
    	super();
    }
    
    public DeleteAdjustingRequest( Adjusting adjusting ) {
		super();
		this.adjusting = adjusting;
	}

	/**
     * Gets the value of the adjusting property.
     * 
     * @return
     *     possible object is
     *     {@link Adjusting }
     *     
     */
    public Adjusting getAdjusting() {
        return adjusting;
    }

    /**
     * Sets the value of the adjusting property.
     * 
     * @param value
     *     allowed object is
     *     {@link Adjusting }
     *     
     */
    public void setAdjusting(Adjusting value) {
        this.adjusting = value;
    }

}
