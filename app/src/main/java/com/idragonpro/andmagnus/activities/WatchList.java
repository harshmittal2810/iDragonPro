package com.idragonpro.andmagnus.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.adapters.WatchListAdapter;
import com.idragonpro.andmagnus.helpers.LocaleHelper;
import com.idragonpro.andmagnus.helpers.NonSwipeableViewPager;
import com.idragonpro.andmagnus.utility.UtilityInterface;

public class WatchList extends AppCompatActivity  implements TabLayout.OnTabSelectedListener, UtilityInterface {
    Context context;
    private TabLayout tabLayout;
    private NonSwipeableViewPager viewPager;
    Dialog dialog;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ImageView imgBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

        context = this;

        //By Archana: Google analytics
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        recordScreenView();

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (NonSwipeableViewPager) findViewById(R.id.pager);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.your_movies)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.watch_List)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        WatchListAdapter adapter = new WatchListAdapter(0, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        tabLayout.setOnTabSelectedListener(WatchList.this);

        //By Bushra: to handle pip window close
//        this.finishActivity(GlobalModule.sActivity);

    }

    private void recordScreenView() {
        // This string must be <= 36 characters long in order for setCurrentScreen to succeed.
        //string screenName = getCurrentImageId() + "-" + getCurrentImageTitle();
        // [START set_current_screen]
        mFirebaseAnalytics.setCurrentScreen(this ,null,null);// [END set_current_screen]
        Log.d("Message","Track Screen WatchList");
    }

    @Override
    public void finishActivity(Activity activity) {
        if (!activity.isDestroyed()) {
            activity.finish();
        }else{
            Log.d("Message","PIP Watchlist Activity");
        }

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
//        tab.setIcon(iconsNotSelected[tab.getPosition()]);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base,"en"));
    }

}
