package com.example.universeofinformation.repository

import com.example.universeofinformation.model.Favorite
import com.example.universeofinformation.model.History
import com.example.universeofinformation.service.dao.HistoryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryQueryRepository@Inject constructor(private val historyDao: HistoryDao,private val favoriteQueryRepository: FavoriteQueryRepository) {


    suspend fun insertAllHistory(historyList: List<History>) {

        CoroutineScope(Dispatchers.IO).launch{

            val starredHistoryList = historyDao.getStarredHistory(true)

            historyDao.deleteAll()

            val uuid = historyDao.insertAll(*historyList.toTypedArray())
            historyList.forEachIndexed { index, history ->
                history.uuid = uuid[index].toInt()

                starredHistoryList.isNotEmpty().let {

                    for(starredList in starredHistoryList){

                        if(starredList.warName == history.warName){
                            history.starred = true
                            historyDao.updateHistory(history)

                        }
                    }
                }
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

    suspend fun getStarredHistory(starred:Boolean): List<History> {

        return withContext(Dispatchers.IO){

            historyDao.getStarredHistory(starred)
        }

    }

    suspend fun getAllHistory(): List<History> {

        return withContext(Dispatchers.IO)
        {
            historyDao.getAllHistory()
        }
    }

    suspend fun updateHistory(uuid:Int, boolean: Boolean){

        CoroutineScope(Dispatchers.IO).launch {

            val history = historyDao.getHistory(uuid)

            history?.let {

                it.starred = boolean
                historyDao.updateHistory(it)

                if(boolean){

                    val title = it.warName
                    val subtitle = it.warHistory
                    val imageUrl = it.imageUrl

                    val favorite = Favorite(title,subtitle,imageUrl)

                    favoriteQueryRepository.insertFavorite(favorite)
                }
                else{
                    favoriteQueryRepository.deleteFavorites(it.warName!!)
                }


            }
        }
    }
}