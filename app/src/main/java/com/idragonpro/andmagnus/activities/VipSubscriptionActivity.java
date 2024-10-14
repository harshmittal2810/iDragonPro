package com.idragonpro.andmagnus.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.loadinganimation.LoadingAnimation;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.adapters.VipAdapter;
import com.idragonpro.andmagnus.api.viewVipModel;
import com.idragonpro.andmagnus.beans.PackageModel;
import com.idragonpro.andmagnus.databinding.ActivityVipSubscriptionBinding;
import com.idragonpro.andmagnus.models.vipModel.Vip;

import java.util.ArrayList;

public class VipSubscriptionActivity extends AppCompatActivity {

    VipAdapter adapter;
    ActivityVipSubscriptionBinding binding;
    ArrayList<PackageModel> vipArrayList = new ArrayList<>();
    LoadingAnimation loadingAnim;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vip_subscription);
        binding.recyclerVip.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerVip.setHasFixedSize(true);

        loadingAnim = findViewById(R.id.loadingAnim);
        loadingAnim.setProgressVector(getResources().getDrawable(R.drawable.bouncy_balls));
        loadingAnim.setTextViewVisibility(true);
        loadingAnim.setTextStyle(true);
        loadingAnim.setTextSize(16);
        loadingAnim.setTextColor(Color.RED);
        loadingAnim.setTextMsg("Please Wait");
        loadingAnim.setEnlarge(6);// Simulating a 3-second background task

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Test Analytics");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "VipSubscriptionPage");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        recordScreenView();
        signUp();

    }

    private void recordScreenView() {
        mFirebaseAnalytics.setCurrentScreen(this, null, null);// [END set_current_screen]
        Log.d("Message", "Track Screen Subscription");
    }

    private void signUp() {
        viewVipModel viewModel = new ViewModelProvider(this).get(viewVipModel.class);
        viewModel.setVipRepository();

        viewModel.getVip().observe(this, (Vip vip) -> {
            if (vip.getStatus() != null) {
                loadingAnim.setVisibility(View.GONE);
                vipArrayList = vip.getData().getPackages();
                adapter = new VipAdapter(this, vipArrayList);
                binding.recyclerVip.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

        });

    }
}