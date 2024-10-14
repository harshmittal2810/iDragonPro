package com.idragonpro.andmagnus.radapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.activities.ViewAllVideos;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.fragments.NewHomeFragment;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;
import com.idragonpro.andmagnus.models.Banners;
import com.idragonpro.andmagnus.models.Sections;

import java.io.Serializable;
import java.util.List;

public class

HomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = HomeRecyclerViewAdapter.class.getSimpleName();
    private static final int AD_TYPE = 0;
    private static final int CONTENT_TYPE = 1;
    private NewHomeFragment newHomeFragment;
    private List<Sections> homeContent;
    private String tabName;
    public String selectedString = new String();
    public boolean isPlaying = false;
    public SimpleExoPlayer exoplayer;
    public MutableLiveData<SimpleExoPlayer> liveDataPlayer = new MutableLiveData<>();

    public HomeRecyclerViewAdapter(NewHomeFragment newHomeFragment, List<Sections> homeContent, String tabName) {
        this.newHomeFragment = newHomeFragment;
        this.homeContent = homeContent;
        this.tabName = tabName;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_main_item, parent, false);
        HomeRecyclerViewAdapter.ViewHolder viewHolder = new HomeRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        List<Banners> contents = homeContent.get(position).getBanners();
        ((ViewHolder) holder).tvTitle.setText(homeContent.get(position).getCategory());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(newHomeFragment.getActivity(),
            LinearLayoutManager.HORIZONTAL,
            false);
        ((ViewHolder) holder).rvHome.setLayoutManager(linearLayoutManager);
        //scroll
        int visiblePos = linearLayoutManager.findFirstVisibleItemPosition();
        Log.d("TAG", "check the mute pos2:" + visiblePos);
        //
        HomeRowRecyclerViewAdapter homeRowRecyclerViewAdapter = new HomeRowRecyclerViewAdapter(newHomeFragment,
            contents,
            tabName,
            homeContent.get(position).getiType());
        exoplayer = homeRowRecyclerViewAdapter.simpleExoPlayer;
        homeRowRecyclerViewAdapter.liveDataPlayer.observeForever(simpleExoPlayer -> liveDataPlayer.setValue(
            simpleExoPlayer));
        ((ViewHolder) holder).rvHome.setAdapter(homeRowRecyclerViewAdapter);

        ((ViewHolder) holder).rvHome.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                newHomeFragment.disableRefresh();
            } else {
                newHomeFragment.enableRefresh();
            }
            return false;
        });
        ((ViewHolder) holder).imgArrow.setOnClickListener(v -> {
            Intent intent = new Intent(newHomeFragment.getActivity(), ViewAllVideos.class);
            intent.putExtra("movies", (Serializable) contents);
            intent.putExtra("title", homeContent.get(position).getCategory());
            newHomeFragment.getActivity().startActivity(intent);
        });
        if (position == 0) {
            refreshAd(((ViewHolder) holder).bannerAdView);
        } else if (position % 4 == 0) {
            ((ViewHolder) holder).bannerAdView.setVisibility(View.VISIBLE);
            refreshAd(((ViewHolder) holder).bannerAdView);
        } else {
            ((ViewHolder) holder).bannerAdView.setVisibility(View.GONE);
        }
    }

    private void bannerAdsShow(FrameLayout bannerAdView) {
        if (SaveSharedPreference.getAdsData(newHomeFragment.getActivity()) != null) {
            if (SaveSharedPreference.getAdsData(newHomeFragment.getActivity())
                .getGStatus() != null && SaveSharedPreference.getAdsData(newHomeFragment.getActivity())
                .getGStatus()
                .equals(API.ACTIVE)) {
                bannerAdView.removeAllViews();
                AdView adView = new AdView(newHomeFragment.getActivity());
                adView.setAdSize(AdSize.LARGE_BANNER);
                if (tabName.equals(API.HOME_ACTION)) {
                    adView.setAdUnitId(SaveSharedPreference.getAdsData(newHomeFragment.getActivity()).getgHomeBanner());
                } else if (tabName.equals(API.MOVIES_ACTION)) {
                    adView.setAdUnitId(SaveSharedPreference.getAdsData(newHomeFragment.getActivity())
                        .getgMovieBanner());
                } else {
                    adView.setAdUnitId(SaveSharedPreference.getAdsData(newHomeFragment.getActivity()).getGBanner());
                }
                AdRequest adRequest = new AdRequest.Builder().build();
                bannerAdView.addView(adView);
                adView.loadAd(adRequest);
            } else if (SaveSharedPreference.getAdsData(newHomeFragment.getActivity())
                .getMopubStatus() != null && SaveSharedPreference.getAdsData(newHomeFragment.getActivity())
                .getMopubStatus()
                .equals(API.ACTIVE)) {
                bannerAdView.removeAllViews();
                AdManagerAdView adView = new AdManagerAdView(newHomeFragment.getActivity());
                adView.setAdSizes(AdSize.BANNER);
                adView.setAdUnitId(SaveSharedPreference.getAdsData(newHomeFragment.getActivity()).getMopubBanner());
                AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
                bannerAdView.addView(adView);
                adView.loadAd(adRequest);
            }

        }
    }

    public void refreshAd(FrameLayout frameLayout_top) {
        if (SaveSharedPreference.getAdsData(newHomeFragment.getActivity()) != null) {
            if (SaveSharedPreference.getAdsData(newHomeFragment.getActivity())
                .getGStatus() != null && SaveSharedPreference.getAdsData(newHomeFragment.getActivity())
                .getGStatus()
                .equals(API.ACTIVE)) {

                AdLoader adLoader = new AdLoader.Builder(newHomeFragment.getActivity(),
                    SaveSharedPreference.getAdsData(newHomeFragment.getActivity())
                        .getGNative()).forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                        if (newHomeFragment != null && newHomeFragment.isAdded()) {
                            LinearLayout adView_top = (LinearLayout) newHomeFragment.getLayoutInflater()
                                .inflate(R.layout.ad_unified, null);
                            TemplateView template = adView_top.findViewById(R.id.my_template);
                            template.setStyles(styles);
                            template.setNativeAd(nativeAd);

                            frameLayout_top.removeAllViews();
                            frameLayout_top.addView(adView_top);
                        }
                    }
                }).build();
                adLoader.loadAd(new AdRequest.Builder().build());
            }
        } else if (SaveSharedPreference.getAdsData(newHomeFragment.getActivity()) != null) {
            if (SaveSharedPreference.getAdsData(newHomeFragment.getActivity())
                .getMopubStatus() != null && SaveSharedPreference.getAdsData(newHomeFragment.getActivity())
                .getMopubStatus()
                .equals(API.ACTIVE)) {

                AdLoader adLoader = new AdLoader.Builder(newHomeFragment.getActivity(),
                    SaveSharedPreference.getAdsData(newHomeFragment.getActivity())
                        .getMopubNative()).forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                        LinearLayout adView_top = (LinearLayout) newHomeFragment.getLayoutInflater()
                            .inflate(R.layout.ad_unified, null);
                        TemplateView template = adView_top.findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);

                        frameLayout_top.removeAllViews();
                        frameLayout_top.addView(adView_top);
                    }
                }).build();
                adLoader.loadAd(new AdManagerAdRequest.Builder().build());
            }
        }
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
    ////        } else     {
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
    //    }

    @Override
    public int getItemCount() {
        return homeContent.size();
    }

    public void setHomeContent(List<Sections> homeContent) {
        this.homeContent = homeContent;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final RecyclerView rvHome;
        private final FrameLayout bannerAdView;
        private final ImageView imgArrow;
        private final LinearLayout homeMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            rvHome = (RecyclerView) itemView.findViewById(R.id.rvHome);
            bannerAdView = (FrameLayout) itemView.findViewById(R.id.bannerAdView);
            imgArrow = (ImageView) itemView.findViewById(R.id.imgArrow);
            homeMain = (LinearLayout) itemView.findViewById(R.id.homeMain);
        }
    }

    private class AdViewHolder extends RecyclerView.ViewHolder {

        private final FrameLayout bannerAdView;
        private final LinearLayout homeMain;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerAdView = (FrameLayout) itemView.findViewById(R.id.bannerAdView);
            homeMain = (LinearLayout) itemView.findViewById(R.id.homeMain);
        }
    }

    @Override
    public int getItemViewType(int position) {
        /*if (position == 0) {
            return AD_TYPE;
        }*/
        return CONTENT_TYPE;
    }

    /*@Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.computeVerticalScrollOffset();
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        Log.d("TAG","check the mute posattach11:"+manager);
        if(manager instanceof LinearLayoutManager && getItemCount() > 0) {
            LinearLayoutManager llm = (LinearLayoutManager) manager;
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    int visiblePosition = llm.findFirstCompletelyVisibleItemPosition();
                    if (visiblePosition > -1) {
                        View v = llm.findViewByPosition(visiblePosition);


                        Log.d("TAG", "check the mute posattach1:" + visiblePosition);
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visiblePosition = llm.findFirstCompletelyVisibleItemPosition();
                    if(visiblePosition > -1) {
                        View v = llm.findViewByPosition(visiblePosition);
                        Log.d("TAG","check the mute posattach2:"+visiblePosition);
                        //do something
                    }
                }
            });
        }
    }*/

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

    }
}
