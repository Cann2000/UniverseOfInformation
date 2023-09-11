package com.example.universeofinformation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.universeofinformation.adapter.DataAdapter
import com.example.universeofinformation.model.GeographicEvent
import com.example.universeofinformation.repository.APIRepository
import com.example.universeofinformation.repository.GeographicQueryRepository
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
class GeographicEventListViewModel @Inject constructor(private val apiRepository: APIRepository, private val geographicQueryRepository: GeographicQueryRepository) :ViewModel() {

    val geographicalEvents = MutableLiveData<List<GeographicEvent>>()
    val errorMessage = MutableLiveData<Boolean>()
    val uploading = MutableLiveData<Boolean>()

    private lateinit var geographicEventList:List<GeographicEvent>

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println(throwable.localizedMessage)
    }


    fun showGeographicalEvents(geographicEventList:List<GeographicEvent>){

        geographicalEvents.value = geographicEventList
        this.geographicEventList = geographicEventList
        errorMessage.value = false
        uploading.value = false
    }
    fun getDataFromInternet()
    {
        uploading.value = true

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = apiRepository.getData()

            if (response.isSuccessful) {

                val geographicalEvents = response.body()?.geographical_events

                geographicalEvents?.let {

                    saveToSql(it)

                    withContext(Dispatchers.Main){

                        showGeographicalEvents(it)
                    }
                }
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

                    adapter.dataListUpdate(geographicEventList)

                }
            }
        }
    }
}