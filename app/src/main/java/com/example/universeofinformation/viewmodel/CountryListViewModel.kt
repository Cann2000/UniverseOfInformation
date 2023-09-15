package com.example.universeofinformation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universeofinformation.adapter.DataAdapter
import com.example.universeofinformation.model.Country
import com.example.universeofinformation.model.GeographicEvent
import com.example.universeofinformation.repository.APIRepository
import com.example.universeofinformation.repository.CountryQueryRepository
import com.example.universeofinformation.repository.GeographicQueryRepository
import com.example.universeofinformation.repository.SharedPreferencesRepository
import com.example.universeofinformation.utility.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CountryListViewModel@Inject constructor(private val apiRepository: APIRepository, private val countryQueryRepository: CountryQueryRepository, private val sharedPreferencesRepository: SharedPreferencesRepository):ViewModel() {

    val countrys = MutableLiveData<List<Country>>()
    val errorMessage = MutableLiveData<Boolean>()
    val uploading = MutableLiveData<Boolean>()

    private lateinit var countryList:List<Country>

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println(throwable.localizedMessage)
    }


    fun refreshData(){

        val saveTime = sharedPreferencesRepository.takeTime()

        if (saveTime != null && saveTime != 0L && System.nanoTime() - saveTime < Constants.updateTime){ // updateTime in Constants
            //Sqlite'tan çek
            getDataFromSql()
            println("sql")
        } else {
            getDataFromInternet()
            println("internet")
        }
    }

    fun refreshInternet(){

        getDataFromInternet()
    }

    fun showGeographicalEvents(countryList:List<Country>){

        countrys.value = countryList
        this.countryList = countryList
        errorMessage.value = false
        uploading.value = false
    }

    fun getDataFromInternet()
    {
        uploading.value = true

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = apiRepository.getData()

            if (response.isSuccessful) {

                val countries = response.body()?.turk_countries

                countries?.let {

                    saveToSql(it)

                    withContext(Dispatchers.Main){

                        showGeographicalEvents(it)
                    }
                }
            }
        }
    }
    private fun getDataFromSql(){

        job = viewModelScope.launch {

            val countryList = countryQueryRepository.getAllCountry()

            if(!countryList.isNullOrEmpty()){ // bu koşul eğer hiç internetten veri çekmeden ve sqle veri kaydetmeden sqlden veri çekmesini engeller

                withContext(Dispatchers.Main){

                    showGeographicalEvents(countryList)
                }
            }
            else{

                getDataFromInternet()
            }
        }
    }
    private suspend fun saveToSql(countryList: List<Country>){

        countryQueryRepository.insertAllCountry(countryList)

        sharedPreferencesRepository.saveTime(System.nanoTime())
    }


    fun searchViewFilterList(query:String?,adapter: DataAdapter){

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            if(!query.isNullOrEmpty()){

                val filteredList = ArrayList<Any>()


                if(countryList != null){

                    countryList.forEach {

                        val geographicEventName = it.countryName?.lowercase(Locale.ROOT)
                        if(geographicEventName?.startsWith(query) == true){

                            filteredList.add(it)
                        }
                        else
                        {
                            withContext(Dispatchers.Main){

                                adapter.setFilteredList(filteredList)

                            }
                        }
                    }
                }
            }
            else{

                withContext(Dispatchers.Main){

                    adapter.dataListUpdate(countryList)

                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}