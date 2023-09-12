package com.example.universeofinformation.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universeofinformation.adapter.DataAdapter
import com.example.universeofinformation.model.History
import com.example.universeofinformation.repository.APIRepository
import com.example.universeofinformation.repository.HistoryQueryRepository
import com.example.universeofinformation.repository.SharedPreferencesRepository
import com.example.universeofinformation.utility.Constants.updateTime
import com.example.universeofinformation.utility.HiddenSharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HistoryListViewModel@Inject constructor(private val apiRepository: APIRepository,private val historyQueryRepository: HistoryQueryRepository, private val sharedPreferencesRepository: SharedPreferencesRepository) : ViewModel() {

    val history = MutableLiveData<List<History>>()
    val errorMessage = MutableLiveData<Boolean>()
    val uploading = MutableLiveData<Boolean>()

    private lateinit var historyList:List<History>


    //private val compositeDisposable = CompositeDisposable()


    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println(throwable.localizedMessage)
    }

    fun refreshData()
    {
        val saveTime = sharedPreferencesRepository.takeTime()

        if (saveTime != null && saveTime != 0L && System.nanoTime() - saveTime < updateTime ){ // updateTime in Constants
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
    private fun showHistory(historyList:List<History>)
    {
        history.value=historyList
        this.historyList = historyList
        errorMessage.value = false
        uploading.value = false
    }
    private fun getDataFromInternet()
    {
        println("internet2")

        uploading.value = true

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = apiRepository.getData()

            if(response.isSuccessful)
            {
                val history = response.body()?.history_events

                history?.let {

                    saveToSql(it)

                    withContext(Dispatchers.Main) {
                        showHistory(it)
                    }
                }
            }

        }

    }
    private fun getDataFromSql() {

        job = viewModelScope.launch {

            val historyList = historyQueryRepository.getAllHistory()

            println(historyList)
            if(historyList != null && historyList.isNotEmpty()) // bu koşul eğer hiç internetten veri çekmeden ve sqle veri kaydetmeden sqlden veri çekmesini engeller
            {
                withContext(Dispatchers.Main)
                {
                    showHistory(historyList)
                }
            }
            else{
                getDataFromInternet()
            }
        }
    }

    private suspend fun saveToSql(historyList: List<History>)
    {
        historyQueryRepository.insertAllHistory(historyList)

        sharedPreferencesRepository.saveTime(System.nanoTime())
    }

    fun searchViewFilterList(query:String?,adapter:DataAdapter)
    {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            if (!query.isNullOrEmpty()) {

                val filteredList = ArrayList<Any>()

                if (historyList != null) {

                    historyList.forEach {

                        val warName = it.warName?.lowercase(Locale.ROOT)
                        if (warName?.startsWith(query) == true) {
                            filteredList.add(it)
                        }
                        else {

                            withContext(Dispatchers.Main)
                            {
                                adapter.setFilteredList(filteredList)
                            }
                        }
                    }
                }
            }
            else
            {
                withContext(Dispatchers.Main)
                {
                    adapter.dataListUpdate(historyList)
                }
            }
        }
    }

    override fun onCleared() {

        super.onCleared()
        job?.cancel()
    }

}