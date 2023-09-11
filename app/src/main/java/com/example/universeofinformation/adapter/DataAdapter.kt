package com.example.universeofinformation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.universeofinformation.R
import com.example.universeofinformation.databinding.RecyclerRowGeographicEventListBinding
import com.example.universeofinformation.databinding.RecyclerRowHistoryListBinding
import com.example.universeofinformation.model.GeographicEvent
import com.example.universeofinformation.model.History
import com.example.universeofinformation.view.HistoryListFragmentDirections

class DataAdapter(var dataList:ArrayList<Any>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HISTORY = 1
    private val GEOGRAPHICAL_EVENT = 2

    class HistoryHolder(val binding: RecyclerRowHistoryListBinding): RecyclerView.ViewHolder(binding.root),ClickListener
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
        }

    }
    class GeographicalEventHolder(val binding: RecyclerRowGeographicEventListBinding): RecyclerView.ViewHolder(binding.root),ClickListener{

        override fun dataClicked(view: View) {

            val history = binding.geographicEvent

            if(history!=null)
            {
                //val action = HistoryListFragmentDirections.actionHistoryListFragmentToHistoryDetailsFragment(history.uuid!!)
                //Navigation.findNavController(view).navigate(action)
            }
        }

        override fun starClicked(view: View) {
            binding.starImage.setBackgroundColor(Color.YELLOW)
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

                HistoryHolder(binding)
            }
            GEOGRAPHICAL_EVENT -> {

                binding = DataBindingUtil.inflate<RecyclerRowGeographicEventListBinding>(
                    inflater,
                    R.layout.recycler_row_geographic_event_list,
                    parent,
                    false
                )

                GeographicalEventHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is HistoryHolder -> {
                // History veri türü için bağlama işlemini yapın
                holder.binding.history = dataList[position] as History
                holder.binding.listener = holder
            }
            is GeographicalEventHolder -> {
                holder.binding.geographicEvent = dataList[position] as GeographicEvent
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