package com.rambostudio.zojoz.taxiparty.activity;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.rambostudio.zojoz.taxiparty.R;
import com.rambostudio.zojoz.taxiparty.fragment.PartyDialogFragment;
import com.rambostudio.zojoz.taxiparty.fragment.PartyFragment;
import com.rambostudio.zojoz.taxiparty.fragment.RoomFragment;
import com.rambostudio.zojoz.taxiparty.model.Party;
import com.rambostudio.zojoz.taxiparty.utils.Utils;

public class PartyActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String  mPartyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);
        initInstances();

        Intent intent = getIntent();
        mPartyId = intent.getStringExtra("partyId");

        if (Utils.isOnline()) {
            getDataFromServer(mPartyId);
        } else {
            getDataFromLocal(mPartyId);
        }

        showDialog();

        initInstances();
    }

    private void showDialog() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.contentContainer, PartyFragment.newInstance(mPartyId))
                .commit();
//        PartyDialogFragment dialogFragment = PartyDialogFragment.newInstance(mPartyId);
//        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
//        dialogFragment.show(getSupportFragmentManager().beginTransaction(), "PartyDialogFragment");

    }

    private void getDataFromLocal(String  partyId) {
        // TODO: 17/4/2560 get party data by partyId from local
    }

    private void getDataFromServer(String partyId) {
        // TODO: 17/4/2560 get party data by partyId from api
    }

    private void initInstances() {

    }


}
