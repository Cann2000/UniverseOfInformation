package com.example.universeofinformation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universeofinformation.model.Country
import com.example.universeofinformation.model.GeographicEvent
import com.example.universeofinformation.repository.CountryQueryRepository
import com.example.universeofinformation.repository.GeographicQueryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryDetailsViewModel@Inject constructor(private val countryQueryRepository: CountryQueryRepository):ViewModel() {

    val countryLiveData = MutableLiveData<Country>()


    fun getCountry(uuid: Int){

        viewModelScope.launch {

            try {
                //Bu işlem ana iş parçacığında yapılıyormuş gibi görünse de, geographicQueryRepository.getGeographicEvent(uuid) withContext(Dispatchers.IO) içinde çalıştığı için arka planda çalışır.
                val country = countryQueryRepository.getCountry(uuid)
                countryLiveData.value = country

            }
            catch (e: Exception){

                e.printStackTrace()
            }
        }
    }
}