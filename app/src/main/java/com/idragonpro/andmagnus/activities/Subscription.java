package com.idragonpro.andmagnus.activities;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.idragonpro.andmagnus.MyApp;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.api.WebApi;
import com.idragonpro.andmagnus.beans.BundleMovies;
import com.idragonpro.andmagnus.beans.Movies;
import com.idragonpro.andmagnus.beans.PackageModel;
import com.idragonpro.andmagnus.helpers.LocaleHelper;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;
import com.idragonpro.andmagnus.models.BundlePackage;
import com.idragonpro.andmagnus.responseModels.AnalyticsResponseModel;
import com.idragonpro.andmagnus.responseModels.BundlePackageResponseModel;
import com.idragonpro.andmagnus.responseModels.SplashResponseModel;
import com.idragonpro.andmagnus.utility.UtilityInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Subscription extends AppCompatActivity implements UtilityInterface {

    private static final String TAG = Subscription.class.getSimpleName();
    Context context;
    Movies sMovie;
    BundlePackageResponseModel bundlePackageResponseModel;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    LinearLayout loaderview;

    private FirebaseAnalytics mFirebaseAnalytics;
    private UnifiedNativeAd nativeAd;
    private LinearLayout subParentLayout;
    int price = 0;
    private PackageModel packageModel1, packageModel2;
    private ImageView imgSubscription, imgCancel;
    private FrameLayout frameLayout;

    private boolean isFABOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3;
    private MediaPlayer mp;
    private ImageView img_help, img_close;
    private TextView tvHelp;
    private ObjectAnimator rotate;
    public static final String PACKAGE = "package";

    public static final String MOVIE = "movie";
    public static final String ISDOUBLESUBS = "isDoubleSubs";
    public static final String ISMOVIESBUNDLE = "isMovieBundle";
    public static final String PACKAGE1 = "package1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_bottomsheet);

        context = this;

        sMovie = (Movies) getIntent().getSerializableExtra(Info.MOVIESLIST);
        if (getIntent().hasExtra(Info.BUNDLEPACKAGE)) {
            bundlePackageResponseModel = (BundlePackageResponseModel) getIntent().getSerializableExtra(Info.BUNDLEPACKAGE);
        }
        subParentLayout = findViewById(R.id.subParentLayout);
        imgSubscription = findViewById(R.id.imgSubscription);
        imgCancel = findViewById(R.id.imgCancel);
        frameLayout = findViewById(R.id.fl_adplaceholder_top);
        if (sMovie.getnSubscriptionBannerUrl() != null && !sMovie.getnSubscriptionBannerUrl().isEmpty()) {
            Glide.with(imgSubscription.getContext())
                .load(sMovie.getnSubscriptionBannerUrl()).centerCrop()
                .into(imgSubscription);
        } else {
            Glide.with(imgSubscription.getContext()).load(R.drawable.not_img).centerCrop().into(imgSubscription);
        }
        imgCancel.setOnClickListener(v -> onBackPressed());

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        recordScreenView();
        MobileAds.initialize(this, initializationStatus -> {});

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        img_help = findViewById(R.id.img_help);
        img_close = findViewById(R.id.img_close);
        tvHelp = findViewById(R.id.tvHelp);

        fab.setOnClickListener(view -> {
            if (!isFABOpen) {
                showFABMenu();
            } else {
                closeFABMenu();
            }
        });
        img_close.setOnClickListener(v -> {
            img_help.setVisibility(View.GONE);
            tvHelp.setVisibility(View.GONE);
            img_close.setVisibility(View.GONE);
        });
        setPaymentAudio();

        refreshAd();
        addPackageDetails();
        sendSubscription();
    }

    @SuppressLint("SetTextI18n")
    private void addPackageDetails() {
        if (sMovie.getPackageModels() != null) {
            for (int q = 0; q < sMovie.getPackageModels().size(); q++) {
                boolean addBundlePackage = false;
                PackageModel packageModel = sMovie.getPackageModels().get(q);
                if (packageModel.getVideoType().equalsIgnoreCase(sMovie.getsType())) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View rowView = inflater.inflate(R.layout.payment_card_new, null);
                    TextView btnSingleMovie = rowView.findViewById(R.id.btnSingleMovie);
                    TextView tvSingleMovie = rowView.findViewById(R.id.tvSingleMovie);
                    TextView tv_single2d_movie = rowView.findViewById(R.id.tv_single2d_movie);
                    TextView btnBuy = rowView.findViewById(R.id.btnBuy);
                    CardView cvPAyment = rowView.findViewById(R.id.cvPAyment);
                    RelativeLayout rlPAyment = rowView.findViewById(R.id.rlPAyment);

                    if (sMovie.getsAllowedInPackage().equalsIgnoreCase(API.ACTIVE)) {
                        if (packageModel.getIsPackage().equalsIgnoreCase(API.ACTIVE)) {
                            tvSingleMovie.setText(packageModel.getPackage());
                        } else {
                            tvSingleMovie.setText(sMovie.getsName());
                            rlPAyment.setBackground(ContextCompat.getDrawable(context, R.drawable.subscription_silver));
                            if (bundlePackageResponseModel != null) {
                                addBundlePackage = true;
                            }
                        }
                        tv_single2d_movie.setText(packageModel.getDescription());
                        btnSingleMovie.setText(packageModel.getPrice());
                        cvPAyment.setOnClickListener(v -> {
                            packageModel.setOgPrice(packageModel.getPrice());
                            imgCancel.performClick();
                            paymentAlert(packageModel, false, false);
                        });
                        if (bundlePackageResponseModel == null || packageModel.getIsShowWithBundle()
                            .equalsIgnoreCase(API.ACTIVE)) {
                            subParentLayout.addView(rowView);
                        }
                        if (addBundlePackage) {
                            addBundlePackageDetails();
                        }
                    } else {
                        if (sMovie.getsType()
                            .equalsIgnoreCase("5") && (SaveSharedPreference.getWebRemDays(context) > 0 || SaveSharedPreference.getWebTimeDiff(
                            context) > 0)) {
                            if (!packageModel.getIsPackage().equalsIgnoreCase(API.ACTIVE)) {
                                tvSingleMovie.setText(sMovie.getsName());
                                rlPAyment.setBackground(ContextCompat.getDrawable(context,
                                    R.drawable.subscription_silver));
                                tv_single2d_movie.setText(packageModel.getDescription());
                                btnSingleMovie.setText(packageModel.getPrice());
                                cvPAyment.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        packageModel.setOgPrice(packageModel.getPrice());
                                        imgCancel.performClick();
                                        paymentAlert(packageModel, false, false);
                                    }
                                });
                                subParentLayout.addView(rowView);
                            }
                        } else if (sMovie.getsType()
                            .equalsIgnoreCase("2") && (SaveSharedPreference.getRemDays(context) > 0 || SaveSharedPreference.getTimeDiff(
                            context) > 0)) {
                            if (!packageModel.getIsPackage().equalsIgnoreCase(API.ACTIVE)) {
                                tvSingleMovie.setText(sMovie.getsName());
                                rlPAyment.setBackground(ContextCompat.getDrawable(context,
                                    R.drawable.subscription_silver));
                                tv_single2d_movie.setText(packageModel.getDescription());
                                btnSingleMovie.setText(packageModel.getPrice());
                                cvPAyment.setOnClickListener(v -> {
                                    packageModel.setOgPrice(packageModel.getPrice());
                                    imgCancel.performClick();
                                    paymentAlert(packageModel, false, false);
                                });
                                subParentLayout.addView(rowView);
                            }
                        } else {
                            if (packageModel.getIsPackage().equalsIgnoreCase(API.ACTIVE)) {
                                tvSingleMovie.setText(sMovie.getsName() + " + " + packageModel.getPackage());
                                tv_single2d_movie.setText(packageModel.getDescription());
                                try {
                                    int packPrice = Integer.parseInt(packageModel.getPrice());
                                    packPrice = price + packPrice;
                                    packageModel.setOgPrice(packageModel.getPrice());
                                    packageModel.setPrice(String.valueOf(packPrice));
                                    btnSingleMovie.setText(packageModel.getPrice());
                                } catch (NumberFormatException e) {
                                    btnSingleMovie.setText(packageModel.getPrice());
                                }
                                cvPAyment.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        imgCancel.performClick();
                                        paymentAlert(packageModel, true, false);
                                    }
                                });
                                subParentLayout.addView(rowView);
                            } else {
                                tvSingleMovie.setText(sMovie.getsName());
                                rlPAyment.setBackground(ContextCompat.getDrawable(context,
                                    R.drawable.subscription_silver));
                                try {
                                    price = Integer.parseInt(packageModel.getiPriceWithPackage());
                                } catch (NumberFormatException e) {
                                    price = Integer.parseInt(packageModel.getPrice());

                                }
                                packageModel1 = packageModel;
                                tv_single2d_movie.setText(packageModel.getDescription());

                                btnSingleMovie.setText(packageModel.getPrice());
                                cvPAyment.setOnClickListener(v -> {
                                    imgCancel.performClick();
                                    packageModel.setOgPrice(packageModel.getPrice());
                                    paymentAlert(packageModel, false, false);
                                });
                                subParentLayout.addView(rowView);
                            }

                        }
                    }
                }
            }
        }
    }

    private void addBundlePackageDetails() {
        if (bundlePackageResponseModel != null && bundlePackageResponseModel.getBundlePackageList() != null && bundlePackageResponseModel.getBundlePackageList()
            .size() > 0) {
            for (int q = 0; q < bundlePackageResponseModel.getBundlePackageList().size(); q++) {
                BundlePackage packageModel = bundlePackageResponseModel.getBundlePackageList().get(q);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.payment_card_bundle, null);
                TextView btnSingleMovie = rowView.findViewById(R.id.btnSingleMovie);
                TextView tvSingleMovie = rowView.findViewById(R.id.tvSingleMovie);
                TextView tv_single2d_movie = rowView.findViewById(R.id.tv_single2d_movie);
                TextView btnBuy = rowView.findViewById(R.id.btnBuy);
                CardView cvPAyment = rowView.findViewById(R.id.cvPAyment);
                RelativeLayout rlPAyment = rowView.findViewById(R.id.rlPAyment);
                LinearLayout llParent = rowView.findViewById(R.id.llParent);

                if (sMovie.getsAllowedInPackage().equalsIgnoreCase(API.ACTIVE)) {
                    tvSingleMovie.setText(packageModel.getBundlePackage());
                    tv_single2d_movie.setText(packageModel.getDescription());
                    btnSingleMovie.setText(packageModel.getAndroidPrice());
                    rlPAyment.setOnClickListener(v -> {
                        PackageModel packageModelNew = convertBundlePackagetoPackage(packageModel);
                        imgCancel.performClick();
                        paymentAlert(packageModelNew, false, true);
                    });
                    for (int i = 0; i < packageModel.getVideoList().size(); i++) {
                        BundleMovies movies = packageModel.getVideoList().get(i);
                        final View cardView = inflater.inflate(R.layout.bundle_movie_card, null);
                        ImageView imgMovieBanner = cardView.findViewById(R.id.imgMovieBanner);
                        if (movies.getsBigBanner() != null && !movies.getsBigBanner().isEmpty()) {
                            Glide.with(imgMovieBanner.getContext())
                                .load(movies.getsBigBanner()).centerCrop()
                                .into(imgMovieBanner);
                        } else {
                            Glide.with(imgMovieBanner.getContext()).load(R.drawable.not_img).centerCrop()
                                .into(imgMovieBanner);
                        }
                        float paddingDp = 5f;
                        int paddingPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            paddingDp,
                            context.getResources().getDisplayMetrics());
                        cardView.setPadding(paddingPx, 0, paddingPx, 0);
                        llParent.addView(cardView);
                    }
                    subParentLayout.addView(rowView);
                }
            }
        }

    }

    private PackageModel convertBundlePackagetoPackage(BundlePackage bundlePackage) {
        PackageModel packageModel = new PackageModel();
        packageModel.setPrice(bundlePackage.getAndroidPrice());
        packageModel.setOgPrice(bundlePackage.getAndroidPrice());
        packageModel.setSCode(bundlePackage.getsCode());
        packageModel.setId(bundlePackage.getBundlePackagesId());
        packageModel.setDescription(bundlePackage.getDescription());
        packageModel.setPackage(bundlePackage.getBundlePackage());
        packageModel.setValidityInDays(bundlePackage.getAndroidValidityInDays());
        packageModel.setVideoList(bundlePackage.getVideoList());
        return packageModel;
    }

    private void recordScreenView() {
        mFirebaseAnalytics.setCurrentScreen(this, null, null);// [END set_current_screen]
        Log.d("Message", "Track Screen Subscription");
    }

    private void refreshAd() {

        if (SaveSharedPreference.getAdsData(this) != null) {
            FrameLayout frameLayout = findViewById(R.id.fl_adplaceholder_top);
            if (SaveSharedPreference.getAdsData(this).getGStatus() != null && SaveSharedPreference.getAdsData(this)
                .getGStatus()
                .equals(API.ACTIVE)) {

                AdLoader adLoader = new AdLoader.Builder(this,
                    SaveSharedPreference.getAdsData(this)
                        .getGNative()).forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                        LinearLayout adView_top = (LinearLayout) getLayoutInflater().inflate(R.layout.ad_unified, null);
                        TemplateView template = adView_top.findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView_top);
                    }
                }).build();
                adLoader.loadAd(new AdRequest.Builder().build());

            } else if (SaveSharedPreference.getAdsData(this) != null) {
                if (SaveSharedPreference.getAdsData(this).getMopubStatus() != null && SaveSharedPreference.getAdsData(
                    this).getMopubStatus().equals(API.ACTIVE)) {

                    AdLoader adLoader = new AdLoader.Builder(this,
                        SaveSharedPreference.getAdsData(this)
                            .getMopubNative()).forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                            LinearLayout adView_top = (LinearLayout) getLayoutInflater().inflate(R.layout.ad_unified,
                                null);
                            TemplateView template = adView_top.findViewById(R.id.my_template);
                            template.setStyles(styles);
                            template.setNativeAd(nativeAd);
                            frameLayout.removeAllViews();
                            frameLayout.addView(template);
                        }
                    }).build();
                    adLoader.loadAd(new AdManagerAdRequest.Builder().build());
                }
            }
        }
    }

    //    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
    //        // Set the media view.
    //        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
    //        adView.setBodyView(adView.findViewById(R.id.ad_body));
    //        if (nativeAd.getBody() == null) {
    //            adView.getBodyView().setVisibility(View.INVISIBLE);
    //        } else {
    //            adView.getBodyView().setVisibility(View.VISIBLE);
    //            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
    //        }
    //        adView.setNativeAd(nativeAd);
    //        VideoController vc = nativeAd.getVideoController();
    //
    //        if (vc.hasVideoContent()) {
    //            Log.e("Message","video status : "+String.format(Locale.getDefault(),
    //                    "Video status: Ad contains a %.2f:1 video asset.",
    //                    vc.getAspectRatio()));
    //
    //            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
    //                @Override
    //                public void onVideoEnd() {
    //                    Log.e("Message","Video status: Video playback has ended.");
    //                    super.onVideoEnd();
    //                }
    //            });
    //        } else {
    //            Log.e("Message","Video status: Ad does not contain a video asset.");
    //        }
    //    }

    private void paymentAlert(PackageModel PackageModel, boolean isDoubleSubs, boolean isMovieBundle) {
        Intent intent = new Intent(Subscription.this, PaymentActivity.class);
        intent.putExtra(PACKAGE, PackageModel);
        intent.putExtra(MOVIE, sMovie);
        intent.putExtra(ISDOUBLESUBS, isDoubleSubs);
        intent.putExtra(ISMOVIESBUNDLE, isMovieBundle);
        intent.putExtra(PACKAGE1, packageModel1);
        startActivity(intent);
    }

    @Override
    public void finishActivity(Activity activity) {
        if (!activity.isDestroyed()) {
            activity.finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, Info.class);
        intent.putExtra("movie", sMovie);
        startActivity(intent);
        finish();
    }

    private void sendSubscription() {
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<AnalyticsResponseModel> call = webApi.sendsubscriptionAnalytics(SaveSharedPreference.getUserId(context),
            sMovie.getsVedioId());
        call.enqueue(new Callback<AnalyticsResponseModel>() {
            @Override
            public void onResponse(Call<AnalyticsResponseModel> call, Response<AnalyticsResponseModel> response) {

            }

            @Override
            public void onFailure(Call<AnalyticsResponseModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    private void showFABMenu() {
        isFABOpen = true;
        fab1.animate().translationY(getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(getResources().getDimension(R.dimen.standard_105));
        fab3.animate().translationY(getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
    }

    private void setPaymentAudio() {
        SplashResponseModel.Promoflash splashData = SaveSharedPreference.getSplashData(context);
        if (splashData != null && splashData.getEnableSubscritionFlashVideo() != null && splashData.getEnableSubscritionFlashVideo()
            .equalsIgnoreCase(API.ACTIVE)) {
            mp = new MediaPlayer();
            try {
                mp.setDataSource(splashData.getSubscriptionAudioUrl());//Write your location here
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        fab1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.exo_icon_play));
                        stopAnimation();
                    }
                });
                mp.prepare();
                mp.start();
                startAnimation();
                fab3.setOnClickListener(v -> {
                    mp.seekTo(0);
                    mp.pause();
                    fab1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.exo_icon_play));
                    stopAnimation();
                });
                fab2.setOnClickListener(v -> {
                    mp.seekTo(0);
                    mp.start();
                    fab1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.exo_icon_pause));
                    startAnimation();
                });
                fab1.setOnClickListener(v -> {
                    if (mp.isPlaying()) {
                        mp.pause();
                        fab1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.exo_icon_play));
                        stopAnimation();
                    } else {
                        mp.start();
                        fab1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.exo_icon_pause));
                        startAnimation();
                    }
                });

            } catch (Exception e) {e.printStackTrace();}
        } else {
            fab.hide();
            fab1.hide();
            fab2.hide();
            fab3.hide();
            img_close.setVisibility(View.GONE);
            img_help.setVisibility(View.GONE);
            tvHelp.setVisibility(View.GONE);
        }
    }

    private void startAnimation() {
        rotate = ObjectAnimator.ofFloat(fab,
            "rotation",
            0f,
            20f,
            0f,
            -20f,
            0f); // rotate o degree then 20 degree and so on for one loop of rotation.
        rotate.setRepeatCount(100); // repeat the loop 20 times
        rotate.setDuration(100); // animation play time 100 ms
        rotate.start();
    }

    private void stopAnimation() {
        if (rotate != null) {
            rotate.cancel();
        }
    }

    @Override
    protected void onPause() {
        if (mp != null) {
            mp.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mp != null) {
            mp.start();
        }
    }

    @Override
    protected void onDestroy() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;

        }
        super.onDestroy();
    }
}


