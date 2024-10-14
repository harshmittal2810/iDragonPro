package com.idragonpro.andmagnus.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {
    public static final String BASE_NEW_URL = "https://idragonpro.com/idragon/api/v.08.2021/";
    //    public static final String BASE_NEW_URL = "https://cwm.idragonpro.com/idragon/api/v.08.2021/";
    public static final String RETROFIT_BASE_NEW_URL = "https://api.razorpay.com/";
    public static final String HOME_ACTION = "get_home_banners";
    public static final String WEB_SERIES_ACTION = "get_series_banners";
    public static final String MOVIES_ACTION = "get_movie_banners";
    public static final String KIDS_ACTION = "get_kids_banners";
    public static final String ADS_ACTION = "getAdsDetails";
    public static final String ACTIVE = "0";
    public static final String VIEW_ALL_ACTION = "viewAll";
    public static final String REVIEW_ACTION = "getcomments";
    public static final String USER_ROLE = "2";
    public static final String SUCCESSFULL_STATUS = "True";
    public static final String ADD_DEVICE = "add";
    public static final String DELETE_DEVICE = "delete";
    public static final String VIDEO_ANALYTICS_VISIT = "visit";
    public static final String VIDEO_ANALYTICS_VIEW = "view";
    public static final String VIDEO_ANALYTICS_PLAY = "play";
    public static final String VIDEO_ANALYTICS_TRAILER = "trailer";
    public static final String PRE_ROLL_1 = "pre_roll_1";
    public static final String PRE_ROLL_2 = "pre_roll_2";
    public static final String PRE_ROLL_3 = "pre_roll_3";
    public static final String MID_ROLL_1 = "mid_roll_1";
    public static final String MID_ROLL_2 = "mid_roll_2";
    public static final String MID_ROLL_3 = "mid_roll_3";
    public static final String MID_ROLL_4 = "mid_roll_4";
    public static final String SKIP_1 = "skip_1";
    public static final String SKIP_2 = "skip_2";
    public static final String VERTICAL = "Vertical";
    public static final String COMMING_SOON = "Comming Soon";
    public static final String TOP_RIGHT = "Top Right";
    public static final String TOP_LEFT = "Top Left";
    public static final String PROMO = "Promo";




        public static Retrofit retrofit = null;
        public static Retrofit getRetrofit() {
            if (retrofit == null) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                        .connectTimeout(100, TimeUnit.SECONDS)
                        .readTimeout(100, TimeUnit.SECONDS).build();
                retrofit = new Retrofit.Builder().baseUrl(BASE_NEW_URL)
                        .addConverterFactory(GsonConverterFactory.create()).client(client).build();
            }
            return retrofit;
        }


}
