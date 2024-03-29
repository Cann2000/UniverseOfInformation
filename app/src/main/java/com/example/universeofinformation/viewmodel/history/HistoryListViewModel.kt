package com.example.universeofinformation.viewmodel.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universeofinformation.adapter.DataAdapter
import com.example.universeofinformation.model.History
import com.example.universeofinformation.repository.APIRepository
import com.example.universeofinformation.repository.HistoryQueryRepository
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
class HistoryListViewModel@Inject constructor(private val apiRepository: APIRepository,private val historyQueryRepository: HistoryQueryRepository) : ViewModel() {

    val history = MutableLiveData<List<History>>()
    val errorMessage = MutableLiveData<Boolean>()
    val uploading = MutableLiveData<Boolean>()

    private lateinit var historyList:List<History>


    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println(throwable.localizedMessage)
    }

    fun refreshData() {

        if (Constants.loadHistory){

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
                    val history = response.body()?.history_events
                    history?.let {

                        saveToSql(it).apply {

                            withContext(Dispatchers.Main) {

                                Constants.loadHistory = false

                                delay(100)
                                showHistory(it)
                            }
                        }
                    }
                }
            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
    private fun getDataFromSql() {

        job = viewModelScope.launch {

            uploading.value = true

            val historyList = historyQueryRepository.getAllHistory()

            if(!historyList.isNullOrEmpty()) // bu koşul eğer hiç internetten veri çekmeden ve sqle veri kaydetmeden sqlden veri çekmesini engeller
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

    private suspend fun saveToSql(historyList: List<History>) {

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
                        if (warName?.startsWith(query.lowercase(Locale.ROOT)) == true) {

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

    private fun showHistory(historyList:List<History>)
    {
        history.value=historyList
        this.historyList = historyList
        errorMessage.value = false
        uploading.value = false
    }

    override fun onCleared() {

        super.onCleared()
        job?.cancel()
    }

}