package com.example.remindertask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.remindertask.models.data.AddReminderForm
import java.time.LocalDateTime

class AddReminderViewModel : ViewModel() {
    private val _startDateLiveDate: MutableLiveData<LocalDateTime> =
        MutableLiveData(LocalDateTime.now())
    private val _endDateLiveDate: MutableLiveData<LocalDateTime> =
        MutableLiveData(LocalDateTime.now())

    val startDateLiveData: LiveData<LocalDateTime>
        get() = _startDateLiveDate

    val endDateLiveData: LiveData<LocalDateTime>
        get() = _endDateLiveDate

    fun setStartDate(dateTime: LocalDateTime) {
        _startDateLiveDate.value = dateTime
        if (dateTime > _endDateLiveDate.value) {
            setEndDate(dateTime)
        }
    }

    fun setEndDate(dateTime: LocalDateTime) {
        _endDateLiveDate.value = dateTime
    }

    fun onSave(title: String,description: String,location :String) {

    }
}