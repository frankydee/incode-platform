package org.incode.domainapp.example.app;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.isis.applib.ModuleAbstract;

@XmlRootElement(name = "module")
public class ExampleAppSubmodule extends ModuleAbstract {

    public static class PropertyDomainEvent<S,T>
            extends org.apache.isis.applib.services.eventbus.PropertyDomainEvent<S,T> {}
    public static class CollectionDomainEvent<S,T>
            extends org.apache.isis.applib.services.eventbus.CollectionDomainEvent<S,T> {}
    public static class ActionDomainEvent<S> extends
            org.apache.isis.applib.services.eventbus.ActionDomainEvent<S> {}

}
