package com.idragonpro.andmagnus.adapters;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.idragonpro.andmagnus.fragments.WatchListFragment;

public class WatchListAdapter  extends FragmentStatePagerAdapter {

    int tabCount;
    int code;
    Bundle bundle;

    public WatchListAdapter() {
        super(null);
    }

    public WatchListAdapter(int code, FragmentManager fm, int tabCount) {
        super(fm);
        this.code = code;
        this.tabCount = tabCount;
        bundle = new Bundle();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                WatchListFragment tab2 = new WatchListFragment();
                tab2.setArguments(bundle);
                return tab2;
            case 1:
                WatchListFragment tab1 = new WatchListFragment();
                return tab1;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}



