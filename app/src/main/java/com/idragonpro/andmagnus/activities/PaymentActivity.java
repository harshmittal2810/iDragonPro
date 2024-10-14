package com.idragonpro.andmagnus.activities;

import static com.pokkt.PokktAds.Debugging.showToast;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.CFSession;
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback;
import com.cashfree.pg.core.api.exception.CFException;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.cashfree.pg.ui.api.upi.intent.CFIntentTheme;
import com.cashfree.pg.ui.api.upi.intent.CFUPIIntentCheckout;
import com.cashfree.pg.ui.api.upi.intent.CFUPIIntentCheckoutPayment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.idragonpro.andmagnus.MyApp;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.api.RetrofitWebApi;
import com.idragonpro.andmagnus.api.WebApi;
import com.idragonpro.andmagnus.beans.BundleMovies;
import com.idragonpro.andmagnus.beans.Movies;
import com.idragonpro.andmagnus.beans.PackageModel;
import com.idragonpro.andmagnus.helpers.LocaleHelper;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;
import com.idragonpro.andmagnus.models.GatewayOrderStatus;
import com.idragonpro.andmagnus.models.GetOrderIDRequest;
import com.idragonpro.andmagnus.models.GetOrderIDResponse;
import com.idragonpro.andmagnus.models.GetPaymentGatewayDetails;
import com.idragonpro.andmagnus.models.GetPaymentGatewayResponse;
import com.idragonpro.andmagnus.models.GetRazorOrderIDRequest;
import com.idragonpro.andmagnus.responseModels.AnalyticsResponseModel;
import com.idragonpro.andmagnus.responseModels.GeneralResponseModel;
import com.idragonpro.andmagnus.responseModels.RazorOrderIdResponse;
import com.idragonpro.andmagnus.responseModels.SplashResponseModel;
import com.idragonpro.andmagnus.responseModels.SubscriptionResponseModel;
import com.idragonpro.andmagnus.responseModels.TokenResponseModel;
import com.idragonpro.andmagnus.utility.AvenuesParams;
import com.idragonpro.andmagnus.utility.ServiceUtility;
import com.razorpay.Checkout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements CFCheckoutResponseCallback {
    private static final String TAG = PaymentActivity.class.getSimpleName();
    Movies sMovie;
    Context context;
    private FirebaseAnalytics mFirebaseAnalytics;
    private PackageModel packageModel, packageModel1;
    private boolean isDoubleSubs, isMovieBundle;
    private final String UPI_ORDER_ID = "";
    private String CASHFREE_TOKEN = "";
    private String CASHFREE_ORDER_ID = "";
    private String INSTAMOJO_ORDER_ID;
    private ConstraintLayout instamojoPayment, pakistanNepalBangladeshPayment;
    private View cashFreePayment, razorPayLayout;
    private LinearLayout upiPayment;
    private LinearLayout qrPayment, bankQrPayment;
    private ImageView payTmPayment, gPayPayment, phonePayPayment;
    private ProgressDialog progressDialog;
    private AlertDialog dialog;

    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    final int UPI_PAYMENT = 0;
    private String razorPayAPIKey = "rzp_live_VMWhnq2Lcuw2RF";
    private String razorPaySecretAPIKey = "HFoai1Nj0DnoG55xp7DNg2co";

    private boolean isFABOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3;
    private MediaPlayer mp;
    private ImageView img_help, img_close;
    private TextView tvHelp;
    private ObjectAnimator rotate;

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected() && netInfo.isConnectedOrConnecting() && netInfo.isAvailable();
        }
        return false;
    }

    private void payUsingUpi() {
        String orderAmount = ServiceUtility.chkNull(packageModel.getPrice()).toString().trim();
        Uri uri = new Uri.Builder().scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", "9987714307@paytm")
                .appendQueryParameter("pn", "Aeon Pictures")
                .appendQueryParameter("mc", "pSBFVR92990626709285")
                .appendQueryParameter("tr", "12345")
                .appendQueryParameter("tn", "iDragon")
                .appendQueryParameter("am", "10")
                .appendQueryParameter("cu", "INR")
                .appendQueryParameter("mcc", "pSBFVR92990626709285")
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage("net.one97.paytm");
        Intent chooser = Intent.createChooser(intent, "Pay with");
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }

        //        Uri uri = Uri.parse("upi://pay").buildUpon()
        //                .appendQueryParameter("pa", "9987714307@paytm")
        //                .appendQueryParameter("pn", "Aeon Pictures")
        //                .appendQueryParameter("tn", "iDragon")
        //                .appendQueryParameter("am", String.valueOf(Double.parseDouble(orderAmount)))
        //                .appendQueryParameter("cu", "INR")
        //                .build();
        //        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        //        upiPayIntent.setData(uri);
        //        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        //        if(null != chooser.resolveActivity(getPackageManager())) {
        //            startActivityForResult(chooser, UPI_PAYMENT);
        //        } else {
        //            Toast.makeText(this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        //        }
    }

    private void goToUpiAppsActivity(PackageModel PackageModel, boolean isDoubleSubs) {
        Intent intent = new Intent(PaymentActivity.this, UpiAppsActivity.class);
        intent.putExtra("package", PackageModel);
        intent.putExtra("movie", sMovie);
        intent.putExtra("isDoubleSubs", isDoubleSubs);
        intent.putExtra("package1", packageModel1);
        startActivity(intent);
    }

    private void goToQrCodeActivity(PackageModel PackageModel, boolean isDoubleSubs) {
        Intent intent = new Intent(PaymentActivity.this, QRCodeActivity.class);
        intent.putExtra("package", PackageModel);
        intent.putExtra("movie", sMovie);
        intent.putExtra("isDoubleSubs", isDoubleSubs);
        intent.putExtra("package1", packageModel1);
        startActivity(intent);
    }

    private void UpiPaymentStart() {
/*        getInputParams();
        String orderAmount = ServiceUtility.chkNull(packageModel.getPrice()).toString().trim();

        String customerName = SaveSharedPreference.getFirstName(context) + SaveSharedPreference.getLastName(context);
        String customerPhone = SaveSharedPreference.getMobileNumber(context) + "@upi";
        UPI_ORDER_ID = new Date().getTime() + "" + SaveSharedPreference.getUserId(context);

        UPIPayment upiPayment = new UPIPayment.Builder()
                .with(this)
                .setPayeeVpa(customerPhone)
                .setPayeeName(customerName)
                .setTransactionId(UPI_ORDER_ID)
                .setTransactionRefId(UPI_ORDER_ID)
                .setDescription("Process the payment")
                .setAmount(String.valueOf(Double.parseDouble(orderAmount)))
                .build();

        upiPayment.setPaymentStatusListener(this);

        if (upiPayment.isDefaultAppExist()) {
            onAppNotFound();
            return;
        }
        upiPayment.startPayment();*/
    }

    private void getPaymentGatewayList() {
        progressDialog.show();
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<GetPaymentGatewayResponse> call = webApi.getPaymentGatewayList();
        call.enqueue(new Callback<GetPaymentGatewayResponse>() {
            @Override
            public void onResponse(Call<GetPaymentGatewayResponse> call, Response<GetPaymentGatewayResponse> response) {
                if (response.body() != null) {
                    progressDialog.hide();
                    if (response.body().getPaymentGateways() != null) {
                        setPaymentOptionVisibility(response.body().getPaymentGateways());
                    }
                } else {
                    progressDialog.hide();
                }
            }

            @Override
            public void onFailure(Call<GetPaymentGatewayResponse> call, Throwable t) {
                t.printStackTrace();
                progressDialog.hide();
            }
        });
    }

    private List<GetPaymentGatewayDetails> filterList(List<GetPaymentGatewayDetails> originalList, String targetValue) {
        List<GetPaymentGatewayDetails> filteredList = new ArrayList<>();
        for (GetPaymentGatewayDetails item : originalList) {
            String itemValue = item.getPgName();
            if (itemValue.equalsIgnoreCase(targetValue) && item.getIsActive() == 0) {
                filteredList.add(item);
                break;
            }
        }
        return filteredList;
    }

    private void setPaymentOptionVisibility(List<GetPaymentGatewayDetails> paymentGateways) {
        for (int i = 0; i < paymentGateways.size(); i++) {

            if (!filterList(paymentGateways, "Razorpay").isEmpty()) {
                GetPaymentGatewayDetails getPaymentGatewayDetails = filterList(paymentGateways, "Razorpay").get(0);
                razorPayLayout.setVisibility(View.VISIBLE);
                razorPayAPIKey = getPaymentGatewayDetails.getApiKey();
                razorPaySecretAPIKey = getPaymentGatewayDetails.getApiSecretKey();
            } else {
                razorPayLayout.setVisibility(View.GONE);
            }

            if (!filterList(paymentGateways, "Cashfree").isEmpty()) {
                cashFreePayment.setVisibility(View.VISIBLE);
            } else {
                cashFreePayment.setVisibility(View.GONE);
            }

            if (!filterList(paymentGateways, "QR Payment").isEmpty()) {
                qrPayment.setVisibility(View.VISIBLE);
            } else {
                qrPayment.setVisibility(View.GONE);
            }

            payTmPayment.setVisibility(View.GONE);
            instamojoPayment.setVisibility(View.GONE);
            gPayPayment.setVisibility(View.GONE);
            pakistanNepalBangladeshPayment.setVisibility(View.GONE);
        }
    }

    /*private void initPaymentCashfree() {
        sendPaymentPageAnalytics("Cashfree", "click");
        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);
        cfPaymentService.doPayment(PaymentActivity.this, getInputParams(), CASHFREE_TOKEN, "PROD", "#784BD2", "#FFFFFF", false);
    }*/

    /*private Map<String, String> getInputParams() {
        String appId = "114227c348a88472bed694aba4722411";
        String orderId = CASHFREE_ORDER_ID;
        String orderAmount = ServiceUtility.chkNull(packageModel.getPrice()).toString().trim();
        String customerName = SaveSharedPreference.getFirstName(context) + SaveSharedPreference.getLastName(context);
        String customerPhone = SaveSharedPreference.getMobileNumber(context);
        String customerEmail = SaveSharedPreference.getEmail(context);

        Map<String, String> params = new HashMap<>();

        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmount);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        params.put(PARAM_ORDER_CURRENCY, "INR");
        return params;
    }*/

    CFSession.Environment cfEnvironment = CFSession.Environment.PRODUCTION;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment);

        context = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        sMovie = (Movies) getIntent().getSerializableExtra(Subscription.MOVIE);
        packageModel = (PackageModel) getIntent().getSerializableExtra(Subscription.PACKAGE);
        packageModel1 = (PackageModel) getIntent().getSerializableExtra(Subscription.PACKAGE1);
        isDoubleSubs = (Boolean) getIntent().getSerializableExtra(Subscription.ISDOUBLESUBS);
        isMovieBundle = (Boolean) getIntent().getSerializableExtra(Subscription.ISMOVIESBUNDLE);

        razorPayLayout = findViewById(R.id.razorPayLayout);
        cashFreePayment = findViewById(R.id.cashFreeLayout);
        payTmPayment = findViewById(R.id.imageViewPaytmUpi);
        gPayPayment = findViewById(R.id.imageViewGpay);
        phonePayPayment = findViewById(R.id.imageViewPhonePay);
        instamojoPayment = findViewById(R.id.instamojoConstraintLayout);
        qrPayment = findViewById(R.id.qr_code);
        bankQrPayment = findViewById(R.id.bank_qr_code);
        upiPayment = findViewById(R.id.upiPaymentCardView1);
        pakistanNepalBangladeshPayment = findViewById(R.id.pakistanNepalBangladeshPaymentConstraintLayout);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        getPaymentGatewayList();
        recordScreenView();
        razorPayLayout.setOnClickListener(v -> {
            initOrderPayment();
        });

        cashFreePayment.setOnClickListener(v -> getPaymentToken());
        instamojoPayment.setOnClickListener(v -> createOrderOnServer());
        sendPaymentPageAnalytics();

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

        upiPayment.setOnClickListener(view -> payUsingUpi());
        qrPayment.setOnClickListener(view -> {
            Intent intent = new Intent(PaymentActivity.this, UPIQrCodeScannerActivity.class);
            startActivity(intent);
        });

        bankQrPayment.setOnClickListener(view -> {
            Intent intent = new Intent(PaymentActivity.this, UPIQrCodeScannerActivity.class);
            intent.putExtra("isForBank", true);
            startActivity(intent);
        });

        try {
            // If you are using a fragment then you need to add this line inside onCreate() of your Fragment
            CFPaymentGatewayService.getInstance().setCheckoutCallback(this);
        } catch (CFException e) {
            e.printStackTrace();
        }

        Checkout.preload(getApplicationContext());
//        bankQrPayment.setVisibility(View.GONE);
    }

    private void initOrderPayment() {

        RetrofitWebApi webApi = MyApp.Companion.getInstance().createRazorAppRetrofitNewInstance();

        GetRazorOrderIDRequest getRazorOrderIDRequest = new GetRazorOrderIDRequest();

        String price = ServiceUtility.chkNull(packageModel.getPrice()).toString().trim();
        getRazorOrderIDRequest.setCurrency("INR");
        getRazorOrderIDRequest.setAmount("" + Integer.parseInt(price) * 100);
        getRazorOrderIDRequest.setReceipt("Order #" + (new Date().getTime() + SaveSharedPreference.getUserId(context)).trim());

        String razorAuth = razorPayAPIKey + ":" + razorPaySecretAPIKey;
        String encodedString = Base64.encodeToString(razorAuth.getBytes(), Base64.DEFAULT);

        String authKey = "Basic " + encodedString.trim();
        Call<RazorOrderIdResponse> call = webApi.createRetrofitOrder(authKey, getRazorOrderIDRequest);
        call.enqueue(new Callback<RazorOrderIdResponse>() {
            @Override
            public void onResponse(Call<RazorOrderIdResponse> call, Response<RazorOrderIdResponse> response) {

                if (response.body() != null) {
                    if (response.body().getId() != null) {
                        initPayment(response.body().getId());
                    }
                }

                if (!PaymentActivity.this.isFinishing()) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<RazorOrderIdResponse> call, Throwable t) {
                t.printStackTrace();
                if (!PaymentActivity.this.isFinishing()) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    private void getPaymentToken() {
        progressDialog.show();
        CASHFREE_ORDER_ID = new Date().getTime() + SaveSharedPreference.getUserId(context);
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<TokenResponseModel> call = webApi.generate_cashfree_token(CASHFREE_ORDER_ID,
                ServiceUtility.chkNull(packageModel.getPrice()).toString().trim(),
                "INR");
        call.enqueue(new Callback<TokenResponseModel>() {
            @Override
            public void onResponse(Call<TokenResponseModel> call, Response<TokenResponseModel> response) {
                if (response != null && response.body() != null) {
                    CASHFREE_TOKEN = response.body().getToken();
                    doWebCheckoutPayment();
                    //                    initPaymentCashfree();
                }
                if (!PaymentActivity.this.isFinishing()) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.hide();
                    }
                }
            }

            @Override
            public void onFailure(Call<TokenResponseModel> call, Throwable t) {
                t.printStackTrace();
                if (!PaymentActivity.this.isFinishing()) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.hide();
                    }
                }
            }
        });
    }

    public void doWebCheckoutPayment() {
        sendPaymentPageAnalytics("CashFree", "click");
        String appId = "114227c348a88472bed694aba4722411";
        String orderId = CASHFREE_ORDER_ID;
        String orderAmount = ServiceUtility.chkNull(packageModel.getPrice()).toString().trim();
        String customerName = SaveSharedPreference.getFirstName(context) + SaveSharedPreference.getLastName(context);
        String customerPhone = SaveSharedPreference.getMobileNumber(context);
        String customerEmail = SaveSharedPreference.getEmail(context);

        try {
            CFSession cfSession = new CFSession.CFSessionBuilder().setEnvironment(cfEnvironment)
                    .setPaymentSessionID(CASHFREE_TOKEN)
                    .setOrderId(CASHFREE_ORDER_ID)
                    .build();

            // Replace with your application's theme colors
            CFIntentTheme cfTheme = new CFIntentTheme.CFIntentThemeBuilder().setBackgroundColor("#ffffff")
                    .setPrimaryTextColor("#784BD2")
                    .build();

            CFUPIIntentCheckout cfupiIntentCheckout = new CFUPIIntentCheckout.CFUPIIntentBuilder()
                    // Use either the enum or the application package names to order the UPI apps as per your needed
                    // Remove both if you want to use the default order which cashfree provides based on the popularity
                    // NOTE - only one is needed setOrder or setOrderUsingPackageName
                    .setOrder(Arrays.asList(CFUPIIntentCheckout.CFUPIApps.BHIM, CFUPIIntentCheckout.CFUPIApps.PHONEPE))
                    .setOrderUsingPackageName(Arrays.asList("com.dreamplug.androidapp", "in.org.npci.upiapp"))
                    .build();

            CFUPIIntentCheckoutPayment cfupiIntentCheckoutPayment = new CFUPIIntentCheckoutPayment.CFUPIIntentPaymentBuilder().setSession(
                    cfSession).setCfUPIIntentCheckout(cfupiIntentCheckout).setCfIntentTheme(cfTheme).build();

            CFPaymentGatewayService.getInstance().doPayment(PaymentActivity.this, cfupiIntentCheckoutPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }
    }

    private void recordScreenView() {
        mFirebaseAnalytics.setCurrentScreen(this, null, null);// [END set_current_screen]
    }

    private void createOrderOnServer() {
        progressDialog.show();
        String orderAmount = ServiceUtility.chkNull(packageModel.getPrice()).toString().trim();
        String customerName = SaveSharedPreference.getFirstName(context) + SaveSharedPreference.getLastName(context);
        String customerPhone = SaveSharedPreference.getMobileNumber(context);
        String customerEmail = SaveSharedPreference.getEmail(context);

        GetOrderIDRequest request = new GetOrderIDRequest();

        request.setBuyerName(customerName);
        request.setBuyerEmail(customerEmail);
        request.setBuyerPhone(customerPhone);
        request.setDescription("");
        request.setAmount(orderAmount);

        INSTAMOJO_ORDER_ID = new Date().getTime() + SaveSharedPreference.getUserId(context);
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<GetOrderIDResponse> call = webApi.createOrder(request);
        call.enqueue(new Callback<GetOrderIDResponse>() {
            @Override
            public void onResponse(Call<GetOrderIDResponse> call, Response<GetOrderIDResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String orderId = response.body().getOrderId();
                    // Initiate the default SDK-provided payment activity

                } else {
                    // Handle api errors
                    Log.d(TAG, "Error in response" + response.errorBody().toString());

                }
                if (!PaymentActivity.this.isFinishing()) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.hide();
                    }
                }

            }

            @Override
            public void onFailure(Call<GetOrderIDResponse> call, Throwable t) {
                // Handle call failure
                Log.d(TAG, "Failure");
                if (!PaymentActivity.this.isFinishing()) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.hide();
                    }
                }
            }
        });

    }

    private void sendPaymentPageAnalytics() {
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<AnalyticsResponseModel> call = webApi.sendpaymentpageAnalytics(SaveSharedPreference.getUserId(context),
                sMovie.getsVedioId(),
                packageModel.getId(),
                packageModel.getPrice());
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

    private void showFABMenu() {
        isFABOpen = true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
    }

    private void setPaymentAudio() {
        SplashResponseModel.Promoflash splashData = SaveSharedPreference.getSplashData(context);
        if (splashData != null && splashData.getEnablePaymentFlashVideo() != null && splashData.getEnablePaymentFlashVideo()
                .equalsIgnoreCase(API.ACTIVE)) {
            mp = new MediaPlayer();
            try {
                mp.setDataSource(splashData.getPaymentAudioUrl());//Write your location here
                mp.setOnCompletionListener(mp -> {
                    fab1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.exo_icon_play));
                    stopAnimation();
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

            } catch (Exception e) {
                e.printStackTrace();
            }
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //upiOpenMethod(requestCode,resultCode,data);
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        //Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        //Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }

        /*if (data != null && requestCode == CFPaymentService.REQ_CODE) {
            Bundle bundle = data.getExtras();
            JSONObject json = new JSONObject();
            String status = "";
            if (bundle != null) for (String key : bundle.keySet()) {
                if (bundle.getString(key) != null) {
                    if (key.equalsIgnoreCase("txStatus")) {
                        status = bundle.getString(key);
                    }
                    try {
                        json.put(key, bundle.getString(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
            sendPaymentPageAnalytics("Cashfree", status);
            if (status.equalsIgnoreCase("success")) {
                if (isMovieBundle) {
                    sendMovieBundleDetails(json.toString());
                } else {
                    if (isDoubleSubs) {
                        sendPaymentDetails1(json.toString());
                    }
                    sendPaymentDetails(json.toString());
                }
            }
        }*/
    }

    private void upiOpenMethod(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        sendPaymentDetailsUpi(String.valueOf(dataList));
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        sendPaymentDetailsUpi(String.valueOf(dataList));
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    sendPaymentDetailsUpi(String.valueOf(dataList));
                }
                break;
        }
    }

    private void sendPaymentDetailsUpi(String paymentData) {
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<SubscriptionResponseModel> call = webApi.addSubscriptionDetails(SaveSharedPreference.getUserId(context),
                sMovie.getsVedioId(),
                packageModel.getOgPrice(),
                "",
                "",
                packageModel.getSCode(),
                packageModel.getId(),
                "",
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

    /**
     * Will check for the transaction status of a particular Transaction
     *
     * @param transactionID Unique identifier of a transaction ID
     */
    private void checkPaymentStatus(final String transactionID, final String orderID) {
        if (transactionID == null && orderID == null) {
            return;
        }

        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }

        showToast(getApplicationContext(), "Checking transaction status");
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<GatewayOrderStatus> getOrderStatusCall = webApi.orderStatus("TEST", orderID, transactionID);
        getOrderStatusCall.enqueue(new retrofit2.Callback<GatewayOrderStatus>() {
            @Override
            public void onResponse(Call<GatewayOrderStatus> call, final Response<GatewayOrderStatus> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (response.isSuccessful()) {
                    GatewayOrderStatus orderStatus = response.body();
                    if (orderStatus.getStatus().equalsIgnoreCase("successful")) {
                        showToast(getApplicationContext(), "Transaction still pending");
                        return;
                    }

                    showToast(getApplicationContext(), "Transaction successful for id - " + orderStatus.getPaymentID());
                    //refundTheAmount(transactionID, orderStatus.getAmount());

                } else {
                    showToast(getApplicationContext(), "Error occurred while fetching transaction status");
                }
            }

            @Override
            public void onFailure(Call<GatewayOrderStatus> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        showToast(getApplicationContext(), "Failed to fetch the transaction status");
                    }
                });
            }
        });
    }

    private void sendPaymentDetails(String paymentData) {
        progressDialog.show();
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<SubscriptionResponseModel> call = webApi.addSubscriptionDetails(SaveSharedPreference.getUserId(context),
                sMovie.getsVedioId(),
                packageModel.getOgPrice(),
                CASHFREE_ORDER_ID,
                "",
                packageModel.getSCode(),
                packageModel.getId(),
                "3",
                paymentData

        );
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
                if (!PaymentActivity.this.isFinishing()) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<SubscriptionResponseModel> call, Throwable t) {
                t.printStackTrace();
                if (!PaymentActivity.this.isFinishing()) {
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
                CASHFREE_ORDER_ID,
                "",
                packageModel1.getSCode(),
                packageModel1.getId(),
                "3",
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
                CASHFREE_ORDER_ID,
                null,
                null,
                null,
                "3",
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
                if (!PaymentActivity.this.isFinishing()) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<GeneralResponseModel> call, Throwable t) {
                t.printStackTrace();
                if (!PaymentActivity.this.isFinishing()) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    public void initPayment(String orderId) {
        progressDialog.hide();
        /*Log.d(TAG, "Price:: " + ServiceUtility.chkNull(packageModel.getPrice()).toString().trim());
        Log.d(TAG, "Api key:: " + razorPayAPIKey);
        final Checkout checkout = new Checkout();
        try {
            checkout.setKeyID(ServiceUtility.chkNull(razorPayAPIKey).toString().trim());
            checkout.setFullScreenDisable(true);
            checkout.setImage(R.drawable.logo);

            JSONObject options = new JSONObject();
            options.put("name", "iDragon");
            options.put("order_id", orderId);
            options.put("description",
                "Order #" + (new Date().getTime() + SaveSharedPreference.getUserId(context)).trim());
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            options.put("currency", "INR");
            options.put("amount", ServiceUtility.chkNull(packageModel.getPrice()).toString().trim());

            JSONObject preFill = new JSONObject();
            preFill.put("email", SaveSharedPreference.getEmail(context));
            preFill.put("contact", SaveSharedPreference.getMobileNumber(context));

            options.put("prefill", preFill);

            checkout.open(this, options);
        } catch (Exception e) {
            Toast.makeText(this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*/

        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(AvenuesParams.API_KEY, ServiceUtility.chkNull(razorPayAPIKey).toString().trim());
        intent.putExtra(AvenuesParams.ORDER_ID, orderId);
        intent.putExtra(AvenuesParams.CURRENCY, "INR".trim());
        intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(packageModel.getPrice()).toString().trim());

        intent.putExtra(AvenuesParams.REDIRECT_URL, "https://idragonpro.com/android/ccavResponseHandler.php".trim());
        intent.putExtra(AvenuesParams.CANCEL_URL, "https://idragonpro.com/android/ccavResponseHandler.php".trim());
        intent.putExtra(AvenuesParams.RSA_KEY_URL, "https://idragonpro.com/android/GetRSA.php".trim());
        intent.putExtra("movie", sMovie);

        //By Bushra: to specify subscribe type
        intent.putExtra("subscribe_type", packageModel.getSCode());
        intent.putExtra("package", packageModel);
        intent.putExtra("isDoubleSubs", isDoubleSubs);
        intent.putExtra("package1", packageModel1);
        intent.putExtra("isMovieBundle", isMovieBundle);
        startActivity(intent);
        //        finish();
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(PaymentActivity.this)) {
            String str = data.get(0);
            //Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String[] response = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String[] equalStr = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].equalsIgnoreCase("Status")) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].equalsIgnoreCase("ApprovalRefNo") || equalStr[0].equalsIgnoreCase("txnRef")) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(PaymentActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                // Log.d("UPI", "responseStr: "+approvalRefNo);
                Toast.makeText(PaymentActivity.this,
                        "YOUR ORDER HAS BEEN PLACED\n THANK YOU AND ORDER AGAIN",
                        Toast.LENGTH_LONG).show();
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(PaymentActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PaymentActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PaymentActivity.this,
                    "Internet connection is not available. Please check and try again",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentVerify(String orderID) {
        Log.e("onPaymentVerify", "verifyPayment triggered");

    }

    @Override
    public void onPaymentFailure(CFErrorResponse cfErrorResponse, String orderID) {
        Log.e("onPaymentFailure " + orderID, cfErrorResponse.getMessage());

    }
}
