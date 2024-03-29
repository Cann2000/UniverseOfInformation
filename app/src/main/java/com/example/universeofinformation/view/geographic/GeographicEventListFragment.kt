package com.example.universeofinformation.view.geographic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universeofinformation.adapter.DataAdapter
import com.example.universeofinformation.databinding.FragmentGeographicEventListBinding
import com.example.universeofinformation.repository.GeographicQueryRepository
import com.example.universeofinformation.utility.Constants
import com.example.universeofinformation.utility.isNetworkAvailable
import com.example.universeofinformation.viewmodel.geographic.GeographicEventListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GeographicEventListFragment : Fragment() {

    @Inject
    lateinit var geographicQueryRepository: GeographicQueryRepository

    private var _binding: FragmentGeographicEventListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : GeographicEventListViewModel
    private val adapter = DataAdapter(arrayListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isConnectedToNetwork = isNetworkAvailable(requireContext())

        if(!isConnectedToNetwork){

            Toast.makeText(requireContext(), "You don't have internet access", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGeographicEventListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(GeographicEventListViewModel::class.java)
        viewModel.refreshData()

        adapter.geographicQueryRepository = viewModel.geographicQueryRepository

        binding.geographicalEventsRecycler.adapter = adapter
        binding.geographicalEventsRecycler.layoutManager = LinearLayoutManager(requireView().context)
        binding.geographicalEventsRecycler.setHasFixedSize(true)

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.errorText.visibility = View.GONE
            binding.loadProgressBar.visibility = View.VISIBLE

            viewModel.refreshInternet()

            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchViewFilterList(newText,adapter)
                return true
            }

        })

        observeLiveData()
    }

    private fun observeLiveData(){

        viewModel.geographicalEvents.observe(viewLifecycleOwner, Observer { geographic_event ->

            geographic_event?.let {

                binding.geographicalEventsRecycler.visibility = View.VISIBLE
                adapter.dataListUpdate(geographic_event)
            }
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { boolean ->

            boolean?.let {

                if (boolean == true) {
                    binding.geographicalEventsRecycler.visibility = View.GONE
                    binding.errorText.visibility = View.VISIBLE
                } else {
                    binding.searchView.setQuery("", false)
                    binding.errorText.visibility = View.INVISIBLE
                }
            }
        })
        viewModel.uploading.observe(viewLifecycleOwner, Observer { boolean ->

            boolean?.let {

                if (boolean == true) {
                    binding.geographicalEventsRecycler.visibility = View.GONE
                    binding.errorText.visibility = View.GONE
                    binding.loadProgressBar.visibility = View.VISIBLE
                } else {
                    binding.loadProgressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}