package com.example.universeofinformation.adapter.Holders

import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.universeofinformation.adapter.ClickListener
import com.example.universeofinformation.databinding.RecyclerRowLiteratureListBinding
import com.example.universeofinformation.repository.LiteratureQueryRepository
import com.example.universeofinformation.utility.starClickedUtil
import com.example.universeofinformation.utility.toggleStarredState
import com.example.universeofinformation.view.LiteratureListFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LiteratureHolder(val binding: RecyclerRowLiteratureListBinding, val literatureQueryRepository: LiteratureQueryRepository): RecyclerView.ViewHolder(binding.root),
    ClickListener {

    override fun dataClicked(view: View) {

        val literature = binding.literature

        if(literature!=null)
        {
            val action = LiteratureListFragmentDirections.actionLiteratureListFragmentToLiteratureDetailsFragment(literature.uuid!!)
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun starClicked(view: View) {


        CoroutineScope(Dispatchers.Default).launch {

            val literatureUuid = binding.literature?.uuid!!

            val literatureStarred = literatureQueryRepository.getLiterature(literatureUuid)

            literatureStarred?.let {

                if(it.starred == true){

                    literatureQueryRepository.updateLiterature(literatureUuid!!,false)
                }
                else{
                    literatureQueryRepository.updateLiterature(literatureUuid!!,true)
                }

                binding.starImage.starClickedUtil(it.starred) // util

            }
        }
    }

}