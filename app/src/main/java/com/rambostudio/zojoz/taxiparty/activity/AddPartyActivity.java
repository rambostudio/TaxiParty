package com.rambostudio.zojoz.taxiparty.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.rambostudio.zojoz.taxiparty.R;
import com.rambostudio.zojoz.taxiparty.fragment.AddPartyFragment;

public class AddPartyActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_party);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.contentContainer,AddPartyFragment.newInstance())
                .commit();
        initInstances();
    }

    private void initInstances() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }




}
