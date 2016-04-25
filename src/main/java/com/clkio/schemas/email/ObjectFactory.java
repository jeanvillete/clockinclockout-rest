
package com.clkio.schemas.email;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.clkio.schemas.email package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.clkio.schemas.email
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ListEmailResponse }
     * 
     */
    public ListEmailResponse createListEmailResponse() {
        return new ListEmailResponse();
    }

    /**
     * Create an instance of {@link ConfirmEmailRequest }
     * 
     */
    public ConfirmEmailRequest createConfirmEmailRequest() {
        return new ConfirmEmailRequest();
    }

    /**
     * Create an instance of {@link InsertEmailRequest }
     * 
     */
    public InsertEmailRequest createInsertEmailRequest() {
        return new InsertEmailRequest();
    }

    /**
     * Create an instance of {@link ListEmailRequest }
     * 
     */
    public ListEmailRequest createListEmailRequest() {
        return new ListEmailRequest();
    }

    /**
     * Create an instance of {@link DeleteEmailRequest }
     * 
     */
    public DeleteEmailRequest createDeleteEmailRequest() {
        return new DeleteEmailRequest();
    }

    /**
     * Create an instance of {@link SetEmailAsPrimaryRequest }
     * 
     */
    public SetEmailAsPrimaryRequest createSetEmailAsPrimaryRequest() {
        return new SetEmailAsPrimaryRequest();
    }

    /**
     * Create an instance of {@link Email }
     * 
     */
    public Email createEmail() {
        return new Email();
    }

}
