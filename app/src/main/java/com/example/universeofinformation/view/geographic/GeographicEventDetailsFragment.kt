package com.example.universeofinformation.view.geographic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.universeofinformation.R
import com.example.universeofinformation.databinding.FragmentGeographicEventDetailsBinding
import com.example.universeofinformation.viewmodel.geographic.GeographicEventDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeographicEventDetailsFragment : Fragment() {

    private var _binding: FragmentGeographicEventDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: GeographicEventDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_geographic_event_details,container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(GeographicEventDetailsViewModel::class.java)

        arguments?.let {

            val geographicEventId = GeographicEventDetailsFragmentArgs.fromBundle(it).geographicEventId
            viewModel.getGeographicEvent(geographicEventId)
        }

        obserLiveData()
    }

    fun obserLiveData()
    {
        viewModel.geographicEventLiveData.observe(viewLifecycleOwner, Observer { geographic_event ->

            geographic_event?.let {

                binding.selectedGeographicEvent = geographic_event
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}