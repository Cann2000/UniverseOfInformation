package com.example.universeofinformation.adapter.Holders

import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.universeofinformation.adapter.ClickListener
import com.example.universeofinformation.databinding.RecyclerRowHistoryListBinding
import com.example.universeofinformation.repository.HistoryQueryRepository
import com.example.universeofinformation.utility.starClickedUtil
import com.example.universeofinformation.view.HistoryListFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryHolder(val binding: RecyclerRowHistoryListBinding, val historyQueryRepository: HistoryQueryRepository): RecyclerView.ViewHolder(binding.root),
    ClickListener
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

        CoroutineScope(Dispatchers.Default).launch {

            val historyUuid = binding.history?.uuid!!

            val historyStarred = historyQueryRepository.getHistory(historyUuid)

            historyStarred?.let {

                if(it.starred == true){

                    historyQueryRepository.updateHistory(historyUuid!!,false)
                }
                else{

                    historyQueryRepository.updateHistory(historyUuid!!,true)
                }

                binding.starImage.starClickedUtil(it.starred) // util

            }
        }
    }

}