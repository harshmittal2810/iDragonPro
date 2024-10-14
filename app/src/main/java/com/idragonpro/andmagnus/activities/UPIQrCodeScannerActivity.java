package com.idragonpro.andmagnus.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.idragonpro.andmagnus.R;

public class UPIQrCodeScannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upiqr_code_scanner);
        boolean isForBank = getIntent().getBooleanExtra("isForBank", false);

        if (isForBank) {
            findViewById(R.id.qrCode).setVisibility(View.GONE);
            findViewById(R.id.bankQrCode).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.qrCode).setVisibility(View.VISIBLE);
            findViewById(R.id.bankQrCode).setVisibility(View.GONE);
        }

    }
}