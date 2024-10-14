package com.idragonpro.andmagnus.adapters;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.idragonpro.andmagnus.beans.Movies;
import com.idragonpro.andmagnus.fragments.EpisodesFragment;
import com.idragonpro.andmagnus.fragments.ExtraDetailsFragment;

public class InfoAdapter extends FragmentStatePagerAdapter {

    int tabCount;
    private final Movies sMovie;
    private boolean showEpisodes;
    Bundle bundle;

    public InfoAdapter(FragmentManager fm, int tabCount, Movies sMovie, boolean showEpisodes) {
        super(fm);
        this.tabCount = tabCount;
        this.sMovie = sMovie;
        this.showEpisodes = showEpisodes;
        bundle = new Bundle();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if(showEpisodes){
                    EpisodesFragment tab2 = new EpisodesFragment();
                    bundle.putSerializable("Movies",sMovie);
                    tab2.setArguments(bundle);
                    return tab2;
                }else {
                    ExtraDetailsFragment tab1 = new ExtraDetailsFragment();
                    bundle.putSerializable("Movies", sMovie);
                    tab1.setArguments(bundle);
                    return tab1;
                }
            case 1:
                ExtraDetailsFragment tab1 = new ExtraDetailsFragment();
                bundle.putSerializable("Movies", sMovie);
                tab1.setArguments(bundle);
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


