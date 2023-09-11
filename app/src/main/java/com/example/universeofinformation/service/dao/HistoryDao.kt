package com.example.universeofinformation.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.universeofinformation.model.History

@Dao
interface HistoryDao {

    @Insert
    suspend fun insertAll(vararg history: History):List<Long>

    @Query("DELETE FROM history")
    suspend fun deleteAll()

    @Query("SELECT * FROM history WHERE uuid = :historyId")
    suspend fun getHistory(historyId:Int): History

    @Query("SELECT * FROM history")
    suspend fun getAllHistory(): List<History>
}