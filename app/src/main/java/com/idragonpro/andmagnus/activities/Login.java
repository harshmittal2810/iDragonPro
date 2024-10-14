package com.idragonpro.andmagnus.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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

public class Login extends AppCompatActivity {

    private static final String TAG = Login.class.getSimpleName();
    private static final int RESOLVE_HINT = 100;
    EditText etUsername;
    Button btnLogin;
    Context context;
    TextView tvSignUp, tvSignUpHindi;
    Dialog dialog;
    ImageView imgLogo;
    LinearLayout logoView;
    private FirebaseAnalytics mFirebaseAnalytics;
    RelativeLayout parentLayout;
    //By Archana : Banner
    private AdView adView;
    AdRequest adRequest;
    String sFrom1;
    private LinearLayout llRegister, llSkip, llRegisterHindi;
    private TextView tvSkip;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //By Archana : google Banner
        adView = findViewById(R.id.ad_view_login);
        //  AdSize adSize = new AdSize(300,90);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        sFrom1 = getIntent().getStringExtra("from");
        Log.d("Message", "sFor login : " + sFrom1);

        //By Bushra: Google analytics
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        context = this;
        logoView = findViewById(R.id.logoView);
        imgLogo = findViewById(R.id.imgLogo);
        parentLayout = findViewById(R.id.parentLayout);
        llRegister = findViewById(R.id.llRegister);
        llRegisterHindi = findViewById(R.id.llRegisterHindi);
        llSkip = findViewById(R.id.llSkip);
        tvSkip = findViewById(R.id.tvSkip);
        imgBack = findViewById(R.id.imgBack);
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, com.idragonpro.andmagnus.activities.Home.class);
                startActivity(intent);
                finish();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recordScreenView();

        if (SaveSharedPreference.getLoginFromGoogle(context)) {
            logoView.setVisibility(View.GONE);
            imgLogo.setVisibility(View.GONE);
            if (GlobalModule.startActivity != null && GlobalModule.startActivity.equals(com.idragonpro.andmagnus.activities.GoogleSignupActivity.class.getSimpleName())) {
                llSkip.setVisibility(View.GONE);
                llRegister.setVisibility(View.GONE);
                llRegisterHindi.setVisibility(View.GONE);
            } else {
                llSkip.setVisibility(View.GONE);
                llRegister.setVisibility(View.VISIBLE);
                llRegisterHindi.setVisibility(View.GONE);
            }

        } else {
            logoView.setVisibility(View.VISIBLE);
            imgLogo.setVisibility(View.VISIBLE);
        }
        etUsername = findViewById(R.id.etUsername);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUpHindi = findViewById(R.id.tvSignUpHindi);

        btnLogin.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "LoginActivity");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            if (etUsername.getText().toString().isEmpty()) {
                Toast.makeText(context, "Enter Mobile No", Toast.LENGTH_SHORT).show();
            } else {
                dialog = GlobalModule.showProgressDialog("Authenticating...", context);
                logoView.setVisibility(View.VISIBLE);
                imgLogo.setVisibility(View.VISIBLE);
                callLoginOrRegister();
            }

        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, com.idragonpro.andmagnus.activities.Register.class);
                startActivity(intent);
                finish();
            }
        });
        tvSignUpHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, com.idragonpro.andmagnus.activities.Register.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void recordScreenView() {
        mFirebaseAnalytics.setCurrentScreen(this, null, null);// [END set_current_screen]
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    private void callLoginOrRegister() {
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<ProfileResponseModel> call = webApi.loginORRegister(SaveSharedPreference.getUserId(context),
            etUsername.getText().toString(),
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
                        Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                } else {
                    Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileResponseModel> call, Throwable t) {
                Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
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
                                if (GlobalModule.startActivity.equals(com.idragonpro.andmagnus.activities.GoogleSignupActivity.class.getSimpleName())) {
                                    GlobalModule.startActivity = Login.class.getSimpleName();
                                    Intent intent = new Intent(context, com.idragonpro.andmagnus.activities.Home.class);
                                    intent.putExtra("from", "login");
                                    startActivity(intent);
                                    finish();
                                } else {
                                    GlobalModule.startActivity = "";
                                    finish();
                                }
                            } else {
                                showMultiLoginDialog();
                            }
                        } else {
                            Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();
                }
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponseModel> call, Throwable t) {
                Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
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
        adExp.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                adExp.dismiss();
                Login.this.finish();
            }
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
        //            etUsername.setFocusable(true);
        //            etUsername.setFocusableInTouchMode(true);
        //            etUsername.setClickable(true);
        //        }
    }

    public String parsePhoneNumber(String s) {
        String Trimmed = s.trim();
        if (Trimmed.length() > 10) {
            char[] number = Trimmed.toCharArray();
            int extra;
            int dif = Trimmed.length() - 10;
            for (int i = dif; i < Trimmed.length(); i++) {
                extra = i - dif;
                number[extra] = number[i];
            }
            for (int i = 10; i < Trimmed.length(); i++) {
                number[i] = ' ';
            }
            return String.valueOf(number);
        }
        return Trimmed;
    }
}
