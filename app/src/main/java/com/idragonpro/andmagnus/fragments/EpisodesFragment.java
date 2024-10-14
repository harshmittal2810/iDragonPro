package com.idragonpro.andmagnus.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.activities.Info;
import com.idragonpro.andmagnus.adapters.EpisodeAdapter;
import com.idragonpro.andmagnus.beans.Movies;

import java.util.ArrayList;

public class EpisodesFragment extends Fragment {

    private Context context;
    private View rootView;
    private Bundle bundle;
    private Movies sMovie;
    private RecyclerView recyclerView;
    private ArrayList<Movies> episodeList;
    private EpisodeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        rootView = inflater.inflate(R.layout.episodes_fragment, container, false);
        bundle = getArguments();
        sMovie = (Movies) bundle.getSerializable("Movies");
        recyclerView = rootView.findViewById(R.id.recyclerView);
        if (sMovie != null && sMovie.getAllepisodes() != null && sMovie.getAllepisodes().size() > 0) {
            episodeList = new ArrayList<>();
            for (int i = 0; i < sMovie.getAllepisodes().size(); i++) {
                Movies movies = Movies.cloneMovies(sMovie);
                movies.setVideoUrl(sMovie.getAllepisodes().get(i).getLink());
                movies.setsEpisode(sMovie.getAllepisodes().get(i).getEpisodes_no());
                movies.setsSmallBanner(sMovie.getAllepisodes().get(i).getBanner());

                episodeList.add(movies);
            }
            mAdapter = new EpisodeAdapter(context, episodeList, (Info) getActivity(), this);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setHasFixedSize(true);

            // use a linear layout manager
            recyclerView.setLayoutManager( new GridLayoutManager(context, 2));
        }
        return rootView;
    }
}
