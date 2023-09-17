package com.example.universeofinformation.repository

import com.example.universeofinformation.model.Favorite
import com.example.universeofinformation.model.Literature
import com.example.universeofinformation.service.dao.LiteratureDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LiteratureQueryRepository@Inject constructor(private val literatureDao: LiteratureDao,private val favoriteQueryRepository: FavoriteQueryRepository) {

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
    suspend fun updateLiterature(uuid:Int, boolean: Boolean){

        CoroutineScope(Dispatchers.IO).launch {

            val literature = literatureDao.getLiterature(uuid)

            literature?.let {

                it.starred = boolean
                literatureDao.updateLiterature(it)

                if(boolean){

                    val title = it.workName
                    val subtitle = it.workType
                    val imageUrl = it.imageUrl

                    val favorite = Favorite(title,subtitle,imageUrl)

                    favoriteQueryRepository.insertFavorite(favorite)
                }
                else{
                    favoriteQueryRepository.deleteFavorites(it.workName!!)
                }


            }
        }
    }
}