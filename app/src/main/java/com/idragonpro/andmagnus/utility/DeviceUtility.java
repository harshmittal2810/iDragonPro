package com.idragonpro.andmagnus.utility;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;

public class DeviceUtility {

    private static final String TAG = DeviceUtility.class.getSimpleName();

    public static String getDeviceName(){
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String UniqueID = "";
        if(manufacturer!=null && !manufacturer.equalsIgnoreCase("null")){
            UniqueID = manufacturer;
        }
        if(model!=null && !model.equalsIgnoreCase("null")){
            UniqueID = UniqueID + model;
        }
        return UniqueID;
    }

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
