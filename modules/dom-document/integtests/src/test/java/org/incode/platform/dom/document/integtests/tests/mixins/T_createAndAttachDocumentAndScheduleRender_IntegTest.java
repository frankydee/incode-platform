package org.incode.platform.dom.document.integtests.tests.mixins;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.services.background.BackgroundCommandService;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.wrapper.DisabledException;

import org.incode.module.document.dom.impl.docs.DocumentTemplate;
import org.incode.domainapp.example.dom.dom.document.dom.demo.DemoObject;
import org.incode.domainapp.example.dom.dom.document.fixture.data.DemoObjectsFixture;
import org.incode.domainapp.example.dom.dom.document.fixture.DocumentDemoAppTearDownFixture;
import org.incode.domainapp.example.dom.dom.document.fixture.seed.DocumentTypeAndTemplatesApplicableForDemoObjectFixture;
import org.incode.platform.dom.document.integtests.DocumentModuleIntegTestAbstract;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assume.assumeThat;

public class T_createAndAttachDocumentAndScheduleRender_IntegTest extends DocumentModuleIntegTestAbstract {

    DemoObject demoObject;


    DocumentTypeAndTemplatesApplicableForDemoObjectFixture templateFs;

    @Before
    public void setUpData() throws Exception {
        fixtureScripts.runFixtureScript(new DocumentDemoAppTearDownFixture(), null);

        // types + templates
        templateFs = new DocumentTypeAndTemplatesApplicableForDemoObjectFixture();
        fixtureScripts.runFixtureScript(templateFs, null);

        // demo objects
        final DemoObjectsFixture demoObjectsFixture = new DemoObjectsFixture();
        fixtureScripts.runFixtureScript(demoObjectsFixture, null);
        demoObject = demoObjectsFixture.getDemoObjects().get(0);

        transactionService.flushTransaction();
    }


    @Inject
    BackgroundCommandService backgroundCommandService;

    public static class Disabled_IntegTest extends T_createAndAttachDocumentAndScheduleRender_IntegTest {

        @Test
        public void if_no_background_service() throws Exception {

            // given
            assumeThat(backgroundCommandService, is(nullValue()));

            // when
            final TranslatableString reason = _createAndAttachDocumentAndScheduleRender(demoObject).disable$$();

            // then
            assertThat(reason).isNotNull();

            // expect
            expectedExceptions.expect(DisabledException.class);

            // when
            final DocumentTemplate anyTemplate = templateFs.getFmkTemplate();
            wrap(_createAndAttachDocumentAndScheduleRender(demoObject)).$$(anyTemplate);
        }
    }


}