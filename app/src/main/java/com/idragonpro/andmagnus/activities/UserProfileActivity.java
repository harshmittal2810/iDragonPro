package com.idragonpro.andmagnus.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.idragonpro.andmagnus.MyApp;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.api.WebApi;
import com.idragonpro.andmagnus.helpers.GlobalModule;
import com.idragonpro.andmagnus.helpers.LocaleHelper;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;
import com.idragonpro.andmagnus.responseModels.AnalyticsResponseModel;
import com.idragonpro.andmagnus.utility.DeviceUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button editButton;
    private ImageView imageViewBackArrow;
    private TextView textViewUserName;
    private ImageView circleImageView;
    private Button buttonLogout;
    private Context context;
    private ImageView imageViewSettings;
    private RelativeLayout rltLytVipLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setContext();

        initViews();
        setClickListeners();
    }

    private void setContext() {
        context = this;
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        textViewUserName.setText(SaveSharedPreference.getFirstName(context) + " " + SaveSharedPreference.getLastName(
            context));

        SaveSharedPreference.getProfilePic(UserProfileActivity.this);
        if (!SaveSharedPreference.getProfilePic(UserProfileActivity.this).isEmpty()) {
            Glide.with(circleImageView.getContext())
                .load(SaveSharedPreference.getProfilePic(UserProfileActivity.this))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_profile_placeholder)
                .into(circleImageView);
        }

    }

    private void setClickListeners() {
        editButton.setOnClickListener(this);
        imageViewBackArrow.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);
        imageViewSettings.setOnClickListener(this);
        rltLytVipLogin.setOnClickListener(this);
    }

    private void initViews() {
        editButton = findViewById(R.id.editButton);
        imageViewBackArrow = findViewById(R.id.imageViewBackArrow);
        textViewUserName = findViewById(R.id.textViewUserName);
        circleImageView = findViewById(R.id.circleImageView);
        buttonLogout = findViewById(R.id.buttonLogout);
        imageViewSettings = findViewById(R.id.imageViewSettings);
        rltLytVipLogin = findViewById(R.id.rltLytVipLogin);
        if (SaveSharedPreference.getLoginFromGoogle(context)) {
            rltLytVipLogin.setVisibility(View.VISIBLE);
        } else {
            rltLytVipLogin.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editButton:
                startActivity(new Intent(this, EditProfileActivity.class));
                break;
            case R.id.imageViewBackArrow:
                onBackPressed();
                break;
            case R.id.buttonLogout:
                userLogout();
                break;
            case R.id.imageViewSettings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.rltLytVipLogin:
                GlobalModule.startActivity = GoogleSignupActivity.class.getSimpleName();
                startActivity(new Intent(this, Login.class));
                break;
        }
    }

    private void userLogout() {
        final AlertDialog adExp = new AlertDialog.Builder(this).create();
        adExp.setMessage("Are you sure you want to Logout?");
        adExp.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                validate_device_access_info();
                SaveSharedPreference.clearOnLogout(context);

                Intent intent = new Intent(context, GoogleSignupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        adExp.setButton(DialogInterface.BUTTON_NEGATIVE, "No", (dialog, which) -> adExp.dismiss());
        adExp.show();
    }

    private void validate_device_access_info() {
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<AnalyticsResponseModel> call = webApi.validate_device_access_info(SaveSharedPreference.getUserId(context),
            DeviceUtility.getDeviceName(),
            "",
            API.DELETE_DEVICE);
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
    protected void onResume() {
        super.onResume();
        setData();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}