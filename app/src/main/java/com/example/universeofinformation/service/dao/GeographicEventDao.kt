package com.example.universeofinformation.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.universeofinformation.model.GeographicEvent

@Dao
interface GeographicEventDao {

    @Insert
    suspend fun insertAll(vararg geographicalEvent: GeographicEvent):List<Long>

    @Query("DELETE FROM geographicevent")
    suspend fun deleteAll()

    @Query("SELECT * FROM geographicevent WHERE uuid = :geographicId")
    suspend fun getGeographicEvent(geographicId:Int): GeographicEvent

    @Query("SELECT * FROM geographicevent WHERE starred = :starred")
    suspend fun getStarredGeographicEvent(starred:Boolean): List<GeographicEvent>

    @Query("SELECT * FROM geographicevent")
    suspend fun getAllGeographicEvent(): List<GeographicEvent>

    @Update
    suspend fun updateEvent(geographicEvent: GeographicEvent)
}