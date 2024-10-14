package com.idragonpro.andmagnus.radapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.idragonpro.andmagnus.MyApp;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.activities.Info;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.api.WebApi;
import com.idragonpro.andmagnus.beans.Movies;
import com.idragonpro.andmagnus.helpers.SaveSharedPreference;
import com.idragonpro.andmagnus.models.Banners;
import com.idragonpro.andmagnus.responseModels.GeneralResponseModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BannerAdapter extends SliderViewAdapter<BannerAdapter.SliderAdapterViewHolder> {
    private static final String TAG = BannerAdapter.class.getSimpleName();
    private Context ctx;
    private LayoutInflater layoutInflater;
    private List<Banners> banners;
    private int mCurrentPosition;

    public BannerAdapter(Context ctx, List<Banners> banners) {
        this.banners = banners;
        this.ctx = ctx;
    }

    private void addWishList(Movies sMovie) {
        WebApi webApi = MyApp.Companion.getInstance().createRetrofitNewInstance();
        Call<GeneralResponseModel> call = webApi.addWishList(SaveSharedPreference.getUserId(ctx), sMovie.getsVedioId());
        call.enqueue(new Callback<GeneralResponseModel>() {
            @Override
            public void onResponse(Call<GeneralResponseModel> call, Response<GeneralResponseModel> response) {
                if (response.body() != null && response.body().getStatus() != null) {
                    if (response.body().getStatus().equalsIgnoreCase(API.SUCCESSFULL_STATUS)) {
                        Toast.makeText(ctx, ctx.getString(R.string.added_to_watchlist), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ctx, ctx.getString(R.string.already_in_watchlist), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ctx, ctx.getString(R.string.already_in_watchlist), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResponseModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(ctx, ctx.getString(R.string.already_in_watchlist), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_layout, null);
        return new SliderAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        Movies sMovie = banners.get(position).getVideodetails();
        if (banners.get(position).getBannerUrl() != null && !banners.get(position).getBannerUrl().isEmpty()) {
            Glide.with(viewHolder.ivImage.getContext()).load(banners.get(position).getBannerUrl()).centerCrop()
                .into(viewHolder.ivImage);
        }

        viewHolder.item_view.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, Info.class);
            intent.putExtra("movie", sMovie);
            ctx.startActivity(intent);
        });

        viewHolder.imgWatchlist.setOnClickListener(v -> addWishList(sMovie));

        viewHolder.imgShare.setOnClickListener(v -> {
            String sUrl = "https://idragonpro.com/info.php?play=" + sMovie.getsVedioId();
            //String sUrl = "https://play.google.com/store/apps/details?id=com.idragonpro.andmagnus&hl=en" + sMovie.getsVedioId();

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT,
                ctx.getString(R.string.hey_watch) + sMovie.getsName() + ctx.getString(R.string.check_out_idragon) + sUrl);
            sharingIntent.putExtra("from", "info");
            ctx.startActivity(Intent.createChooser(sharingIntent, "Share via"));

        });
    }

    @Override
    public int getCount() {
        return banners.size();
    }

    static class SliderAdapterViewHolder extends BannerAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        ImageView ivImage, imgWatchlist, imgShare;
        View item_view;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            imgWatchlist = (ImageView) itemView.findViewById(R.id.imgWatchlist);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            imgShare = (ImageView) itemView.findViewById(R.id.imgShare);
            this.item_view = itemView;

        }
    }

}
