package com.example.universeofinformation.adapter.Holders

import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.universeofinformation.adapter.ClickListener
import com.example.universeofinformation.databinding.RecyclerRowCountryListBinding
import com.example.universeofinformation.repository.CountryQueryRepository
import com.example.universeofinformation.utility.starClickedUtil
import com.example.universeofinformation.utility.toggleStarredState
import com.example.universeofinformation.view.CountryListFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountryHolder(val binding: RecyclerRowCountryListBinding, val countryQueryRepository: CountryQueryRepository): RecyclerView.ViewHolder(binding.root),
    ClickListener {

    override fun dataClicked(view: View) {

        val country = binding.country

        if(country!=null)
        {
            val action = CountryListFragmentDirections.actionCountryListFragmentToCountryDetailsFragment(country.uuid!!)
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun starClicked(view: View) {


        CoroutineScope(Dispatchers.Default).launch {

            val countryUuid = binding.country?.uuid!!

            val literatureStarred = countryQueryRepository.getCountry(countryUuid)

            literatureStarred?.let {

                if(it.starred == true){

                    countryQueryRepository.updateCountry(countryUuid!!,false)
                }
                else{

                    countryQueryRepository.updateCountry(countryUuid!!,true)
                }

                binding.starImage.starClickedUtil(it.starred) // util

            }
        }
    }

}