package com.example.universeofinformation.repository

import com.example.universeofinformation.model.Literature
import com.example.universeofinformation.service.dao.LiteratureDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LiteratureQueryRepository@Inject constructor(private val literatureDao: LiteratureDao) {

    suspend fun insertAllLiterature(literatureList: List<Literature>){

        CoroutineScope(Dispatchers.IO).launch {

            literatureDao.deleteAll()

            val uuid = literatureDao.insertAll(*literatureList.toTypedArray())
            literatureList.forEachIndexed { index, literature ->

                literature.uuid = uuid[index].toInt()
            }
        }
    }

    suspend fun deleteAllLiterature(){

        CoroutineScope(Dispatchers.IO).launch {
            literatureDao.deleteAll()
        }
    }

    suspend fun getLiterature(uuid:Int):Literature{

        return withContext(Dispatchers.IO){

            literatureDao.getLiterature(uuid)
        }
    }

    suspend fun getAllLiterature():List<Literature>{

        return withContext(Dispatchers.IO){

            literatureDao.getAllLiterature()
        }
    }
}