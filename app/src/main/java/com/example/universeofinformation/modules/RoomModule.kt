package com.example.universeofinformation.modules

import android.content.Context
import androidx.room.Room
import com.example.universeofinformation.service.dao.HistoryDao
import com.example.universeofinformation.service.Room_Database
import com.example.universeofinformation.service.dao.CountryDao
import com.example.universeofinformation.service.dao.FavoritesDao
import com.example.universeofinformation.service.dao.GeographicEventDao
import com.example.universeofinformation.service.dao.LiteratureDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomModule {

    @Singleton
    @Provides
    fun getDatabase(@ApplicationContext context: Context): Room_Database {
        return Room.databaseBuilder(context, Room_Database::class.java,"Database").fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun getHistoryDao(database: Room_Database): HistoryDao {
        return database.historyDao()
    }

    @Singleton
    @Provides
    fun getGeographicEventDao(database: Room_Database): GeographicEventDao {
        return database.geographicEventDao()
    }

    @Singleton
    @Provides
    fun getLiteratureDao(database: Room_Database): LiteratureDao {
        return database.literatureDao()
    }

    @Singleton
    @Provides
    fun getCountryDao(database: Room_Database): CountryDao {
        return database.countryDao()
    }
    @Singleton
    @Provides
    fun getFavoritesDao(database: Room_Database): FavoritesDao {
        return database.favoritesDao()
    }
}