package com.idragonpro.andmagnus.helpers;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.idragonpro.andmagnus.R;
//import com.idragonpro.andmagnus.activities.Home;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GlobalModule {
    public static String startActivity="",vmap = "";

    public static String UrlEncode(String data, String key, String value) {
        try {
            if(data!=null && key!=null) {
                data += "&" + URLEncoder.encode(key, "UTF-8")
                        + "=" + URLEncoder.encode(value, "UTF-8");
            }else {
                data += "&" + key + "=" + value;
            }
        } catch (UnsupportedEncodingException | NullPointerException e) {
            data += "&" + key + "=" + value;
        }
        return data;
    }



    public static Dialog showProgressDialog(String message, Context context) {
        Dialog progressDialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewRoot = inflater.inflate(R.layout.custom_loader, null);
        ImageView imageView = (ImageView) viewRoot.findViewById(R.id.imgCustomProgress);
        TextView textView = (TextView) viewRoot.findViewById(R.id.tvCustomProgress);
        textView.setText(message.toUpperCase());
        ObjectAnimator animation = ObjectAnimator.ofFloat(imageView, "rotationY", 0.0f, 360f);
        animation.setDuration(3600);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
        viewRoot.getBackground().setAlpha(125);
        progressDialog.setContentView(viewRoot);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
//        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        progressDialog.show();
        return progressDialog;
    }
}
