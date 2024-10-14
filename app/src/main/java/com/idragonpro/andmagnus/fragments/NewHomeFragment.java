package com.idragonpro.andmagnus.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.loadinganimation.LoadingAnimation;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.idragonpro.andmagnus.BundleKeys;
import com.idragonpro.andmagnus.MyApp;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.activities.Home;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.api.WebApi;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;
import com.idragonpro.andmagnus.models.Banners;
import com.idragonpro.andmagnus.models.Sections;
import com.idragonpro.andmagnus.radapters.BannerAdapter;
import com.idragonpro.andmagnus.radapters.HomeRecyclerViewAdapter;
import com.idragonpro.andmagnus.responseModels.HomeResponseModel;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewHomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = NewHomeFragment.class.getSimpleName();
    private View view;
    private RecyclerView rvHome;
    private final boolean isfirstTime = true;
    int count = 0;
    private HomeRecyclerViewAdapter homeRecyclerViewAdapter;
    private ShimmerFrameLayout shimmerLayout;
    private List<Banners> banners;
    private String tabName;
    private AdManagerAdView adView;
    private SwipeRefreshLayout swipeRL;
    private UnifiedNativeAd nativeAd;
    private final ArrayList<SimpleExoPlayer> exoplayer = new ArrayList<>();
    private Context mContext;
    private ViewGroup rlMain;
    private ArrayList<SimpleExoPlayer> tempList;
    LoadingAnimation loadingAnim;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabName = getArguments().getString(BundleKeys.TAB_NAME, "");
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_home, container, false);
        initComponents();
        recordScreenView();

        loadingAnim = view.findViewById(R.id.loadingAnim);
        loadingAnim.setProgressVector(getResources().getDrawable(R.drawable.bouncy_balls));
        loadingAnim.setTextViewVisibility(true);
        loadingAnim.setTextStyle(true);
        loadingAnim.setTextSize(16);
        loadingAnim.setTextColor(Color.RED);
        loadingAnim.setTextMsg("Please Wait");
        loadingAnim.setEnlarge(6);

        return view;
    }

    private void recordScreenView() {
        if (mContext != null) {
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
            mFirebaseAnalytics.setCurrentScreen((Activity) mContext, null, null);
        }
    }

    private void initComponents() {
        rvHome = view.findViewById(R.id.rvHome);
        //        shimmerLayout = (ShimmerFrameLayout) view.findViewById(R.id.shimmerLayout);
        rlMain = view.findViewById(R.id.rlMain);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rvHome.setLayoutManager(linearLayoutManager);
        swipeRL = view.findViewById(R.id.swipeRL);
        if (swipeRL != null && mContext != null) {
            swipeRL.setOnRefreshListener(this);
            swipeRL.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.colorRed));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        /*if(tabName.equalsIgnoreCase(API.HOME_ACTION) && isfirstTime){
            isfirstTime = false;
            refreshAd();
        }*/
    }

    public void refreshAd() {
        if (mContext != null) {
            if (SaveSharedPreference.getAdsData(mContext) != null) {
                FrameLayout frameLayout_top = view.findViewById(R.id.native_ad_holder);

                if (SaveSharedPreference.getAdsData(mContext).getGStatus() != null && SaveSharedPreference.getAdsData(
                        mContext).getGStatus().equals(API.ACTIVE)) {

                    AdLoader adLoader = new AdLoader.Builder(getActivity(),
                            SaveSharedPreference.getAdsData(getContext()).getGNative()).forNativeAd(nativeAd -> {
                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                        LinearLayout adView_top = (LinearLayout) getLayoutInflater().inflate(R.layout.ad_unified, null);
                        TemplateView template = adView_top.findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);

                        frameLayout_top.removeAllViews();
                        frameLayout_top.addView(adView_top);
                    }).build();
                    adLoader.loadAd(new AdRequest.Builder().build());
                } else if (SaveSharedPreference.getAdsData(mContext) != null) {
                    if (SaveSharedPreference.getAdsData(mContext)
                            .getMopubStatus() != null && SaveSharedPreference.getAdsData(mContext)
                            .getMopubStatus()
                            .equals(API.ACTIVE)) {

                        AdLoader adLoader = new AdLoader.Builder(getActivity(),
                                SaveSharedPreference.getAdsData(getContext()).getMopubNative()).forNativeAd(nativeAd -> {
                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                            LinearLayout adView_top = (LinearLayout) getLayoutInflater().inflate(R.layout.ad_unified,
                                    null);
                            TemplateView template = adView_top.findViewById(R.id.my_template);
                            template.setStyles(styles);
                            template.setNativeAd(nativeAd);

                            frameLayout_top.removeAllViews();
                            frameLayout_top.addView(adView_top);
                        }).build();
                        adLoader.loadAd(new AdManagerAdRequest.Builder().build());
                    }
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        callHomeData();
    }

    private void callHomeData() {
        //        startShimmer();
        loadingAnim.setVisibility(View.VISIBLE);
        WebApi webApi = API.getRetrofit().create(WebApi.class);
        Call<HomeResponseModel> call = webApi.getHomeData(tabName, MyApp.Companion.getInstance().getVersionCode());
        Log.e(TAG, "callHomeData: " + webApi.getHomeData(tabName, MyApp.Companion.getInstance().getVersionCode()));
        call.enqueue(new Callback<HomeResponseModel>() {
            @Override
            public void onResponse(Call<HomeResponseModel> call, Response<HomeResponseModel> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Log.e(TAG, "onResponse: " + response.message());
                        loadingAnim.setVisibility(View.GONE);
                        if (response.body().getHomeContent() != null) {
                            updateHomeData(response.body().getHomeContent());
                        }
                        if (response.body().getBanner() != null) {
                            updateBannerData(response.body().getBanner());
                        }
                    } else {
                        loadingAnim.setVisibility(View.GONE);
                        //                    stopShimmer();
                    }
                }

            }

            @Override
            public void onFailure(Call<HomeResponseModel> call, Throwable t) {
                t.printStackTrace();
                loadingAnim.setVisibility(View.GONE);
            }
        });
    }

    private void startShimmer() {
        rlMain.setVisibility(View.GONE);
        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmer();
    }

    private void stopShimmer() {
        shimmerLayout.setVisibility(View.GONE);
        shimmerLayout.stopShimmer();
        rlMain.setVisibility(View.VISIBLE);
    }

    private void updateBannerData(List<Banners> banners) {
        if (mContext != null) {
            // initializing the slider view.
            SliderView sliderView = view.findViewById(R.id.slider);
            BannerAdapter adapter = new BannerAdapter(mContext, banners);
            sliderView.setSliderAdapter(adapter);

            this.banners = banners;
            sliderView.setAutoCycle(true);
            //sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
            sliderView.setScrollTimeInSec(3); //set scroll delay in seconds :
            sliderView.startAutoCycle();

            sliderView.getLayoutParams().height = (int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.8);
            sliderView.requestLayout();
        }
    }

    private void updateHomeData(List<Sections> homeContent) {
        List<Sections> homeContentNew = new ArrayList<>();
        for (int i = 0; i < homeContent.size(); i++) {
            List<Banners> contents = homeContent.get(i).getBanners();
            if (contents != null && contents.size() > 0) {
                homeContentNew.add(homeContent.get(i));
            }
        }
        if (homeRecyclerViewAdapter == null) {
            homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(this, homeContentNew, tabName);
            rvHome.setAdapter(homeRecyclerViewAdapter);
            homeRecyclerViewAdapter.liveDataPlayer.observeForever(simpleExoPlayer -> {
                //                    exoplayer.clear();
                exoplayer.add(simpleExoPlayer);
                tempList = exoplayer;
            });
        } else {
            homeRecyclerViewAdapter.setHomeContent(homeContentNew);
            homeRecyclerViewAdapter.notifyDataSetChanged();
        }
        rvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager lm = ((LinearLayoutManager) recyclerView.getLayoutManager());
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                LinearLayoutManager llm = (LinearLayoutManager) manager;
                int visiblePosition = llm.findFirstCompletelyVisibleItemPosition();
                if (visiblePosition > -1) {
                    View v = llm.findViewByPosition(visiblePosition);
                    //do something
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeRL.setRefreshing(false);
        if (mContext != null) {
            ((Home) mContext).onRefresh();
        }
    }

    public void onRefreshCalled() {
        callHomeData();
    }

    public void disableRefresh() {
        swipeRL.setEnabled(false);
    }

    public void enableRefresh() {
        swipeRL.setEnabled(true);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onDestroy() {
        if (homeRecyclerViewAdapter != null && homeRecyclerViewAdapter.exoplayer != null) {
            homeRecyclerViewAdapter.exoplayer.setPlayWhenReady(false);
        }
        super.onDestroy();
    }

    @Override
    public void onStop() {
        if (tempList != null) {
            for (SimpleExoPlayer player : tempList) {
                player.setPlayWhenReady(false);
            }
        }

        super.onStop();
    }
}
