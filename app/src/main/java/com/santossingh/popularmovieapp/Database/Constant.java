package com.santossingh.popularmovieapp.Database;

/**
 * Created by Stark on 18/04/2016.
 */
public class Constant {
    public static final class HTTP {
// API-Keys------------------------
public static final String MOVIE_API_KEY = "xxxxxxxxxxx";
        public static final String YOUTUBE_API_KEY = "xxxxxxxxxxx";
//URLs-----------------------------
        public static final String BASE_URL="http://api.themoviedb.org";
        public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";
        public static final String BACKPOSTER_BASE_URL = "http://image.tmdb.org/t/p/w500";

        public static final String KEY= "&api_key="+MOVIE_API_KEY;

        public static final String VIDEO_TRAILER= "videos?api_key="+MOVIE_API_KEY;
    }

}
