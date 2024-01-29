package com.example.remindertask.presentation.screens.addRemainderMap.mapSearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.remindertask.databinding.FragmentMapSearchBinding
import com.example.remindertask.presentation.screens.addRemainderMap.AddRemainderMapViewModel


class MapSearchFragment : Fragment() {

    private lateinit var binding: FragmentMapSearchBinding
    private lateinit var viewModel: AddRemainderMapViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(requireActivity())[AddRemainderMapViewModel(requireActivity().application)::class.java]
        val searchItemRecyclerAdapter = MapSearchRecyclerAdapter {
            viewModel.setSelectedPlaces(it)
            this.findNavController().navigate(MapSearchFragmentDirections.mapSearchToAddMap())
        }
        binding.searchRecycler.adapter = searchItemRecyclerAdapter
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.searchPlaces(text.toString())
        }
        viewModel.resultLiveData.observe(viewLifecycleOwner) {
            searchItemRecyclerAdapter.updateList(it)
        }
    }
}