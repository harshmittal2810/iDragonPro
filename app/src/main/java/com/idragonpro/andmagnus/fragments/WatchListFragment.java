package com.idragonpro.andmagnus.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.idragonpro.andmagnus.MyApp;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.activities.Info;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.api.WebApi;
import com.idragonpro.andmagnus.beans.Movies;
import com.idragonpro.andmagnus.beans.SubscriptionModel;
import com.idragonpro.andmagnus.helpers.GlobalModule;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;
import com.idragonpro.andmagnus.responseModels.GetAllSingleMovieSubResponseModel;
import com.idragonpro.andmagnus.responseModels.HistoryResponseModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchListFragment extends Fragment {
    Context context;
    View rootView;
    Dialog dialog;
    LinearLayout lvMoviesList;
    Bundle bundle;

    //By Archana : google Ads
    // private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-2561754853520293/7194659582";
    private UnifiedNativeAd nativeAd;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        rootView = inflater.inflate(R.layout.movies_list, container, false);

        bundle = getArguments();

        lvMoviesList = rootView.findViewById(R.id.lvMoviesList);

        dialog = GlobalModule.showProgressDialog("Loading...", context);

        if (bundle == null) {
            getWishList("getwishlist");
        } else {
            if (!SaveSharedPreference.getLoginFromGoogle(context)) {
                getSubscriptionList();
            } else {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }

        //By Archana: Google analytics
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        recordScreenView();

        //refreshAd();

        return rootView;

    }

    private void getSubscriptionList() {
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<GetAllSingleMovieSubResponseModel> call = webApi.getAllSingleMovieSubscription(SaveSharedPreference.getUserId(
            context));
        call.enqueue(new Callback<GetAllSingleMovieSubResponseModel>() {
            @Override
            public void onResponse(Call<GetAllSingleMovieSubResponseModel> call, Response<GetAllSingleMovieSubResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getSubscriptions() != null) {
                        if (response.body().getSubscriptions().size() > 0) {
                            final ArrayList<SubscriptionModel> popular = new ArrayList<SubscriptionModel>();
                            popular.addAll(response.body().getSubscriptions());

                            for (int i = 0; i < popular.size(); i++) {
                                SubscriptionModel subscription = popular.get(i);
                                Movies movie = popular.get(i).getVideodetails();
                                if (movie != null) {
                                    LayoutInflater inflater1 = ((Activity) context).getLayoutInflater();
                                    View chlidView = inflater1.inflate(R.layout.movie_list_item, lvMoviesList, false);

                                    ImageView imgMovieBanner = chlidView.findViewById(R.id.imgMovieIcon);
                                    TextView tvName = chlidView.findViewById(R.id.tvName);
                                    TextView tvGenre = chlidView.findViewById(R.id.tvGenre);
                                    TextView tvYear = chlidView.findViewById(R.id.tvYear);
                                    TextView tvRemTime = chlidView.findViewById(R.id.tvRemTime);
                                    FrameLayout bannerAdView = chlidView.findViewById(R.id.bannerAdView);
                                    if (movie.getsSmallBanner() != null && !movie.getsSmallBanner()
                                        .equalsIgnoreCase("null") && !movie.getsSmallBanner().equals("")) {
                                        Glide.with(imgMovieBanner.getContext())
                                            .load(movie.getsSmallBanner()).centerCrop()
                                            .into(imgMovieBanner);
                                    } else {
                                        Glide.with(imgMovieBanner.getContext()).load(R.drawable.not_img).centerCrop()
                                            .into(imgMovieBanner);
                                    }
                                    tvName.setText(movie.getsName());
                                    tvYear.setText(movie.getsYear());
                                    if (subscription.getDaysdiff() > 0) {
                                        tvRemTime.setText(getString(R.string.remaining_time) + " : " + subscription.getDaysdiff() + " " + getString(
                                            R.string.days));
                                    } else {
                                        if (subscription.getTimediff() > 0) {
                                            tvRemTime.setText(getString(R.string.expires_today));
                                        } else {
                                            tvRemTime.setText(getString(R.string.expired));
                                        }
                                    }
                                    tvRemTime.setVisibility(View.VISIBLE);
                                    if (i != 0 && !(i % 2 == 0)) {
                                        bannerAdsShow(bannerAdView);
                                    }
                                    chlidView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(context, Info.class);
                                            intent.putExtra("movie", movie);
                                            startActivity(intent);
                                        }
                                    });

                                    lvMoviesList.addView(chlidView);
                                }
                            }
                        }

                    }
                }
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GetAllSingleMovieSubResponseModel> call, Throwable t) {
                t.printStackTrace();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    private void recordScreenView() {
        // This string must be <= 36 characters long in order for setCurrentScreen to succeed.
        //string screenName = getCurrentImageId() + "-" + getCurrentImageTitle();
        // [START set_current_screen]
        mFirebaseAnalytics.setCurrentScreen(getActivity(), null, null);// [END set_current_screen]
        Log.d("Message", "Track Screen WatchListFragment");
    }

    private void refreshAd() {
        FrameLayout frameLayout_top = getActivity().findViewById(R.id.fl_adplaceholder_top_watch_list);

        AdLoader adLoader = new AdLoader.Builder(getActivity(),
            getString(R.string.admob_ad_native_id)).forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                LinearLayout adView_top = (LinearLayout) getLayoutInflater().inflate(R.layout.ad_unified, null);
                TemplateView template = adView_top.findViewById(R.id.my_template);
                template.setStyles(styles);
                template.setNativeAd(nativeAd);
                frameLayout_top.removeAllViews();
                frameLayout_top.addView(adView_top);
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    //    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
    //        // Set the media view.
    //        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
    //
    //        // Set other ad assets.
    ////        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
    //        adView.setBodyView(adView.findViewById(R.id.ad_body));
    ////        //adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
    ////        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
    //////        adView.setPriceView(adView.findViewById(R.id.ad_price));
    ////        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
    //////        adView.setStoreView(adView.findViewById(R.id.ad_store));
    ////        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
    //
    //        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
    ////        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
    ////        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
    //
    //        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
    //        // check before trying to display them.
    //        if (nativeAd.getBody() == null) {
    //            adView.getBodyView().setVisibility(View.INVISIBLE);
    //        } else {
    //            adView.getBodyView().setVisibility(View.VISIBLE);
    //            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
    //        }
    //
    ////        if (nativeAd.getCallToAction() == null) {
    ////          adView.getCallToActionView().setVisibility(View.INVISIBLE);
    ////        } else {
    ////          adView.getCallToActionView().setVisibility(View.VISIBLE);
    ////          ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
    ////        }
    //
    ////        if (nativeAd.getIcon() == null) {
    ////            adView.getIconView().setVisibility(View.GONE);
    ////        } else {
    ////            ((ImageView) adView.getIconView()).setImageDrawable(
    ////                    nativeAd.getIcon().getDrawable());
    ////            adView.getIconView().setVisibility(View.VISIBLE);
    ////        }
    //
    ////        if (nativeAd.getPrice() == null) {
    ////            adView.getPriceView().setVisibility(View.INVISIBLE);
    ////        } else {
    ////            adView.getPriceView().setVisibility(View.VISIBLE);
    ////            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
    ////        }
    //
    ////        if (nativeAd.getStore() == null) {
    ////            adView.getStoreView().setVisibility(View.INVISIBLE);
    ////        } else {
    ////            adView.getStoreView().setVisibility(View.VISIBLE);
    ////            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
    ////        }
    //
    ////        if (nativeAd.getStarRating() == null) {
    ////            adView.getStarRatingView().setVisibility(View.INVISIBLE);
    ////        } else {
    ////            ((RatingBar) adView.getStarRatingView())
    ////                    .setRating(nativeAd.getStarRating().floatValue());
    ////            adView.getStarRatingView().setVisibility(View.VISIBLE);
    ////        }
    //
    ////        if (nativeAd.getAdvertiser() == null) {
    ////            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
    ////        } else {
    ////            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
    ////            adView.getAdvertiserView().setVisibility(View.VISIBLE);
    ////        }
    //
    //        // This method tells the Google Mobile Ads SDK that you have finished populating your
    //        // native ad view with this native ad.
    //        adView.setNativeAd(nativeAd);
    //
    //        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
    //        // have a video asset.
    //        VideoController vc = nativeAd.getVideoController();
    //
    //        // Updates the UI to say whether or not this ad has a video asset.
    //        if (vc.hasVideoContent()) {
    ////            videoStatus.setText(String.format(Locale.getDefault(),
    ////                    "Video status: Ad contains a %.2f:1 video asset.",
    ////                    vc.getAspectRatio()));
    //            Log.e("Message","video status : "+String.format(Locale.getDefault(),
    //                    "Video status: Ad contains a %.2f:1 video asset.",
    //                    vc.getAspectRatio()));
    //
    //            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
    //            // VideoController will call methods on this object when events occur in the video
    //            // lifecycle.
    //            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
    //                @Override
    //                public void onVideoEnd() {
    //                    // Publishers should allow native ads to complete video playback before
    //                    // refreshing or replacing them with another ad in the same UI location.
    ////                    refresh.setEnabled(true);
    ////                    videoStatus.setText("Video status: Video playback has ended.");
    //                    Log.e("Message","Video status: Video playback has ended.");
    //                    super.onVideoEnd();
    //                }
    //            });
    //        } else {
    //            Log.e("Message","Video status: Ad does not contain a video asset.");
    ////            videoStatus.setText("Video status: Ad does not contain a video asset.");
    ////            refresh.setEnabled(true);
    //        }
    //
    //    }

    private void getWishList(String url) {
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<HistoryResponseModel> call = webApi.getHistory(url, SaveSharedPreference.getUserId(context));
        call.enqueue(new Callback<HistoryResponseModel>() {
            @Override
            public void onResponse(Call<HistoryResponseModel> call, Response<HistoryResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getVideo_data() != null) {
                        if (response.body().getVideo_data().size() > 0) {
                            final ArrayList<Movies> popular = new ArrayList<Movies>();
                            popular.addAll(response.body().getVideo_data());

                            for (int i = 0; i < popular.size(); i++) {
                                Movies movie = popular.get(i);
                                LayoutInflater inflater1 = ((Activity) context).getLayoutInflater();
                                View chlidView = inflater1.inflate(R.layout.movie_list_item, lvMoviesList, false);

                                ImageView imgMovieBanner = chlidView.findViewById(R.id.imgMovieIcon);
                                TextView tvName = chlidView.findViewById(R.id.tvName);
                                TextView tvGenre = chlidView.findViewById(R.id.tvGenre);
                                TextView tvYear = chlidView.findViewById(R.id.tvYear);
                                TextView tvRemTime = chlidView.findViewById(R.id.tvRemTime);
                                FrameLayout bannerAdView = chlidView.findViewById(R.id.bannerAdView);
                                if (movie.getsSmallBanner() != null && !movie.getsSmallBanner()
                                    .equalsIgnoreCase("null") && !movie.getsSmallBanner().equals("")) {
                                    Glide.with(imgMovieBanner.getContext())
                                        .load(movie.getsSmallBanner()).centerCrop()
                                        .into(imgMovieBanner);

                                } else {
                                    Glide.with(imgMovieBanner.getContext()).load(R.drawable.not_img).centerCrop()
                                        .into(imgMovieBanner);
                                    Log.d("Message", "Not img found ");

                                }
                                //tvGenre.setText(movie.getsGenre());
                                tvName.setText(movie.getsName());
                                tvYear.setText(movie.getsYear());
                                   /* if (GlobalModule.sSubcription.equalsIgnoreCase("1_movie")) {
                                        tvRemTime.setText("Remaining Time : " + movie.getiRemTime() + " Days");
                                    } else {
                                        tvRemTime.setText("Remaining Time : " + GlobalModule.sRemDays + " Days");
                                    }
                                    tvRemTime.setVisibility(View.VISIBLE);*/
                                if (i != 0 && !(i % 2 == 0)) {
                                    bannerAdsShow(bannerAdView);
                                }
                                chlidView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, Info.class);
                                        intent.putExtra("movie", movie);
                                        startActivity(intent);
                                    }
                                });

                                lvMoviesList.addView(chlidView);
                            }
                        }

                    }
                }
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<HistoryResponseModel> call, Throwable t) {
                t.printStackTrace();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    private void bannerAdsShow(FrameLayout bannerAdView) {
        if (SaveSharedPreference.getAdsData(getActivity()) != null) {
            if (SaveSharedPreference.getAdsData(getActivity()).getGStatus() != null && SaveSharedPreference.getAdsData(
                getActivity()).getGStatus().equals(API.ACTIVE)) {
                bannerAdView.removeAllViews();
                AdView adView = new AdView(getActivity());
                adView.setAdSize(AdSize.LARGE_BANNER);
                adView.setAdUnitId(SaveSharedPreference.getAdsData(getActivity()).getGBanner());
                AdRequest adRequest = new AdRequest.Builder().build();
                bannerAdView.addView(adView);
                adView.loadAd(adRequest);
            } else if (SaveSharedPreference.getAdsData(context)
                .getMopubStatus() != null && SaveSharedPreference.getAdsData(context)
                .getMopubStatus()
                .equals(API.ACTIVE)) {
                bannerAdView.removeAllViews();
                AdManagerAdView adView = new AdManagerAdView(context);
                adView.setAdSizes(AdSize.BANNER);
                adView.setAdUnitId(SaveSharedPreference.getAdsData(context).getMopubBanner());
                AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
                bannerAdView.addView(adView);
                adView.loadAd(adRequest);

            }
        }
    }
}



