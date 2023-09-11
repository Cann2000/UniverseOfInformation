package com.example.universeofinformation.repository

import com.example.universeofinformation.model.History
import com.example.universeofinformation.service.dao.HistoryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryQueryRepository@Inject constructor(private val historyDao: HistoryDao) {


    suspend fun insertAllHistory(historyList: List<History>) {

        CoroutineScope(Dispatchers.IO).launch{

            historyDao.deleteAll()

            val uuid = historyDao.insertAll(*historyList.toTypedArray())
            historyList.forEachIndexed { index, history ->
                history.uuid = uuid[index].toInt()
            }
        }

    }

    suspend fun deleteAllHistory(){

        CoroutineScope(Dispatchers.IO).launch {
            historyDao.deleteAll()

        }
    }

    suspend fun getHistory(uuid:Int): History {

        return withContext(Dispatchers.IO){
            historyDao.getHistory(uuid)
        }

    }

    suspend fun getAllHistory(): List<History> {

        return withContext(Dispatchers.IO)
        {
            historyDao.getAllHistory()
        }
    }

}