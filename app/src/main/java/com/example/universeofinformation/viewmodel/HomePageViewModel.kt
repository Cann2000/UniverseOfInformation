package com.example.universeofinformation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.universeofinformation.model.HomePageContent
import com.example.universeofinformation.view.HomePageFragmentDirections

class HomePageViewModel:ViewModel() {

    val homePageContent = MutableLiveData<List<HomePageContent>>()

    fun getData(){

        val contentList = ArrayList<HomePageContent>()

        val contentHistory = HomePageContent("Tarih","https://raw.githubusercontent.com/Cann2000/HistoryBookData/main/a.jpg",HomePageFragmentDirections.actionHomePageFragmentToHistoryListFragment())
        val contentGeography = HomePageContent("Coğrafi Olaylar","https://raw.githubusercontent.com/Cann2000/HistoryBookData/main/a.jpg",HomePageFragmentDirections.actionHomePageFragmentToGeographicalEventsFragment())
        val contentUniverse = HomePageContent("Evren","https://raw.githubusercontent.com/Cann2000/HistoryBookData/main/a.jpg",HomePageFragmentDirections.actionHomePageFragmentToHistoryListFragment())

        contentList.add(contentHistory)
        contentList.add(contentGeography)
        contentList.add(contentUniverse)



        homePageContent.value = contentList
    }
}