package com.example.remindertask.views.addReminder

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.remindertask.R
import com.example.remindertask.databinding.FragmentAddReminderBinding
import com.example.remindertask.databinding.TextFieldBinding
import com.example.remindertask.models.data.SelectedLocation
import com.example.remindertask.viewmodel.AddReminderViewModel
import java.time.LocalDateTime
import java.time.ZoneId

class FragmentAddReminder : Fragment() {
    private lateinit var viewModel: AddReminderViewModel
    private lateinit var binding: FragmentAddReminderBinding
    private lateinit var startDateBinding: TextFieldBinding
    private lateinit var endDateBinding: TextFieldBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddReminderBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this)[AddReminderViewModel::class.java]
        startDateBinding = binding.startDate
        endDateBinding = binding.endDate

        initTextField(binding.title, "Remainder Title", "Grocery")
        initTextField(binding.description, "Remainder Description", "Don't Forget to buy milk")
        initTextField(binding.location, "Remainder Location", "Bangkok")
        initTextField(startDateBinding, "Start Date", "2/12/2022")
        initTextField(endDateBinding, "End Date", "4/12/2022")
        initStartDateField()
        initEndDateField()
        initLocationField()

        binding.cancel.setOnClickListener {
            this.findNavController().navigate(FragmentAddReminderDirections.addReminderToMain())
        }

        binding.save.setOnClickListener {
            viewModel.onSave(
                title = binding.title.field.text.toString(),
                description = binding.description.field.text.toString(),
                location = binding.location.field.text.toString()
            )
        }

        return binding.root
    }

    private fun initLocationField() {
        val locationField = binding.location.field
        val locationData = arguments?.get("map_select")
        if (locationData != null) {
            locationField.setText((locationData as SelectedLocation).address)
        }
        disableFieldSettings(locationField)
        locationField.setOnClickListener {
            this.findNavController().navigate(FragmentAddReminderDirections.addReminderToMap())
        }
    }

    private fun initTextField(binding: TextFieldBinding, title: String, hint: String) {
        binding.title.text = title
        binding.field.hint = hint
    }

    private fun initStartDateField() {
        val fieldBinding = startDateBinding.field
        disableFieldSettings(fieldBinding)

        viewModel.startDateLiveData.observe(this.viewLifecycleOwner) { startDate ->
            val year = startDate.year
            val month = startDate.monthValue
            val day = startDate.dayOfMonth
            fieldBinding.setText("$day/$month/$year")
        }

        fieldBinding.setOnClickListener {
            val startDate = viewModel.startDateLiveData.value!!
            displayDatePicker(startDate, LocalDateTime.now(), viewModel::setStartDate)
        }
    }

    private fun initEndDateField() {
        val fieldBinding = endDateBinding.field
        disableFieldSettings(fieldBinding)

        viewModel.endDateLiveData.observe(this.viewLifecycleOwner) { endDate ->
            val year = endDate.year
            val month = endDate.monthValue
            val day = endDate.dayOfMonth
            fieldBinding.setText("$day/$month/$year")
        }

        fieldBinding.setOnClickListener {
            val endDate = viewModel.endDateLiveData.value!!
            val startDate = viewModel.startDateLiveData.value!!
            displayDatePicker(endDate, startDate, viewModel::setEndDate)
        }
    }

    private fun disableFieldSettings(field: EditText) {
        field.showSoftInputOnFocus = false
        field.inputType = InputType.TYPE_NULL
        field.isFocusable = false
    }

    private fun displayDatePicker(
        dateHighlight: LocalDateTime,
        minDate: LocalDateTime,
        setDate: (newDate: LocalDateTime) -> Unit
    ) {
        val datePicker = DatePickerDialog(
            this.requireContext(),
            R.style.DialogTheme,
            { _, year, monthOfYear, dayOfMonth ->
                setDate(LocalDateTime.of(year, monthOfYear + 1, dayOfMonth, 0, 0))
            },
            dateHighlight.year,
            dateHighlight.monthValue - 1,
            dateHighlight.dayOfMonth
        )

        datePicker.datePicker.minDate =
            minDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        datePicker.show()
    }
}