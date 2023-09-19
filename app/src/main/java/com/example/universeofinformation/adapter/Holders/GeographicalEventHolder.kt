package com.example.universeofinformation.adapter.Holders

import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.universeofinformation.adapter.ClickListener
import com.example.universeofinformation.databinding.RecyclerRowGeographicEventListBinding
import com.example.universeofinformation.repository.GeographicQueryRepository
import com.example.universeofinformation.utility.starClickedUtil
import com.example.universeofinformation.utility.toggleStarredState
import com.example.universeofinformation.view.GeographicEventListFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeographicalEventHolder(val binding: RecyclerRowGeographicEventListBinding, val geographicQueryRepository: GeographicQueryRepository): RecyclerView.ViewHolder(binding.root),ClickListener{

    override fun dataClicked(view: View) {

        val geographicEvent = binding.geographicEvent

        if(geographicEvent!=null)
        {
            val action = GeographicEventListFragmentDirections.actionGeographicalEventsFragmentToGeographicEventDetailsFragment(geographicEvent.uuid!!)
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun starClicked(view: View) {

        CoroutineScope(Dispatchers.Default).launch {

            val geographicEventUuid = binding.geographicEvent?.uuid!!

            val geographicEventStarred = geographicQueryRepository.getGeographicEvent(geographicEventUuid)

            geographicEventStarred?.let {

                if(it.starred == true){


                    geographicQueryRepository.updateGeographicalEvent(geographicEventUuid!!,false)
                }
                else{

                    geographicQueryRepository.updateGeographicalEvent(geographicEventUuid!!,true)
                }

                binding.starImage.starClickedUtil(it.starred) // util

            }
        }

    }

}