package com.idragonpro.andmagnus.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.idragonpro.andmagnus.models.AdsData;
import com.idragonpro.andmagnus.responseModels.SplashResponseModel;

/**
 * Created by nitin on 13/1/17.
 */

public class SaveSharedPreference {
    static final String PREF_USER_ID = "userid";
    static final String PREF_USER_FIRST_NAME = "user_first_name";
    static final String PREF_USER_LAST_NAME = "user_last_name";
    static final String PREF_USER_EMAIL = "user_email";
    static final String PREF_USER_MOBILE_NUMBER = "user_mobile_number";
    static final String DEV_OPTIONS_ENABLED = "dev_options_enabled";
    static final String PREF_USER_COUNTRY = "user_country";
    static final String PREF_USER_PROFILE_PIC = "user_profile_pic";
    static final String PREF_USER_NOTIFICATION = "user_notification";
    static final String PREF_SUB_TITLE = "user_sub_title";
    static final String PREF_WEB_SUB_TITLE = "user_web_sub_title";
    static final String PREF_REM_DAYS = "rem_days";
    static final String PREF_TIME_DIFF = "time_diff";
    static final String PREF_WEB_REM_DAYS = "web_rem_days";
    static final String PREF_WEB_TIME_DIFF = "web_time_diff";
    static final String PREF_SESSION_USER_ID = "user_sessionid";
    static final String PREF_LOGIN_FROM_GOOGLE = "login_from_google";
    static final String ADS_CONTENT = "ads_content";
    static final String SPLASH_CONTENT = "splash_content";
    static final String SPLASH_CONTENT_ADS = "splash_content_ads";
    static final String APP_VERSION = "app_version";
    static final String FIREBASE_ID = "fb_id";
    static final String SECRET_KEY = "secret_key";
    static final String LANGUAGE = "language";
    static final String LANGUAGE_SET = "language_set";
    private static final String TAG = SaveSharedPreference.class.getSimpleName();

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }


    public static void setUserId(Context ctx, String userid) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, userid);
        editor.commit();
    }

    public static String getUserId(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
    }

    public static void setUserSessionId(Context ctx, String sessionid) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_SESSION_USER_ID, sessionid);
        editor.commit();
    }

    public static String getUserSessionId(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_SESSION_USER_ID, "");
    }

    public static void setLoginFromGoogle(Context ctx, boolean loginFromGoogle) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_LOGIN_FROM_GOOGLE, loginFromGoogle);
        editor.commit();
    }

    public static Boolean getLoginFromGoogle(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(PREF_LOGIN_FROM_GOOGLE, false);
    }

    public static void setAdsContent(Context ctx, String adsContent) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(ADS_CONTENT, adsContent);
        editor.commit();
    }

    public static AdsData getAdsData(Context ctx) {
        String ads = getSharedPreferences(ctx).getString(ADS_CONTENT, null);
        if (ads != null) {
            Gson g = new Gson();
            return g.fromJson(ads, AdsData.class);
        }
        return null;
    }

    public static void setSplashData(Context ctx, String splashContent) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(SPLASH_CONTENT, splashContent);
        editor.commit();
    }

    public static SplashResponseModel.Promoflash getSplashData(Context ctx) {
        String splashContent = getSharedPreferences(ctx).getString(SPLASH_CONTENT, null);
        if (splashContent != null) {
            Gson g = new Gson();
            return g.fromJson(splashContent, SplashResponseModel.Promoflash.class);
        }
        return null;
    }

    public static void setSplashAdsSettings(Context ctx, String splashContent) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(SPLASH_CONTENT_ADS, splashContent);
        editor.commit();
    }

    public static SplashResponseModel.PromoflashAnalyticsSetting getSplashAdsSettings(Context ctx) {
        String splashContent = getSharedPreferences(ctx).getString(SPLASH_CONTENT_ADS, null);
        if (splashContent != null) {
            Gson g = new Gson();
            return g.fromJson(splashContent, SplashResponseModel.PromoflashAnalyticsSetting.class);
        }
        return null;
    }

    public static void setAppVersion(Context ctx, String versionName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(APP_VERSION, versionName);
        editor.commit();
    }

    public static String getAppVersion(Context ctx) {
        //return "1.3.9";
        return getSharedPreferences(ctx).getString(APP_VERSION, "");
    }

    public static void clearOnLogout(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.apply();
    }

    public static void setFbId(Context ctx, String versionName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(FIREBASE_ID, versionName);
        editor.commit();
    }


    public static String getFbId(Context ctx) {
        return getSharedPreferences(ctx).getString(FIREBASE_ID, "");
    }

    public static void setLanguage(Context ctx, String language) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(LANGUAGE, language);
        editor.commit();
        setLanguageSet(ctx, true);
    }

    public static String getLanguage(Context ctx, String defaultLanguage) {
        return getSharedPreferences(ctx).getString(LANGUAGE, defaultLanguage);
    }

    public static void setLanguageSet(Context ctx, boolean isLanguageSet) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(LANGUAGE_SET, isLanguageSet);
        editor.commit();
    }

    public static Boolean isLanguageSet(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(LANGUAGE_SET, false);
    }

    public static void setFirstName(Context ctx, String firstName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_FIRST_NAME, firstName);
        editor.commit();
    }

    public static String getFirstName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_FIRST_NAME, "");
    }

    public static void setLastName(Context ctx, String lastName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_LAST_NAME, lastName);
        editor.commit();
    }

    public static String getLastName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_LAST_NAME, "");
    }

    public static void setEmail(Context ctx, String email) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_EMAIL, email);
        editor.commit();
    }

    public static String getEmail(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_EMAIL, "");
    }

    public static void setRemDays(Context ctx, long remDays) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putLong(PREF_REM_DAYS, remDays);
        editor.commit();
    }

    public static long getRemDays(Context ctx) {
        return getSharedPreferences(ctx).getLong(PREF_REM_DAYS, 0);
    }

    public static void setWebRemDays(Context ctx, long remDays) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putLong(PREF_WEB_REM_DAYS, remDays);
        editor.commit();
    }

    public static long getWebRemDays(Context ctx) {
        return getSharedPreferences(ctx).getLong(PREF_WEB_REM_DAYS, 0);
    }

    public static void setTimeDiff(Context ctx, long timeDiff) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putLong(PREF_TIME_DIFF, timeDiff);
        editor.commit();
    }

    public static long getTimeDiff(Context ctx) {
        return getSharedPreferences(ctx).getLong(PREF_TIME_DIFF, 0);
    }

    public static void setWebTimeDiff(Context ctx, long timeDiff) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putLong(PREF_WEB_TIME_DIFF, timeDiff);
        editor.commit();
    }

    public static long getWebTimeDiff(Context ctx) {
        return getSharedPreferences(ctx).getLong(PREF_WEB_TIME_DIFF, 0);
    }

    public static void setMobileNumber(Context ctx, String mobileNumber) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_MOBILE_NUMBER, mobileNumber);
        editor.commit();
    }

    public static String getMobileNumber(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_MOBILE_NUMBER, "");
    }

    //Enable this for disable developer option
    public static void setDevOptionsEnabled(Context ctx, boolean isDevOptionEnabled) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(DEV_OPTIONS_ENABLED, isDevOptionEnabled);
        editor.commit();
    }

    public static Boolean getDevOption(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(DEV_OPTIONS_ENABLED, false);
    }

    public static void setCountry(Context ctx, String country) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_COUNTRY, country);
        editor.commit();
    }

    public static String getCountry(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_COUNTRY, "");
    }

    public static void setProfilePic(Context ctx, String profilePic) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_PROFILE_PIC, profilePic);
        editor.commit();
    }

    public static String getProfilePic(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_PROFILE_PIC, "");
    }

    public static void setNotification(Context ctx, String noti) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NOTIFICATION, noti);
        editor.commit();
    }

    public static String getNotification(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NOTIFICATION, "1");
    }

    public static void setSubTitle(Context ctx, String subTitle) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_SUB_TITLE, subTitle);
        editor.commit();
    }

    public static String getSubTitle(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_SUB_TITLE, "");
    }

    public static void setWebSubTitle(Context ctx, String subTitle) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_WEB_SUB_TITLE, subTitle);
        editor.commit();
    }

    public static String getWebSubTitle(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_WEB_SUB_TITLE, "");
    }
}
