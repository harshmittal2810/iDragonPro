package com.idragonpro.andmagnus.api;

import com.idragonpro.andmagnus.models.GatewayOrderStatus;
import com.idragonpro.andmagnus.models.GetOrderIDRequest;
import com.idragonpro.andmagnus.models.GetOrderIDResponse;
import com.idragonpro.andmagnus.models.GetPaymentGatewayResponse;
import com.idragonpro.andmagnus.models.pari_section_details_response.PariSectionDetailResponse;
import com.idragonpro.andmagnus.models.vipModel.Vip;
import com.idragonpro.andmagnus.responseModels.AddReviewResponseModel;
import com.idragonpro.andmagnus.responseModels.AdsResponseModel;
import com.idragonpro.andmagnus.responseModels.AnalyticsResponseModel;
import com.idragonpro.andmagnus.responseModels.ApkVersionResponseModel;
import com.idragonpro.andmagnus.responseModels.BundlePackageResponseModel;
import com.idragonpro.andmagnus.responseModels.ContactResponseModel;
import com.idragonpro.andmagnus.responseModels.GeneralResponseModel;
import com.idragonpro.andmagnus.responseModels.GetAllSingleMovieSubResponseModel;
import com.idragonpro.andmagnus.responseModels.HistoryResponseModel;
import com.idragonpro.andmagnus.responseModels.HomeResponseModel;
import com.idragonpro.andmagnus.responseModels.IPResponseModel;
import com.idragonpro.andmagnus.responseModels.NewRegisterResponseModel;
import com.idragonpro.andmagnus.responseModels.ProfileResponseModel;
import com.idragonpro.andmagnus.responseModels.ReviewResponseModels;
import com.idragonpro.andmagnus.responseModels.SearchResponseModel;
import com.idragonpro.andmagnus.responseModels.SplashResponseModel;
import com.idragonpro.andmagnus.responseModels.SubscriptionResponseModel;
import com.idragonpro.andmagnus.responseModels.TokenResponseModel;
import com.idragonpro.andmagnus.responseModels.VideoResponseModel;
import com.idragonpro.andmagnus.responseModels.ViewAllResponseModels;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebApi {

    @GET("promoflash")
    Call<SplashResponseModel> getSplashData();

    @POST("register")
    Call<NewRegisterResponseModel> register(@Query("role_id") String action, @Query("name") String fname, @Query("lastname") String lname, @Query("email") String email, @Query("profile_pic") String photoUrl, @Query("versionCode") int versionCode);

    @POST("userupdatebyid")
    Call<NewRegisterResponseModel> updateProfile(@Query("role_id") String action, @Query("name") String fname, @Query("lastname") String lname, @Query("id") String id);

    @POST("update_profile_pic")
    @FormUrlEncoded
    Call<ProfileResponseModel> updateProfileImage(@Field("id") String id, @Field("profile_pic") String profilePic);

    @POST("userinfobyid_new")
    Call<ProfileResponseModel> getProfile(@Query("id") String id, @Query("deviceId") String deviceName, @Query("firebaseId") String fbId, @Query("versionCode") int versionCode);

    @GET("{fullUrl}")
    Call<HomeResponseModel> getHomeData(@Path(value = "fullUrl", encoded = true) String fullUrl, @Query("versionCode") int versionCode);

    @GET("apkversion")
    Call<ApkVersionResponseModel> getAppVersion();

    @GET("adsdetails")
    Call<AdsResponseModel> getAdsData();

    @POST("videodata?")
    Call<ViewAllResponseModels> getViewAllData(@Query("Language") String iLanguageId, @Query("size") Integer size, @Query("page") Integer page);

    @POST("commentcreate")
    Call<AddReviewResponseModel> addReview(@Query("movie_id") String movie_id, @Query("user_id") String user_id, @Query("comment") String comment);

    @POST("getcomment")
    Call<ReviewResponseModels> getReviews(@Query("videoId") String iLanguageId, @Query("size") Integer size, @Query("page") Integer page);

    @POST("updatemobile")
    Call<ProfileResponseModel> loginORRegister(@Query("id") String id, @Query("mobileno") String mobileno, @Query("role_id") String role_id, @Query("name") String fname, @Query("lastname") String lname, @Query("email") String email, @Query("versionCode") int versionCode);

    @POST("getvideobyid_new")
    Call<VideoResponseModel> getMovie(@Query("id") String videoId, @Query("userid") String userid, @Query("versionCode") int versionCode);

    @POST("getpackages_payment")
    Call<Vip> vipUserPackage();

    @POST("wishlistcreate")
    Call<GeneralResponseModel> addWishList(@Query("iUserId") String iUserId, @Query("iVideoId") String iVideoId);

    @POST("watchlistcreate")
    Call<GeneralResponseModel> addHistory(@Query("iUserId") String iUserId, @Query("iVideoId") String iVideoId, @Query("sDuration") String sDuration);

    @POST("{fullUrl}")
    Call<HistoryResponseModel> getHistory(@Path(value = "fullUrl", encoded = true) String fullUrl, @Query("iUserId") String iUserId);

    @POST("allsubbyuid_new")
    Call<GetAllSingleMovieSubResponseModel> getAllSingleMovieSubscription(@Query("id") String iUserId);

    @POST("getsearchitems")
    Call<SearchResponseModel> getSearchItems(@Query("search") String searchKeyword);

    @GET("contactdetails")
    Call<ContactResponseModel> getContactDetails();

    @POST("subscriptioncreate_new")
    Call<SubscriptionResponseModel> addSubscriptionDetails(@Query("iUserId") String iUserId, @Query("iVideoId") String iVideoId, @Query("sAmount") String sAmount, @Query("orderid") String orderid, @Query("sRazorPayId") String sRazorPayId, @Query("sType") String sType, @Query("packageId") long id, @Query("pgId") String pgId, @Query("paymentParams") String paymentParams);

    @POST("bundle_pkg_subscription")
    Call<GeneralResponseModel> sendBundlePackageDetails(@Query("iUserId") String iUserId, @Query("iVideoId") String iVideoId, @Query("sAmount") String sAmount, @Query("orderid") String orderid, @Query("sRazorPayId") String sRazorPayId, @Query("sType") String sType, @Query("packageId") String id, @Query("pgId") String pgId, @Query("paymentParams") String paymentParams, @Query("isIOS") String isIOS, @Query("bundle_packages_id") long bundle_packages_id, @Query("sCode") String sCode, @Query("video_bundle") String video_bundle);

    @GET("json")
    Call<IPResponseModel> getIPDetails();

    @POST("subscriptionAnlyticsCreate")
    Call<AnalyticsResponseModel> sendsubscriptionAnalytics(@Query("userid") String userid, @Query("videoid") String videoid);

    @POST("visitPaymentPageAnlyticsCreate")
    Call<AnalyticsResponseModel> sendpaymentpageAnalytics(@Query("userid") String userid, @Query("videoid") String videoid, @Query("planid") long id, @Query("amount") String price);

    @POST("paymentClickAnlyticsCreate")
    Call<AnalyticsResponseModel> sendpaymentclickAnalytics(@Query("userid") String userid, @Query("videoid") String videoid, @Query("planid") long id, @Query("amount") String price, @Query("payment_gateway") String payment_gateway, @Query("payment_status") String payment_status);

    @POST("validate_device_access_info")
    Call<AnalyticsResponseModel> validate_device_access_info(@Query("userId") String userid, @Query("deviceId") String deviceId, @Query("videoId") String videoId, @Query("accessType") String price);

    @POST("video_analytics_create")
    Call<AnalyticsResponseModel> video_analytics_create(@Query("iUserId") String userid, @Query("iVideoId") String iVideoId, @Query("iActivity") String iActivity, @Query("iTimeInSeconds") long iTimeInSeconds, @Query("iIsPaidUser") String iIsPaidUser);

    @POST("google_ad_analytics_request")
    Call<AnalyticsResponseModel> google_ad_analytics_request(@Query("iUserId") String userid, @Query("iVideoId") String iVideoId, @Query("iGoogleAdId") String iGoogleAdId, @Query("iPosition") String iPosition);

    @POST("google_ad_analytics_response")
    Call<AnalyticsResponseModel> google_ad_analytics_response(@Query("iUserId") String userid, @Query("iVideoId") String iVideoId, @Query("iRequestId") String iRequestId);

    @POST("google_ad_analytics_load")
    Call<AnalyticsResponseModel> google_ad_analytics_load(@Query("iUserId") String userid, @Query("iVideoId") String iVideoId, @Query("iRequestId") String iRequestId);

    @POST("update_user_notification_status")
    Call<GeneralResponseModel> updateNotificationStatus(@Query("iUserId") String userid, @Query("IsNotificationActive") String isNotificationActive);

    @POST("splash_screen_analytics_create")
    Call<AnalyticsResponseModel> splash_screen_analytics_create(@Query("iUserId") String userid, @Query("iDeviceId") String iDeviceId);

    @GET("get_pari_section_setails")
    Call<PariSectionDetailResponse> getPariSectionDetails();

    @POST("generate_cashfree_token")
    Call<TokenResponseModel> generate_cashfree_token(@Query("orderId") String orderId, @Query("orderAmount") String orderAmount, @Query("orderCurrency") String orderCurrency);

    @POST("create_payment_request_im")
    Call<GetOrderIDResponse> createOrder(@Body GetOrderIDRequest request);

    @GET("get_payment_gateway_list")
    Call<GetPaymentGatewayResponse> getPaymentGatewayList();

    @GET("/status")
    Call<GatewayOrderStatus> orderStatus(@Query("env") String env, @Query("order_id") String orderID, @Query("transaction_id") String transactionID);

    @POST("ecom_analytics_request")
    Call<AnalyticsResponseModel> sendShopClickAnalytics(@Query("iUserId") String userid, @Query("iTask") String iTask);

    @POST("get_active_bundle_packages")
    Call<BundlePackageResponseModel> getBundlePackages(@Query("iVideo") String videId);

    @POST("add_active_user_data")
    Call<GeneralResponseModel> addFirebaseId(@Query("iUserId") String id, @Query("firebase_id") String fbId);

    @POST("promo_flash_analytics_range1")
    Call<AnalyticsResponseModel> promo_flash_analytics_range1(@Query("iUserId") String userid, @Query("iWatchTimeRange1") long iTimeInSeconds);

    @POST("promo_flash_analytics_range2")
    Call<AnalyticsResponseModel> promo_flash_analytics_range2(@Query("iUserId") String userid, @Query("iWatchTimeRange2") long iTimeInSeconds, @Query("iRequestId") String iRequestId);

    @POST("promo_flash_analytics_range3")
    Call<AnalyticsResponseModel> promo_flash_analytics_range3(@Query("iUserId") String userid, @Query("iWatchTimeRange3") long iTimeInSeconds, @Query("iRequestId") String iRequestId);
}
