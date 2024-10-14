package com.idragonpro.andmagnus.radapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.activities.Info;
import com.idragonpro.andmagnus.activities.SearchActivity;
import com.idragonpro.andmagnus.beans.Movies;

import java.util.List;

public class SearchRVAdapter extends RecyclerView.Adapter<SearchRVAdapter.ViewHolder> {

    private static final String TAG = SearchRVAdapter.class.getSimpleName();
    private final SearchActivity searchActivity;
    private final List<Movies> moviesList;

    public SearchRVAdapter(SearchActivity searchActivity, List<Movies> moviesList) {
        this.searchActivity = searchActivity;
        this.moviesList = moviesList;
    }

    @NonNull
    @Override
    public SearchRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_new_item, parent, false);
        SearchRVAdapter.ViewHolder viewHolder = new SearchRVAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRVAdapter.ViewHolder holder, int position) {
        Movies movie = moviesList.get(position);
        if (movie.getsSmallBanner() != null && !movie.getsSmallBanner()
            .equalsIgnoreCase("null") && !movie.getsSmallBanner().isEmpty()) {
            Glide.with(holder.imgMovieBanner.getContext())
                .load(movie.getsSmallBanner())
                .centerCrop()
                .into(holder.imgMovieBanner);
        } else {
            Glide.with(holder.imgMovieBanner.getContext()).load(R.drawable.not_img)
                .centerCrop()
                .into(holder.imgMovieBanner);
        }
        holder.imgMovieBanner.setOnClickListener(view -> {
            Intent intent = new Intent(searchActivity, Info.class);
            intent.putExtra("movie", movie);
            searchActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + moviesList.size());
        return moviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgMovieBanner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMovieBanner = itemView.findViewById(R.id.imgMovieIcon);
        }
    }
}
