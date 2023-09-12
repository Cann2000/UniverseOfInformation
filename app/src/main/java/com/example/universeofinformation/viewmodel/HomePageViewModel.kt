package com.example.universeofinformation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universeofinformation.model.HomePageContent
import com.example.universeofinformation.view.HomePageFragmentDirections
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomePageViewModel:ViewModel() {

    val homePageContent = MutableLiveData<List<HomePageContent>>()

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println(throwable.localizedMessage)
    }

    fun getData(){

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val contentList = ArrayList<HomePageContent>()

            contentList.clear()

            val contentHistory = HomePageContent("Tarih","https://raw.githubusercontent.com/Cann2000/HistoryBookData/main/a.jpg",HomePageFragmentDirections.actionHomePageFragmentToHistoryListFragment())
            val contentGeography = HomePageContent("Coğrafi Olaylar","https://raw.githubusercontent.com/Cann2000/HistoryBookData/main/a.jpg",HomePageFragmentDirections.actionHomePageFragmentToGeographicalEventsFragment())
            val contentUniverse = HomePageContent("Evren","https://raw.githubusercontent.com/Cann2000/HistoryBookData/main/a.jpg",HomePageFragmentDirections.actionHomePageFragmentToHistoryListFragment())

            contentList.add(contentHistory)
            contentList.add(contentGeography)
            contentList.add(contentUniverse)

            withContext(Dispatchers.Main){

                homePageContent.value = contentList
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}