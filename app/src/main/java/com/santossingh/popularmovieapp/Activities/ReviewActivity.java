package com.santossingh.popularmovieapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.santossingh.popularmovieapp.Adapters.ReviewsAdapter;
import com.santossingh.popularmovieapp.Models.ReviewsModels.MovieReviews;
import com.santossingh.popularmovieapp.Models.ReviewsModels.ReviewsResult;
import com.santossingh.popularmovieapp.R;
import com.santossingh.popularmovieapp.Services.DataManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {

    @Bind(R.id.ReviewRecyclerView)
    RecyclerView recyclerView;
    ReviewsAdapter recyclerAdapter;
    DataManager dataManager;
    private List<ReviewsResult> resultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        String movie_id = getIntent().getStringExtra("movie_id");
        configRecycleView(movie_id);
    }

    private void configRecycleView(String movie_id) {
        recyclerAdapter = new ReviewsAdapter(ReviewActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
        makeService(movie_id);
    }

    private void makeService(String movie_id){
        dataManager = new DataManager();
        Call<MovieReviews> listCall;
        listCall= dataManager.getJSONData().getMovieReviews(movie_id);
        listCall.enqueue(new Callback<MovieReviews>() {
            @Override
            public void onResponse(Call<MovieReviews> call, Response<MovieReviews> response) {
                if (response.isSuccessful()) {
                    if (response.body().getReviewsResults().length>0){
                        ReviewsResult[] results = response.body().getReviewsResults();
                        resultsList = new ArrayList<ReviewsResult>(Arrays.asList(results));
                        recyclerAdapter.addReviewsList(resultsList);
                    }else {
                        Toast.makeText(ReviewActivity.this, R.string.Review_empty, Toast
                                .LENGTH_LONG)
                                .show();
                        finish();
                    }
                }
            }
            @Override
            public void onFailure(Call<MovieReviews> call, Throwable t) {
                Toast.makeText(ReviewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
