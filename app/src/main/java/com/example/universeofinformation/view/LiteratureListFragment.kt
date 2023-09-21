package com.example.universeofinformation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universeofinformation.R
import com.example.universeofinformation.adapter.DataAdapter
import com.example.universeofinformation.databinding.FragmentHistoryListBinding
import com.example.universeofinformation.databinding.FragmentLiteratureListBinding
import com.example.universeofinformation.repository.GeographicQueryRepository
import com.example.universeofinformation.repository.LiteratureQueryRepository
import com.example.universeofinformation.viewmodel.LiteratureListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LiteratureListFragment : Fragment() {

    @Inject
    lateinit var literatureQueryRepository: LiteratureQueryRepository

    private var _binding: FragmentLiteratureListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LiteratureListViewModel
    private val adapter = DataAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLiteratureListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(LiteratureListViewModel::class.java)
        viewModel.refreshData()

        binding.literatureRecycler.adapter = adapter
        binding.literatureRecycler.layoutManager = LinearLayoutManager(requireView().context)
        binding.literatureRecycler.setHasFixedSize(true)

        adapter.literatureQueryRepository = literatureQueryRepository

        binding.swipeRefreshLayout.setOnRefreshListener {

            binding.errorText.visibility = View.GONE
            binding.loadProgressBar.visibility = View.VISIBLE

            viewModel.refreshInternet()

            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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

    fun observeLiveData(){

        viewModel.literature.observe(viewLifecycleOwner, Observer {

            binding.literatureRecycler.visibility = View.VISIBLE
            adapter.dataListUpdate(it)
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {

            it?.let {

                if (it) {
                    binding.literatureRecycler.visibility = View.GONE
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
                    binding.literatureRecycler.visibility = View.GONE
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