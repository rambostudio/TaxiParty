package com.rambostudio.zojoz.taxiparty.manager;

import android.content.Context;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;
import com.rambostudio.zojoz.taxiparty.model.Party;

import java.util.List;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class PartyListManager {

    List<Party> partyList;
    private static PartyListManager instance;

    public static PartyListManager getInstance() {
        if (instance == null)
            instance = new PartyListManager();
        return instance;
    }


    private Context mContext;

    private PartyListManager() {
        mContext = Contextor.getInstance().getContext();
    }

    public List<Party> getPartyList() {
        return partyList;
    }

    public void setPartyList(List<Party> partyList) {
        this.partyList = partyList;
    }

    public void addParttyList(Party party) {
        this.partyList.add(party);
    }
}
