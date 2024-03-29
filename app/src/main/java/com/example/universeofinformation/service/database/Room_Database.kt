package com.example.universeofinformation.service.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.universeofinformation.model.Country
import com.example.universeofinformation.model.Favorite
import com.example.universeofinformation.model.GeographicEvent
import com.example.universeofinformation.model.History
import com.example.universeofinformation.model.Literature
import com.example.universeofinformation.service.dao.CountryDao
import com.example.universeofinformation.service.dao.FavoritesDao
import com.example.universeofinformation.service.dao.GeographicEventDao
import com.example.universeofinformation.service.dao.HistoryDao
import com.example.universeofinformation.service.dao.LiteratureDao

@Database(entities = [History::class, GeographicEvent::class, Literature::class, Country::class,Favorite::class], version = 1)
abstract class Room_Database : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun geographicEventDao(): GeographicEventDao
    abstract fun literatureDao(): LiteratureDao
    abstract fun countryDao(): CountryDao
    abstract fun favoritesDao(): FavoritesDao
}