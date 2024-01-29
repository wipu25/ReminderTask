package com.example.remindertask.presentation.screens.addReminder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.remindertask.R
import com.example.remindertask.data.models.data.SelectedLocation
import com.example.remindertask.databinding.FragmentAddReminderBinding
import com.example.remindertask.databinding.TextFieldBinding
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.min

class AddRemainderFragment : Fragment() {
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
        initStartDateField()
        initEndDateField()
        initLocationField()

        binding.switchAlert.setOnClickListener {
            viewModel.toggleAlertField()
        }
        viewModel.displayAlertField.observe(viewLifecycleOwner) {
            if (it) {
                initTextField(binding.alertField, "Alert notification time", "12.00")
                initAlertField()
                binding.alertField.root.visibility = View.VISIBLE
            } else {
                binding.alertField.root.visibility = View.GONE
            }
        }

        titleFormField.setText(viewModel.titleLiveData.value)
        titleFormField.doOnTextChanged { title, _, _, _ ->
            titleFormField.error = viewModel.setTitle(title.toString())
        }

        descriptionFormField.setText(viewModel.descriptionLiveData.value)
        binding.description.field.doOnTextChanged { description, _, _, _ ->
            descriptionFormField.error = viewModel.setDescription(description.toString())
        }

        binding.cancel.setOnClickListener {
            this.findNavController().navigate(AddRemainderFragmentDirections.addReminderToMain())
        }

        binding.save.setOnClickListener {
            onSavePressed()
        }

        return binding.root
    }

    private fun onSavePressed() {
        val titleFormField = binding.title.field
        val descriptionFormField = binding.title.field
        val titleError = viewModel.checkTitleValue()
        val descriptionError = viewModel.checkDescriptionValue()
        if(titleError != null && descriptionError != null) {
            titleFormField.error = titleError
            descriptionFormField.error = descriptionError
            Toast.makeText(this.context, "Please fill both empty field", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (titleError != null) {
            titleFormField.error = titleError
            Toast.makeText(this.context, "Title field are empty please add", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (descriptionError != null) {
            descriptionFormField.error = descriptionError
            Toast.makeText(
                this.context,
                "Description field are empty please add",
                Toast.LENGTH_SHORT
            )
                .show()
            return
        }

        viewModel.onSave()
        Toast.makeText(this.context, "Successfully save new reminder", Toast.LENGTH_SHORT)
            .show()
        this.findNavController().navigate(AddRemainderFragmentDirections.addReminderToMain())
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
            this.findNavController().navigate(AddRemainderFragmentDirections.addReminderToMap())
        }
    }

    private fun initTextField(binding: TextFieldBinding, title: String, hint: String) {
        binding.field.id = (binding.field.parent as ConstraintLayout).id + binding.field.id
        binding.title.text = title
        binding.field.hint = hint
    }

    private fun initStartDateField() {
        val fieldBinding = startDateBinding.field
        disableFieldSettings(fieldBinding)

        viewModel.startDateLiveData.observe(this.viewLifecycleOwner) { startDate ->
            fieldBinding.setText(startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        }

        fieldBinding.setOnClickListener {
            val startDate = viewModel.startDateLiveData.value!!
            displayDatePicker(startDate, LocalDateTime.now(), viewModel::setStartDate)
        }
    }

    private fun initEndDateField() {
        val fieldBinding = endDateBinding.field
        disableFieldSettings(fieldBinding)

        viewModel.endDateLiveData.observe(viewLifecycleOwner) { endDate ->
            fieldBinding.setText(endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
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
            var minute = it.minute.toString()
            if(it.minute!! < 10) {
                minute = "0${minute}"
            }
            binding.alertField.field.setText("${it.hour}:$minute")
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