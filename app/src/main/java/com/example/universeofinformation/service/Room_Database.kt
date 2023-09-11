package com.example.universeofinformation.service

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.universeofinformation.model.GeographicEvent
import com.example.universeofinformation.model.History
import com.example.universeofinformation.service.dao.GeographicEventDao
import com.example.universeofinformation.service.dao.HistoryDao

@Database(entities = [History::class ,GeographicEvent::class], version = 2)
abstract class Room_Database : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun geographicEventDao(): GeographicEventDao
}