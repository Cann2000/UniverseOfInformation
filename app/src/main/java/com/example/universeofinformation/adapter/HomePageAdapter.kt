package com.example.universeofinformation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.universeofinformation.R
import com.example.universeofinformation.databinding.RecyclerRowHomepageBinding
import com.example.universeofinformation.model.HomePageContent

class HomePageAdapter(var contentList:ArrayList<HomePageContent>):RecyclerView.Adapter<HomePageAdapter.ContentHolder>() {

    class ContentHolder(val binding:RecyclerRowHomepageBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentHolder {
        val binding = DataBindingUtil.inflate<RecyclerRowHomepageBinding>(LayoutInflater.from(parent.context),R.layout.recycler_row_homepage,parent,false)
        return ContentHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentHolder, position: Int) {
        holder.binding.homePageContent = contentList[position]

        holder.itemView.setOnClickListener {

            Navigation.findNavController(it).navigate(contentList[position].action)
        }
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    fun adapterUpdate(newContent:List<HomePageContent>){
        contentList.clear()
        contentList.addAll(newContent)
        notifyDataSetChanged()

    }

}