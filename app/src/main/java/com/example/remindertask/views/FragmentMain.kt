package com.example.remindertask.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.remindertask.databinding.FragmentMainBinding
import com.example.remindertask.models.HorizontalModel
import com.example.remindertask.models.VerticalItem
import com.example.remindertask.views.intro.FragmentIntroDirections
import com.example.remindertask.views.main.RecyclerAdapter

class FragmentMain : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        val recyclerAdapter = RecyclerAdapter(listOf(HorizontalModel(listOf<String>("a","b","c")),VerticalItem("hi"), VerticalItem("hi2"), VerticalItem("hi3")))
        binding.recyclerView.adapter = recyclerAdapter
        binding.addReminder.setOnClickListener {
            this.findNavController().navigate(FragmentMainDirections.mainToAddReminder())
        }
        return binding.root
    }
}