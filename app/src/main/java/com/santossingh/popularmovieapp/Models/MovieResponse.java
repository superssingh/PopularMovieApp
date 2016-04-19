package com.santossingh.popularmovieapp.Models;

/**
 * Created by Stark on 18/04/2016.
 */
public class MovieResponse {

    private Results[] results;
    private String page;
    private String total_pages;
    private String total_results;

    public Results[] getResults() {
        return results;
    }

    public void setResults(Results[] results) {
        this.results = results;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotalPages() {
        return total_pages;
    }

    public void setTotalPages(String totalPages) {
        this.total_pages = totalPages;
    }

    public String getTotalResults() {
        return total_results;
    }

    public void setTotalResults(String totalResults) {
        this.total_results = totalResults;
    }
}
