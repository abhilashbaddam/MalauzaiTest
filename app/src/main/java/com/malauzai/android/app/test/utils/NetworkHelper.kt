package com.malauzai.android.app.test.utils

import android.content.Context
import android.net.ConnectivityManager

class NetworkHelper() {

    companion object {
        @Volatile
        private var INSTANCE: NetworkHelper? = null

        fun getInstance(): NetworkHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NetworkHelper().also { INSTANCE = it }
            }
    }

    fun isNetworkConnected(ctx: Context): Boolean {
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnected ?: false
    }
}