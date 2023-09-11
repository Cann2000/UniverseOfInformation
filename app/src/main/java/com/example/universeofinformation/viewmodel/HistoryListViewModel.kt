package com.example.universeofinformation.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.universeofinformation.adapter.DataAdapter
import com.example.universeofinformation.model.History
import com.example.universeofinformation.repository.APIRepository
import com.example.universeofinformation.repository.HistoryQueryRepository
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
class HistoryListViewModel@Inject constructor(private val apiRepository: APIRepository,private val historyQueryRepository: HistoryQueryRepository) : ViewModel() {

    val history = MutableLiveData<List<History>>()
    val errorMessage = MutableLiveData<Boolean>()
    val uploading = MutableLiveData<Boolean>()

    private lateinit var historyList:List<History>
    private val hiddenSharedPreferences = HiddenSharedPreferences()


    //private val compositeDisposable = CompositeDisposable()

    private var updateTime = 0.1 * 60 * 1000* 1000 * 1000L

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println(throwable.localizedMessage)
    }

    fun refreshData()
    {
        val saveTime = hiddenSharedPreferences.takeTime()

        if (saveTime != null && saveTime != 0L && System.nanoTime() - saveTime < saveTime){
            //Sqlite'tan çek
            getDataSql()
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
    private fun getDataSql() {

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val historyList = historyQueryRepository.getAllHistory()

            historyList?.let {

                withContext(Dispatchers.Main)
                {
                    showHistory(it)

                }
            }
        }
    }

    private suspend fun saveToSql(historyList: List<History>)
    {
        historyQueryRepository.insertAllHistory(historyList)
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
                    }
                    if (filteredList.isEmpty()) {

                        withContext(Dispatchers.Main)
                        {
                            //Toast.makeText(coroutineContext, "No Data found", Toast.LENGTH_SHORT).show()
                        }
                    } else {

                        withContext(Dispatchers.Main)
                        {
                            adapter.setFilteredList(filteredList)
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