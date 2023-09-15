package com.example.universeofinformation.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.universeofinformation.model.Country
import com.example.universeofinformation.model.Literature

@Dao
interface CountryDao {

    @Insert
    suspend fun insertAll(vararg country: Country):List<Long>

    @Query("DELETE FROM country")
    suspend fun deleteAll()

    @Query("SELECT * FROM country WHERE uuid = :countryId")
    suspend fun getCountry(countryId:Int): Country

    @Query("SELECT * FROM country")
    suspend fun getAllCountry(): List<Country>
}