package com.idragonpro.andmagnus.security;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Debug;
import android.provider.Settings;

public class USBDebugUtils {

    public static boolean isUSBDebuggingEnabled(Context context) {
        int adb = Settings.Secure.getInt(context.getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0);
        return adb == 1;
    }

    public static boolean isDebuggable(Context context) {
        return ((context.getApplicationContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
    }

    public static boolean detectDebugger() {
        return Debug.isDebuggerConnected();
    }
}
