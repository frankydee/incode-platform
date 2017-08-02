package org.incode.domainapp.appdefn;

import java.util.List;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.domainapp.example.dom.wkt.excel.fixture.ExcelWicketSetUpFixtureForSven;

public class DomainappAppManifestWithExcelWicketSetupFixture extends DomainappAppManifest {

    @Override protected void overrideFixtures(final List<Class<? extends FixtureScript>> fixtureScripts) {
        fixtureScripts.add(ExcelWicketSetUpFixtureForSven.class);
    }


}