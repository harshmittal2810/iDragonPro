package com.idragonpro.andmagnus.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.idragonpro.andmagnus.MyApp;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.api.WebApi;
import com.idragonpro.andmagnus.helpers.GlobalModule;
import com.idragonpro.andmagnus.helpers.LocaleHelper;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;
import com.idragonpro.andmagnus.models.UserModel;
import com.idragonpro.andmagnus.responseModels.GeneralResponseModel;
import com.idragonpro.andmagnus.responseModels.ProfileResponseModel;
import com.idragonpro.andmagnus.utility.DeviceUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    private static final int RESOLVE_HINT = 100;
    Context context;
    EditText etMoblie;
    TextView tvTerms, tvLogin;
    Button btnRegister;
    Dialog progressDialog;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;

        etMoblie = findViewById(R.id.etMobile);
        tvTerms = findViewById(R.id.tvTerms);
        tvLogin = findViewById(R.id.tvLogin);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        recordScreenView();
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            if (etMoblie.getText().toString().isEmpty()) {
                Toast.makeText(context, "Enter Mobile No", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog = GlobalModule.showProgressDialog("Registering...", context);
                callLoginOrRegister();
            }
        });
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(context, Login.class);
            GlobalModule.startActivity = Register.class.getSimpleName();
            startActivity(intent);
            finish();
        });

        MobileAds.initialize(this, initializationStatus -> {
        });
        if (!GlobalModule.startActivity.equals(Info.class.getSimpleName())) {
            refreshAd();
        }
    }

    private void recordScreenView() {
        mFirebaseAnalytics.setCurrentScreen(this, null, null);// [END set_current_screen]
    }

    private void refreshAd() {
        if (SaveSharedPreference.getAdsData(this) != null) {
            FrameLayout frameLayout_top = findViewById(R.id.fl_adplaceholder_register);
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
            } else if (SaveSharedPreference.getAdsData(this) != null) {
                if (SaveSharedPreference.getAdsData(this).getMopubStatus() != null && SaveSharedPreference.getAdsData(
                        this).getMopubStatus().equals(API.ACTIVE)) {

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
    }

    private void callLoginOrRegister() {
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<ProfileResponseModel> call = webApi.loginORRegister(SaveSharedPreference.getUserId(context),
                etMoblie.getText().toString(),
                API.USER_ROLE,
                SaveSharedPreference.getFirstName(context),
                SaveSharedPreference.getLastName(context),
                SaveSharedPreference.getEmail(context),
                MyApp.Companion.getInstance().getVersionCode());
        call.enqueue(new Callback<ProfileResponseModel>() {
            @Override
            public void onResponse(Call<ProfileResponseModel> call, Response<ProfileResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getUser() != null) {
                        UserModel userModel = response.body().getUser();
                        callProfile(String.valueOf(userModel.getId()));
                    } else {
                        Toast.makeText(context, "Registration Failed", Toast.LENGTH_LONG).show();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                } else {
                    Toast.makeText(context, "Registration Failed", Toast.LENGTH_LONG).show();
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

            }

            @Override
            public void onFailure(Call<ProfileResponseModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Registration Failed", Toast.LENGTH_LONG).show();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void callProfile(String userId) {
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<ProfileResponseModel> call = webApi.getProfile(userId,
                DeviceUtility.getDeviceName(),
                SaveSharedPreference.getFbId(context),
                MyApp.Companion.getInstance().getVersionCode());
        call.enqueue(new Callback<ProfileResponseModel>() {
            @Override
            public void onResponse(Call<ProfileResponseModel> call, Response<ProfileResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getUser() != null) {
                        UserModel userModel = response.body().getUser();
                        if (userModel.getMobileno() != null && !userModel.getMobileno()
                                .isEmpty() && !userModel.getMobileno().equalsIgnoreCase("null")) {
                            if (response.body().isStatus()) {
                                SaveSharedPreference.setUserId(context, String.valueOf(userModel.getId()));
                                SaveSharedPreference.setFirstName(context, userModel.getName());
                                SaveSharedPreference.setLastName(context, userModel.getLastname());
                                SaveSharedPreference.setEmail(context, userModel.getEmail());
                                if (userModel.getMobileno() != null && !userModel.getMobileno()
                                        .isEmpty() && !userModel.getMobileno().equalsIgnoreCase("null")) {
                                    SaveSharedPreference.setMobileNumber(context, userModel.getMobileno());
                                }
                                if (userModel.getSubscriptions() != null && userModel.getSubscriptions()
                                        .getPackage() != null && !userModel.getSubscriptions()
                                        .getPackage()
                                        .isEmpty() && !userModel.getSubscriptions().getPackage().equalsIgnoreCase("null")) {
                                    SaveSharedPreference.setSubTitle(context,
                                            userModel.getSubscriptions().getPackage());
                                }
                                if (userModel.getDaysdiff() >= 0) {
                                    SaveSharedPreference.setRemDays(context, userModel.getDaysdiff());
                                }
                                if (userModel.getTimediff() >= 0) {
                                    SaveSharedPreference.setTimeDiff(context, userModel.getTimediff());
                                }
                                if (userModel.getSubscriptions_web() != null && userModel.getSubscriptions_web()
                                        .getPackage() != null && !userModel.getSubscriptions_web()
                                        .getPackage()
                                        .isEmpty() && !userModel.getSubscriptions_web()
                                        .getPackage()
                                        .equalsIgnoreCase("null")) {
                                    SaveSharedPreference.setWebSubTitle(context,
                                            userModel.getSubscriptions_web().getPackage());
                                }
                                if (userModel.getDaysdiff_web() >= 0) {
                                    SaveSharedPreference.setWebRemDays(context, userModel.getDaysdiff_web());
                                }
                                if (userModel.getTimediff_web() >= 0) {
                                    SaveSharedPreference.setWebTimeDiff(context, userModel.getTimediff_web());
                                }
                                if (SaveSharedPreference.getLoginFromGoogle(context)) {
                                    SaveSharedPreference.setLoginFromGoogle(context, false);
                                }
                                updateFirebaseId();
                                if (GlobalModule.startActivity.equals(Info.class.getSimpleName())) {
                                    GlobalModule.startActivity = "";
                                    finish();
                                } else if (GlobalModule.startActivity.equals(AllDownloadActivity.class.getSimpleName())) {
                                    GlobalModule.startActivity = "";
                                    finish();
                                } else {
                                    GlobalModule.startActivity = Login.class.getSimpleName();
                                    Intent intent = new Intent(context, Home.class);
                                    intent.putExtra("from", "login");
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                showMultiLoginDialog();
                            }
                        } else {
                            Toast.makeText(context, "Registration Failed", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Registration Failed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Registration Failed", Toast.LENGTH_LONG).show();
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponseModel> call, Throwable t) {
                Toast.makeText(context, "Registration Failed", Toast.LENGTH_LONG).show();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void updateFirebaseId() {
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<GeneralResponseModel> call = webApi.addFirebaseId(SaveSharedPreference.getUserId(context),
                SaveSharedPreference.getFbId(context));
        call.enqueue(new Callback<GeneralResponseModel>() {
            @Override
            public void onResponse(Call<GeneralResponseModel> call, Response<GeneralResponseModel> response) {

            }

            @Override
            public void onFailure(Call<GeneralResponseModel> call, Throwable t) {

            }
        });
    }

    private void showMultiLoginDialog() {
        final AlertDialog adExp = new AlertDialog.Builder(context).create();
        adExp.setMessage(getString(R.string.multi_login));
        adExp.setCancelable(false);
        adExp.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialog, which) -> {
            adExp.dismiss();
            Register.this.finish();
        });
        adExp.show();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //        if (requestCode == RESOLVE_HINT) {
        //            etMoblie.setFocusable(true);
        //            etMoblie.setFocusableInTouchMode(true);
        //            etMoblie.setClickable(true);
        //        }
    }
}
