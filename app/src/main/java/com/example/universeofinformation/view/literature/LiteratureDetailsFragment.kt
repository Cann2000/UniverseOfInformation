package com.example.universeofinformation.view.literature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.universeofinformation.R
import com.example.universeofinformation.databinding.FragmentLiteratureDetailsBinding
import com.example.universeofinformation.viewmodel.literature.LiteratureDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LiteratureDetailsFragment : Fragment() {

    private var _binding: FragmentLiteratureDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LiteratureDetailsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_literature_details,container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(LiteratureDetailsViewModel::class.java)

        arguments?.let {
            val literatureId = LiteratureDetailsFragmentArgs.fromBundle(it).literatureId
            viewModel.getRoomData(literatureId)
        }

        observeLiveData()
    }

    fun observeLiveData()
    {
        viewModel.literatureLiveData.observe(viewLifecycleOwner, Observer { literature ->

            literature?.let {

                binding.selectedLiterature = literature
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}