package com.example.universeofinformation.utility

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class HiddenSharedPreferences {

    companion object{

        private val TIME = "time"

        private var sharedPreferences: SharedPreferences? = null

        @Volatile private var instance:HiddenSharedPreferences? = null

        private val lock = Any()

        operator fun invoke(context: Context):HiddenSharedPreferences = instance?: synchronized(lock){
            instance?: hiddenSharedPreferences(context).also {
                instance = it
            }
        }

        private fun hiddenSharedPreferences(context: Context):HiddenSharedPreferences{

            sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)

            return HiddenSharedPreferences()
        }
    }

    fun saveTime(time:Long)
    {
        sharedPreferences?.edit {
            putLong(TIME,time)
        }
    }
    fun takeTime() = sharedPreferences?.getLong(TIME,0)
}