package com.idragonpro.andmagnus.radapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.activities.Info;
import com.idragonpro.andmagnus.api.API;
import com.idragonpro.andmagnus.beans.Movies;
import com.idragonpro.andmagnus.fragments.NewHomeFragment;
import com.idragonpro.andmagnus.models.Banners;

import java.util.List;

public class HomeRowRecyclerViewAdapter extends RecyclerView.Adapter<HomeRowRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = HomeRecyclerViewAdapter.class.getSimpleName();
    private final NewHomeFragment newHomeFragment;
    private final List<Banners> contentList;
    private final String tabName;
    private final String iType;
    public SimpleExoPlayer simpleExoPlayer;
    public MutableLiveData<SimpleExoPlayer> liveDataPlayer = new MutableLiveData<>();

    public HomeRowRecyclerViewAdapter(NewHomeFragment newHomeFragment, List<Banners> contentList, String tabName, String iType) {
        this.newHomeFragment = newHomeFragment;
        this.contentList = contentList;
        this.tabName = tabName;
        this.iType = iType;
    }

    @NonNull
    @Override
    public HomeRowRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (iType.equalsIgnoreCase(API.VERTICAL)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_row_item_vertical, parent, false);
        } else if (iType.equalsIgnoreCase(API.COMMING_SOON)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_row_item_comming, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_row_item, parent, false);
        }
        HomeRowRecyclerViewAdapter.ViewHolder viewHolder = new HomeRowRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRowRecyclerViewAdapter.ViewHolder holder, final int position) {
        Banners content = contentList.get(position);
        Movies videodetails = contentList.get(position).getVideodetails();
        if (content.getBannerUrl() != null && !content.getBannerUrl().isEmpty()) {
            Glide.with(holder.imgMovieBanner.getContext()).load(content.getBannerUrl()).centerCrop()
                .into(holder.imgMovieBanner);
        } else {
            Glide.with(holder.imgMovieBanner.getContext()).load(R.drawable.not_img).centerCrop()
                .into(holder.imgMovieBanner);
        }

        if (videodetails != null && videodetails.getTagPosition() != null && !videodetails.getTagPosition().isEmpty()) {
            if (videodetails.getTagPosition().equalsIgnoreCase(API.TOP_RIGHT)) {
                if (videodetails.getTagUrl() != null && !videodetails.getTagUrl().isEmpty()) {
                    holder.imgTagRight.setVisibility(View.VISIBLE);
                    Glide.with(holder.imgTagRight.getContext())
                        .load(videodetails.getTagUrl())
                        .centerCrop()
                        .into(holder.imgTagRight);
                    holder.imgTagLeft.setVisibility(View.GONE);
                } else {
                    holder.imgTagLeft.setVisibility(View.GONE);
                    holder.imgTagRight.setVisibility(View.GONE);
                }

            } else {
                if (videodetails.getTagUrl() != null && !videodetails.getTagUrl().isEmpty()) {
                    holder.imgTagLeft.setVisibility(View.VISIBLE);
                    Glide.with(holder.imgTagLeft.getContext())
                        .load(videodetails.getTagUrl())
                        .centerCrop()
                        .into(holder.imgTagLeft);
                    holder.imgTagRight.setVisibility(View.GONE);
                } else {
                    holder.imgTagLeft.setVisibility(View.GONE);
                    holder.imgTagRight.setVisibility(View.GONE);
                }
            }
        } else {
            holder.imgTagLeft.setVisibility(View.GONE);
            holder.imgTagRight.setVisibility(View.GONE);
        }
        holder.imgMovieBanner.setOnClickListener(v -> {
            Intent intent = new Intent(newHomeFragment.getActivity(), Info.class);
            intent.putExtra("movie", videodetails);
            intent.putExtra("from", tabName);
            newHomeFragment.getActivity().startActivity(intent);
        });
        /*  }*/

    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final PlayerView playerView;
        private final ImageView imageViewPlay;

        private final ImageView imgMovieBanner, imgTagRight, imgTagLeft;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMovieBanner = itemView.findViewById(R.id.imgMovieBanner);
            imgTagRight = itemView.findViewById(R.id.imgTagRight);
            imgTagLeft = itemView.findViewById(R.id.imgTagLeft);
            playerView = itemView.findViewById(R.id.exoPlayer);
            imageViewPlay = itemView.findViewById(R.id.imageViewPlay);
        }
    }

}
