package com.example.universeofinformation.viewmodel.geographic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universeofinformation.adapter.DataAdapter
import com.example.universeofinformation.model.GeographicEvent
import com.example.universeofinformation.repository.APIRepository
import com.example.universeofinformation.repository.GeographicQueryRepository
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
class GeographicEventListViewModel @Inject constructor(private val apiRepository: APIRepository, val geographicQueryRepository: GeographicQueryRepository) :ViewModel() {

    val geographicalEvents = MutableLiveData<List<GeographicEvent>>()
    val errorMessage = MutableLiveData<Boolean>()
    val uploading = MutableLiveData<Boolean>()

    private lateinit var geographicEventList:List<GeographicEvent>

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println(throwable.localizedMessage)
    }


    fun refreshData(){

        if (Constants.loadGeographic){

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

                    val geographicalEvents = response.body()?.geographical_events

                    geographicalEvents?.let {

                        saveToSql(it).apply {

                            withContext(Dispatchers.Main){

                                Constants.loadGeographic = false

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

            val geographicEventList = geographicQueryRepository.getAllGeographicalEvent()

            if(!geographicEventList.isNullOrEmpty()){ // bu koşul eğer hiç internetten veri çekmeden ve sqle veri kaydetmeden sqlden veri çekmesini engeller

                withContext(Dispatchers.Main){

                    showGeographicalEvents(geographicEventList)
                }
            }
            else{

                getDataFromInternet()
            }
        }
    }
    private suspend fun saveToSql(geographicList: List<GeographicEvent>){

        geographicQueryRepository.insertAllGeographicEvent(geographicList)
    }


    fun searchViewFilterList(query:String?,adapter:DataAdapter){

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            if(!query.isNullOrEmpty()){

                val filteredList = ArrayList<Any>()

                if(geographicEventList != null){

                    geographicEventList.forEach {

                        val geographicEventName = it.eventName?.lowercase(Locale.ROOT)
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

                    adapter.dataListUpdate(geographicEventList)

                }
            }
        }
    }

    private fun showGeographicalEvents(geographicEventList:List<GeographicEvent>){

        geographicalEvents.value = geographicEventList
        this.geographicEventList = geographicEventList
        errorMessage.value = false
        uploading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}