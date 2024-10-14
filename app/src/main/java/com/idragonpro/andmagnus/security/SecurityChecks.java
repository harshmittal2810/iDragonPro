package com.idragonpro.andmagnus.security;

import android.content.Context;

import com.idragonpro.andmagnus.BuildConfig;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;
import com.scottyab.rootbeer.RootBeer;

import java.util.concurrent.Executors;

public final class SecurityChecks {
    private static boolean isChecking = false;
    private static SecurityChecks securityChecks = null;

    private SecurityChecks() {
    }

    public static void initializeSecurityChecks() {
        securityChecks = new SecurityChecks();
    }

    public static SecurityChecks getInstance() {
        if (securityChecks == null) securityChecks = new SecurityChecks();
        return securityChecks;
    }

    public static boolean isChecking() {
        return isChecking;
    }

    public synchronized void checkSecurity(Context context, SecurityChecksResultCallback callback) {
        Executors.newSingleThreadExecutor().execute(() -> {

            if (BuildConfig.DISABLE_SECURITY_CHECKS) {
                callback.onSecurityChecksCompleted(true, null);
                return;
            }

            isChecking = true;
            RunningProcessCheck runningProcessCheck = new RunningProcessCheck();
            if (runningProcessCheck.checkRunningProcesses()) {
                isChecking = false;
                callback.onSecurityChecksCompleted(false, context.getString(R.string.frida_check_failure));
                return;
            }

            if (SaveSharedPreference.getDevOption(context)) {
                if (USBDebugUtils.isUSBDebuggingEnabled(context)) {
                    isChecking = false;
                    callback.onSecurityChecksCompleted(false, context.getString(R.string.developer_options_failure));
                    return;
                }
            }
            if (USBDebugUtils.isDebuggable(context)) {
                isChecking = false;
                callback.onSecurityChecksCompleted(false, context.getString(R.string.debug_check_failure));
                return;
            }
            if (USBDebugUtils.detectDebugger()) {
                isChecking = false;
                callback.onSecurityChecksCompleted(false, context.getString(R.string.debug_options_failure));
                return;
            }

            EmulatorChecks emulatorChecks = new EmulatorChecks();
            if (emulatorChecks.isEmulator(context)) {
                isChecking = false;
                callback.onSecurityChecksCompleted(false, context.getString(R.string.emulator_check_failure));
                return;
            }
            RootBeer rootBeer = new RootBeer(context);
            if (rootBeer.isRooted()) {
                isChecking = false;
                callback.onSecurityChecksCompleted(false, context.getString(R.string.root_check_failure));
                return;
            }
            RunTimeIntegrity runTimeIntegrity = new RunTimeIntegrity();
            if (!runTimeIntegrity.checkRunTimeIntegrity()) {
                isChecking = false;
                callback.onSecurityChecksCompleted(false, context.getString(R.string.app_integrity_check_failure));
                return;
            }
            isChecking = false;
            callback.onSecurityChecksCompleted(true, null);
        });
    }

    public interface SecurityChecksResultCallback {
        void onSecurityChecksCompleted(boolean result, String message);
    }
}