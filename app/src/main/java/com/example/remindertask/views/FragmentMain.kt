package com.example.remindertask.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.remindertask.databinding.FragmentMainBinding
import com.example.remindertask.models.HorizontalModel
import com.example.remindertask.models.VerticalItem
import com.example.remindertask.viewmodel.MainViewModel
import com.example.remindertask.views.main.RecyclerAdapter

class FragmentMain : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val mainViewModel: MainViewModel by viewModels { MainViewModel.Factory }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel.getAllReminder()


        binding = FragmentMainBinding.inflate(inflater, container, false)
        val recyclerAdapter = RecyclerAdapter()
        recyclerAdapter.recyclerList = listOf(HorizontalModel(listOf<String>("a", "b", "c")))

        recyclerAdapter.notifyItemInserted(0)

        mainViewModel.allReminder.observe(viewLifecycleOwner) { it ->
            recyclerAdapter.recyclerList =
                recyclerAdapter.recyclerList + it.map { e -> VerticalItem(e) }.toList()
            recyclerAdapter.notifyItemRangeInserted(recyclerAdapter.recyclerList.size, it.size)
            binding.loadingReminder.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }

        binding.recyclerView.adapter = recyclerAdapter
        binding.addReminder.setOnClickListener {
            this.findNavController().navigate(FragmentMainDirections.mainToAddReminder())
        }
        return binding.root
    }
}