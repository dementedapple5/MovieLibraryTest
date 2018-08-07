package com.example.dementedapple5.movielibrarytest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.dementedapple5.movielibrarytest.api_io.MoviesApiClient
import com.example.dementedapple5.movielibrarytest.api_io.MoviesResponse
import com.example.dementedapple5.movielibrarytest.models.Movie
import com.example.dementedapple5.movielibrarytest.utils.EndlessScrollListener
import kotlinx.android.synthetic.main.activity_movie_list.*
import kotlinx.android.synthetic.main.nav_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

const val initialPage = 1
const val apiKey = "93aea0c77bc168d8bbce3918cefefa45"
const val language = "en-US"

class MovieListActivity : AppCompatActivity() {

    var mMovies: ArrayList<Movie> = ArrayList()
    lateinit var linearLayoutManager: LinearLayoutManager
    var currentPage = initialPage
    var isLastPage = false
    var isLoading = false
    var totalPages: Int = 0
    lateinit var mAdapter: MovieAdapter

    private lateinit var scrollListener: EndlessScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        setSupportActionBar(mToolBar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        mAdapter = MovieAdapter(mMovies, applicationContext)
        linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        //Triggered when the list reaches the end of the page
        scrollListener = object : EndlessScrollListener(linearLayoutManager) {
            override val totalPageCount: Int
                get() = totalPages
            override val isLastPageScroll: Boolean
                get() = isLastPage
            override val isLoadingScroll: Boolean
                get() = isLoading

            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1
                populateMovies()
            }
        }


        movies_rv.addOnScrollListener(scrollListener)

        movies_rv.layoutManager = linearLayoutManager


        //Adding the searching service when typing.
        search_tool_et.addTextChangedListener(object: TextWatcher{

            //When text changes the adapter cleans the data and get a new one(based on the query) and the current page is set to 1.
            override fun afterTextChanged(p0: Editable?) {
                if (!p0!!.isBlank()){
                    val textQuery = p0.toString()
                    currentPage = initialPage
                    mAdapter.clear()
                    val call: Call<MoviesResponse> = MoviesApiClient.getInstance()!!.searchMovies(apiKey, language, textQuery, currentPage)
                    load_movies_pb.visibility = View.VISIBLE
                    call.enqueue(FirstPageCallBack())
                }
                //If there's no text the adapter cleans the data and get the most popular movies again.
                else {
                    mAdapter.clear()
                    currentPage = 1
                    populateMovies()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

        populateMovies()
    }

    //Make the call to the API
    private fun populateMovies() {

        //If the current page is 1 execute the FirstPageCallBack
        if (currentPage == 1){
            val call: Call<MoviesResponse> = MoviesApiClient.getInstance()!!.getPopularMovies(apiKey, language, currentPage)
            load_movies_pb.visibility = View.VISIBLE
            call.enqueue(FirstPageCallBack())
        }

        /*If the current page is higher than 1, first check if the edittext is empty,
        if it is then the app will search for more movies that match the text entered
        otherwise the app will retrieve another page of most popular movies
        * */
        else {
            if(!search_tool_et.text.isBlank()) {
                val textQuery = search_tool_et.text.toString()
                val call: Call<MoviesResponse> = MoviesApiClient.getInstance()!!.searchMovies(apiKey, language, textQuery, currentPage)
                call.enqueue(NextPageCallBack())
            }else {
                val call: Call<MoviesResponse> = MoviesApiClient.getInstance()!!.getPopularMovies(apiKey, language, currentPage)
                mAdapter.addLoadingFooter()
                call.enqueue(NextPageCallBack())

            }

        }
    }

    //Handle the response for the first page
    inner class FirstPageCallBack : Callback<MoviesResponse> {


        override fun onFailure(call: Call<MoviesResponse>?, t: Throwable?) {
            Toast.makeText(applicationContext, "Internet connection error", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<MoviesResponse>?, response: Response<MoviesResponse>?) {
            if (response != null) {
                if (response.isSuccessful) {
                    val moviesResponse = response.body()
                    if (moviesResponse != null) {

                        totalPages = moviesResponse.totalPages!!

                        load_movies_pb.visibility = View.GONE
                        mAdapter.addAll(moviesResponse.movies)

                        movies_rv.adapter = mAdapter

                        if (currentPage <= totalPages) mAdapter.addLoadingFooter()
                        else isLastPage = true


                        Log.d("SIZE::", mAdapter.mMovies.size.toString())
                    }
                }
            }
        }
    }


    //Handle the response for the next pages
    inner class NextPageCallBack : Callback<MoviesResponse> {
        override fun onFailure(call: Call<MoviesResponse>?, t: Throwable?) {
            Toast.makeText(applicationContext, "Internet connection error", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<MoviesResponse>?, response: Response<MoviesResponse>?) {
            if (response != null) {
                if (response.isSuccessful) {
                    val moviesResponse = response.body()
                    if (moviesResponse != null) {

                        mAdapter.removeLoadingFooter()
                        isLoading = false

                        mAdapter.addAll(moviesResponse.movies)

                        if (currentPage != totalPages) mAdapter.addLoadingFooter()
                        else isLastPage = true

                        Log.d("SIZE::", mAdapter.mMovies.size.toString())
                    }
                }
            }
        }
    }




}


