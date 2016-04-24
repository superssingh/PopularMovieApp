package com.santossingh.popularmovieapp.Models.ReviewsModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Stark on 18/04/2016.
 */
public class MovieReviews {
    private int id;
    int page;
    @SerializedName("results")
    private ReviewsResult[] reviewsResults;
    int total_pages;
    int total_results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ReviewsResult[] getReviewsResults() {
        return reviewsResults;
    }

    public void setReviewsResults(ReviewsResult[] reviewsResults) {
        this.reviewsResults = reviewsResults;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }
}
