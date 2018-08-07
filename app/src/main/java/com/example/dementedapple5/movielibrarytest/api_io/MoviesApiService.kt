package com.example.dementedapple5.movielibrarytest.api_io

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {

    //Method to get the most popular movies
    @GET("movie/popular")
    fun getPopularMovies(
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
            @Query("page") page: Int
            ): Call<MoviesResponse>

    //Method to search movies passing a query
    @GET("search/movie")
    fun searchMovies(
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
            @Query("query") query: String,
            @Query("page") page: Int
    ): Call<MoviesResponse>
}