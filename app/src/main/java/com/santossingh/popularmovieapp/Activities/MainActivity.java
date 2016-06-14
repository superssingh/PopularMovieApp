package com.santossingh.popularmovieapp.Activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.santossingh.popularmovieapp.Adapters.RecycleAdapter;
import com.santossingh.popularmovieapp.Fragments.BaseFragment;
import com.santossingh.popularmovieapp.Fragments.DetailFragment;
import com.santossingh.popularmovieapp.Models.Results;
import com.santossingh.popularmovieapp.R;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener{

    RecycleAdapter recyclerAdapter;
    BroadcastReceiver receiver;
    Intent intent;
    private Realm realm;
    private Results results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder
                (MainActivity.this)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }

    @Override
    public void onTabletListener(Results result) {

        DetailFragment detailFragment = (DetailFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_detail);

        if (detailFragment != null) {
            detailFragment.updateTabletUI(result);
        }
    }

    @Override
    public void onFragmentInteraction(Results currentMovie) {
        DetailFragment detailFragment = (DetailFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_detail);

        if (detailFragment == null) {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra("movie_Id", currentMovie.getId())
                    .putExtra("movie_Name", currentMovie.getTitle())
                    .putExtra("poster_Path", currentMovie.getPoster_path())
                    .putExtra("back_poster_Path", currentMovie.getBackdrop_path())
                    .putExtra("release_Date", currentMovie.getRelease_date())
                    .putExtra("users_Rating", currentMovie.getVote_average())
                    .putExtra("overview", currentMovie.getOverview());
            startActivity(intent);
        } else {
            detailFragment.updateTabletUI(currentMovie);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.favoriteList) {
            Intent intent=new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
