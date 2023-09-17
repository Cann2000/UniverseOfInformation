package com.example.universeofinformation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.universeofinformation.R
import com.example.universeofinformation.databinding.RecyclerRowCountryListBinding
import com.example.universeofinformation.databinding.RecyclerRowGeographicEventListBinding
import com.example.universeofinformation.databinding.RecyclerRowHistoryListBinding
import com.example.universeofinformation.databinding.RecyclerRowLiteratureListBinding
import com.example.universeofinformation.model.Country
import com.example.universeofinformation.model.GeographicEvent
import com.example.universeofinformation.model.History
import com.example.universeofinformation.model.Literature
import com.example.universeofinformation.repository.CountryQueryRepository
import com.example.universeofinformation.repository.GeographicQueryRepository
import com.example.universeofinformation.repository.HistoryQueryRepository
import com.example.universeofinformation.repository.LiteratureQueryRepository
import com.example.universeofinformation.view.CountryListFragmentDirections
import com.example.universeofinformation.view.GeographicEventListFragmentDirections
import com.example.universeofinformation.view.HistoryListFragmentDirections
import com.example.universeofinformation.view.LiteratureListFragmentDirections
import com.example.universeofinformation.viewmodel.GeographicEventListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataAdapter(var dataList:ArrayList<Any>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var historyQueryRepository: HistoryQueryRepository
    lateinit var geographicQueryRepository: GeographicQueryRepository
    lateinit var literatureQueryRepository: LiteratureQueryRepository
    lateinit var countryQueryRepository: CountryQueryRepository
    private val HISTORY = 1
    private val GEOGRAPHICAL_EVENT = 2
    private val LITERATURE = 3
    private val COUNTRY = 4

    class HistoryHolder(val binding: RecyclerRowHistoryListBinding, val historyQueryRepository: HistoryQueryRepository): RecyclerView.ViewHolder(binding.root),ClickListener
    {
        override fun dataClicked(view: View) {

            val history = binding.history

            if(history!=null)
            {
                val action = HistoryListFragmentDirections.actionHistoryListFragmentToHistoryDetailsFragment(history.uuid!!)
                Navigation.findNavController(view).navigate(action)
            }
        }

        override fun starClicked(view: View) {
            binding.starImage.setBackgroundColor(Color.YELLOW)

            CoroutineScope(Dispatchers.Default).launch {

                val historyUuid = binding.history?.uuid!!

                historyUuid?.let {

                    val historyStarred = historyQueryRepository.getHistory(it)

                    historyStarred?.let {

                        if(it.starred == true){

                            historyQueryRepository.updateHistory(historyUuid!!,false)
                        }
                        else{
                            historyQueryRepository.updateHistory(historyUuid!!,true)

                        }
                    }
                }
            }
        }

    }
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

            binding.starImage.setBackgroundColor(Color.YELLOW)


            CoroutineScope(Dispatchers.Default).launch {

                val geographicEventUuid = binding.geographicEvent?.uuid!!

                geographicEventUuid?.let {

                    val geographicEventStarred = geographicQueryRepository.getGeographicEvent(it)

                    geographicEventStarred?.let {

                        if(it.starred == true){

                            geographicQueryRepository.updateGeographicalEvent(geographicEventUuid!!,false)
                        }
                        else{
                            geographicQueryRepository.updateGeographicalEvent(geographicEventUuid!!,true)
                        }
                    }
                }
            }
        }

    }

    class LiteratureHolder(val binding: RecyclerRowLiteratureListBinding, val literatureQueryRepository: LiteratureQueryRepository): RecyclerView.ViewHolder(binding.root),ClickListener{

        override fun dataClicked(view: View) {

            val literature = binding.literature

            if(literature!=null)
            {
                val action = LiteratureListFragmentDirections.actionLiteratureListFragmentToLiteratureDetailsFragment(literature.uuid!!)
                Navigation.findNavController(view).navigate(action)
            }
        }

        override fun starClicked(view: View) {
            binding.starImage.setBackgroundColor(Color.YELLOW)

            CoroutineScope(Dispatchers.Default).launch {

                val literatureUuid = binding.literature?.uuid!!

                literatureUuid?.let {

                    val literatureStarred = literatureQueryRepository.getLiterature(it)

                    literatureStarred?.let {

                        if(it.starred == true){

                            literatureQueryRepository.updateLiterature(literatureUuid!!,false)
                        }
                        else{
                            literatureQueryRepository.updateLiterature(literatureUuid!!,true)

                        }
                    }
                }
            }
        }

    }
    class CountryHolder(val binding: RecyclerRowCountryListBinding, val countryQueryRepository: CountryQueryRepository): RecyclerView.ViewHolder(binding.root),ClickListener{

        override fun dataClicked(view: View) {

            val country = binding.country

            if(country!=null)
            {
                val action = CountryListFragmentDirections.actionCountryListFragmentToCountryDetailsFragment(country.uuid!!)
                Navigation.findNavController(view).navigate(action)
            }
        }

        override fun starClicked(view: View) {
            binding.starImage.setBackgroundColor(Color.YELLOW)

            CoroutineScope(Dispatchers.Default).launch {

                val countryUuid = binding.country?.uuid!!

                countryUuid?.let {

                    val literatureStarred = countryQueryRepository.getCountry(it)

                    literatureStarred?.let {

                        if(it.starred == true){

                            countryQueryRepository.updateCountry(countryUuid!!,false)
                        }
                        else{
                            countryQueryRepository.updateCountry(countryUuid!!,true)

                        }
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding

        return when (viewType) {

            HISTORY -> {

                binding = DataBindingUtil.inflate<RecyclerRowHistoryListBinding>(
                    inflater,
                    R.layout.recycler_row_history_list,
                    parent,
                    false
                )

                HistoryHolder(binding,historyQueryRepository)
            }
            GEOGRAPHICAL_EVENT -> {

                binding = DataBindingUtil.inflate<RecyclerRowGeographicEventListBinding>(
                    inflater,
                    R.layout.recycler_row_geographic_event_list,
                    parent,
                    false
                )

                GeographicalEventHolder(binding,geographicQueryRepository)
            }
            LITERATURE -> {
                binding = DataBindingUtil.inflate<RecyclerRowLiteratureListBinding>(
                    inflater,
                    R.layout.recycler_row_literature_list,
                    parent,
                    false
                )

                LiteratureHolder(binding,literatureQueryRepository)
            }
            COUNTRY -> {
                binding = DataBindingUtil.inflate<RecyclerRowCountryListBinding>(
                    inflater,
                    R.layout.recycler_row_country_list,
                    parent,
                    false
                )

                CountryHolder(binding,countryQueryRepository)
            }
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is HistoryHolder -> {
                holder.binding.history = dataList[position] as History
                holder.binding.listener = holder
            }
            is GeographicalEventHolder -> {
                holder.binding.geographicEvent = dataList[position] as GeographicEvent
                holder.binding.listener = holder
            }
            is LiteratureHolder ->{
                holder.binding.literature = dataList[position] as Literature
                holder.binding.listener = holder
            }
            is CountryHolder -> {
                holder.binding.country = dataList[position] as Country
                holder.binding.listener = holder
            }
        }
    }



    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {

        // Pozisyona göre veri türünü belirleyin ve viewType olarak döndürün
        return when (dataList[position]) {

            is History -> HISTORY
            is GeographicEvent -> GEOGRAPHICAL_EVENT
            is Literature -> LITERATURE
            is Country -> COUNTRY
            else -> throw IllegalArgumentException("Unknown data type")
        }
    }

    fun dataListUpdate(newData:List<Any>){
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()
    }
    fun setFilteredList(dataList:ArrayList<Any>){
        this.dataList  = dataList
        notifyDataSetChanged()
    }

}