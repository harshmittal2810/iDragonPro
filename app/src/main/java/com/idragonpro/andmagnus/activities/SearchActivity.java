package com.idragonpro.andmagnus.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.idragonpro.andmagnus.MyApp;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.api.WebApi;
import com.idragonpro.andmagnus.beans.Movies;
import com.idragonpro.andmagnus.helpers.LocaleHelper;
import com.idragonpro.andmagnus.radapters.SearchRVAdapter;
import com.idragonpro.andmagnus.responseModels.SearchResponseModel;
import com.idragonpro.andmagnus.utility.UtilityInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements UtilityInterface {

    Context context;
    EditText etSearch;
    RecyclerView lvSearchResults;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    //By Archana : Banner
    private AdView adView;
    AdRequest adRequest;

    private FirebaseAnalytics mFirebaseAnalytics;
    private SearchRVAdapter viewAllRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        context = this;

        //By Archana: Google analytics
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        recordScreenView();

        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.onBackPressed();
            }
        });
        etSearch = (EditText) findViewById(R.id.etSearch);
        lvSearchResults = (RecyclerView) findViewById(R.id.lvSearchResults);
        lvSearchResults.setLayoutManager(new GridLayoutManager(this, 2));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    getWishList(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //By Archana : google Banner
        adView = (AdView)findViewById(R.id.ad_view_search);
        //  AdSize adSize = new AdSize(300,90);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);



    }

    private void recordScreenView() {
        // This string must be <= 36 characters long in order for setCurrentScreen to succeed.
        //string screenName = getCurrentImageId() + "-" + getCurrentImageTitle();
        // [START set_current_screen]
        mFirebaseAnalytics.setCurrentScreen(this ,null,null);// [END set_current_screen]
        Log.d("Message","Track Screen SearchActivity");
    }
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void finishActivity(Activity activity) {
        if (!activity.isDestroyed()) {
            activity.finish();
        }

    }

    private void getWishList(String keyword) {
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<SearchResponseModel> call = webApi.getSearchItems(
                keyword
        );
        call.enqueue(new Callback<SearchResponseModel>() {
            @Override
            public void onResponse(Call<SearchResponseModel> call, Response<SearchResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getVideo_data() != null) {
                        if (response.body().getVideo_data().size() > 0) {
                            List<Movies> movies = new ArrayList<Movies>();
                            movies = response.body().getVideo_data();
                            viewAllRvAdapter = new SearchRVAdapter(SearchActivity.this, movies);
                            lvSearchResults.setAdapter(viewAllRvAdapter);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponseModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base,"en"));
    }
}
