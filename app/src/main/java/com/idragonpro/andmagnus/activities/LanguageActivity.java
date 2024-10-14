package com.idragonpro.andmagnus.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.helpers.GlobalModule;
import com.idragonpro.andmagnus.helpers.LocaleHelper;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private RelativeLayout rlEnglish, rlHindi, rlMarathi, rlPunjabi, rlTamil, rlTelugu, rlKannada;
    private RadioButton radioEnglish, radioHindi, radioMarathi, radioPunjabi, radioTamil, radioTelugu, radioKannada;
    String currentLanguage = "en";
    private AppCompatTextView tvContinue;
    private Locale myLocale;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        context = this;

        initComponents();
        setCurrentLanguage();
    }

    private void initComponents() {
        rlEnglish = findViewById(R.id.rlEnglish);
        rlEnglish.setOnClickListener(this);

        rlHindi = findViewById(R.id.rlHindi);
        rlHindi.setOnClickListener(this);

        rlMarathi = findViewById(R.id.rlMarathi);
        rlMarathi.setOnClickListener(this);

        rlPunjabi = findViewById(R.id.rlPunjabi);
        rlPunjabi.setOnClickListener(this);

        rlTamil = findViewById(R.id.rlTamil);
        rlTamil.setOnClickListener(this);

        rlTelugu = findViewById(R.id.rlTelugu);
        rlTelugu.setOnClickListener(this);

        rlKannada = findViewById(R.id.rlKannada);
        rlKannada.setOnClickListener(this);

        tvContinue = findViewById(R.id.tvContinue);
        tvContinue.setOnClickListener(this);

        radioEnglish = findViewById(R.id.radioEnglish);
        radioHindi = findViewById(R.id.radioHindi);
        radioMarathi = findViewById(R.id.radioMarathi);
        radioPunjabi = findViewById(R.id.radioPunjabi);
        radioTamil = findViewById(R.id.radioTamil);
        radioTelugu = findViewById(R.id.radioTelugu);
        radioKannada = findViewById(R.id.radioKannada);
    }

    private void setCurrentLanguage() {
        if (SaveSharedPreference.getLanguage(this, "en").equalsIgnoreCase("en")) {
            currentLanguage = "en";
            setSelectedBg(rlEnglish, radioEnglish);
            setUnSelectedBg(rlHindi, radioHindi);
            setUnSelectedBg(rlMarathi, radioMarathi);
            setUnSelectedBg(rlPunjabi, radioPunjabi);
            setUnSelectedBg(rlTamil, radioTamil);
            setUnSelectedBg(rlTelugu, radioTelugu);
            setUnSelectedBg(rlKannada, radioKannada);
        } else if (SaveSharedPreference.getLanguage(this, "en").equalsIgnoreCase("hi")) {
            currentLanguage = "hi";
            setUnSelectedBg(rlEnglish, radioEnglish);
            setSelectedBg(rlHindi, radioHindi);
            setUnSelectedBg(rlMarathi, radioMarathi);
            setUnSelectedBg(rlPunjabi, radioPunjabi);
            setUnSelectedBg(rlTamil, radioTamil);
            setUnSelectedBg(rlTelugu, radioTelugu);
            setUnSelectedBg(rlKannada, radioKannada);
        } else if (SaveSharedPreference.getLanguage(this, "en").equalsIgnoreCase("mr")) {
            currentLanguage = "mr";
            setUnSelectedBg(rlEnglish, radioEnglish);
            setUnSelectedBg(rlHindi, radioHindi);
            setSelectedBg(rlMarathi, radioMarathi);
            setUnSelectedBg(rlPunjabi, radioPunjabi);
            setUnSelectedBg(rlTamil, radioTamil);
            setUnSelectedBg(rlTelugu, radioTelugu);
            setUnSelectedBg(rlKannada, radioKannada);
        } else if (SaveSharedPreference.getLanguage(this, "en").equalsIgnoreCase("pa")) {
            currentLanguage = "pa";
            setUnSelectedBg(rlEnglish, radioEnglish);
            setUnSelectedBg(rlHindi, radioHindi);
            setUnSelectedBg(rlMarathi, radioMarathi);
            setSelectedBg(rlPunjabi, radioPunjabi);
            setUnSelectedBg(rlTamil, radioTamil);
            setUnSelectedBg(rlTelugu, radioTelugu);
            setUnSelectedBg(rlKannada, radioKannada);
        } else if (SaveSharedPreference.getLanguage(this, "en").equalsIgnoreCase("te")) {
            currentLanguage = "te";
            setUnSelectedBg(rlEnglish, radioEnglish);
            setUnSelectedBg(rlHindi, radioHindi);
            setUnSelectedBg(rlMarathi, radioMarathi);
            setUnSelectedBg(rlPunjabi, radioPunjabi);
            setUnSelectedBg(rlTamil, radioTamil);
            setSelectedBg(rlTelugu, radioTelugu);
            setUnSelectedBg(rlKannada, radioKannada);
        } else if (SaveSharedPreference.getLanguage(this, "en").equalsIgnoreCase("ta")) {
            currentLanguage = "ta";
            setUnSelectedBg(rlEnglish, radioEnglish);
            setUnSelectedBg(rlHindi, radioHindi);
            setUnSelectedBg(rlMarathi, radioMarathi);
            setUnSelectedBg(rlPunjabi, radioPunjabi);
            setSelectedBg(rlTamil, radioTamil);
            setUnSelectedBg(rlTelugu, radioTelugu);
            setUnSelectedBg(rlKannada, radioKannada);
        } else if (SaveSharedPreference.getLanguage(this, "en").equalsIgnoreCase("kn")) {
            currentLanguage = "kn";
            setUnSelectedBg(rlEnglish, radioEnglish);
            setUnSelectedBg(rlHindi, radioHindi);
            setUnSelectedBg(rlMarathi, radioMarathi);
            setUnSelectedBg(rlPunjabi, radioPunjabi);
            setUnSelectedBg(rlTamil, radioTamil);
            setUnSelectedBg(rlTelugu, radioTelugu);
            setSelectedBg(rlKannada, radioKannada);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlEnglish:
                currentLanguage = "en";
                setSelectedBg(rlEnglish, radioEnglish);
                setUnSelectedBg(rlHindi, radioHindi);
                setUnSelectedBg(rlMarathi, radioMarathi);
                setUnSelectedBg(rlPunjabi, radioPunjabi);
                setUnSelectedBg(rlTamil, radioTamil);
                setUnSelectedBg(rlTelugu, radioTelugu);
                setUnSelectedBg(rlKannada, radioKannada);
                break;
            case R.id.rlHindi:
                currentLanguage = "hi";
                setUnSelectedBg(rlEnglish, radioEnglish);
                setSelectedBg(rlHindi, radioHindi);
                setUnSelectedBg(rlMarathi, radioMarathi);
                setUnSelectedBg(rlPunjabi, radioPunjabi);
                setUnSelectedBg(rlTamil, radioTamil);
                setUnSelectedBg(rlTelugu, radioTelugu);
                setUnSelectedBg(rlKannada, radioKannada);
                break;
            case R.id.rlMarathi:
                currentLanguage = "mr";
                setUnSelectedBg(rlEnglish, radioEnglish);
                setUnSelectedBg(rlHindi, radioHindi);
                setSelectedBg(rlMarathi, radioMarathi);
                setUnSelectedBg(rlPunjabi, radioPunjabi);
                setUnSelectedBg(rlTamil, radioTamil);
                setUnSelectedBg(rlTelugu, radioTelugu);
                setUnSelectedBg(rlKannada, radioKannada);
                break;
            case R.id.rlPunjabi:
                currentLanguage = "pa";
                setUnSelectedBg(rlEnglish, radioEnglish);
                setUnSelectedBg(rlHindi, radioHindi);
                setUnSelectedBg(rlMarathi, radioMarathi);
                setSelectedBg(rlPunjabi, radioPunjabi);
                setUnSelectedBg(rlTamil, radioTamil);
                setUnSelectedBg(rlTelugu, radioTelugu);
                setUnSelectedBg(rlKannada, radioKannada);
                break;
            case R.id.rlTamil:
                currentLanguage = "ta";
                setUnSelectedBg(rlEnglish, radioEnglish);
                setUnSelectedBg(rlHindi, radioHindi);
                setUnSelectedBg(rlMarathi, radioMarathi);
                setUnSelectedBg(rlPunjabi, radioPunjabi);
                setSelectedBg(rlTamil, radioTamil);
                setUnSelectedBg(rlTelugu, radioTelugu);
                setUnSelectedBg(rlKannada, radioKannada);
                break;
            case R.id.rlTelugu:
                currentLanguage = "te";
                setUnSelectedBg(rlEnglish, radioEnglish);
                setUnSelectedBg(rlHindi, radioHindi);
                setUnSelectedBg(rlMarathi, radioMarathi);
                setUnSelectedBg(rlPunjabi, radioPunjabi);
                setUnSelectedBg(rlTamil, radioTamil);
                setSelectedBg(rlTelugu, radioTelugu);
                setUnSelectedBg(rlKannada, radioKannada);
                break;
            case R.id.rlKannada:
                currentLanguage = "kn";
                setUnSelectedBg(rlEnglish, radioEnglish);
                setUnSelectedBg(rlHindi, radioHindi);
                setUnSelectedBg(rlMarathi, radioMarathi);
                setUnSelectedBg(rlPunjabi, radioPunjabi);
                setUnSelectedBg(rlTamil, radioTamil);
                setUnSelectedBg(rlTelugu, radioTelugu);
                setSelectedBg(rlKannada, radioKannada);
                break;
            case R.id.tvContinue:
                setLocale(currentLanguage);
                break;
        }
    }

    public void setLocale(String localeName) {
        LocaleHelper.setLocale(LanguageActivity.this, localeName);
        startHomeActivity();
    }

    private void startHomeActivity() {
        Intent intent = new Intent(LanguageActivity.this, com.idragonpro.andmagnus.activities.Home.class);
        GlobalModule.startActivity = LanguageActivity.class.getSimpleName();
        startActivity(intent);
        finish();
    }

    private void setSelectedBg(RelativeLayout rl, RadioButton rb) {
        rl.setBackground(ContextCompat.getDrawable(context, R.drawable.new_button_bg));
        rb.setChecked(true);
    }

    private void setUnSelectedBg(RelativeLayout rl, RadioButton rb) {
        rl.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_unselected));
        rb.setChecked(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAd();
    }

    public void refreshAd() {
        if (SaveSharedPreference.getAdsData(this) != null) {
            FrameLayout frameLayout_top = findViewById(R.id.native_ad_holder);
            if (SaveSharedPreference.getAdsData(this).getGStatus() != null && SaveSharedPreference.getAdsData(this)
                .getGStatus()
                .equals(API.ACTIVE)) {

                AdLoader adLoader = new AdLoader.Builder(this,
                    SaveSharedPreference.getAdsData(this).getGNative()).forNativeAd(nativeAd -> {
                    NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                    LinearLayout adView_top = (LinearLayout) getLayoutInflater().inflate(R.layout.ad_unified, null);
                    TemplateView template = adView_top.findViewById(R.id.my_template);
                    template.setStyles(styles);
                    template.setNativeAd(nativeAd);
                    frameLayout_top.removeAllViews();
                    frameLayout_top.addView(adView_top);
                }).build();
                adLoader.loadAd(new AdRequest.Builder().build());
            } else if (SaveSharedPreference.getAdsData(this)
                .getMopubStatus() != null && SaveSharedPreference.getAdsData(this)
                .getMopubStatus()
                .equals(API.ACTIVE)) {
                AdLoader adLoader = new AdLoader.Builder(this,
                    SaveSharedPreference.getAdsData(this).getMopubNative()).forNativeAd(nativeAd -> {
                    NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                    LinearLayout adView_top = (LinearLayout) getLayoutInflater().inflate(R.layout.ad_unified, null);
                    TemplateView template = adView_top.findViewById(R.id.my_template);
                    template.setStyles(styles);
                    template.setNativeAd(nativeAd);
                    frameLayout_top.removeAllViews();
                    frameLayout_top.addView(adView_top);
                }).build();
                adLoader.loadAd(new AdManagerAdRequest.Builder().build());
            }
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}
