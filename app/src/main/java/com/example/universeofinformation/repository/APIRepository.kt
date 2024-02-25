package com.example.universeofinformation.repository

import com.example.universeofinformation.model.InfoData
import com.example.universeofinformation.service.api.DataAPI
import retrofit2.Response
import javax.inject.Inject

class APIRepository @Inject constructor(private val dataAPI: DataAPI) {

    suspend fun getData():Response<InfoData>{

        return dataAPI.getData()
    }

}