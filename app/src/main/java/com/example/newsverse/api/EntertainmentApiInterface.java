package com.example.newsverse.api;

import com.example.newsverse.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EntertainmentApiInterface {

    @GET("top-headlines?category=entertainment")
    Call<News> getNews(

            @Query("country") String country,
            @Query("apiKey") String apiKey

    );
}