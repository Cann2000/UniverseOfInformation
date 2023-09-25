package com.example.universeofinformation.view

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
import com.example.universeofinformation.viewmodel.GeographicEventDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeographicEventDetailsFragment : Fragment() {

    private var _dataBinding: FragmentGeographicEventDetailsBinding? = null
    private val dataBinding get() = _dataBinding!!

    private lateinit var viewModel: GeographicEventDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_geographic_event_details,container, false)
        val view = dataBinding.root
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

    override fun onDestroyView() {
        super.onDestroyView()
        _dataBinding = null
    }

    fun obserLiveData()
    {
        viewModel.geographicEventLiveData.observe(viewLifecycleOwner, Observer {

            it?.let {
                dataBinding.selectedGeographicEvent = it
            }
        })
    }
}