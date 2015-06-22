package org.isisaddons.module.publishmq.soapsubscriber.demoobject;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.1.1
 * 2015-06-19T16:26:16.630+01:00
 * Generated source version: 3.1.1
 * 
 */
@WebService(targetNamespace = "http://isisaddons.org/module/publishmq/soapsubscriber/DemoObject/", name = "DemoObject")
@XmlSeeAlso({ObjectFactory.class})
public interface DemoObject {

    @WebMethod(operationName = "Processed", action = "http://isisaddons.org/module/publishmq/soapsubscriber/DemoObject/Processed")
    @RequestWrapper(localName = "Processed", targetNamespace = "http://isisaddons.org/module/publishmq/soapsubscriber/DemoObject/", className = "org.isisaddons.module.publishmq.soapsubscriber.demoobject.Processed")
    @ResponseWrapper(localName = "ProcessedResponse", targetNamespace = "http://isisaddons.org/module/publishmq/soapsubscriber/DemoObject/", className = "org.isisaddons.module.publishmq.soapsubscriber.demoobject.ProcessedResponse")
    @WebResult(name = "out", targetNamespace = "")
    public java.lang.String processed(
        @WebParam(name = "name", targetNamespace = "")
        java.lang.String name,
        @WebParam(name = "description", targetNamespace = "")
        java.lang.String description
    );
}
