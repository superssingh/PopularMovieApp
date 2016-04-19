package com.santossingh.popularmovieapp.Services;

/**
 * Created by Stark on 18/04/2016.
 */
public class Constant {
    public static final class HTTP {
// API-Keys------------------------
        public static final String MOVIE_API_KEY = "f074b4f71849509c940cd25d16e9cab9";
        public static final String YOUTUBE_API_KEY = "AIzaSyBXOJsdlSoSMnZOxIR_b73bNhiwoxB2pdI";
//URLs-----------------------------
        public static final String BASE_URL="http://api.themoviedb.org";
        public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";
        public static final String BACKPOSTER_BASE_URL = "http://image.tmdb.org/t/p/w500";

//Queries -------------------------
        public static final String POPULAR= "sort_by=popularity.desc&api_key="+MOVIE_API_KEY;
        public static final String TOP_RATED= "certification_country=US&certification=R&" +
                "sort_by=vote_average.desc&api_key="+MOVIE_API_KEY;

//Query for youtbe video trailer key
        public static final String VIDEO_TRAILER= "videos?api_key="+MOVIE_API_KEY;
    }
}
