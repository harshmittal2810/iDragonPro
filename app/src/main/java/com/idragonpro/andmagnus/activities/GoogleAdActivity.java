package com.idragonpro.andmagnus.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.idragonpro.andmagnus.R;

public class GoogleAdActivity extends AppCompatActivity {

    AdView mAdView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_ad);

        AdLoader adLoader = new AdLoader.Builder(GoogleAdActivity.this,
            "ca-app-pub-2561754853520293/7194659582")//"ca-app-pub-3940256099942544/2247696110")
            .forNativeAd(nativeAd -> {
            }).withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(LoadAdError errorCode) {
                    // Handle the failure by logging, altering the UI, and so on.
                }
            }).withNativeAdOptions(new NativeAdOptions.Builder().build()).build();
        //            adLoader.loadAd(new AdRequest.Builder().build());
        adLoader.loadAds(new AdRequest.Builder().build(), 3);
    }
}
