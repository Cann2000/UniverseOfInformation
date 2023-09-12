package com.example.universeofinformation.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.universeofinformation.utility.Constants
import javax.inject.Inject

class SharedPreferencesRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {


    fun saveTime(time:Long)
    {
        sharedPreferences?.edit {
            putLong(Constants.TIME,time)
        }
    }
    fun takeTime() = sharedPreferences?.getLong(Constants.TIME,0)
}