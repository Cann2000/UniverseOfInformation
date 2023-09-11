package com.example.universeofinformation.service

import com.example.universeofinformation.model.InfoData
import retrofit2.Response
import retrofit2.http.GET

interface DataAPI {

    @GET("Cann2000/HistoryBookData/main/Data.json")
    suspend fun getData(): Response<InfoData>
}