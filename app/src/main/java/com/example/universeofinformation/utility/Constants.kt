package com.example.universeofinformation.utility


import android.content.Context
import android.net.ConnectivityManager

object Constants {

    const val BASE_URL = "https://raw.githubusercontent.com/"

    const val TIME = "time" // shared preferences

    const val updateTime = 30 * 60 * 1000 * 1000 * 1000L // 30 dk

    fun isNetworkAvailable(context: Context): Boolean {
        val service = Context.CONNECTIVITY_SERVICE
        val manager = context.getSystemService(service) as ConnectivityManager?
        val network = manager?.activeNetworkInfo
        return (network != null)
    }
}