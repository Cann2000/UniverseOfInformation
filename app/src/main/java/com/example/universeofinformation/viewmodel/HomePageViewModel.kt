package com.example.universeofinformation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universeofinformation.model.Favorite
import com.example.universeofinformation.model.HomePageContent
import com.example.universeofinformation.repository.FavoriteQueryRepository
import com.example.universeofinformation.view.HomePageFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel@Inject constructor(private val favoriteQueryRepository: FavoriteQueryRepository):ViewModel() {

    val homePageContent = MutableLiveData<List<HomePageContent>>()
    val favoriteList = MutableLiveData<List<Favorite>>()

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println(throwable.localizedMessage)
    }

    fun getContent(){

        val contentList = ArrayList<HomePageContent>()

        contentList.clear()

        val contentHistory = HomePageContent("Tarih","https://raw.githubusercontent.com/Cann2000/UniverseOfInformationData/main/PicturesContent/Tarih.jpg",HomePageFragmentDirections.actionHomePageFragmentToHistoryListFragment())
        val contentGeography = HomePageContent("Coğrafi Olaylar","https://raw.githubusercontent.com/Cann2000/UniverseOfInformationData/main/PicturesContent/Cografya.jpg",HomePageFragmentDirections.actionHomePageFragmentToGeographicalEventsFragment())
        val contentLiterature = HomePageContent("Edebiyat","https://raw.githubusercontent.com/Cann2000/UniverseOfInformationData/main/PicturesContent/Edebiyat.jpg",HomePageFragmentDirections.actionHomePageFragmentToLiteratureListFragment())
        val contentCountry = HomePageContent("Türk Devletleri","https://raw.githubusercontent.com/Cann2000/UniverseOfInformationData/main/PicturesContent/TurkDevletleri.png",HomePageFragmentDirections.actionHomePageFragmentToCountryListFragment())

        contentList.add(contentHistory)
        contentList.add(contentGeography)
        contentList.add(contentLiterature)
        contentList.add(contentCountry)

        homePageContent.value = contentList

    }

    fun getFavorites(){

        viewModelScope.launch {

            val favorites = favoriteQueryRepository.getAllFavorites()
            favoriteList.value = favorites
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}