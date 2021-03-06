package org.incode.platform.spi.publishmq.integtests.app;

import java.util.List;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.domainapp.example.dom.spi.publishmq.fixture.PublishMqDemoObject_recreate3;

public class PublishMqSpiAppManifestWithFixtures extends PublishMqSpiAppManifest {

    @Override
    protected void overrideFixtures(final List<Class<? extends FixtureScript>> fixtureScripts) {
        fixtureScripts.add(PublishMqDemoObject_recreate3.class);
    }
}
