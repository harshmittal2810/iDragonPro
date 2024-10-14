package com.idragonpro.andmagnus.adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.idragonpro.andmagnus.BundleKeys;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.fragments.NewHomeFragment;

public class HomeTabAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = HomeTabAdapter.class.getSimpleName();
    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();
    int tabCount;
    int code;
    Bundle bundle;

    public HomeTabAdapter(int code, FragmentManager fm, int tabCount) {
        super(fm);
        this.code = code;
        this.tabCount = tabCount;
        bundle = new Bundle();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        NewHomeFragment tab1 = new NewHomeFragment();
        switch (position) {
            case 0:
                args.putString(BundleKeys.TAB_NAME, API.HOME_ACTION);
                tab1.setArguments(args);
                return tab1;

            case 1:
                args.putString(BundleKeys.TAB_NAME, API.WEB_SERIES_ACTION);
                tab1.setArguments(args);
                return tab1;
            case 2:
                args.putString(BundleKeys.TAB_NAME, API.MOVIES_ACTION);
                tab1.setArguments(args);
                return tab1;
            case 3:
                args.putString(BundleKeys.TAB_NAME, API.KIDS_ACTION);
                tab1.setArguments(args);
                return tab1;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        try {
            super.restoreState(state, loader);
        } catch (Exception e) {
            Log.e("TAG", "Error Restore State of Fragment : " + e.getMessage(), e);
        }
    }
}


