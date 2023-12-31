package com.example.universeofinformation.view

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
import com.example.universeofinformation.databinding.FragmentCountryListBinding
import com.example.universeofinformation.repository.CountryQueryRepository
import com.example.universeofinformation.utility.Constants
import com.example.universeofinformation.viewmodel.CountryListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CountryListFragment : Fragment() {

    @Inject
    lateinit var countryQueryRepository: CountryQueryRepository

    private var _binding: FragmentCountryListBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel : CountryListViewModel
    private val adapter = DataAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isConnectedToNetwork = Constants.isNetworkAvailable(requireContext())

        if(!isConnectedToNetwork){

            Toast.makeText(requireContext(), "You don't have internet access", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCountryListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(CountryListViewModel::class.java)
        viewModel.refreshData()

        binding.countryRecycler.adapter = adapter
        binding.countryRecycler.layoutManager = LinearLayoutManager(requireView().context)
        binding.countryRecycler.setHasFixedSize(true)

        adapter.countryQueryRepository = countryQueryRepository

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeLiveData(){

        viewModel.countrys.observe(viewLifecycleOwner, Observer {

            it?.let {

                binding.countryRecycler.visibility = View.VISIBLE
                adapter.dataListUpdate(it)
            }
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {

            it?.let {

                if (it) {
                    binding.countryRecycler.visibility = View.GONE
                    binding.errorText.visibility = View.VISIBLE
                } else {
                    binding.searchView.setQuery("", false)
                    binding.errorText.visibility = View.INVISIBLE
                }
            }
        })
        viewModel.uploading.observe(viewLifecycleOwner, Observer {

            it?.let {

                if (it) {
                    binding.countryRecycler.visibility = View.GONE
                    binding.errorText.visibility = View.INVISIBLE
                    binding.loadProgressBar.visibility = View.VISIBLE
                } else {
                    binding.loadProgressBar.visibility = View.GONE
                }
            }
        })
    }
}