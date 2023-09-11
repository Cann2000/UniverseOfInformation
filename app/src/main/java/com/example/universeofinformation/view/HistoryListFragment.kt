package com.example.universeofinformation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universeofinformation.R
import com.example.universeofinformation.adapter.DataAdapter
import com.example.universeofinformation.databinding.FragmentHistoryListBinding
import com.example.universeofinformation.viewmodel.HistoryListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HistoryListFragment : Fragment() {

    private var _binding: FragmentHistoryListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HistoryListViewModel
    private val adapter = DataAdapter(arrayListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (requireActivity() as MenuHost).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId){
                    R.id.favorites -> {
                        //val action= HistoryListFragmentDirections.actionHistoryListFragmentToHistoryDetailsFragment()
                        //Navigation.findNavController(requireView()).navigate(action)
                        return true
                    }
                }

                return false
            }

        },viewLifecycleOwner)


        viewModel = ViewModelProvider(this).get(HistoryListViewModel::class.java)
        viewModel.refreshData()

        binding.historyRecycler.adapter = adapter
        binding.historyRecycler.layoutManager = LinearLayoutManager(requireView().context)
        binding.historyRecycler.setHasFixedSize(true)


        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.errorText.visibility = View.GONE
            binding.loadProgressBar.visibility = View.VISIBLE

            viewModel.refreshInternet()

            binding.swipeRefreshLayout.isRefreshing = false
        }


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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


    fun observeLiveData() {

        viewModel.history.observe(viewLifecycleOwner, Observer {

            it?.let {
                binding.historyRecycler.visibility = View.VISIBLE
                adapter.dataListUpdate(it)
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {

            it?.let {

                if (it) {
                    binding.historyRecycler.visibility = View.GONE
                    binding.errorText.visibility = View.VISIBLE
                } else {
                    binding.errorText.visibility = View.INVISIBLE
                }
            }
        })

        viewModel.uploading.observe(viewLifecycleOwner, Observer {

            it?.let {

                if (it) {
                    binding.historyRecycler.visibility = View.GONE
                    binding.errorText.visibility = View.GONE
                    binding.loadProgressBar.visibility = View.VISIBLE
                } else {
                    binding.loadProgressBar.visibility = View.GONE
                }
            }
        })
    }
}