package com.example.universeofinformation.utility

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Point
import android.net.ConnectivityManager
import android.util.Log

object Constants {

    const val BASE_URL = "https://raw.githubusercontent.com/"

    const val TIME = "time" // shared preferences

    const val updateTime = 10 * 60 * 1000* 1000 * 1000L

    fun isNetworkAvailable(context: Context): Boolean {
        val service = Context.CONNECTIVITY_SERVICE
        val manager = context.getSystemService(service) as ConnectivityManager?
        val network = manager?.activeNetworkInfo
        return (network != null)
    }
}