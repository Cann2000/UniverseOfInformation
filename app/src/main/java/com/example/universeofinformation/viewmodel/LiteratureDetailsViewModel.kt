package com.example.universeofinformation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universeofinformation.model.Literature
import com.example.universeofinformation.repository.HistoryQueryRepository
import com.example.universeofinformation.repository.LiteratureQueryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LiteratureDetailsViewModel@Inject constructor(private val literatureQueryRepository: LiteratureQueryRepository):ViewModel(){

    val literatureLiveData = MutableLiveData<Literature>()


    fun getRoomData(uuid: Int)
    {
        viewModelScope.launch {
            try {
                //Bu işlem ana iş parçacığında yapılıyormuş gibi görünse de, historyQueryRepository.getHistory(uuid) withContext(Dispatchers.IO) içinde çalıştığı için arka planda çalışır.
                val literature = literatureQueryRepository.getLiterature(uuid)
                literatureLiveData.value = literature

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }

    }}