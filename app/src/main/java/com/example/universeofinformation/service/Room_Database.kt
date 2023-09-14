package com.example.universeofinformation.service

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.universeofinformation.model.GeographicEvent
import com.example.universeofinformation.model.History
import com.example.universeofinformation.model.Literature
import com.example.universeofinformation.service.dao.GeographicEventDao
import com.example.universeofinformation.service.dao.HistoryDao
import com.example.universeofinformation.service.dao.LiteratureDao

@Database(entities = [History::class, GeographicEvent::class, Literature::class], version = 3)
abstract class Room_Database : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun geographicEventDao(): GeographicEventDao
    abstract fun literatureDao(): LiteratureDao
}