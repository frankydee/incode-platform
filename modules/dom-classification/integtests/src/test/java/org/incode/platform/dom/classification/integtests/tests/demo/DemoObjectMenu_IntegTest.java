package org.incode.platform.dom.classification.integtests.tests.demo;

import org.assertj.core.api.Assertions;
import org.incode.domainapp.example.dom.demo.dom.demowithatpath.DemoObjectWithAtPath;
import org.incode.domainapp.example.dom.demo.dom.demowithatpath.DemoObjectWithAtPathMenu;
import org.incode.domainapp.example.dom.dom.classification.fixture.ClassifiedDemoObjectsFixture;
import org.incode.platform.dom.classification.integtests.ClassificationModuleIntegTestAbstract;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

public class DemoObjectMenu_IntegTest extends ClassificationModuleIntegTestAbstract {

    ClassifiedDemoObjectsFixture fs;

    @Before
    public void setUpData() throws Exception {
        fs = new ClassifiedDemoObjectsFixture();
        fixtureScripts.runFixtureScript(fs, null);
    }

    @Test
    public void listAll() throws Exception {

        // given
        int numDemoObjects = fs.getDemoObjects().size();

        // when
        final List<DemoObjectWithAtPath> all = wrap(demoObjectMenu).listAll();

        // then
        Assertions.assertThat(all.size()).isEqualTo(numDemoObjects);
    }
    
    @Test
    public void create() throws Exception {

        // given
        final List<DemoObjectWithAtPath> before = wrap(demoObjectMenu).listAll();
        int numBefore = before.size();

        // when
        wrap(demoObjectMenu).create("Faz", "/");

        // then
        final List<DemoObjectWithAtPath> after = wrap(demoObjectMenu).listAll();
        Assertions.assertThat(after.size()).isEqualTo(numBefore+1);
    }

    @Inject
    DemoObjectWithAtPathMenu demoObjectMenu;


}