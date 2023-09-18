package com.example.universeofinformation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.universeofinformation.databinding.RecyclerRowFavoriteListBinding
import com.example.universeofinformation.model.Favorite

class FavoriteListAdapter(var favoriteList:ArrayList<Favorite>):RecyclerView.Adapter<FavoriteListAdapter.FavoritesHolder>() {

    class FavoritesHolder(val binding:RecyclerRowFavoriteListBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesHolder {
        val binding = RecyclerRowFavoriteListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavoritesHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritesHolder, position: Int) {
        holder.binding.favorite = favoriteList[position]
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    fun dataListUpdate(newData:List<Favorite>){
        favoriteList.clear()
        favoriteList.addAll(newData)
        notifyDataSetChanged()
    }

}