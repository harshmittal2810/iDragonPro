package com.idragonpro.andmagnus.activities;

import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.idragonpro.andmagnus.R;
import com.idragonpro.andmagnus.adapters.PariSectionDetailAdapter;
import com.idragonpro.andmagnus.models.pari_section_details_response.Section;

import java.util.List;

public class PariSectionDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = PariSectionDetailActivity.class.getSimpleName();
    private MediaPlayer mp;
    private ImageView imageViewBackArrow;
    private RecyclerView recyclerView;
    private FloatingActionButton fab,fab1,fab2,fab3;
    private boolean isFABOpen = false;
    private ObjectAnimator rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pari_section_detail);

        initViews();
        setClickListener();
        setRecyclerAdapter();
    }

    private void setRecyclerAdapter() {
        List<Section> sectionList = (List<Section>) getIntent().getExtras().getSerializable("SECTION");
        recyclerView.setAdapter(new PariSectionDetailAdapter(sectionList,this));
    }

    private void initViews() {
        imageViewBackArrow = findViewById(R.id.imageViewBackArrow);
        recyclerView = findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
    }

    private void setClickListener() {
        imageViewBackArrow.setOnClickListener(this);
    }

    private void startBGAudio() {
        mp = new MediaPlayer();
        try {
            mp.setDataSource(getIntent().getExtras().getString("MP3_URL"));//Write your location here
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    fab1.setImageDrawable(ContextCompat.getDrawable(PariSectionDetailActivity.this, R.drawable.exo_icon_play));
                    stopAnimation();
                }
            });
            mp.prepare();
            mp.start();
            startAnimation();
            fab3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mp!=null) {
                        mp.seekTo(0);
                        mp.pause();
                        fab1.setImageDrawable(ContextCompat.getDrawable(PariSectionDetailActivity.this, R.drawable.exo_icon_play));
                        stopAnimation();
                    }
                }
            });
            fab2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mp!=null) {
                        mp.seekTo(0);
                        mp.start();
                        fab1.setImageDrawable(ContextCompat.getDrawable(PariSectionDetailActivity.this, R.drawable.exo_icon_pause));
                        startAnimation();
                    }
                }
            });
            fab1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mp!=null) {
                        if (mp.isPlaying()) {
                            mp.pause();
                            fab1.setImageDrawable(ContextCompat.getDrawable(PariSectionDetailActivity.this, R.drawable.exo_icon_play));
                            stopAnimation();
                        } else {
                            mp.start();
                            fab1.setImageDrawable(ContextCompat.getDrawable(PariSectionDetailActivity.this, R.drawable.exo_icon_pause));
                            startAnimation();
                        }
                    }
                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isFABOpen){
                        showFABMenu();
                    }else{
                        closeFABMenu();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            fab.hide();
            fab1.hide();
            fab2.hide();
            fab3.hide();
        }

    }

    private void startAnimation(){
        rotate = ObjectAnimator.ofFloat(fab, "rotation", 0f, 20f, 0f, -20f, 0f); // rotate o degree then 20 degree and so on for one loop of rotation.
        rotate.setRepeatCount(100); // repeat the loop 20 times
        rotate.setDuration(100); // animation play time 100 ms
        rotate.start();
    }

    private void stopAnimation(){
        if(rotate!=null){
            rotate.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mp != null) {
            mp.start();
        }else {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startBGAudio();
                }
            }, 1000);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mp!=null) {
            mp.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mp!=null) {
            mp.stop();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imageViewBackArrow:
                onBackPressed();
                break;
        }
    }

    public void stopAudio(){
        if(mp!=null)
            mp.pause();
    }

    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
    }

    public void restartAudio() {
        if(mp!=null) {
            mp.start();
            fab1.setImageDrawable(ContextCompat.getDrawable(PariSectionDetailActivity.this, R.drawable.exo_icon_pause));
            startAnimation();
        }
    }
}