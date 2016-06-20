package com.santossingh.popularmovieapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.santossingh.popularmovieapp.Adapters.FavoriteRecycleAdapter;
import com.santossingh.popularmovieapp.Database.FavoriteMovies;
import com.santossingh.popularmovieapp.R;
import com.santossingh.popularmovieapp.Utilities.AutoFitGridLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class FavoriteActivity extends AppCompatActivity implements FavoriteRecycleAdapter.GetDataFromAdapter {
    @Bind(R.id.recyclerListView) RecyclerView recyclerView;
    @Bind(R.id.TXT_emptyList) TextView emptyText;
    private FavoriteRecycleAdapter recyclerAdapter;
    private Realm realm;
    private RealmResults<FavoriteMovies> results;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        // Initialize Realm ----------------------
        realm = Realm.getDefaultInstance();
        getFavoriteList();
    }

    private void getFavoriteList() {
        results = realm.where(FavoriteMovies.class).findAll();
        if (results.size()==0){
            recyclerView.setVisibility(View.GONE);
            emptyText.setText(R.string.Favorite_empty_list);
            emptyText.setVisibility(View.VISIBLE);
        }else{
            configRecycleView(results);
        }
    }
    private void configRecycleView(RealmResults<FavoriteMovies> results) {
        AutoFitGridLayout layoutManager = new AutoFitGridLayout(FavoriteActivity.this, 320 );
        recyclerView.setHasFixedSize(true);
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerAdapter = new FavoriteRecycleAdapter(this, results, FavoriteActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onCurrentMovie(FavoriteMovies currentMovie) {
        Intent intent = new Intent(this, DetailActivity.class)
                .putExtra("movie_Id", currentMovie.getMovie_id())
                .putExtra("movie_Name", currentMovie.getTitle())
                .putExtra("poster_Path", currentMovie.getPoster_path())
                .putExtra("back_poster_Path", currentMovie.getBackdrop_path())
                .putExtra("release_Date", currentMovie.getRelease_date())
                .putExtra("users_Rating", currentMovie.getVote_average())
                .putExtra("overview", currentMovie.getOverview());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFavoriteList();
    }
}

