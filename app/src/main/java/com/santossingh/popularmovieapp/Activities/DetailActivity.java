package com.santossingh.popularmovieapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.santossingh.popularmovieapp.Fragments.DetailFragment;
import com.santossingh.popularmovieapp.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent= getIntent();
        DetailFragment detailFragment =(DetailFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_detail);

        detailFragment.setDataforHandsetUI(intent);
    }

}