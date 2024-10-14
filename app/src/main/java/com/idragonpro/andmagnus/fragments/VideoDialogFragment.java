package com.idragonpro.andmagnus.fragments;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.idragonpro.andmagnus.activities.PariSectionDetailActivity;
import com.idragonpro.andmagnus.R;

import org.jetbrains.annotations.NotNull;

public class VideoDialogFragment extends DialogFragment {

    private static PariSectionDetailActivity pariSectionDetailActivity;
    private PlayerView playerView;
    private ImageView imageViewCancel;
    private SimpleExoPlayer simpleExoPlayer;

    public static VideoDialogFragment newInstance(String videoUrl, PariSectionDetailActivity pariSectionDetailActivity) {
        VideoDialogFragment.pariSectionDetailActivity = pariSectionDetailActivity;
        VideoDialogFragment f = new VideoDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("VIDEO_URL", videoUrl);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video_dialog, container, false);
        playerView = v.findViewById(R.id.exoPlayer);
        imageViewCancel = v.findViewById(R.id.imageViewCancel);
        return v;
    }

    @Override
    public void setupDialog(@NonNull @NotNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        dialog.setCancelable(false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get video url
        String videoUrl = getArguments().getString("VIDEO_URL");

        //init video
        if (videoUrl != null) {

            simpleExoPlayer = new SimpleExoPlayer.Builder(this.getContext()).build();
            playerView.setPlayer(simpleExoPlayer);
            DataSource.Factory dataSourceFactory =
                    new DefaultDataSourceFactory(this.getContext(), Util.getUserAgent(this.getContext(), "iDragonPro"));
            ProgressiveMediaSource.Factory mediaSourceFactory =
                    new ProgressiveMediaSource.Factory(dataSourceFactory);

            MediaSource ObjMediaSource = mediaSourceFactory.createMediaSource(MediaItem.fromUri(Uri.parse(videoUrl)));
            simpleExoPlayer.prepare(ObjMediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
            //

            imageViewCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                    simpleExoPlayer.stop();
                    pariSectionDetailActivity.restartAudio();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(simpleExoPlayer!=null) {
            simpleExoPlayer.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(true);
        }
        int width = 1000;
        int height = 1000;
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(false);
        }
    }
}
