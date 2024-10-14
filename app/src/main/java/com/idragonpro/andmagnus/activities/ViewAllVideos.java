package com.idragonpro.andmagnus.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.models.Banners;
import com.idragonpro.andmagnus.radapters.ViewAllRvAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewAllVideos extends AppCompatActivity {

    Context context;
    Dialog dialog;
    private FirebaseAnalytics mFirebaseAnalytics;
    private List<Banners> contentListNew = new ArrayList<>();
    private ViewAllRvAdapter viewAllRvAdapter;
    private RecyclerView rvViewAll;
    private final int visibleThreshold = 2;
    private boolean loading;
    private final int pageNumber = 1;
    private final int pageSize = 10;
    private ProgressBar progress_bar;
    private final boolean loadMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_videos);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_title);
        View customView = getSupportActionBar().getCustomView();
        TextView tvTitle = customView.findViewById(R.id.tvTitle);
        ImageView imgBack = customView.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());

        context = this;

        contentListNew = (List<Banners>) getIntent().getSerializableExtra("movies");
        String title = getIntent().getStringExtra("title");
        progress_bar = findViewById(R.id.progress_bar);
        rvViewAll = findViewById(R.id.rvViewAll);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        tvTitle.setText(title);
        recordScreenView();

        // Check the device orientation
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape mode
            GridLayoutManager llmanager = new GridLayoutManager(this, 2);
            rvViewAll.setLayoutManager(llmanager);
            showContent();

        } else {
            LinearLayoutManager llmanager = new LinearLayoutManager(this);
            rvViewAll.setLayoutManager(llmanager);
            showContent();

        }

    }

    private void showContent() {
        viewAllRvAdapter = new ViewAllRvAdapter(this, contentListNew);
        rvViewAll.setAdapter(viewAllRvAdapter);
    }

    private void recordScreenView() {
        // This string must be <= 36 characters long in order for setCurrentScreen to succeed.
        //string screenName = getCurrentImageId() + "-" + getCurrentImageTitle();
        // [START set_current_screen]
        mFirebaseAnalytics.setCurrentScreen(this, null, null);// [END set_current_screen]
        Log.d("Message", "Track Screen ViewAllVideos");
    }
}
