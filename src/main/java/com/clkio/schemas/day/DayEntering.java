
package com.clkio.schemas.day;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.clkio.schemas.clockinclockout.Clockinclockout;
import com.clkio.schemas.manualentering.ManualEntering;


/**
 * <p>Java class for dayEntering complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dayEntering">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dayEntering")
@XmlSeeAlso({
    Clockinclockout.class,
    ManualEntering.class
})
public class DayEntering {


}
