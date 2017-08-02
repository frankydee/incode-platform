package org.incode.platform.demo.webapp.app.services.homepage.poly;

import org.incode.domainapp.example.dom.lib.poly.dom.democasemgmt.Case;
import org.incode.domainapp.example.dom.lib.poly.dom.democasemgmt.Cases;
import org.incode.domainapp.example.dom.lib.poly.dom.democommchannel.CommunicationChannel;
import org.incode.domainapp.example.dom.lib.poly.dom.democommchannel.CommunicationChannels;
import org.incode.domainapp.example.dom.lib.poly.dom.demofixedasset.FixedAsset;
import org.incode.domainapp.example.dom.lib.poly.dom.demofixedasset.FixedAssets;
import org.incode.domainapp.example.dom.lib.poly.dom.demoparty.Parties;
import org.incode.domainapp.example.dom.lib.poly.dom.demoparty.Party;

import java.util.List;
import org.apache.isis.applib.annotation.ViewModel;

@ViewModel
public class HomePage {

    //region > title
    public String title() {
        return getParties().size() + " parties, " + getFixedAssets().size() + " fixed assets, "+ getCases().size() + " cases, " +  getCommunicationChannels().size() + " comm channels";
    }
    //endregion

    public List<CommunicationChannel> getCommunicationChannels() {
        return communicationChannelsMenu.listAll();
    }

    public List<Party> getParties() {
        return parties.listAll();
    }

    public List<FixedAsset> getFixedAssets() {
        return fixedAssets.listAll();
    }

    public List<Case> getCases() {
        return cases.listAll();
    }

    @javax.inject.Inject
    CommunicationChannels communicationChannelsMenu;

    @javax.inject.Inject
    Parties parties;

    @javax.inject.Inject
    FixedAssets fixedAssets;

    @javax.inject.Inject
    Cases cases;
}

