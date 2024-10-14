package com.idragonpro.andmagnus.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.beans.Movies;

public class ExtraDetailsFragment extends Fragment {

    private Context context;
    private View rootView;
    private Bundle bundle;
    TextView tvGenre, tvYear, tvTime, tvDirectors, tvCast, tvLanguage;
    private Movies sMovie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        rootView = inflater.inflate(R.layout.extra_details, container, false);
        bundle = getArguments();
        sMovie = (Movies) bundle.getSerializable("Movies");

        tvGenre = (TextView) rootView.findViewById(R.id.tvGenre);
        tvDirectors = (TextView) rootView.findViewById(R.id.tvDirectors);
        tvCast = (TextView) rootView.findViewById(R.id.tvCast);
        tvLanguage = (TextView) rootView.findViewById(R.id.tvLanguage);


        tvGenre.setText(sMovie.getsGenre());
        tvDirectors.setText(sMovie.getsDirector());
        tvCast.setText(sMovie.getsCast());
        tvLanguage.setText(sMovie.getLanguage_category());
        return rootView;
    }

}
