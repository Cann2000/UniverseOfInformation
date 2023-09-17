package com.example.universeofinformation.repository

import com.example.universeofinformation.model.Favorite
import com.example.universeofinformation.model.GeographicEvent
import com.example.universeofinformation.service.dao.CountryDao
import com.example.universeofinformation.service.dao.FavoritesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteQueryRepository@Inject constructor(private val favoritesDao: FavoritesDao) {

    suspend fun insertFavorite(favorite:Favorite) {

        CoroutineScope(Dispatchers.IO).launch {

            val uuid = favoritesDao.insertFavorite(favorite)
            favorite.uuid = uuid.toInt()
        }
    }

    suspend fun deleteFavorites(title:String) {

        CoroutineScope(Dispatchers.IO).launch {

            val favoriteList = favoritesDao.getAllFavorites()
            favoriteList.forEachIndexed { index, favorite ->

                if(favorite.title == title){
                    val uuid = favorite.uuid
                    favoritesDao.deleteFavorites(uuid!!)
                }
            }
        }
    }

    suspend fun getFavorites(uuid:Int): Favorite {

        return withContext(Dispatchers.IO){

            favoritesDao.getFavorite(uuid)
        }
    }

    suspend fun getAllFavorites():List<Favorite>{

        return withContext(Dispatchers.IO){

            favoritesDao.getAllFavorites()
        }
    }
}
