
package com.clkio.schemas.timecard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.clkio.schemas.common.Response;


/**
 * <p>Java class for timeCardResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="timeCardResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.clkio.com/common}response">
 *       &lt;sequence>
 *         &lt;element name="timeCard" type="{http://schemas.clkio.com/timecard}timeCard"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "timeCardResponse", propOrder = {
    "timeCard"
})
public class TimeCardResponse
    extends Response
{

    @XmlElement(required = true)
    protected TimeCard timeCard;

    /**
     * Gets the value of the timeCard property.
     * 
     * @return
     *     possible object is
     *     {@link TimeCard }
     *     
     */
    public TimeCard getTimeCard() {
        return timeCard;
    }

    /**
     * Sets the value of the timeCard property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeCard }
     *     
     */
    public void setTimeCard(TimeCard value) {
        this.timeCard = value;
    }

}
