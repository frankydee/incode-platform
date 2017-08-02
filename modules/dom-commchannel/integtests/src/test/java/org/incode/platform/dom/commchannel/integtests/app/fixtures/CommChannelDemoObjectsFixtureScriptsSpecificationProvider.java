package org.incode.platform.dom.commchannel.integtests.app.fixtures;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.fixturespec.FixtureScriptsSpecification;
import org.apache.isis.applib.services.fixturespec.FixtureScriptsSpecificationProvider;

import org.incode.domainapp.example.dom.dom.commchannel.fixture.CommChannelDemoObjectsFixture;

/**
 * Specifies where to find fixtures, and other settings.
 */
@DomainService(nature = NatureOfService.DOMAIN)
public class CommChannelDemoObjectsFixtureScriptsSpecificationProvider
        implements FixtureScriptsSpecificationProvider {

    @Override
    public FixtureScriptsSpecification getSpecification() {
        return FixtureScriptsSpecification
                .builder(CommChannelDemoObjectsFixtureScriptsSpecificationProvider.class)
                .with(FixtureScripts.MultipleExecutionStrategy.EXECUTE)
                .withRunScriptDefault(CommChannelDemoObjectsFixture.class)
                .withRunScriptDropDown(FixtureScriptsSpecification.DropDownPolicy.CHOICES)
                .withRecreate(CommChannelDemoObjectsFixture.class)
                .build();

    }
}
