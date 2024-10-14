package com.idragonpro.andmagnus.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.activities.Info;
import com.idragonpro.andmagnus.activities.Register;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.beans.Movies;
import com.idragonpro.andmagnus.helpers.GlobalModule;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;
import com.idragonpro.andmagnus.utility.StringUtility;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.MyViewHolder> {

    private final List<Movies> moviesList;
    private final Info info;
    private final Fragment fragment;
    Context context;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_episode, parent, false);

        return new MyViewHolder(itemView);
    }

    public EpisodeAdapter(Context context, List<Movies> moviesList, Info info, Fragment fragment) {
        this.context = context;
        this.moviesList = moviesList;
        this.info = info;
        this.fragment = fragment;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Movies sMovie = moviesList.get(position);

        if (sMovie.getsSmallBanner() != null && !sMovie.getsSmallBanner()
            .equalsIgnoreCase("null") && !sMovie.getsSmallBanner().isEmpty()) {
            Glide.with(holder.imgMovieIcon.getContext())
                .load(sMovie.getsSmallBanner())
                .centerCrop()
                .into(holder.imgMovieIcon);
        } else {
            Glide.with(holder.imgMovieIcon.getContext()).load(R.drawable.not_img)
                .centerCrop()
                .into(holder.imgMovieIcon);
        }

        holder.imgMovieIcon.setOnClickListener(v -> {
            boolean isContentAvailable = false;
            List<String> listOfStringfromString = StringUtility.getListOfStringFromString(sMovie.getsRegion());
            for (int i = 0; i < listOfStringfromString.size(); i++) {
                if (SaveSharedPreference.getCountry(context)
                    .equalsIgnoreCase(listOfStringfromString.get(i)) || listOfStringfromString.get(i)
                    .equalsIgnoreCase("Worldwide") || isContentAvailable) {
                    isContentAvailable = true;
                    if (sMovie.getsComingSoon() != null && sMovie.getsComingSoon().equalsIgnoreCase("yes")) {

                        final AlertDialog adExp = new AlertDialog.Builder(context).create();
                        adExp.setMessage("This movie is coming soon");
                        adExp.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adExp.dismiss();
                            }
                        });

                        adExp.show();
                    } else if (sMovie.getiSfree() != null && sMovie.getiSfree().equalsIgnoreCase("yes")) {
                        info.setTempData(sMovie.getVideoUrl().replaceAll(" ", "%20"), false, position, false);
                    } else if (SaveSharedPreference.getLoginFromGoogle(context)) {
                        GlobalModule.startActivity = Info.class.getSimpleName();
                        info.isLoginAfterGoogle = true;
                        Intent intent = new Intent(context, Register.class);
                        context.startActivity(intent);
                    } else {
                        if (sMovie.getsAllowedInPackage().equalsIgnoreCase(API.ACTIVE)) {
                            if (SaveSharedPreference.getWebRemDays(context) > 0 || SaveSharedPreference.getWebTimeDiff(
                                context) > 0) {
                                info.setTempData(sMovie.getVideoUrl().replaceAll(" ", "%20"), false, position, true);
                            } else if (sMovie.getSubscriptions() != null && (sMovie.getDaysdiff() > 0 || sMovie.getTimediff() > 0)) {
                                info.setTempData(sMovie.getVideoUrl().replaceAll(" ", "%20"), false, position, true);
                            } else {
                                info.showSubsBottomSheet(sMovie);
                            }
                        } else {
                            if (sMovie.getSubscriptions() != null && (sMovie.getDaysdiff() > 0 || sMovie.getTimediff() > 0)) {
                                info.setTempData(sMovie.getVideoUrl().replaceAll(" ", "%20"), false, position, true);
                            } else {
                                info.showSubsBottomSheet(sMovie);
                            }
                        }
                    }
                    break;
                }
            }
            if (!isContentAvailable) {
                final AlertDialog adExp = new AlertDialog.Builder(context).create();
                String msg = "Sorry " + sMovie.getsName() + " is currently not available in your region";
                adExp.setMessage(msg);
                adExp.setCancelable(true);
                adExp.setCanceledOnTouchOutside(true);
                adExp.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adExp.dismiss();
                    }
                });
                adExp.show();

            }
            if (fragment instanceof Info.MoreBottomDialogFragment) {
                ((Info.MoreBottomDialogFragment) fragment).dismiss();
            }

        });
    }

    /*private void bannerAdsShow(FrameLayout bannerAdView) {
        if (SaveSharedPreference.getAdsData(context) != null) {
            if (SaveSharedPreference.getAdsData(context).getGStatus() != null
                    && SaveSharedPreference.getAdsData(context).getGStatus().equals(API.ACTIVE)) {
                bannerAdView.removeAllViews();
                AdView adView = new AdView(context);
                adView.setAdSize(AdSize.BANNER);
                adView.setAdUnitId(SaveSharedPreference.getAdsData(context).getGBanner());
                AdRequest adRequest = new AdRequest.Builder().build();
                bannerAdView.addView(adView);
                adView.loadAd(adRequest);
            } else if (SaveSharedPreference.getAdsData(context).getMopubStatus() != null
                    && SaveSharedPreference.getAdsData(context).getMopubStatus().equals(API.ACTIVE)) {
                bannerAdView.removeAllViews();
                PublisherAdView adView = new PublisherAdView(context);
                adView.setAdSizes(AdSize.BANNER);
                adView.setAdUnitId(SaveSharedPreference.getAdsData(context).getMopubBanner());
                PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
                bannerAdView.addView(adView);
                adView.loadAd(adRequest);

            }
        }
    }*/

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgMovieIcon;

        public MyViewHolder(View view) {
            super(view);
            imgMovieIcon = view.findViewById(R.id.imgMovieIcon);
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}