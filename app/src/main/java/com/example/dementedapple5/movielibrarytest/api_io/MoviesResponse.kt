package com.example.dementedapple5.movielibrarytest.api_io

import com.example.dementedapple5.movielibrarytest.models.Movie
import com.google.gson.annotations.SerializedName

class MoviesResponse {

    @SerializedName("results")
    var movies: ArrayList<Movie> = ArrayList()

    var page: Int? = null

   @SerializedName("total_pages")
    var totalPages: Int? = null

    @SerializedName("total_results")
    var totalResults: Int? = null

}