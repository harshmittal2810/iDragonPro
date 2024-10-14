package com.idragonpro.andmagnus.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.helpers.LocaleHelper;

public class SplashActivity extends AppCompatActivity {
    private ImageView logo;
    private static int splashTimeOut = 2500;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo = findViewById(R.id.splash_img);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        recordScreenView();

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this, FlashActivity.class); //FlashActivity
            startActivity(i);
            finish();
        }, splashTimeOut);

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mysplashanimation);
        logo.startAnimation(myanim);
    }

    private void recordScreenView() {
        mFirebaseAnalytics.setCurrentScreen(this, null, null);// [END set_current_screen]
        Log.d("Message", "Track Screen SplashActivity");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}
