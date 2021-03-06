
package com.clkio.schemas.timecard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.clkio.schemas.manualentering.ManualEntering;
import com.clkio.schemas.profile.Profile;


/**
 * <p>Java class for insertManualEnteringRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="insertManualEnteringRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="profile" type="{http://schemas.clkio.com/profile}profile"/>
 *         &lt;element name="manualEntering" type="{http://schemas.clkio.com/manualentering}manualEntering"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "insertManualEnteringRequest", propOrder = {
    "profile",
    "manualEntering"
})
public class InsertManualEnteringRequest {

    @XmlElement(required = true)
    protected Profile profile;
    @XmlElement(required = true)
    protected ManualEntering manualEntering;

    public InsertManualEnteringRequest() {
    	super();
    }
    
    public InsertManualEnteringRequest( Profile profile, ManualEntering manualEntering ) {
		super();
		this.profile = profile;
		this.manualEntering = manualEntering;
	}

	/**
     * Gets the value of the profile property.
     * 
     * @return
     *     possible object is
     *     {@link Profile }
     *     
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Sets the value of the profile property.
     * 
     * @param value
     *     allowed object is
     *     {@link Profile }
     *     
     */
    public void setProfile(Profile value) {
        this.profile = value;
    }

    /**
     * Gets the value of the manualEntering property.
     * 
     * @return
     *     possible object is
     *     {@link ManualEntering }
     *     
     */
    public ManualEntering getManualEntering() {
        return manualEntering;
    }

    /**
     * Sets the value of the manualEntering property.
     * 
     * @param value
     *     allowed object is
     *     {@link ManualEntering }
     *     
     */
    public void setManualEntering(ManualEntering value) {
        this.manualEntering = value;
    }

}
