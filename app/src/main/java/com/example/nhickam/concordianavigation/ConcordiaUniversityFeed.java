package com.example.nhickam.concordianavigation;

import com.example.nhickam.concordianavigation.model.Feed;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by nhickam on 11/2/2017.
 */

public interface ConcordiaUniversityFeed {

    //String FEED_URL = "http://www.concordia.edu/calendar/";

    @GET("http://www.concordia.edu/calendar/rss/")
    Call<Feed> getFeed();

}
