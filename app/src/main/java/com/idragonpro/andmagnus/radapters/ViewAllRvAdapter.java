package com.idragonpro.andmagnus.radapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.activities.Info;
import com.idragonpro.andmagnus.activities.ViewAllVideos;
import com.idragonpro.andmagnus.beans.Movies;
import com.idragonpro.andmagnus.models.Banners;

import java.util.List;

public class ViewAllRvAdapter extends RecyclerView.Adapter<ViewAllRvAdapter.ViewHolder> {

    private static final String TAG = ViewAllRvAdapter.class.getSimpleName();
    private final ViewAllVideos viewAllVideos;
    private final List<Banners> moviesList;

    public ViewAllRvAdapter(ViewAllVideos viewAllVideos, List<Banners> moviesList) {
        this.viewAllVideos = viewAllVideos;
        this.moviesList = moviesList;
    }

    @NonNull
    @Override
    public ViewAllRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        ViewAllRvAdapter.ViewHolder viewHolder = new ViewAllRvAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllRvAdapter.ViewHolder holder, int position) {
        Banners movie = moviesList.get(position);
        Movies movies = movie.getVideodetails();
        if (movies.getsSmallBanner() != null && !movies.getsSmallBanner()
            .equalsIgnoreCase("null") && !movies.getsSmallBanner().isEmpty()) {
            Glide.with(holder.imgMovieBanner.getContext())
                .load(movies.getsSmallBanner())
                .centerCrop()
                .into(holder.imgMovieBanner);
        } else {
            Glide.with(holder.imgMovieBanner.getContext()).load(R.drawable.not_img)
                .centerCrop()
                .into(holder.imgMovieBanner);
        }

        holder.tvGenre.setText(movies.getsGenre());
        Log.d(TAG, "onBindViewHolder: " + movies.getsName());
        holder.tvName.setText(movies.getsName());
        holder.tvYear.setText(movies.getsYear());
        holder.llMoviesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewAllVideos, Info.class);
                intent.putExtra("movie", movies);
                viewAllVideos.startActivity(intent);
               /* boolean isContentAvailable = false;
                for (int i=0;i<listOfStringfromString.size();i++) {
                    if (GlobalModule.sCountry.equalsIgnoreCase(listOfStringfromString.get(i)) || listOfStringfromString.get(i).equalsIgnoreCase("Worldwide")) {
                        isContentAvailable = true;
                        Intent intent = new Intent(viewAllVideos, Info.class);
                        intent.putExtra("movie", movie);
                        viewAllVideos.startActivity(intent);
                    }
                }
                if(!isContentAvailable){
                    final AlertDialog adExp = new AlertDialog.Builder(viewAllVideos).create();
                    String msg = "Sorry "+movie.getsName()+" is currently not available in your region";
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

                }*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgMovieBanner;
        private final TextView tvName, tvGenre, tvYear;
        private final RelativeLayout llMoviesList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMovieBanner = (ImageView) itemView.findViewById(R.id.imgMovieIcon);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvGenre = (TextView) itemView.findViewById(R.id.tvGenre);
            tvYear = (TextView) itemView.findViewById(R.id.tvYear);
            llMoviesList = (RelativeLayout) itemView.findViewById(R.id.llMoviesList);
        }
    }
}
