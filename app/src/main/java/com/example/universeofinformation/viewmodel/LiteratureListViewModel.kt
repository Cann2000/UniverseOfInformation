package com.example.universeofinformation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universeofinformation.adapter.DataAdapter
import com.example.universeofinformation.model.GeographicEvent
import com.example.universeofinformation.model.Literature
import com.example.universeofinformation.repository.APIRepository
import com.example.universeofinformation.repository.LiteratureQueryRepository
import com.example.universeofinformation.repository.SharedPreferencesRepository
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
class LiteratureListViewModel@Inject constructor(private val apiRepository: APIRepository, private val literatureQueryRepository: LiteratureQueryRepository, private val sharedPreferencesRepository: SharedPreferencesRepository):ViewModel() {

    val literature = MutableLiveData<List<Literature>>()
    val errorMessage = MutableLiveData<Boolean>()
    val uploading = MutableLiveData<Boolean>()

    private lateinit var literatureList: List<Literature>

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

    fun showLiteraryWorks(literatureList:List<Literature>){

        literature.value = literatureList
        this.literatureList = literatureList
        errorMessage.value = false
        uploading.value = false
    }

    private fun getDataFromInternet(){

        uploading.value = true

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            try {

                val response = apiRepository.getData()

                val literaryWorks = response.body()?.literary_works

                literaryWorks?.let {

                    saveToSql(it).apply {

                        withContext(Dispatchers.Main){

                            delay(100)
                            showLiteraryWorks(it)
                        }
                    }
                }
            }
            catch (e:Exception){

                e.printStackTrace()
            }
        }
    }

    private fun getDataFromSql() {

        job = viewModelScope.launch {

            uploading.value = true

            val literatureList = literatureQueryRepository.getAllLiterature()

            if(!literatureList.isNullOrEmpty()){

                withContext(Dispatchers.Main){

                    showLiteraryWorks(literatureList)
                }
            }
            else{
                getDataFromInternet()
            }
        }
    }

    private suspend fun saveToSql(literatureList: List<Literature>){

        literatureQueryRepository.insertAllLiterature(literatureList)

        sharedPreferencesRepository.saveTime(System.nanoTime())

    }

    fun searchViewFilterList(query:String?,adapter: DataAdapter){

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            if(!query.isNullOrEmpty()){

                val filteredList = ArrayList<Any>()


                if(literatureList != null){

                    literatureList.forEach {

                        val geographicEventName = it.workName?.lowercase(Locale.ROOT)
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

                    adapter.dataListUpdate(literatureList)

                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}