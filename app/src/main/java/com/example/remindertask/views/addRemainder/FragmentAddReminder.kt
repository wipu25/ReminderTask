package com.example.remindertask.views.addRemainder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.remindertask.AddReminderViewModel
import com.example.remindertask.databinding.FragmentAddReminderBinding
import com.example.remindertask.databinding.TextFieldBinding

class FragmentAddReminder : Fragment() {
    private lateinit var viewModel: AddReminderViewModel
    private lateinit var binding: FragmentAddReminderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddReminderBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this)[AddReminderViewModel::class.java]
        initTextField(binding.title, "Remainder Title", "Grocery")
        initTextField(binding.description, "Remainder Description", "Don't Forget to buy milk")
        initTextField(binding.location, "Remainder Location", "Bangkok")
        initTextField(binding.startDate, "Start Date", "2/12/2022")
        initTextField(binding.endDate, "End Date", "4/12/2022")

        return binding.root
    }

    private fun initTextField(binding: TextFieldBinding, title: String, hint: String) {
        binding.title.text = title
        binding.field.hint = hint
    }
}