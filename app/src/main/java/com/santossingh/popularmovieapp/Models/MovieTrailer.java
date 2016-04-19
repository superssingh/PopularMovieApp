package com.santossingh.popularmovieapp.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Stark on 18/04/2016.
 */
public class MovieTrailer {
    private int id;
    @SerializedName("results")
    private TrailerResult[] trailerResults;

    public TrailerResult[] getTrailerResults() {
        return trailerResults;
    }

    public void setTrailerResults(TrailerResult[] trailerResults) {
        this.trailerResults = trailerResults;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
