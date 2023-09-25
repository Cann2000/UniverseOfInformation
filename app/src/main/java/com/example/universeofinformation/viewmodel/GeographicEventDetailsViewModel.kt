package com.example.universeofinformation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universeofinformation.model.GeographicEvent
import com.example.universeofinformation.repository.GeographicQueryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeographicEventDetailsViewModel@Inject constructor(private val geographicQueryRepository: GeographicQueryRepository):ViewModel() {

    val geographicEventLiveData = MutableLiveData<GeographicEvent>()


    fun getGeographicEvent(uuid: Int){

        viewModelScope.launch {

            try {
                //Bu işlem ana iş parçacığında yapılıyormuş gibi görünse de, geographicQueryRepository.getGeographicEvent(uuid) withContext(Dispatchers.IO) içinde çalıştığı için arka planda çalışır.
                val geographicEvent = geographicQueryRepository.getGeographicEvent(uuid)
                geographicEventLiveData.value = geographicEvent

            }
            catch (e: Exception){

                e.printStackTrace()
            }
        }
    }
}