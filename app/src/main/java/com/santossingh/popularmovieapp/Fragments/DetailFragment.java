package com.santossingh.popularmovieapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.santossingh.popularmovieapp.Activities.ReviewActivity;
import com.santossingh.popularmovieapp.Activities.VideoActivity;
import com.santossingh.popularmovieapp.Database.ContentProvider.ContentProvider;
import com.santossingh.popularmovieapp.Models.Results;
import com.santossingh.popularmovieapp.Models.TrailerModels.MovieTrailer;
import com.santossingh.popularmovieapp.R;
import com.santossingh.popularmovieapp.Services.DataManager;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Stark on 24/04/2016.
 */
public class DetailFragment extends android.app.Fragment {

    @Bind(R.id.movieTitle) TextView movie_Title;
    @Bind(R.id.moviePoster) ImageView movie_Poster;
    @Bind(R.id.releaseDate) TextView movie_ReleaseDate;
    @Bind(R.id.rating_Percent) TextView movie_UsersRating;
    @Bind(R.id.ratingBar) RatingBar movie_RatingStars;
    @Bind(R.id.overViewDetail) TextView movie_Overview;
    @Bind(R.id.BTN_favorite) Button favorite;
    @Bind(R.id.BTN_play) Button play;
    @Bind(R.id.BTN_review) Button review;
    @Bind(R.id.TXT_trailerstatus) TextView trailerStatus;
    @Bind(R.id.details) LinearLayout linearLayout;

    private String videoKey=null;
    private int movie_id;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_detail,container,false);
        ButterKnife.bind(this,view);
        Animation animation= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_in);
        linearLayout.setAnimation(animation);
        return view;
    }

    // Method for Tablet screen--------------
    public void updateTabletUI(final Results result){
        movie_id=result.getId();
        Picasso.with(getActivity())
                .load("http://image.tmdb.org/t/p/w185/" + result.getPoster_path())
                .resize(250, 320)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(movie_Poster);

        movie_Title.setText(result.getTitle());
        movie_ReleaseDate.setText(result.getRelease_date().substring(0, 4));
        movie_UsersRating.setText(result.getVote_average() + "/10");
        movie_RatingStars.setRating(result.getVote_average() * 5 / 10);
        movie_Overview.setText(result.getOverview());

        getTrailer(Integer.toString(result.getId())); // getting trailer key into videoKey varialbe
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentProvider contentProvider = new ContentProvider();
                contentProvider.addMovieResult(v.getContext(), result);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), VideoActivity.class)
                        .putExtra("videoKey",videoKey);
                startActivity(intent);
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ReviewActivity.class)
                        .putExtra("movie_id", Integer.toString(movie_id));
                startActivity(intent);
            }
        });

    }

    // Method for Handset screen--------------
    public void setDataforHandsetUI(final Intent intent){
        movie_id=intent.getIntExtra("movie_Id", 0);

        String movie_name=intent.getStringExtra("movie_Name");
        String movie_poster=intent.getStringExtra("poster_Path");
        String movie_backposter=intent.getStringExtra("back_poster_Path");
        String release_date=intent.getStringExtra("release_Date");
        Float users_rating=intent.getFloatExtra("users_Rating", 0);
        String overview=intent.getStringExtra("overview");

        Picasso.with(getActivity())
                .load("http://image.tmdb.org/t/p/w185/" + movie_poster)
                .resize(250, 320)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(movie_Poster);

        movie_Title.setText(movie_name);
        movie_ReleaseDate.setText(release_date.substring(0,4));
        movie_UsersRating.setText(users_rating + "/10");
        movie_RatingStars.setRating(users_rating * 5 / 10);
        movie_Overview.setText(overview);

        getTrailer(Integer.toString(movie_id)); // getting trailer key into videoKey varialbe

        //add data in Favorite List -----------------
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentProvider contentProvider = new ContentProvider();
                contentProvider.addMovieIntent(v.getContext(),intent);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), VideoActivity.class)
                        .putExtra("videoKey",videoKey);
                startActivity(intent);
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ReviewActivity.class)
                        .putExtra("movie_id", Integer.toString(movie_id));
                startActivity(intent);
            }
        });
    }

    // getting trailer key ----------------------------------
    public void getTrailer(String id){
        DataManager dataManager = new DataManager();
        Call<MovieTrailer> trailerCall= dataManager.getJSONData().getMovieTrailer(id);
        trailerCall.enqueue(new Callback<MovieTrailer>() {
            @Override
            public void onResponse(Call<MovieTrailer> call, Response<MovieTrailer> response) {
                if (response.isSuccessful()) {
                    if (response.body().getTrailerResults().length == 0) {
                        Toast.makeText(getActivity(), "Trailer unavailable",
                                Toast.LENGTH_SHORT).show();
                        trailerStatus.setText("Unavailable");
                        play.setVisibility(View.INVISIBLE);
                    } else {
                        videoKey = response.body().getTrailerResults()[0].getKey();
                        trailerStatus.setText("Available");
                        play.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieTrailer> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
