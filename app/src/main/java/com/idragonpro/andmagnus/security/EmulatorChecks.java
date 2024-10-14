package com.idragonpro.andmagnus.security;

import android.content.Context;
import android.os.Build;


public class EmulatorChecks {
    public boolean isEmulator(Context context) {
        return Build.BRAND.equalsIgnoreCase("generic") ||
                Build.DEVICE.equalsIgnoreCase("generic") ||
                Build.FINGERPRINT.equalsIgnoreCase("generic") ||
                Build.HARDWARE.equalsIgnoreCase("goldfish") ||
                Build.HOST.equalsIgnoreCase("android-test") ||
                Build.ID.equalsIgnoreCase("FRF91") ||
                Build.MANUFACTURER.equalsIgnoreCase("unknown") ||
                Build.MODEL.equalsIgnoreCase("sdk") ||
                Build.PRODUCT.equalsIgnoreCase("sdk") ||
                Build.SERIAL == null ||
                Build.SERIAL.equalsIgnoreCase("null");
    }
}