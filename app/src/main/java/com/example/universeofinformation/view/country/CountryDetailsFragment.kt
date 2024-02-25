package com.example.universeofinformation.view.country

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
import com.example.universeofinformation.viewmodel.country.CountryDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CountryDetailsFragment : Fragment() {

    private var _binding: FragmentCountryDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CountryDetailsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_country_details,container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(CountryDetailsViewModel::class.java)

        arguments?.let {

            val countryId = CountryDetailsFragmentArgs.fromBundle(it).countryId
            viewModel.getCountry(countryId)
        }

        observeLiveData()
    }

    fun observeLiveData()
    {
        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer { country ->

            country?.let {

                binding.selectedCountry = country
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}