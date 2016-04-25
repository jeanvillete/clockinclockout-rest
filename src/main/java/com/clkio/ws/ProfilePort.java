
package com.clkio.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import com.clkio.schemas.common.Response;
import com.clkio.schemas.profile.DeleteProfileRequest;
import com.clkio.schemas.profile.InsertProfileRequest;
import com.clkio.schemas.profile.ListProfileRequest;
import com.clkio.schemas.profile.ListProfileResponse;
import com.clkio.schemas.profile.UpdateProfileRequest;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "ProfilePort", targetNamespace = "http://ws.clkio.com")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    com.clkio.schemas.ObjectFactory.class,
    com.clkio.schemas.adjusting.ObjectFactory.class,
    com.clkio.schemas.clockinclockout.ObjectFactory.class,
    com.clkio.schemas.common.ObjectFactory.class,
    com.clkio.schemas.day.ObjectFactory.class,
    com.clkio.schemas.email.ObjectFactory.class,
    com.clkio.schemas.login.ObjectFactory.class,
    com.clkio.schemas.manualentering.ObjectFactory.class,
    com.clkio.schemas.profile.ObjectFactory.class,
    com.clkio.schemas.reason.ObjectFactory.class,
    com.clkio.schemas.resetpassword.ObjectFactory.class,
    com.clkio.schemas.timecard.ObjectFactory.class,
    com.clkio.schemas.user.ObjectFactory.class
})
public interface ProfilePort {


    /**
     * 
     * @param request
     * @param clkioLoginCode
     * @return
     *     returns com.clkio.schemas.profile.ListProfileResponse
     * @throws ResponseException
     */
    @WebMethod
    @WebResult(name = "listProfileResponse", targetNamespace = "http://schemas.clkio.com", partName = "result")
    public ListProfileResponse list(
        @WebParam(name = "clkioLoginCode", targetNamespace = "http://schemas.clkio.com", header = true, partName = "clkioLoginCode")
        String clkioLoginCode,
        @WebParam(name = "listProfileRequest", targetNamespace = "http://schemas.clkio.com", partName = "request")
        ListProfileRequest request)
        throws ResponseException
    ;

    /**
     * 
     * @param request
     * @param clkioLoginCode
     * @return
     *     returns com.clkio.schemas.common.Response
     * @throws ResponseException
     */
    @WebMethod
    @WebResult(name = "response", targetNamespace = "http://schemas.clkio.com", partName = "result")
    public Response insert(
        @WebParam(name = "clkioLoginCode", targetNamespace = "http://schemas.clkio.com", header = true, partName = "clkioLoginCode")
        String clkioLoginCode,
        @WebParam(name = "insertProfileRequest", targetNamespace = "http://schemas.clkio.com", partName = "request")
        InsertProfileRequest request)
        throws ResponseException
    ;

    /**
     * 
     * @param request
     * @param clkioLoginCode
     * @return
     *     returns com.clkio.schemas.common.Response
     * @throws ResponseException
     */
    @WebMethod
    @WebResult(name = "response", targetNamespace = "http://schemas.clkio.com", partName = "result")
    public Response update(
        @WebParam(name = "clkioLoginCode", targetNamespace = "http://schemas.clkio.com", header = true, partName = "clkioLoginCode")
        String clkioLoginCode,
        @WebParam(name = "updateProfileRequest", targetNamespace = "http://schemas.clkio.com", partName = "request")
        UpdateProfileRequest request)
        throws ResponseException
    ;

    /**
     * 
     * @param request
     * @param clkioLoginCode
     * @return
     *     returns com.clkio.schemas.common.Response
     * @throws ResponseException
     */
    @WebMethod
    @WebResult(name = "response", targetNamespace = "http://schemas.clkio.com", partName = "result")
    public Response delete(
        @WebParam(name = "clkioLoginCode", targetNamespace = "http://schemas.clkio.com", header = true, partName = "clkioLoginCode")
        String clkioLoginCode,
        @WebParam(name = "deleteProfileRequest", targetNamespace = "http://schemas.clkio.com", partName = "request")
        DeleteProfileRequest request)
        throws ResponseException
    ;

}