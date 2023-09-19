package com.example.universeofinformation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.universeofinformation.R
import com.example.universeofinformation.adapter.Holders.CountryHolder
import com.example.universeofinformation.adapter.Holders.GeographicalEventHolder
import com.example.universeofinformation.adapter.Holders.HistoryHolder
import com.example.universeofinformation.adapter.Holders.LiteratureHolder
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
import com.example.universeofinformation.utility.toggleStarredState
import com.example.universeofinformation.view.CountryListFragmentDirections
import com.example.universeofinformation.view.GeographicEventListFragmentDirections
import com.example.universeofinformation.view.LiteratureListFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DataAdapter(var dataList:ArrayList<Any>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    lateinit var historyQueryRepository: HistoryQueryRepository
    lateinit var geographicQueryRepository: GeographicQueryRepository
    lateinit var literatureQueryRepository: LiteratureQueryRepository
    lateinit var countryQueryRepository: CountryQueryRepository
    private val HISTORY = 1
    private val GEOGRAPHICAL_EVENT = 2
    private val LITERATURE = 3
    private val COUNTRY = 4


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

                val history = dataList[position] as History
                holder.binding.history = history
                holder.binding.listener = holder

            }
            is GeographicalEventHolder -> {
                val geographicEvent = dataList[position] as GeographicEvent
                holder.binding.geographicEvent = geographicEvent
                holder.binding.listener = holder

            }
            is LiteratureHolder ->{
                val literature = dataList[position] as Literature
                holder.binding.literature = literature
                holder.binding.listener = holder

            }
            is CountryHolder -> {
                val country = dataList[position] as Country
                holder.binding.country = country
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

    private fun toggleStarredState(starred:Boolean, imageView: ImageView) {

        CoroutineScope(Dispatchers.Main).launch{
            if (starred) {
                imageView.setImageResource(R.drawable.starred)
            } else {
                imageView.setImageResource(R.drawable.not_starred)
            }
        }
    }

}


