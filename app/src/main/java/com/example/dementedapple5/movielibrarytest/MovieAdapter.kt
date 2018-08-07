package com.example.dementedapple5.movielibrarytest

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.dementedapple5.movielibrarytest.models.Movie
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieAdapter(val mMovies: ArrayList<Movie>, val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val notLoading = 0
    private val loading = 1

    private var isLoading = false


    companion object {
        class MovieViewHolder(var mMovieItemCL: ConstraintLayout): RecyclerView.ViewHolder(mMovieItemCL)
        class LoadViewHolder(var mLoadItemCL: ConstraintLayout): RecyclerView.ViewHolder(mLoadItemCL)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v1: View = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        val v2: View = LayoutInflater.from(parent.context).inflate(R.layout.loading_item, parent, false)

        val viewHolder: RecyclerView.ViewHolder

        viewHolder =  when(viewType) {
            loading -> LoadViewHolder(v2 as ConstraintLayout)
            else -> MovieViewHolder(v1 as ConstraintLayout)
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return mMovies.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mMovies.size-1 && isLoading) loading else notLoading
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {

        if (holder is MovieViewHolder) {
            val imageBaseUrl = "http://image.tmdb.org/t/p/w185/"
            Glide.with(context).load("$imageBaseUrl${mMovies[pos].poster_path}").into(holder.mMovieItemCL.movie_img)
            holder.mMovieItemCL.movie_desc_tv.text = mMovies[pos].overview
            holder.mMovieItemCL.movie_title_tv.text = mMovies[pos].title
            holder.mMovieItemCL.movie_rd_tv.text = mMovies[pos].release_date
        }else {

        }

    }

    fun add(mv: Movie) {
        mMovies.add(mv)
        notifyItemInserted(mMovies.size - 1)
    }

    fun addAll(mvList: List<Movie>) {
        for (mv in mvList) {
            add(mv)
        }
    }


    private fun remove(city: Movie) {
        val position = mMovies.indexOf(city)
        if (position > -1) {
            mMovies.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoading = false
        while (itemCount > 0) {
            remove(mMovies[0])
        }
    }





    fun addLoadingFooter() {
        isLoading = true
    }

    fun removeLoadingFooter() {
        isLoading = false

        val position = mMovies.size - 1
        val item = mMovies[position]

        mMovies.removeAt(position)
        notifyItemRemoved(position)
    }



}