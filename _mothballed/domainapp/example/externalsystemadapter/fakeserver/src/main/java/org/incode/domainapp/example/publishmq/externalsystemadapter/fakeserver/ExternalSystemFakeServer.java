
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package org.incode.domainapp.example.publishmq.externalsystemadapter.fakeserver;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.Holder;

import org.isisaddons.module.publishmq.externalsystemadapter.demoobject.DemoObject;
import org.isisaddons.module.publishmq.externalsystemadapter.demoobject.PostResponse;
import org.isisaddons.module.publishmq.externalsystemadapter.demoobject.Update;

@WebService(
                      serviceName = "DemoObjectService",
                      portName = "DemoObjectOverSOAP",
                      targetNamespace = "http://isisaddons.org/module/publishmq/externalsystemadapter/DemoObject/",
                      wsdlLocation = "classpath:org/isisaddons/module/publishmq/externalsystemadapter/wsdl/DemoObject.wsdl",
                      endpointInterface = "org.isisaddons.module.publishmq.externalsystemadapter.demoobject.DemoObject")

public class ExternalSystemFakeServer implements DemoObject {

    private static final Logger LOG = Logger.getLogger(ExternalSystemFakeServer.class.getName());

    public class Control {
        private List<Update> updates = new ArrayList<>();
        public List<Update> getUpdates() {
            return updates;
        }
        public void clear() {
            updates.clear();
        }
    }

    private Control control = new Control();

    public Control control() { return control; }

    @Override public void get(
            @WebParam(mode = WebParam.Mode.INOUT, name = "internalId", targetNamespace = "") final Holder<String> internalId, @WebParam(mode = WebParam.Mode.OUT, name = "update", targetNamespace = "") final Holder<Update> update) {
        update.value = control().updates.get(Integer.parseInt(internalId.value) - 1);
    }

    public org.isisaddons.module.publishmq.externalsystemadapter.demoobject.PostResponse post(Update update) {
        LOG.info("Executing operation processed");
        System.out.println(update);
        try {
            control().updates.add(update);

            PostResponse response = new PostResponse();
            response.setInternalId(control().updates.size());
            response.setMessageId(update.getMessageId());

            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}
