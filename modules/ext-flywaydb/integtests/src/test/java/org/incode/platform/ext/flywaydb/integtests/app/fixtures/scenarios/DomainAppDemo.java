package org.incode.platform.ext.flywaydb.integtests.app.fixtures.scenarios;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.platform.ext.flywaydb.integtests.app.fixtures.teardown.DomainAppTearDown;
import org.incode.domainapp.example.dom.ext.flywaydb.fixture.scenario.RecreateFlywayDemoObjects;

public class DomainAppDemo extends FixtureScript {

    public DomainAppDemo() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }

    //region > number (optional input)
    private Integer number;

    /**
     * The number of objects to create, up to 10; optional, defaults to 3.
     */
    public Integer getNumber() {
        return number;
    }

    public DomainAppDemo setNumber(final Integer number) {
        this.number = number;
        return this;
    }
    //endregion

    @Override
    protected void execute(final ExecutionContext ec) {

        // defaults
        final int number = defaultParam("number", ec, 3);


        // execute
        ec.executeChild(this, new DomainAppTearDown());
        ec.executeChild(this, new RecreateFlywayDemoObjects().setNumber(number));

    }
}