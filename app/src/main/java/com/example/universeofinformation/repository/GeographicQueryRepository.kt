package com.example.universeofinformation.repository

import com.example.universeofinformation.model.Favorite
import com.example.universeofinformation.model.GeographicEvent
import com.example.universeofinformation.service.dao.FavoritesDao
import com.example.universeofinformation.service.dao.GeographicEventDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GeographicQueryRepository@Inject constructor(private val geographicEventDao: GeographicEventDao, private val favoriteQueryRepository: FavoriteQueryRepository) {


    suspend fun insertAllGeographicEvent(geographicEventList:List<GeographicEvent>) {

        CoroutineScope(Dispatchers.IO).launch {

            val starredGeographicEventList = geographicEventDao.getStarredGeographicEvent(true)

            geographicEventDao.deleteAll()

            val uuid = geographicEventDao.insertAll(*geographicEventList.toTypedArray())
            geographicEventList.forEachIndexed{index, geographicEvent ->
                geographicEvent.uuid = uuid[index].toInt()

                starredGeographicEventList.isNotEmpty().let {

                    for(starredList in starredGeographicEventList){

                        if(starredList.eventName == geographicEvent.eventName){
                            println(geographicEvent)
                            geographicEvent.starred = true
                            geographicEventDao.updateEvent(geographicEvent)
                        }
                    }
                }
            }
        }
    }

    suspend fun deleteAllGeographicEvent() {

        CoroutineScope(Dispatchers.IO).launch {
            geographicEventDao.deleteAll()
        }
    }

    suspend fun getGeographicEvent(uuid:Int):GeographicEvent{

        return withContext(Dispatchers.IO){

            geographicEventDao.getGeographicEvent(uuid)
        }
    }

    suspend fun getAllGeographicalEvent():List<GeographicEvent>{

        return withContext(Dispatchers.IO){

            geographicEventDao.getAllGeographicEvent()
        }
    }

    suspend fun updateGeographicalEvent(uuid:Int, boolean: Boolean){

        CoroutineScope(Dispatchers.IO).launch {

            val geographicEvent = geographicEventDao.getGeographicEvent(uuid)

            geographicEvent?.let {

                it.starred = boolean
                geographicEventDao.updateEvent(it)

                if(boolean){

                    val title = it.eventName
                    val subtitle = it.eventDate
                    val imageUrl = it.imageUrl

                    val favorite = Favorite(title,subtitle,imageUrl)

                    favoriteQueryRepository.insertFavorite(favorite)
                }
                else{
                    favoriteQueryRepository.deleteFavorites(it.eventName!!)
                }


            }
        }
    }
}