package com.idragonpro.andmagnus.radapters;

import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.activities.Info;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;
import com.idragonpro.andmagnus.models.Review;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ReviewRvAdapter extends RecyclerView.Adapter<ReviewRvAdapter.ViewHolder> {


    private final Info info;
    private final List<Review> reviewList;

    public ReviewRvAdapter(Info info, List<Review> reviewList) {
        this.info = info;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        ReviewRvAdapter.ViewHolder viewHolder = new ReviewRvAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewRvAdapter.ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvReview.setText(Html.fromHtml(review.getSReview(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.tvReview.setText(Html.fromHtml(review.getSReview()));
        }
        holder.tvName.setText(review.getSFirstName() + " " + review.getSLastName());

        try {
            String dateStr = review.getSCreatedTimeStamp();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(dateStr);
            df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            df.setTimeZone(TimeZone.getDefault());
            holder.tvTime.setText(df.format(date));
        } catch (ParseException e) {
            holder.tvTime.setText(review.getSCreatedTimeStamp());
        }
        if(position%5 == 0){
            refreshAd(holder.bannerAdView);
        }
    }

    private void bannerAdsShow(FrameLayout bannerAdView) {
        if (SaveSharedPreference.getAdsData(info) != null) {
            if (SaveSharedPreference.getAdsData(info).getGStatus() != null
                    && SaveSharedPreference.getAdsData(info).getGStatus().equals(API.ACTIVE)) {
                bannerAdView.removeAllViews();
                AdView adView = new AdView(info);
                adView.setAdSize(AdSize.BANNER);
                adView.setAdUnitId(SaveSharedPreference.getAdsData(info).getChatGBanner());
                AdRequest adRequest = new AdRequest.Builder().build();
                bannerAdView.addView(adView);
                adView.loadAd(adRequest);
            } else if (SaveSharedPreference.getAdsData(info).getMopubStatus() != null
                    && SaveSharedPreference.getAdsData(info).getMopubStatus().equals(API.ACTIVE)) {
                bannerAdView.removeAllViews();
                AdManagerAdView adView = new AdManagerAdView(info);
                adView.setAdSizes(AdSize.BANNER);
                adView.setAdUnitId(SaveSharedPreference.getAdsData(info).getMopubBanner());
                AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
                bannerAdView.addView(adView);
                adView.loadAd(adRequest);
            }

        }
    }

    public void refreshAd(FrameLayout frameLayout_top) {
        if (SaveSharedPreference.getAdsData(info) != null) {
            if (SaveSharedPreference.getAdsData(info).getGStatus() != null
                    && SaveSharedPreference.getAdsData(info).getGStatus().equals(API.ACTIVE)) {
                AdLoader adLoader = new AdLoader.Builder(info, SaveSharedPreference.getAdsData(info).getGNativeNew())
                        .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(NativeAd nativeAd) {
                                NativeTemplateStyle styles = new
                                        NativeTemplateStyle.Builder().build();
                                LinearLayout adView_top = (LinearLayout) info.getLayoutInflater()
                                        .inflate(R.layout.ad_unified, null);
                                TemplateView template = adView_top.findViewById(R.id.my_template);
                                template.setStyles(styles);
                                template.setNativeAd(nativeAd);

                                frameLayout_top.removeAllViews();
                                frameLayout_top.addView(adView_top);
                            }
                        })
                        .build();
                adLoader.loadAd(new AdRequest.Builder().build());
            }
        } else if (SaveSharedPreference.getAdsData(info) != null) {
            if (SaveSharedPreference.getAdsData(info).getMopubStatus() != null
                    && SaveSharedPreference.getAdsData(info).getMopubStatus().equals(API.ACTIVE)) {

                AdLoader adLoader = new AdLoader.Builder(info, SaveSharedPreference.getAdsData(info).getMopubNative())
                        .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(NativeAd nativeAd) {
                                NativeTemplateStyle styles = new
                                        NativeTemplateStyle.Builder().build();
                                LinearLayout adView_top = (LinearLayout) info.getLayoutInflater()
                                        .inflate(R.layout.ad_unified, null);
                                TemplateView template = adView_top.findViewById(R.id.my_template);
                                template.setStyles(styles);
                                template.setNativeAd(nativeAd);

                                frameLayout_top.removeAllViews();
                                frameLayout_top.addView(adView_top);
                            }
                        })
                        .build();
                adLoader.loadAd(new AdManagerAdRequest.Builder().build());
            }
        }
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName,tvReview,tvTime;
        private final FrameLayout bannerAdView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvReview = (TextView) itemView.findViewById(R.id.tvReview);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            bannerAdView = (FrameLayout) itemView.findViewById(R.id.bannerAdView);
        }
    }
}
