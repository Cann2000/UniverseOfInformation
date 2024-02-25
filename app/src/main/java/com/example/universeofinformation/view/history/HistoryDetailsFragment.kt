package com.example.universeofinformation.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.universeofinformation.R
import com.example.universeofinformation.databinding.FragmentHistoryDetailsBinding
import com.example.universeofinformation.viewmodel.history.HistoryDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryDetailsFragment : Fragment() {

    private var _binding: FragmentHistoryDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HistoryDetailsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_history_details,container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setMenuVisibility(false)

        viewModel = ViewModelProvider(this).get(HistoryDetailsViewModel::class.java)

        arguments?.let {
            val historyId = HistoryDetailsFragmentArgs.fromBundle(it).historyId
            viewModel.getRoomData(historyId)
        }

        observeLiveData()

    }

    fun observeLiveData()
    {
        viewModel.historyLiveData.observe(viewLifecycleOwner, Observer { history ->

            history?.let {

                binding.selectedHistory = history
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}