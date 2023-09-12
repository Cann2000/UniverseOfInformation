package com.example.universeofinformation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universeofinformation.model.History
import com.example.universeofinformation.repository.HistoryQueryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryDetailsViewModel@Inject constructor(private val historyQueryRepository: HistoryQueryRepository): ViewModel() {

    val historyLiveData = MutableLiveData<History>()


    fun getRoomData(uuid: Int)
    {
        viewModelScope.launch {
            try {
                //Bu işlem ana iş parçacığında yapılıyormuş gibi görünse de, historyQueryRepository.getHistory(uuid) withContext(Dispatchers.IO) içinde çalıştığı için arka planda çalışır.
                val history = historyQueryRepository.getHistory(uuid)
                historyLiveData.value = history

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }

    }
}