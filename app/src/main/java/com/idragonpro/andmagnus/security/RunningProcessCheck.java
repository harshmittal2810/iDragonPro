package com.idragonpro.andmagnus.security;

import android.util.Log;

import com.idragonpro.andmagnus.MyApp;


class RunningProcessCheck {
    boolean checkRunningProcesses() {
        boolean isFridaRunning = MyApp.getInstance().isFridaServerListening();
        Log.d("SecurityCheck", "isFridaRunning = " + isFridaRunning);
        return isFridaRunning;
    }
}
