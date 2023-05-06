package com.example.remindertask.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.remindertask.models.data.AddReminderForm
import com.example.remindertask.models.data.SelectedLocation
import java.time.LocalDateTime

class AddReminderViewModel : ViewModel() {
    private val _startDateLiveDate: MutableLiveData<LocalDateTime> =
        MutableLiveData(LocalDateTime.now())
    private val _endDateLiveDate: MutableLiveData<LocalDateTime> =
        MutableLiveData(LocalDateTime.now())
    private val _titleLiveData: MutableLiveData<String> = MutableLiveData("")
    private val _descriptionLiveData: MutableLiveData<String> = MutableLiveData("")
    private val _locationLiveData: MutableLiveData<SelectedLocation> = MutableLiveData()

    val startDateLiveData: LiveData<LocalDateTime>
        get() = _startDateLiveDate

    val endDateLiveData: LiveData<LocalDateTime>
        get() = _endDateLiveDate
    val titleLiveData: LiveData<String>
        get() = _titleLiveData
    val descriptionLiveData: LiveData<String>
        get() = _descriptionLiveData

    fun setStartDate(dateTime: LocalDateTime) {
        _startDateLiveDate.value = dateTime
        if (dateTime > _endDateLiveDate.value) {
            setEndDate(dateTime)
        }
    }

    fun setEndDate(dateTime: LocalDateTime) {
        _endDateLiveDate.value = dateTime
    }

    fun onSave() {
        val a = AddReminderForm(
            _titleLiveData.value!!,
            _descriptionLiveData.value!!,
            _locationLiveData.value!!,
            _startDateLiveDate.value!!,
            _endDateLiveDate.value!!
        )
        Log.d("Data", a.toString())
    }

    fun setTitle(text: String) {
        _titleLiveData.value = text
    }

    fun setDescription(text: String) {
        _descriptionLiveData.value = text
    }

    fun setLocation(location: SelectedLocation) {
        _locationLiveData.value = location
    }
}