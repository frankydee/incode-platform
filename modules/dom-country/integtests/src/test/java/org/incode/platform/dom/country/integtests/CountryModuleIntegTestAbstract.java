package org.incode.platform.dom.country.integtests;

import org.junit.Before;
import org.junit.BeforeClass;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.fixturespec.FixtureScriptsSpecification;
import org.apache.isis.applib.services.fixturespec.FixtureScriptsSpecificationProvider;
import org.apache.isis.core.integtestsupport.IntegrationTestAbstract2;

import org.incode.module.country.CountryModuleDomManifest;
import org.incode.module.country.fixture.teardown.CountryModuleTearDown;

public abstract class CountryModuleIntegTestAbstract extends IntegrationTestAbstract2 {

    @BeforeClass
    public static void initSystem() {
        bootstrapUsing(CountryModuleDomManifest.BUILDER
                        .withAdditionalServices(ModuleFixtureScriptsSpecificationProvider.class)
                        .build());
    }

    @Before
    public void cleanUpFromPreviousTest() {
        runFixtureScript(new CountryModuleTearDown());
    }


    @DomainService(nature = NatureOfService.DOMAIN)
    public static class ModuleFixtureScriptsSpecificationProvider implements FixtureScriptsSpecificationProvider {
        @Override
        public FixtureScriptsSpecification getSpecification() {
            return FixtureScriptsSpecification.builder("org.incode.module.country").with(
                    FixtureScripts.MultipleExecutionStrategy.EXECUTE_ONCE_BY_VALUE).build();
        }
    }
}