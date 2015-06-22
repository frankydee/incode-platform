
package org.isisaddons.module.publishmq.soapsubscriber;

import javax.xml.ws.Endpoint;

/**
 * This class was generated by Apache CXF 3.1.1
 * 2015-06-19T16:26:16.637+01:00
 * Generated source version: 3.1.1
 * 
 */
 
public class DemoObject_DemoObjectOverSOAP_Server{

    protected DemoObject_DemoObjectOverSOAP_Server() throws java.lang.Exception {
        System.out.println("Starting Server");
        Object implementor = new DemoObjectOverSOAPImpl();
        String address = "http://localhost:8080/soap/SoapSubscriber/DemoObject";
        Endpoint.publish(address, implementor);
    }
    
    public static void main(String args[]) throws java.lang.Exception { 
        new DemoObject_DemoObjectOverSOAP_Server();
        System.out.println("Server ready..."); 
        
        Thread.sleep(5 * 60 * 1000); 
        System.out.println("Server exiting");
        System.exit(0);
    }
}
