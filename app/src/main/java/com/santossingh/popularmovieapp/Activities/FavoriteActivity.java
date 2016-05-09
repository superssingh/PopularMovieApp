package com.santossingh.popularmovieapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.santossingh.popularmovieapp.Adapters.FavoriteRecycleAdapter;
import com.santossingh.popularmovieapp.Adapters.Utilities.AutofitGridlayout;
import com.santossingh.popularmovieapp.Database.FavoriteMovies;
import com.santossingh.popularmovieapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class FavoriteActivity extends AppCompatActivity {
    private FavoriteRecycleAdapter recyclerAdapter;
    @Bind(R.id.recyclerListView) RecyclerView recyclerView;
    @Bind(R.id.TXT_emptyList) TextView emptyText;
    private Realm realm;
    private RealmResults<FavoriteMovies> results;
    View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
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
        AutofitGridlayout layoutManager = new AutofitGridlayout(FavoriteActivity.this, 320 );
        recyclerView.setHasFixedSize(true);
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerAdapter = new FavoriteRecycleAdapter(this,results);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }
}

