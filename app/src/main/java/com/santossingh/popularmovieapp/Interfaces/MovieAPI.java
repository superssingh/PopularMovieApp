package com.santossingh.popularmovieapp.Interfaces;

import com.santossingh.popularmovieapp.Models.MovieResponse;
import com.santossingh.popularmovieapp.Models.ReviewsModels.MovieReviews;
import com.santossingh.popularmovieapp.Models.TrailerModels.MovieTrailer;
import com.santossingh.popularmovieapp.Database.Constant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Stark on 18/04/2016.
 */
public interface MovieAPI {
    //By Popularity-----------------------
    @GET("/3/movie/popular?"+ Constant.HTTP.KEY)
    Call<MovieResponse> getPopMovies();

    //By TopRating------------------------
    @GET("/3/movie/top_rated?"+Constant.HTTP.KEY)
    Call<MovieResponse> getTopMovies();

    //By TopRating------------------------
    @GET("/3/movie/{id}/reviews?"+Constant.HTTP.KEY)
    Call<MovieReviews> getMovieReviews(@Path("id") String id);

    //By TopRating------------------------
    @GET("/3/movie/{id}/videos?"+Constant.HTTP.KEY)
    Call<MovieTrailer> getMovieTrailer(@Path("id") String id);
}
