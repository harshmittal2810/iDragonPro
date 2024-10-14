package com.idragonpro.andmagnus.activities;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.beans.Movies;
import com.idragonpro.andmagnus.beans.PackageModel;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;

public class QRCodeActivity extends AppCompatActivity {

    Movies sMovie;
    private boolean isDoubleSubs;
    private PackageModel packageModel, packageModel1;
    TextView tvSingleMovie, tv_single2d_movie, btnSingleMovie;
    RelativeLayout rlPAyment;
    int price = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        sMovie = (Movies) getIntent().getSerializableExtra("movie");
        packageModel = (PackageModel) getIntent().getSerializableExtra("package");
        packageModel1 = (PackageModel) getIntent().getSerializableExtra("package1");
        isDoubleSubs = (Boolean) getIntent().getSerializableExtra("isDoubleSubs");

        tvSingleMovie = findViewById(R.id.tvSingleMovie);
        tv_single2d_movie = findViewById(R.id.tv_single2d_movie);
        btnSingleMovie = findViewById(R.id.btnSingleMovie);
        rlPAyment = (RelativeLayout) findViewById(R.id.rlPAyment);

        setDataToViews();
    }

    private void setDataToViews() {
        tvSingleMovie.setText(packageModel.getPackage());
        tv_single2d_movie.setText(packageModel.getDescription());
        btnSingleMovie.setText(packageModel.getPrice());

        if (sMovie.getsAllowedInPackage().equalsIgnoreCase(API.ACTIVE)) {
            if (packageModel.getIsPackage().equalsIgnoreCase(API.ACTIVE)) {
                tvSingleMovie.setText(packageModel.getPackage());
            } else {
                tvSingleMovie.setText(sMovie.getsName());
                rlPAyment.setBackground(ContextCompat.getDrawable(this, R.drawable.subscription_silver));
            }
            tv_single2d_movie.setText(packageModel.getDescription());
            btnSingleMovie.setText(packageModel.getPrice());
        } else {

            if (sMovie.getsType().equalsIgnoreCase("5") && (SaveSharedPreference.getWebRemDays(this) > 0 || SaveSharedPreference.getWebTimeDiff(this) > 0)) {
                if (!packageModel.getIsPackage().equalsIgnoreCase(API.ACTIVE)) {
                    tvSingleMovie.setText(sMovie.getsName());
                    rlPAyment.setBackground(ContextCompat.getDrawable(this, R.drawable.subscription_silver));
                    tv_single2d_movie.setText(packageModel.getDescription());
                    btnSingleMovie.setText(packageModel.getPrice());
                }
            } else if (sMovie.getsType().equalsIgnoreCase("2") && (SaveSharedPreference.getRemDays(this) > 0 || SaveSharedPreference.getTimeDiff(this) > 0)) {
                if (!packageModel.getIsPackage().equalsIgnoreCase(API.ACTIVE)) {
                    tvSingleMovie.setText(sMovie.getsName());
                    tv_single2d_movie.setText(packageModel.getDescription());
                    btnSingleMovie.setText(packageModel.getPrice());
                }
            } else {
                if (packageModel.getIsPackage().equalsIgnoreCase(API.ACTIVE)) {
                    tvSingleMovie.setText(sMovie.getsName() + " + " + packageModel.getPackage());
                    tv_single2d_movie.setText(packageModel.getDescription());
                    try {
                        int packPrice = Integer.parseInt(packageModel.getPrice());
                        packPrice = price + packPrice;
                        packageModel.setOgPrice(packageModel.getPrice());
                        packageModel.setPrice(String.valueOf(packPrice));
                        btnSingleMovie.setText(packageModel.getPrice());
                    } catch (NumberFormatException e) {
                        btnSingleMovie.setText(packageModel.getPrice());
                    }
                } else {
                    tvSingleMovie.setText(sMovie.getsName());
                    rlPAyment.setBackground(ContextCompat.getDrawable(this, R.drawable.subscription_silver));
                    try {
                        price = Integer.parseInt(packageModel.getiPriceWithPackage());
                    } catch (NumberFormatException e) {
                        price = Integer.parseInt(packageModel.getPrice());

                    }
                    packageModel1 = packageModel;
                    tv_single2d_movie.setText(packageModel.getDescription());

                    btnSingleMovie.setText(packageModel.getPrice());
                }
            }
        }
    }
}