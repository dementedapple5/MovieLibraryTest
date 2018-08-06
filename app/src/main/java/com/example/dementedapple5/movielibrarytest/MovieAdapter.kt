package com.example.dementedapple5.movielibrarytest

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieAdapter(val mMovies: ArrayList<Movie>,val context: Context): RecyclerView.Adapter<MovieAdapter.Companion.MovieViewHolder>() {


    companion object {
        class MovieViewHolder(var mMovieItemCL: ConstraintLayout): RecyclerView.ViewHolder(mMovieItemCL)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val userItem: View = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(userItem as ConstraintLayout)
    }

    override fun getItemCount(): Int {
        return mMovies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, pos: Int) {
        Glide.with(context).load(mMovies[pos].posterPath).into(holder.mMovieItemCL.movie_img)
        holder.mMovieItemCL.movie_desc_tv.text = mMovies[pos].overview
        holder.mMovieItemCL.movie_title_tv.text = mMovies[pos].title
        holder.mMovieItemCL.movie_rd_tv.text = mMovies[pos].releaseDate
    }

}