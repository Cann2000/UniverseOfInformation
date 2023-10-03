package com.example.universeofinformation.modules

import com.example.universeofinformation.service.DataAPI
import com.example.universeofinformation.utility.Constants.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RetrofitAPIModule {

    @Singleton
    @Provides
    fun getRetrofit():Retrofit{
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun getDataAPI(retrofit:Retrofit):DataAPI{
        return retrofit.create(DataAPI::class.java)
    }
}
