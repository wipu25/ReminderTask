package com.example.remindertask.views.addReminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.remindertask.viewmodel.MapSearchViewModel
import com.example.remindertask.databinding.FragmentMapSearchBinding
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient


class MapSearchFragment : Fragment() {

    private lateinit var binding: FragmentMapSearchBinding
    private lateinit var viewModel: MapSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MapSearchViewModel(requireActivity().application)::class.java]
        val searchItemRecyclerAdapter = MapSearchRecyclerAdapter()
        binding.searchRecycler.adapter = searchItemRecyclerAdapter
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.searchPlaces(text.toString())
        }
        viewModel.resultLiveData.observe(viewLifecycleOwner) {
            searchItemRecyclerAdapter.updateList(it)
        }
    }
}