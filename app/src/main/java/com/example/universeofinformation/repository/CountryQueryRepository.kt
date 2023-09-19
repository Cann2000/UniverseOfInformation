package com.example.universeofinformation.repository

import com.example.universeofinformation.model.Country
import com.example.universeofinformation.model.Favorite
import com.example.universeofinformation.model.GeographicEvent
import com.example.universeofinformation.service.dao.CountryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CountryQueryRepository@Inject constructor(private val countryDao: CountryDao,private val favoriteQueryRepository: FavoriteQueryRepository) {

    suspend fun insertAllCountry(countryList:List<Country>) {

        CoroutineScope(Dispatchers.IO).launch {

            val starredCountryList = countryDao.getStarredCountry(true)

            countryDao.deleteAll()

            val uuid = countryDao.insertAll(*countryList.toTypedArray())
            countryList.forEachIndexed { index, country ->
                country.uuid = uuid[index].toInt()

                for(starredList in starredCountryList){

                    if(starredList.countryName == country.countryName){
                        println(country)
                        country.starred = true
                        countryDao.updateCountry(country)
                    }
                }
            }
        }
    }

    suspend fun deleteAllCountry() {

        CoroutineScope(Dispatchers.IO).launch {
            countryDao.deleteAll()
        }
    }

    suspend fun getCountry(uuid:Int): Country {

        return withContext(Dispatchers.IO){

            countryDao.getCountry(uuid)
        }
    }

    suspend fun getAllCountry():List<Country>{

        return withContext(Dispatchers.IO){

            countryDao.getAllCountry()
        }
    }

    suspend fun updateCountry(uuid:Int, boolean: Boolean){

        CoroutineScope(Dispatchers.IO).launch {

            val country = countryDao.getCountry(uuid)

            country?.let {

                it.starred = boolean
                countryDao.updateCountry(it)

                if(boolean){

                    val title = it.countryName
                    val subtitle = it.year
                    val imageUrl = it.imageUrl

                    val favorite = Favorite(title,subtitle,imageUrl)

                    favoriteQueryRepository.insertFavorite(favorite)
                }
                else{
                    favoriteQueryRepository.deleteFavorites(it.countryName!!)
                }


            }
        }
    }
}