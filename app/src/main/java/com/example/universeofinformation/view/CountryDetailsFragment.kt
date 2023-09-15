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
import com.example.universeofinformation.databinding.FragmentCountryDetailsBinding
import com.example.universeofinformation.databinding.FragmentHistoryDetailsBinding
import com.example.universeofinformation.viewmodel.CountryDetailsViewModel
import com.example.universeofinformation.viewmodel.GeographicEventDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CountryDetailsFragment : Fragment() {

    private var _dataBinding: FragmentCountryDetailsBinding? = null
    private val dataBinding get() = _dataBinding!!

    private lateinit var viewModel: CountryDetailsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_country_details,container, false)
        val view = dataBinding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(CountryDetailsViewModel::class.java)

        arguments?.let {

            val countryId = CountryDetailsFragmentArgs.fromBundle(it).countryId
            viewModel.getCountry(countryId)
        }

        obserLiveData()
    }

    fun obserLiveData()
    {
        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer {

            it?.let {
                dataBinding.selectedCountry = it
            }
        })
    }

}