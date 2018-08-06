package com.example.dementedapple5.movielibrarytest

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleton(context: Context) {

    private var mRequestQueue: RequestQueue? = null

    companion object {
        private var mInstance: VolleySingleton? = null

        fun getInstance(context: Context): VolleySingleton? {
            if (mInstance == null) {
                mInstance = VolleySingleton(context)
            }
            return mInstance
        }
    }

    fun getRequestQueue(context: Context): RequestQueue? {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context)
        }
        return mRequestQueue
    }

    init {
        mRequestQueue = getRequestQueue(context)
    }
}