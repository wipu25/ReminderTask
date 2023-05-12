package com.example.remindertask.views.addReminder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.remindertask.R
import com.example.remindertask.databinding.FragmentAddReminderBinding
import com.example.remindertask.databinding.TextFieldBinding
import com.example.remindertask.models.data.SelectedLocation
import com.example.remindertask.viewmodel.AddReminderViewModel
import java.time.LocalDateTime
import java.time.ZoneId

class FragmentAddReminder : Fragment() {
    private val viewModel: AddReminderViewModel by viewModels { AddReminderViewModel.Factory }
    private lateinit var binding: FragmentAddReminderBinding
    private lateinit var startDateBinding: TextFieldBinding
    private lateinit var endDateBinding: TextFieldBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddReminderBinding.inflate(layoutInflater)
        startDateBinding = binding.startDate
        endDateBinding = binding.endDate

        val titleForm = binding.title
        val titleFormField = titleForm.field

        val descriptionForm = binding.description
        val descriptionFormField = descriptionForm.field

        initTextField(titleForm, "Remainder Title", "Grocery")
        initTextField(descriptionForm, "Remainder Description", "Don't Forget to buy milk")
        initTextField(binding.location, "Remainder Location", "Bangkok")
        initTextField(startDateBinding, "Start Date", "2/12/2022")
        initTextField(endDateBinding, "End Date", "4/12/2022")
        initTextField(binding.alertField, "Alert notification time", "12.00")
        initStartDateField()
        initEndDateField()
        initLocationField()
        initAlertField()

        binding.switchAlert.setOnClickListener {
            viewModel.toggleAlertField()
        }
        viewModel.displayAlertField.observe(viewLifecycleOwner) {
            if (it) {
                binding.alertField.root.visibility = View.VISIBLE
            } else {
                binding.alertField.root.visibility = View.GONE
            }
        }

        titleFormField.setText(viewModel.titleLiveData.value)
        titleFormField.doOnTextChanged { title, _, _, _ ->
            viewModel.setTitle(title.toString())
            val error = viewModel.checkTitleValue()
            if (error != null) {
                titleFormField.error = error
                return@doOnTextChanged
            }
            titleFormField.error = null
        }

        descriptionFormField.setText(viewModel.descriptionLiveData.value)
        descriptionFormField.doOnTextChanged { description, _, _, _ ->
            viewModel.setDescription(description.toString())
            val error = viewModel.checkDescriptionValue()
            if (error != null) {
                descriptionFormField.error = error
                return@doOnTextChanged
            }
            descriptionFormField.error = null
        }

        binding.cancel.setOnClickListener {
            this.findNavController().navigate(FragmentAddReminderDirections.addReminderToMain())
        }

        binding.save.setOnClickListener {
            val titleError = viewModel.checkTitleValue()
            val descriptionError = viewModel.checkDescriptionValue()
            if (titleError != null) {
                titleFormField.error = titleError
                Toast.makeText(this.context, "Title field are empty please add", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (descriptionError != null) {
                descriptionFormField.error = descriptionError
                Toast.makeText(
                    this.context,
                    "Description field are empty please add",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@setOnClickListener
            }

            viewModel.onSave()
            Toast.makeText(this.context, "Successfully save new reminder", Toast.LENGTH_SHORT)
                .show()
            this.findNavController().navigate(FragmentAddReminderDirections.addReminderToMain())
        }

        return binding.root
    }

    private fun initLocationField() {
        val locationField = binding.location.field
        val locationData = arguments?.get("map_select")
        if (locationData != null) {
            val selectedLocation = locationData as SelectedLocation
            viewModel.setLocation(selectedLocation)
            locationField.setText(selectedLocation.address)
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

    private fun initAlertField() {
        val fieldBinding = binding.alertField.field
        disableFieldSettings(fieldBinding)

        viewModel.alertTimeLiveDate.observe(viewLifecycleOwner) {
            binding.alertField.field.setText("${it.hour}:${it.minute}")
        }

        fieldBinding.setOnClickListener {
            val alertTime = viewModel.alertTimeLiveDate.value
            timePicker(alertTime?.hour ?: 0, alertTime?.minute ?: 0)
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

    private fun timePicker(setHour: Int, setMinute: Int) {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            viewModel.setAlertTime(hour, minute)
        }
        TimePickerDialog(requireContext(), timeSetListener, setHour, setMinute, true).show()
    }
}