package com.rambostudio.zojoz.taxiparty.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rambostudio.zojoz.taxiparty.fragment.HomeFragment;
import com.rambostudio.zojoz.taxiparty.fragment.PartyFragment;
import com.rambostudio.zojoz.taxiparty.fragment.UserFragment;

/**
 * Created by rambo on 15/4/2560.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HomeFragment.newInstance();
            case 1:
                return UserFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Party";
            case 1:
                return "User";
            default:
                return "";
        }
    }


}
