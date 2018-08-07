package com.example.dementedapple5.movielibrarytest.utils

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager



abstract class EndlessScrollListener(internal var layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    abstract val totalPageCount: Int

    abstract val isLastPageScroll: Boolean

    abstract val isLoadingScroll: Boolean

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLoadingScroll && !isLastPageScroll) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
}