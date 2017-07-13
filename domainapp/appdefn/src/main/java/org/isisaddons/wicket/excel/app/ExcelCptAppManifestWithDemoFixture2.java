package org.isisaddons.wicket.excel.app;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.wicket.excel.fixture.scripts.ExcelWicketSetUpFixtureForSven;

public class ExcelCptAppManifestWithDemoFixture2 extends ExcelCptAppManifest2 {

    @Override
    public List<Class<? extends FixtureScript>> getFixtures() { return Lists.<Class<? extends FixtureScript>>newArrayList(ExcelWicketSetUpFixtureForSven.class); }

    @Override
    public Map<String, String> getConfigurationProperties() {
        Map<String,String> props = super.getConfigurationProperties();
        props.put("isis.persistor.datanucleus.install-fixtures","true");
        return props;
    }

}