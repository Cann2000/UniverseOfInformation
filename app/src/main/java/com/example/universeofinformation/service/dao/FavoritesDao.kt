package com.example.universeofinformation.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.universeofinformation.model.Favorite

@Dao
interface FavoritesDao {

    @Insert
    suspend fun insertFavorite(favorite: Favorite):Long

    @Query("DELETE FROM favorite where uuid= :favoriteId")
    suspend fun deleteFavorites(favoriteId:Int)

    @Query("SELECT * FROM favorite WHERE uuid = :favoriteId")
    suspend fun getFavorite(favoriteId:Int): Favorite

    @Query("SELECT * FROM favorite")
    suspend fun getAllFavorites(): List<Favorite>


}
