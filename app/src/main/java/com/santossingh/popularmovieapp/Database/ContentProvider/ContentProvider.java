package com.santossingh.popularmovieapp.Database.ContentProvider;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.santossingh.popularmovieapp.Database.FavoriteMovies;
import com.santossingh.popularmovieapp.Models.Results;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Stark on 18/04/2016.
 */
public class ContentProvider {
  private Context context;
 //Add method with Realm Asynchronous Transactions---------------------------
 // for write data on background thread which avoid blocking the UI thread---

// adding current movie info which comes from Intent (Handset view)
        public void addMovieIntent(final Context context, final Intent intent) {
            this.context=context;
            Realm realm = Realm.getDefaultInstance();

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    FavoriteMovies favoriteMovies = bgRealm.createObject(FavoriteMovies.class);
                    favoriteMovies.setMovie_id(intent.getIntExtra("movie_Id", 0));
                    favoriteMovies.setTitle(intent.getStringExtra("movie_Name"));
                    favoriteMovies.setOverview(intent.getStringExtra("overview"));
                    favoriteMovies.setRelease_date(intent.getStringExtra("release_Date"));
                    favoriteMovies.setPoster_path(intent.getStringExtra("poster_Path"));
                    favoriteMovies.setBackdrop_path(intent.getStringExtra("back_poster_Path"));
                    favoriteMovies.setVote_average(intent.getFloatExtra("users_Rating", 0));
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Toast.makeText(context, "Movie marked successfully !", Toast.LENGTH_SHORT)
                            .show();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Toast.makeText(context,"Movie Already Exists !",Toast.LENGTH_SHORT).show();
                }
            });
        }

// adding selected movie info which comes from ArratList (Tablet view)
        public void addMovieResult(final Context context, final Results results) {
            this.context=context;
            Realm realm = Realm.getDefaultInstance();

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    FavoriteMovies favoriteMovies = bgRealm.createObject(FavoriteMovies.class);
                    favoriteMovies.setMovie_id(results.getId());
                    favoriteMovies.setTitle(results.getTitle());
                    favoriteMovies.setOverview(results.getOverview());
                    favoriteMovies.setRelease_date(results.getRelease_date());
                    favoriteMovies.setPoster_path(results.getPoster_path());
                    favoriteMovies.setBackdrop_path(results.getBackdrop_path());
                    favoriteMovies.setVote_average(results.getVote_average());
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Toast.makeText(context, "Movie marked successfully !", Toast.LENGTH_SHORT)
                            .show();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Toast.makeText(context, "Movie Already Exists !", Toast.LENGTH_SHORT).show();
                }
            });
        }

//Delete method for favorite list --------------------
    public void deleteMovie(int id) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<FavoriteMovies> result = realm.where(FavoriteMovies.class)
                .equalTo("movie_id", id)
                .findAllAsync();
        realm.beginTransaction();
        result.remove(0);
        realm.commitTransaction();
    }

}