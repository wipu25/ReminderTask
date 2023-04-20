package com.example.remindertask

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class AddReminder : Fragment() {
    private lateinit var viewModel: AddReminderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[AddReminderViewModel::class.java]
        return inflater.inflate(R.layout.fragment_add_reminder, container, false)
    }
}