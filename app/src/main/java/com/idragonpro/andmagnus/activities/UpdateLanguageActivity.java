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
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.helpers.GlobalModule;
import com.idragonpro.andmagnus.helpers.LocaleHelper;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;

import java.util.Locale;

public class UpdateLanguageActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private UnifiedNativeAd nativeAd;
    private RelativeLayout rlEnglish,rlHindi,rlMarathi,rlPunjabi,rlTamil,rlTelugu;
    private RadioButton radioEnglish,radioHindi,radioMarathi,radioPunjabi,radioTamil,radioTelugu;
    String currentLanguage = "en";
    private AppCompatTextView tvContinue;
    private Locale myLocale;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_language);
        context = this;

        initComponents();
        setCurrentLanguage();
    }

    private void setCurrentLanguage() {
        if (SaveSharedPreference.getLanguage(this, "").equalsIgnoreCase("en")) {
            currentLanguage = "en";
            setSelectedBg(rlEnglish,radioEnglish);
            setUnSelectedBg(rlHindi,radioHindi);
            setUnSelectedBg(rlMarathi,radioMarathi);
            setUnSelectedBg(rlPunjabi,radioPunjabi);
            setUnSelectedBg(rlTamil,radioTamil);
            setUnSelectedBg(rlTelugu,radioTelugu);
        } else if (SaveSharedPreference.getLanguage(this, "").equalsIgnoreCase("hi")) {
            currentLanguage = "hi";
            setUnSelectedBg(rlEnglish,radioEnglish);
            setSelectedBg(rlHindi,radioHindi);
            setUnSelectedBg(rlMarathi,radioMarathi);
            setUnSelectedBg(rlPunjabi,radioPunjabi);
            setUnSelectedBg(rlTamil,radioTamil);
            setUnSelectedBg(rlTelugu,radioTelugu);
        } else if (SaveSharedPreference.getLanguage(this, "").equalsIgnoreCase("mr")) {
            currentLanguage = "mr";
            setUnSelectedBg(rlEnglish,radioEnglish);
            setUnSelectedBg(rlHindi,radioHindi);
            setSelectedBg(rlMarathi,radioMarathi);
            setUnSelectedBg(rlPunjabi,radioPunjabi);
            setUnSelectedBg(rlTamil,radioTamil);
            setUnSelectedBg(rlTelugu,radioTelugu);
        } else if (SaveSharedPreference.getLanguage(this, "").equalsIgnoreCase("pa")) {
            currentLanguage = "pa";
            setUnSelectedBg(rlEnglish,radioEnglish);
            setUnSelectedBg(rlHindi,radioHindi);
            setUnSelectedBg(rlMarathi,radioMarathi);
            setSelectedBg(rlPunjabi,radioPunjabi);
            setUnSelectedBg(rlTamil,radioTamil);
            setUnSelectedBg(rlTelugu,radioTelugu);
        } else if (SaveSharedPreference.getLanguage(this, "").equalsIgnoreCase("te")) {
            currentLanguage = "te";
            setUnSelectedBg(rlEnglish,radioEnglish);
            setUnSelectedBg(rlHindi,radioHindi);
            setUnSelectedBg(rlMarathi,radioMarathi);
            setUnSelectedBg(rlPunjabi,radioPunjabi);
            setUnSelectedBg(rlTamil,radioTamil);
            setSelectedBg(rlTelugu,radioTelugu);
        } else if (SaveSharedPreference.getLanguage(this, "").equalsIgnoreCase("ta")) {
            currentLanguage = "ta";
            setUnSelectedBg(rlEnglish,radioEnglish);
            setUnSelectedBg(rlHindi,radioHindi);
            setUnSelectedBg(rlMarathi,radioMarathi);
            setUnSelectedBg(rlPunjabi,radioPunjabi);
            setSelectedBg(rlTamil,radioTamil);
            setUnSelectedBg(rlTelugu,radioTelugu);
        }
    }


    private void initComponents() {
        rlEnglish = (RelativeLayout) findViewById(R.id.rlEnglish);
        rlEnglish.setOnClickListener(this);
        rlHindi = (RelativeLayout) findViewById(R.id.rlHindi);
        rlHindi.setOnClickListener(this);
        rlMarathi = (RelativeLayout) findViewById(R.id.rlMarathi);
        rlMarathi.setOnClickListener(this);
        rlPunjabi = (RelativeLayout) findViewById(R.id.rlPunjabi);
        rlPunjabi.setOnClickListener(this);
        rlTamil = (RelativeLayout) findViewById(R.id.rlTamil);
        rlTamil.setOnClickListener(this);
        rlTelugu = (RelativeLayout) findViewById(R.id.rlTelugu);
        rlTelugu.setOnClickListener(this);
        tvContinue = (AppCompatTextView) findViewById(R.id.tvContinue);
        tvContinue.setOnClickListener(this);

        radioEnglish = (RadioButton) findViewById(R.id.radioEnglish);
        radioHindi = (RadioButton) findViewById(R.id.radioHindi);
        radioMarathi = (RadioButton) findViewById(R.id.radioMarathi);
        radioPunjabi = (RadioButton) findViewById(R.id.radioPunjabi);
        radioTamil = (RadioButton) findViewById(R.id.radioTamil);
        radioTelugu = (RadioButton) findViewById(R.id.radioTelugu);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlEnglish:
                currentLanguage = "en";
                setSelectedBg(rlEnglish,radioEnglish);
                setUnSelectedBg(rlHindi,radioHindi);
                setUnSelectedBg(rlMarathi,radioMarathi);
                setUnSelectedBg(rlPunjabi,radioPunjabi);
                setUnSelectedBg(rlTamil,radioTamil);
                setUnSelectedBg(rlTelugu,radioTelugu);
                break;
            case R.id.rlHindi:
                currentLanguage = "hi";
                setUnSelectedBg(rlEnglish,radioEnglish);
                setSelectedBg(rlHindi,radioHindi);
                setUnSelectedBg(rlMarathi,radioMarathi);
                setUnSelectedBg(rlPunjabi,radioPunjabi);
                setUnSelectedBg(rlTamil,radioTamil);
                setUnSelectedBg(rlTelugu,radioTelugu);
                break;
            case R.id.rlMarathi:
                currentLanguage = "mr";
                setUnSelectedBg(rlEnglish,radioEnglish);
                setUnSelectedBg(rlHindi,radioHindi);
                setSelectedBg(rlMarathi,radioMarathi);
                setUnSelectedBg(rlPunjabi,radioPunjabi);
                setUnSelectedBg(rlTamil,radioTamil);
                setUnSelectedBg(rlTelugu,radioTelugu);
                break;
            case R.id.rlPunjabi:
                currentLanguage = "pa";
                setUnSelectedBg(rlEnglish,radioEnglish);
                setUnSelectedBg(rlHindi,radioHindi);
                setUnSelectedBg(rlMarathi,radioMarathi);
                setSelectedBg(rlPunjabi,radioPunjabi);
                setUnSelectedBg(rlTamil,radioTamil);
                setUnSelectedBg(rlTelugu,radioTelugu);
                break;
            case R.id.rlTamil:
                currentLanguage = "ta";
                setUnSelectedBg(rlEnglish,radioEnglish);
                setUnSelectedBg(rlHindi,radioHindi);
                setUnSelectedBg(rlMarathi,radioMarathi);
                setUnSelectedBg(rlPunjabi,radioPunjabi);
                setSelectedBg(rlTamil,radioTamil);
                setUnSelectedBg(rlTelugu,radioTelugu);
                break;
            case R.id.rlTelugu:
                currentLanguage = "te";
                setUnSelectedBg(rlEnglish,radioEnglish);
                setUnSelectedBg(rlHindi,radioHindi);
                setUnSelectedBg(rlMarathi,radioMarathi);
                setUnSelectedBg(rlPunjabi,radioPunjabi);
                setUnSelectedBg(rlTamil,radioTamil);
                setSelectedBg(rlTelugu,radioTelugu);
                break;
            case R.id.tvContinue:
                setLocale(currentLanguage);
                break;
        }
    }

    public void setLocale(String localeName) {
        LocaleHelper.setLocale(UpdateLanguageActivity.this, localeName);
        startHomeActivity();
    }

    private void startHomeActivity() {
        Intent intent = new Intent(UpdateLanguageActivity.this, Home.class);
        GlobalModule.startActivity = UpdateLanguageActivity.class.getSimpleName();
        startActivity(intent);
        finish();
    }

    private void setSelectedBg(RelativeLayout rl,RadioButton rb) {
        rl.setBackground(ContextCompat.getDrawable(context, R.drawable.language_selected));
        rb.setChecked(true);
    }

    private void setUnSelectedBg(RelativeLayout rl,RadioButton rb) {
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
            FrameLayout frameLayout_top =
                    findViewById(R.id.native_ad_holder);
            if (SaveSharedPreference.getAdsData(this).getGStatus() != null
                    && SaveSharedPreference.getAdsData(this).getGStatus().equals(API.ACTIVE)) {

                AdLoader adLoader = new AdLoader.Builder(this, SaveSharedPreference.getAdsData(this).getGNative())
                        .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(NativeAd nativeAd) {
                                NativeTemplateStyle styles = new
                                        NativeTemplateStyle.Builder().build();
                                LinearLayout adView_top = (LinearLayout) getLayoutInflater()
                                        .inflate(R.layout.ad_unified, null);
                                TemplateView template = adView_top.findViewById(R.id.my_template);
                                template.setStyles(styles);
                                template.setNativeAd(nativeAd);
                                frameLayout_top.removeAllViews();
                                frameLayout_top.addView(adView_top);
                            }
                        })
                        .build();
                adLoader.loadAd(new AdRequest.Builder().build());
            }else if (SaveSharedPreference.getAdsData(this).getMopubStatus() != null
                    && SaveSharedPreference.getAdsData(this).getMopubStatus().equals(API.ACTIVE)) {

                AdLoader adLoader = new AdLoader.Builder(this, SaveSharedPreference.getAdsData(this).getMopubNative())
                        .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(NativeAd nativeAd) {
                                NativeTemplateStyle styles = new
                                        NativeTemplateStyle.Builder().build();
                                LinearLayout adView_top = (LinearLayout) getLayoutInflater()
                                        .inflate(R.layout.ad_unified, null);
                                TemplateView template = adView_top.findViewById(R.id.my_template);
                                template.setStyles(styles);
                                template.setNativeAd(nativeAd);
                                frameLayout_top.removeAllViews();
                                frameLayout_top.addView(adView_top);
                            }
                        })
                        .build();
                adLoader.loadAd(new AdManagerAdRequest.Builder().build());
            }
        }
    }

//    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
//        // Set the media view.
//        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
//
//        // Set other ad assets.
////        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
//        adView.setBodyView(adView.findViewById(R.id.ad_body));
////        //adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
////        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
//////        adView.setPriceView(adView.findViewById(R.id.ad_price));
////        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
//////        adView.setStoreView(adView.findViewById(R.id.ad_store));
////        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
//
//        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
////        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
////        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
//
//        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
//        // check before trying to display them.
//        if (nativeAd.getBody() == null) {
//            adView.getBodyView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getBodyView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
//        }
//
////        if (nativeAd.getCallToAction() == null) {
////          adView.getCallToActionView().setVisibility(View.INVISIBLE);
////        } else {
////          adView.getCallToActionView().setVisibility(View.VISIBLE);
////          ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
////        }
//
////        if (nativeAd.getIcon() == null) {
////            adView.getIconView().setVisibility(View.GONE);
////        } else {
////            ((ImageView) adView.getIconView()).setImageDrawable(
////                    nativeAd.getIcon().getDrawable());
////            adView.getIconView().setVisibility(View.VISIBLE);
////        }
//
////        if (nativeAd.getPrice() == null) {
////            adView.getPriceView().setVisibility(View.INVISIBLE);
////        } else {
////            adView.getPriceView().setVisibility(View.VISIBLE);
////            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
////        }
//
////        if (nativeAd.getStore() == null) {
////            adView.getStoreView().setVisibility(View.INVISIBLE);
////        } else {
////            adView.getStoreView().setVisibility(View.VISIBLE);
////            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
////        }
//
////        if (nativeAd.getStarRating() == null) {
////            adView.getStarRatingView().setVisibility(View.INVISIBLE);
////        } else {
////            ((RatingBar) adView.getStarRatingView())
////                    .setRating(nativeAd.getStarRating().floatValue());
////            adView.getStarRatingView().setVisibility(View.VISIBLE);
////        }
//
////        if (nativeAd.getAdvertiser() == null) {
////            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
////        } else {
////            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
////            adView.getAdvertiserView().setVisibility(View.VISIBLE);
////        }
//
//        // This method tells the Google Mobile Ads SDK that you have finished populating your
//        // native ad view with this native ad.
//        adView.setNativeAd(nativeAd);
//
//        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
//        // have a video asset.
//        VideoController vc = nativeAd.getVideoController();
//
//        // Updates the UI to say whether or not this ad has a video asset.
//        if (vc.hasVideoContent()) {
////            videoStatus.setText(String.format(Locale.getDefault(),
////                    "Video status: Ad contains a %.2f:1 video asset.",
////                    vc.getAspectRatio()));
//            Log.e("Message","video status : "+String.format(Locale.getDefault(),
//                    "Video status: Ad contains a %.2f:1 video asset.",
//                    vc.getAspectRatio()));
//
//            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
//            // VideoController will call methods on this object when events occur in the video
//            // lifecycle.
//            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
//                @Override
//                public void onVideoEnd() {
//                    // Publishers should allow native ads to complete video playback before
//                    // refreshing or replacing them with another ad in the same UI location.
////                    refresh.setEnabled(true);
////                    videoStatus.setText("Video status: Video playback has ended.");
//                    Log.e("Message","Video status: Video playback has ended.");
//                    super.onVideoEnd();
//                }
//            });
//        } else {
//            Log.e("Message","Video status: Ad does not contain a video asset.");
////            videoStatus.setText("Video status: Ad does not contain a video asset.");
////            refresh.setEnabled(true);
//        }
//    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base,"en"));
    }
}
