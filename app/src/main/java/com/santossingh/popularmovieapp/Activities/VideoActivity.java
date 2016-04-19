package com.santossingh.popularmovieapp.Activities;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.santossingh.popularmovieapp.R;
import com.santossingh.popularmovieapp.Services.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoActivity extends YouTubeBaseActivity {
    @Bind(R.id.youtubeView)
    YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private String videoKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ButterKnife.bind(this);
        videoKey=getIntent().getStringExtra("videoKey");
        Play();
    }

    public void Play(){
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(videoKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(VideoActivity.this, youTubeInitializationResult.toString(), Toast
                        .LENGTH_SHORT).show();
            }
        };

        youTubePlayerView.initialize(Constant.HTTP.YOUTUBE_API_KEY, onInitializedListener);
    }
}
