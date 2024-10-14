package com.idragonpro.andmagnus.security;

import android.util.Log;

class RunTimeIntegrity {
    boolean checkRunTimeIntegrity() {
        try {
            throw new Exception();
        } catch (Exception e) {
            int zygoteInitCallCount = 0;
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                if (stackTraceElement.getClassName().equals("com.android.internal.os.ZygoteInit")) {
                    zygoteInitCallCount++;
                    if (zygoteInitCallCount == 2) {
                        Log.d("HookDetection", "Substrate is active on the device.");
                        return false;
                    }
                }
                if (stackTraceElement.getClassName().equals("com.saurik.substrate.MS$2") &&
                        stackTraceElement.getMethodName().equals("invoked")) {
                    Log.d("HookDetection", "A method on the stack trace has been hooked using Substrate.");
                    return false;
                }
                if (stackTraceElement.getClassName().equals("de.robv.android.xposed.XposedBridge") &&
                        stackTraceElement.getMethodName().equals("main")) {
                    Log.d("HookDetection", "Xposed is active on the device.");
                    return false;
                }
                if (stackTraceElement.getClassName().equals("de.robv.android.xposed.XposedBridge") &&
                        stackTraceElement.getMethodName().equals("handleHookedMethod")) {
                    Log.d("HookDetection", "A method on the stack trace has been hooked using Xposed.");
                    return false;
                }
            }
        }
        return true;
    }
}
