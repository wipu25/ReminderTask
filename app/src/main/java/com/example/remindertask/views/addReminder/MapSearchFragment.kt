package com.example.remindertask.views.addReminder

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.example.remindertask.MapSearchViewModel
import com.example.remindertask.databinding.FragmentMapSearchBinding

class MapSearchFragment : Fragment() {

    private lateinit var binding: FragmentMapSearchBinding
    private lateinit var viewModel: MapSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[MapSearchViewModel::class.java]
        binding = FragmentMapSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchEditText.doOnTextChanged { text, start, before, count ->  }
    }
}