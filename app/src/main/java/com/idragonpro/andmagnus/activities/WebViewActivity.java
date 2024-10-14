package com.idragonpro.andmagnus.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.idragonpro.andmagnus.MyApp;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.api.WebApi;
import com.idragonpro.andmagnus.beans.BundleMovies;
import com.idragonpro.andmagnus.beans.Movies;
import com.idragonpro.andmagnus.beans.PackageModel;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;
import com.idragonpro.andmagnus.responseModels.AnalyticsResponseModel;
import com.idragonpro.andmagnus.responseModels.GeneralResponseModel;
import com.idragonpro.andmagnus.responseModels.SubscriptionResponseModel;
import com.idragonpro.andmagnus.utility.AvenuesParams;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebViewActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    Intent mainIntent;
    String sOrderid, sAmount, sReceiptNo, apiKey;
    Context context;
    Movies sMovie;
    private FirebaseAnalytics mFirebaseAnalytics;
    private PackageModel packageModel, packageModel1;
    private Boolean isDoubleSubs, isMovieBundle;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mainIntent = getIntent();

        context = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        sMovie = (Movies) getIntent().getSerializableExtra("movie");
        packageModel = (PackageModel) getIntent().getSerializableExtra("package");
        packageModel1 = (PackageModel) getIntent().getSerializableExtra("package1");
        isDoubleSubs = (Boolean) getIntent().getSerializableExtra("isDoubleSubs");
        isMovieBundle = (Boolean) getIntent().getSerializableExtra("isMovieBundle");
        sAmount = getIntent().getStringExtra(AvenuesParams.AMOUNT);
        sOrderid = getIntent().getStringExtra(AvenuesParams.ORDER_ID);
        apiKey = getIntent().getStringExtra(AvenuesParams.API_KEY);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        sendPaymentPageAnalytics("RazorPay", "click");
        recordScreenView();
        startPayment();
    }

    private void recordScreenView() {
        try {
            mFirebaseAnalytics.setCurrentScreen(this, null, null);// [END set_current_screen]
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startPayment() {
        Checkout checkout = new Checkout();
        int image = R.drawable.logo; // Can be any drawable
        checkout.setKeyID(apiKey);
        checkout.setImage(image);
        checkout.setFullScreenDisable(true);
        checkout.setImage(R.drawable.logo);
        final Activity activity = (Activity) context;
        try {
            JSONObject options = new JSONObject();
            options.put("name", "iDragon");
            options.put("description", "Order #" + sOrderid);
            options.put("currency", "INR");
            options.put("prefill.email", SaveSharedPreference.getEmail(context));
            options.put("prefill.contact", SaveSharedPreference.getMobileNumber(context));
            options.put("amount", Integer.parseInt(sAmount) * 100);
            checkout.open(activity, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendPaymentDetails(String paymentData) {
        progressDialog.show();
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<SubscriptionResponseModel> call = webApi.addSubscriptionDetails(SaveSharedPreference.getUserId(context),
            sMovie.getsVedioId(),
            packageModel.getOgPrice(),
            sOrderid,
            sReceiptNo,
            packageModel.getSCode(),
            packageModel.getId(),
            "1",
            paymentData);
        call.enqueue(new Callback<SubscriptionResponseModel>() {
            @Override
            public void onResponse(Call<SubscriptionResponseModel> call, Response<SubscriptionResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getSubscription() != null) {
                        if (isDoubleSubs) {
                            if (sMovie.getsType() != null && sMovie.getsType().equals("5")) {
                                SaveSharedPreference.setWebRemDays(context, packageModel.getValidityInDays());
                                SaveSharedPreference.setWebSubTitle(context, packageModel.getPackage());
                            } else {
                                SaveSharedPreference.setRemDays(context, packageModel.getValidityInDays());
                                SaveSharedPreference.setSubTitle(context, packageModel.getPackage());
                            }
                            sMovie.setDaysdiff(packageModel1.getValidityInDays());
                        } else {
                            if (packageModel.getIsPackage().equalsIgnoreCase(API.ACTIVE)) {
                                if (sMovie.getsType() != null && sMovie.getsType().equals("5")) {
                                    SaveSharedPreference.setWebRemDays(context, packageModel.getValidityInDays());
                                    SaveSharedPreference.setWebSubTitle(context, packageModel.getPackage());
                                } else {
                                    SaveSharedPreference.setRemDays(context, packageModel.getValidityInDays());
                                    SaveSharedPreference.setSubTitle(context, packageModel.getPackage());
                                }
                            } else {
                                sMovie.setDaysdiff(packageModel.getValidityInDays());
                            }
                        }
                        Intent intent = new Intent(context, Info.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("movie", sMovie);
                        intent.putExtra("isAfterPayment", true);
                        startActivity(intent);
                    }
                }
                if (!WebViewActivity.this.isFinishing()) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<SubscriptionResponseModel> call, Throwable t) {
                t.printStackTrace();
                if (!WebViewActivity.this.isFinishing()) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    private void sendPaymentDetails1(String paymentData) {
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<SubscriptionResponseModel> call = webApi.addSubscriptionDetails(SaveSharedPreference.getUserId(context),
            sMovie.getsVedioId(),
            packageModel1.getPrice(),
            sOrderid,
            sReceiptNo,
            packageModel1.getSCode(),
            packageModel1.getId(),
            "1",
            paymentData);
        call.enqueue(new Callback<SubscriptionResponseModel>() {
            @Override
            public void onResponse(Call<SubscriptionResponseModel> call, Response<SubscriptionResponseModel> response) {
                if (response.body() != null) {

                }
            }

            @Override
            public void onFailure(Call<SubscriptionResponseModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Toast.makeText(context, "Payment Successful.", Toast.LENGTH_SHORT).show();
        sendPaymentPageAnalytics("RazorPay", "Success");
        sReceiptNo = paymentData.getPaymentId();
        if (isMovieBundle) {
            sendMovieBundleDetails(paymentData.getData().toString());
        } else {
            if (isDoubleSubs) {
                sendPaymentDetails1(paymentData.getData().toString());
            }
            sendPaymentDetails(paymentData.getData().toString());
        }
        Checkout.clearUserData(context);

    }

    private void sendMovieBundleDetails(String paymentData) {
        List<String> bundleMovies = new ArrayList<>();
        List<BundleMovies> videoList = packageModel.getVideoList();
        if (videoList != null) {
            for (int i = 0; i < videoList.size(); i++) {
                bundleMovies.add(videoList.get(i).getsVedioId());
            }
        }
        progressDialog.show();
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<GeneralResponseModel> call = webApi.sendBundlePackageDetails(SaveSharedPreference.getUserId(context),
            null,
            packageModel.getOgPrice(),
            sOrderid,
            sReceiptNo,
            null,
            null,
            "1",
            paymentData,
            "0",
            packageModel.getId(),
            packageModel.getSCode(),
            bundleMovies.toString()

        );
        call.enqueue(new Callback<GeneralResponseModel>() {
            @Override
            public void onResponse(Call<GeneralResponseModel> call, Response<GeneralResponseModel> response) {
                if (response.body() != null && response.body().getStatus() != null) {
                    if (response.body().getStatus().equalsIgnoreCase(API.SUCCESSFULL_STATUS)) {
                        sMovie.setDaysdiff(packageModel.getValidityInDays());
                        Intent intent = new Intent(context, Info.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("movie", sMovie);
                        intent.putExtra("isAfterPayment", true);
                        startActivity(intent);
                    }
                }
                if (!WebViewActivity.this.isFinishing()) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<GeneralResponseModel> call, Throwable t) {
                t.printStackTrace();
                if (!WebViewActivity.this.isFinishing()) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.d("WebView", "Payment Error " + s + "\n" + paymentData);
        sendPaymentPageAnalytics("RazorPay", "Failure");
        Toast.makeText(context, "Payment was not Successful.", Toast.LENGTH_SHORT).show();
        Checkout.clearUserData(context);
        finish();

    }

    private void sendPaymentPageAnalytics(String paymentMethod, String paymentStatus) {
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<AnalyticsResponseModel> call = webApi.sendpaymentclickAnalytics(SaveSharedPreference.getUserId(context),
            sMovie.getsVedioId(),
            packageModel.getId(),
            packageModel.getPrice(),
            paymentMethod,
            paymentStatus);
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
}
