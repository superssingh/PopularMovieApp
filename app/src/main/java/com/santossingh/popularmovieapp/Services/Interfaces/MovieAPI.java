package com.santossingh.popularmovieapp.Services.Interfaces;

import com.santossingh.popularmovieapp.Models.MovieResponse;
import com.santossingh.popularmovieapp.Models.MovieTrailer;
import com.santossingh.popularmovieapp.Services.Constant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Stark on 18/04/2016.
 */
public interface MovieAPI {
    //By Popularity-----------------------
    @GET("/3/discover/movie?"+ Constant.HTTP.POPULAR)
    Call<MovieResponse> getPopMovies();

    //By TopRating------------------------
    @GET("/3/discover/movie?"+Constant.HTTP.TOP_RATED)
    Call<MovieResponse> getTopMovies();

    //By TopRating------------------------
    @GET("/3/movie/{id}/"+Constant.HTTP.VIDEO_TRAILER)
    Call<MovieTrailer> getMovieTrailer(@Path("id") String id);
}
