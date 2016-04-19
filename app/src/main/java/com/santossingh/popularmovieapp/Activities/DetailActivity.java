package com.santossingh.popularmovieapp.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.santossingh.popularmovieapp.R;

import layout.DetailFragment;

public class DetailActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent= getIntent();
        DetailFragment detailFragment=(DetailFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_detail);

        detailFragment.setDataforHandsetUI(intent);
    }

}