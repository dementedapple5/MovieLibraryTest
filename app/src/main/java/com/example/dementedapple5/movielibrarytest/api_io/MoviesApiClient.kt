package com.example.dementedapple5.movielibrarytest.api_io

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MoviesApiClient {

    //Method to return an instance of the APIService
    companion object {
        private var moviesApiService: MoviesApiService? = null

        fun getInstance(): MoviesApiService? {
            val client = OkHttpClient()
            val baseUrl = "https://api.themoviedb.org/3/"

            if (moviesApiService == null) {
                val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
                moviesApiService = retrofit.create(MoviesApiService::class.java)
            }
            return moviesApiService
        }
    }




}