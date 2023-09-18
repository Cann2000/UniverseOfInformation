package com.example.universeofinformation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universeofinformation.R
import com.example.universeofinformation.adapter.FavoriteListAdapter
import com.example.universeofinformation.adapter.HomePageAdapter
import com.example.universeofinformation.databinding.FragmentHistoryListBinding
import com.example.universeofinformation.databinding.FragmentHomePageBinding
import com.example.universeofinformation.viewmodel.HomePageViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageFragment : Fragment() {

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel:HomePageViewModel
    private val homePageAdapter = HomePageAdapter(arrayListOf())
    private val favoriteListAdapter = FavoriteListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewContent.adapter= homePageAdapter
        binding.recyclerViewContent.layoutManager =  GridLayoutManager(requireView().context,2)
        binding.recyclerViewContent.setHasFixedSize(true)

        binding.viewPager.adapter = favoriteListAdapter
        TabLayoutMediator(binding.tabDots, binding.viewPager,true) { tab, position -> }.attach()

        viewModel = ViewModelProvider(this).get(HomePageViewModel::class.java)
        viewModel.getContent()
        viewModel.getFavorites()

        observeLiveData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun observeLiveData()
    {
        viewModel.homePageContent.observe(viewLifecycleOwner, Observer {

            it?.let {
                homePageAdapter.adapterUpdate(it)
            }
        })

        viewModel.favoriteList.observe(viewLifecycleOwner, Observer {

            it?.let {

                favoriteListAdapter.dataListUpdate(it)
            }
        })
    }

}