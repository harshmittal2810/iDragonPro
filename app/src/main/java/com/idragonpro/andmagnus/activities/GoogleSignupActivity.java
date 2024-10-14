package com.idragonpro.andmagnus.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.idragonpro.andmagnus.MyApp;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.api.WebApi;
import com.idragonpro.andmagnus.helpers.GlobalModule;
import com.idragonpro.andmagnus.helpers.LocaleHelper;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;
import com.idragonpro.andmagnus.responseModels.NewRegisterResponseModel;
import com.idragonpro.andmagnus.responseModels.RegisterResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleSignupActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = GoogleSignupActivity.class.getSimpleName();
    public static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private Dialog dialog;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_signup);
        context = this;
        createGoogleClient();
        signOut();
        initComponents();

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAd();
    }

    private void signOut() {
        mGoogleSignInClient.signOut();
    }

    private void createGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void initComponents() {
        Button signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signInButton) {
            dialog = GlobalModule.showProgressDialog("Logging In...", context);
            startGoogleSignIn();
        }
    }

    private void startGoogleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            showFailureMsg();
            hideProgressBar();
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null && account.getEmail() != null && !account.getEmail().isEmpty() && !account.getEmail()
                .equalsIgnoreCase("null")) {
            callRegister(account.getGivenName(), account.getFamilyName(), account.getEmail(), account.getPhotoUrl());
            //            FirebaseAuth.getInstance().signOut();
        } else {
            hideProgressBar();
            showFailureMsg();
        }
    }

    private void showFailureMsg() {
        Toast.makeText(this, R.string.login_failed, Toast.LENGTH_LONG).show();
    }

    private void startLanguageActivity() {
        Intent intent = new Intent(GoogleSignupActivity.this, Home.class);
        startActivity(intent);
        finish();
    }

    private void callRegister(String fName, String lName, String email, Uri photoUrl) {
        String imageUrl = null;
        if (photoUrl != null) {
            imageUrl = photoUrl.toString();
        }

        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<NewRegisterResponseModel> call = webApi.register(API.USER_ROLE,
                fName,
                lName, email, imageUrl, MyApp.Companion.getInstance().getVersionCode());
        call.enqueue(new Callback<NewRegisterResponseModel>() {
            @Override
            public void onResponse(Call<NewRegisterResponseModel> call, Response<NewRegisterResponseModel> response) {
                if (response.body() != null && response.body().getRegisterResponseModel() != null) {
                    setLoginDetails(response.body().getRegisterResponseModel());
                    startLanguageActivity();
                    hideProgressBar();
                } else {
                    showFailureMsg();
                    hideProgressBar();
                }

            }

            @Override
            public void onFailure(Call<NewRegisterResponseModel> call, Throwable t) {
                t.printStackTrace();
                showFailureMsg();
                hideProgressBar();
            }
        });
    }

    private void setLoginDetails(RegisterResponseModel registerResponseModel) {
        SaveSharedPreference.setUserId(this, registerResponseModel.getUserId());
        SaveSharedPreference.setFirstName(this, registerResponseModel.getSFirstName());
        SaveSharedPreference.setLastName(this, registerResponseModel.getSLastName());
        SaveSharedPreference.setEmail(this, registerResponseModel.getSEmail());
        SaveSharedPreference.setLoginFromGoogle(this, true);
    }

    private void startLoginActivity() {
        GlobalModule.startActivity = GoogleSignupActivity.class.getSimpleName();
        Intent intent = new Intent(GoogleSignupActivity.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void hideProgressBar() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
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
