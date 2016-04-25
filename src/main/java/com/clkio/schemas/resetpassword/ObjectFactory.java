
package com.clkio.schemas.resetpassword;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.clkio.schemas.resetpassword package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.clkio.schemas.resetpassword
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ConfirmResetPasswordRequest }
     * 
     */
    public ConfirmResetPasswordRequest createConfirmResetPasswordRequest() {
        return new ConfirmResetPasswordRequest();
    }

    /**
     * Create an instance of {@link ConfirmResetPasswordResponse }
     * 
     */
    public ConfirmResetPasswordResponse createConfirmResetPasswordResponse() {
        return new ConfirmResetPasswordResponse();
    }

    /**
     * Create an instance of {@link ResetPasswordRequest }
     * 
     */
    public ResetPasswordRequest createResetPasswordRequest() {
        return new ResetPasswordRequest();
    }

    /**
     * Create an instance of {@link RequestResetPasswordRequest }
     * 
     */
    public RequestResetPasswordRequest createRequestResetPasswordRequest() {
        return new RequestResetPasswordRequest();
    }

}
