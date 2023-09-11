package com.example.universeofinformation.repository

import com.example.universeofinformation.model.GeographicEvent
import com.example.universeofinformation.service.dao.GeographicEventDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GeographicQueryRepository@Inject constructor(private val geographicEventDao: GeographicEventDao) {


    suspend fun insertAllGeographicEvent(geographicEventList:List<GeographicEvent>) {

        CoroutineScope(Dispatchers.IO).launch {

            geographicEventDao.deleteAll()

            val uuid = geographicEventDao.insertAll(*geographicEventList.toTypedArray())
            geographicEventList.forEachIndexed{index, geographicEvent ->
                geographicEvent.uuid = uuid[index].toInt()
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
}