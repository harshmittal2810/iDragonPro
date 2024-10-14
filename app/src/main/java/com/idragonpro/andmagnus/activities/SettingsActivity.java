package com.idragonpro.andmagnus.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.idragonpro.andmagnus.MyApp;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.api.WebApi;
import com.idragonpro.andmagnus.helpers.GlobalModule;
import com.idragonpro.andmagnus.helpers.LocaleHelper;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;
import com.idragonpro.andmagnus.responseModels.ContactResponseModel;
import com.idragonpro.andmagnus.responseModels.GeneralResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageViewBackArrow;
    private Button buttonSubscriptionPlan, buttonSubscriptionPlanWeb;
    private ConstraintLayout constraintLayoutHelpFaq;
    private ConstraintLayout constraintPrivacyPolicy;
    private TextView textViewEmailValue;
    private TextView textViewContactValue;
    private TextView textViewCustomerSupportValue;
    private ConstraintLayout constraintLayoutEmail;
    private ConstraintLayout constraintLayoutContact;
    private ConstraintLayout constraintLayoutCustomerSupport;
    private Button buttonChangeLanguage;
    private TextView textViewValidTill, textViewValidTillWeb;
    private TextView textViewCurrentLanguage;
    private SwitchCompat swOnOff;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        setSwitchCompat();
        setClickListeners();
        setSubscriptions();
        getContactDetails();
        setCurrentLanguage();
    }

    private void setSwitchCompat() {
        swOnOff.setSwitchTextAppearance(this, R.style.SwitchTextAppearance);

        if (SaveSharedPreference.getNotification(SettingsActivity.this) == null || SaveSharedPreference.getNotification(
            SettingsActivity.this).equals("1")) swOnOff.setChecked(true);
        else swOnOff.setChecked(false);

        swOnOff.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                updateNotificationStatus(1);
            } else {
                updateNotificationStatus(0);
            }
        });
    }

    private void setCurrentLanguage() {
        if (SaveSharedPreference.getLanguage(this, "en").equalsIgnoreCase("en")) {
            textViewCurrentLanguage.setText(getString(R.string.english));
        } else if (SaveSharedPreference.getLanguage(this, "en").equalsIgnoreCase("hi")) {
            textViewCurrentLanguage.setText(getString(R.string.hindi));
        } else if (SaveSharedPreference.getLanguage(this, "en").equalsIgnoreCase("mr")) {
            textViewCurrentLanguage.setText(getString(R.string.marathi));
        } else if (SaveSharedPreference.getLanguage(this, "en").equalsIgnoreCase("pa")) {
            textViewCurrentLanguage.setText(getString(R.string.punjabi));
        } else if (SaveSharedPreference.getLanguage(this, "en").equalsIgnoreCase("te")) {
            textViewCurrentLanguage.setText(getString(R.string.telugu));
        } else if (SaveSharedPreference.getLanguage(this, "en").equalsIgnoreCase("ta")) {
            textViewCurrentLanguage.setText(getString(R.string.tamil));
        } else if (SaveSharedPreference.getLanguage(this, "en").equalsIgnoreCase("kn")) {
            textViewCurrentLanguage.setText(getString(R.string.kannada));
        }
    }

    private void setSubscriptions() {
        if (!SaveSharedPreference.getLoginFromGoogle(this)) {
            String remDaysWeb = "";
            if (SaveSharedPreference.getWebRemDays(SettingsActivity.this) == 0 && SaveSharedPreference.getWebTimeDiff(
                SettingsActivity.this) > 0) {
                remDaysWeb = getString(R.string.expires_today);
            } else {
                remDaysWeb = String.valueOf(SaveSharedPreference.getWebRemDays(SettingsActivity.this));
            }
            if (SaveSharedPreference.getWebRemDays(SettingsActivity.this) > 0 || SaveSharedPreference.getWebTimeDiff(
                SettingsActivity.this) > 0) {
                buttonSubscriptionPlanWeb.setText(SaveSharedPreference.getWebSubTitle(SettingsActivity.this)
                    .split("-")[0]);
                if (SaveSharedPreference.getWebRemDays(SettingsActivity.this) == 0) {
                    textViewValidTillWeb.setText(remDaysWeb);
                } else {
                    textViewValidTillWeb.setText(getString(R.string.vaild_till) + " " + remDaysWeb + " " + getString(R.string.days));
                }

            } else {
                buttonSubscriptionPlanWeb.setText(getString(R.string.no_active_subscription));
            }
            String remDays = "";
            if (SaveSharedPreference.getRemDays(SettingsActivity.this) == 0 && SaveSharedPreference.getTimeDiff(
                SettingsActivity.this) > 0) {
                remDays = getString(R.string.expires_today);
            } else {
                remDays = String.valueOf(SaveSharedPreference.getRemDays(SettingsActivity.this));
            }
            if (SaveSharedPreference.getRemDays(SettingsActivity.this) > 0 || SaveSharedPreference.getTimeDiff(
                SettingsActivity.this) > 0) {
                buttonSubscriptionPlan.setText(SaveSharedPreference.getSubTitle(SettingsActivity.this).split("-")[0]);
                if (SaveSharedPreference.getRemDays(SettingsActivity.this) == 0) {
                    textViewValidTill.setText(remDays);
                } else {
                    textViewValidTill.setText(getString(R.string.vaild_till) + " " + remDays + " " + getString(R.string.days));
                }

            } else {
                buttonSubscriptionPlan.setText(getString(R.string.no_active_subscription));
            }
        } else {
            buttonSubscriptionPlan.setText(getString(R.string.no_active_subscription));
            buttonSubscriptionPlanWeb.setText(getString(R.string.no_active_subscription));
        }
    }

    private void setClickListeners() {
        imageViewBackArrow.setOnClickListener(this);
        constraintLayoutHelpFaq.setOnClickListener(this);
        constraintPrivacyPolicy.setOnClickListener(this);
        buttonChangeLanguage.setOnClickListener(this);
    }

    private void initViews() {
        imageViewBackArrow = findViewById(R.id.imageViewBackArrow);
        buttonSubscriptionPlan = findViewById(R.id.buttonSubscriptionPlan);
        buttonSubscriptionPlanWeb = findViewById(R.id.buttonSubscriptionPlanWeb);
        constraintLayoutHelpFaq = findViewById(R.id.constraintLayoutHelpFaq);
        constraintPrivacyPolicy = findViewById(R.id.constraintPrivacyPolicy);
        textViewEmailValue = findViewById(R.id.textViewEmailValue);
        textViewContactValue = findViewById(R.id.textViewContactValue);
        textViewCustomerSupportValue = findViewById(R.id.textViewCustomerSupportValue);
        constraintLayoutEmail = findViewById(R.id.constraintLayoutEmail);
        constraintLayoutContact = findViewById(R.id.constraintLayoutContact);
        constraintLayoutCustomerSupport = findViewById(R.id.constraintLayoutCustomerSupport);
        buttonChangeLanguage = findViewById(R.id.buttonChangeLanguage);
        textViewValidTill = findViewById(R.id.textViewValidTill);
        textViewValidTillWeb = findViewById(R.id.textViewValidTillWeb);
        textViewCurrentLanguage = findViewById(R.id.textViewCurrentLanguage);
        swOnOff = findViewById(R.id.swOnOff);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewBackArrow:
                onBackPressed();
                break;
            case R.id.constraintLayoutHelpFaq:
                startHelpAndFaq();
                break;
            case R.id.constraintPrivacyPolicy:
                startPrivacyPolicy();
                break;
            case R.id.buttonChangeLanguage:
                showLanguageScreen();
                break;
        }
    }

    private void showLanguageScreen() {
        startActivity(new Intent(this, LanguageActivity.class));
    }

    private void startHelpAndFaq() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://idragonpro.com/webapp/policy.html"));
        startActivity(browserIntent);
    }

    private void startPrivacyPolicy() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://idragonpro.com/webapp/policy.html"));
        startActivity(browserIntent);
    }

    private void getContactDetails() {
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<ContactResponseModel> call = webApi.getContactDetails();
        call.enqueue(new Callback<ContactResponseModel>() {
            @Override
            public void onResponse(Call<ContactResponseModel> call, Response<ContactResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getContact() != null) {
                        ContactResponseModel.Contact contactDetails = response.body().getContact();
                        if (contactDetails.getEmailFlag() == null || !contactDetails.getEmailFlag().equals("1")) {
                            constraintLayoutEmail.setVisibility(View.GONE);
                        } else {
                            constraintLayoutEmail.setVisibility(View.VISIBLE);
                            textViewEmailValue.setText(contactDetails.getEmail());
                        }

                        if (contactDetails.getContactFlag() == null || !contactDetails.getContactFlag().equals("1")) {
                            constraintLayoutContact.setVisibility(View.GONE);
                        } else {
                            constraintLayoutContact.setVisibility(View.VISIBLE);
                            textViewContactValue.setText(contactDetails.getContact());
                        }
                        if (contactDetails.getCustomerSupportFlag() == null || !contactDetails.getCustomerSupportFlag()
                            .equals("1")) {
                            constraintLayoutCustomerSupport.setVisibility(View.GONE);
                        } else {
                            constraintLayoutCustomerSupport.setVisibility(View.VISIBLE);
                            textViewCustomerSupportValue.setText(contactDetails.getCustomerSupport());
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ContactResponseModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    private void updateNotificationStatus(int notifcationStatus) {
        dialog = GlobalModule.showProgressDialog("Updating...", this);
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<GeneralResponseModel> call = webApi.updateNotificationStatus(SaveSharedPreference.getUserId(
            SettingsActivity.this), String.valueOf(notifcationStatus));
        call.enqueue(new Callback<GeneralResponseModel>() {
            @Override
            public void onResponse(Call<GeneralResponseModel> call, Response<GeneralResponseModel> response) {
                if (response.body() != null) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    SaveSharedPreference.setNotification(SettingsActivity.this, String.valueOf(notifcationStatus));
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<GeneralResponseModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Notification Updated Failed", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }
}