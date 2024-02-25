package com.example.universeofinformation.viewmodel.country

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universeofinformation.adapter.DataAdapter
import com.example.universeofinformation.model.Country
import com.example.universeofinformation.repository.APIRepository
import com.example.universeofinformation.repository.CountryQueryRepository
import com.example.universeofinformation.utility.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CountryListViewModel@Inject constructor(private val apiRepository: APIRepository, private val countryQueryRepository: CountryQueryRepository):ViewModel() {

    val countrys = MutableLiveData<List<Country>>()
    val errorMessage = MutableLiveData<Boolean>()
    val uploading = MutableLiveData<Boolean>()

    private lateinit var countryList:List<Country>
    lateinit var adapter: DataAdapter

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println(throwable.localizedMessage)
    }


    fun refreshData(){


        if (Constants.loadCountry){

            getDataFromInternet()

        } else {

            getDataFromSql()
        }
    }

    fun refreshInternet(){

        getDataFromInternet()
    }

    private fun getDataFromInternet()
    {
        uploading.value = true

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            try {

                val response = apiRepository.getData()

                if (response.isSuccessful) {

                    val countries = response.body()?.turk_countries

                    countries?.let {

                        saveToSql(it).apply {

                            withContext(Dispatchers.Main){

                                Constants.loadCountry = false

                                delay(100)
                                showGeographicalEvents(it)
                            }
                        }

                    }
                }
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
    private fun getDataFromSql(){

        job = viewModelScope.launch {

            uploading.value = true

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
    }


    fun searchViewFilterList(query:String?,adapter: DataAdapter){

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            if(!query.isNullOrEmpty()){

                val filteredList = ArrayList<Any>()


                if(countryList != null){

                    countryList.forEach {

                        val geographicEventName = it.countryName?.lowercase(Locale.ROOT)
                        if(geographicEventName?.startsWith(query.lowercase(Locale.ROOT)) == true){

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

    private fun showGeographicalEvents(countryList:List<Country>){

        countrys.value = countryList
        this.countryList = countryList
        errorMessage.value = false
        uploading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}