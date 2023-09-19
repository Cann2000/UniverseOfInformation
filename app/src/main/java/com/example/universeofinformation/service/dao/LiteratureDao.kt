package com.example.universeofinformation.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.universeofinformation.model.GeographicEvent
import com.example.universeofinformation.model.History
import com.example.universeofinformation.model.Literature

@Dao
interface LiteratureDao {

    @Insert
    suspend fun insertAll(vararg literature: Literature):List<Long>

    @Query("DELETE FROM literature")
    suspend fun deleteAll()

    @Query("SELECT * FROM literature WHERE uuid = :literatureId")
    suspend fun getLiterature(literatureId:Int): Literature

    @Query("SELECT * FROM literature WHERE starred = :starred")
    suspend fun getStarredLiterature(starred:Boolean): List<Literature>

    @Query("SELECT * FROM literature")
    suspend fun getAllLiterature(): List<Literature>

    @Update
    suspend fun updateLiterature(literature: Literature)
}