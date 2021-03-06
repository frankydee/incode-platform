package org.incode.example.communications.integtests.app;

import java.util.List;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.domainapp.example.dom.dom.communications.fixture.DemoObjectWithNotes_and_DemoInvoice_and_docs_and_comms_recreate;

/**
 * Run the app but without setting up any fixtures.
 */
public class CommunicationsModuleAppManifestWithFixtures extends CommunicationsModuleAppManifest {

    @Override protected void overrideFixtures(final List<Class<? extends FixtureScript>> fixtureScripts) {
        fixtureScripts.add(DemoObjectWithNotes_and_DemoInvoice_and_docs_and_comms_recreate.class);
    }

}
