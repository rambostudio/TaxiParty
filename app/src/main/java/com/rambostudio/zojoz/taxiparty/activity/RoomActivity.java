package com.rambostudio.zojoz.taxiparty.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.rambostudio.zojoz.taxiparty.R;
import com.rambostudio.zojoz.taxiparty.fragment.RoomFragment;
import com.rambostudio.zojoz.taxiparty.utils.Utils;

public class RoomActivity extends BaseActivity {

    private int mPartyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        mPartyId = intent.getIntExtra("partyId", 0);

        if (Utils.isOnline()) {
            getDataFromServer(mPartyId);
        } else {
            getDataFromLocal(mPartyId);
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.contentContainer, RoomFragment.newInstance());
        initInstances();
    }

    private void getDataFromLocal(int partyId) {
        // TODO: 17/4/2560 get party data by partyId from local
    }

    private void getDataFromServer(int partyId) {
        // TODO: 17/4/2560 get party data by partyId from api
    }

    private void initInstances() {
    }
}
