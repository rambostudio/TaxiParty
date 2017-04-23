package com.rambostudio.zojoz.taxiparty.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.rambostudio.zojoz.taxiparty.R;
import com.rambostudio.zojoz.taxiparty.adapter.ViewPagerAdapter;
import com.rambostudio.zojoz.taxiparty.fragment.HomeFragment;
import com.rambostudio.zojoz.taxiparty.listener.PartyRecyclerViewAdapterListener;
import com.rambostudio.zojoz.taxiparty.listener.UserSignOutListener;
import com.rambostudio.zojoz.taxiparty.model.Party;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentListener,UserSignOutListener {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInstances();

    }

    private void initInstances() {
        // Init 'View' instance(s) with rootView.findViewById here
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        toolbar.setTitle("Home");
                        break;
                    case 1:
                        toolbar.setTitle("User Profile");
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (TabLayout) findViewById(R.id.slidingTabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if (item.getItemId() == R.id.action_add_party) {
           Toast.makeText(this, "Add Party", Toast.LENGTH_SHORT).show();
           Intent intent = new Intent(MainActivity.this, AddPartyActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onUserSignOut() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPartyItemClicked(String partyId) {
        Intent intent = new Intent(MainActivity.this, PartyActivity.class);
        intent.putExtra("partyId", partyId);
        startActivity(intent);
    }


}
