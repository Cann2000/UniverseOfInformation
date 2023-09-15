package com.example.universeofinformation.repository

import com.example.universeofinformation.model.Country
import com.example.universeofinformation.model.GeographicEvent
import com.example.universeofinformation.service.dao.CountryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CountryQueryRepository@Inject constructor(private val countryDao: CountryDao) {

    suspend fun insertAllCountry(countryList:List<Country>) {

        CoroutineScope(Dispatchers.IO).launch {

            countryDao.deleteAll()

            val uuid = countryDao.insertAll(*countryList.toTypedArray())
            countryList.forEachIndexed { index, country ->
                country.uuid = uuid[index].toInt()
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
}