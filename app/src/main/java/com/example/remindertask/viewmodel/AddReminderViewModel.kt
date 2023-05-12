package com.example.remindertask.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.remindertask.MainApplication
import com.example.remindertask.models.data.AlertTime
import com.example.remindertask.models.data.ReminderForm
import com.example.remindertask.models.data.SelectedLocation
import com.example.remindertask.models.repo.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*

class AddReminderViewModel(private val reminderRepository: DatabaseRepository) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return AddReminderViewModel(
                    (application as MainApplication).reminderRepository
                ) as T
            }
        }
    }

    private val _calendar = Calendar.getInstance()

    private val _startDateLiveDate: MutableLiveData<LocalDateTime> =
        MutableLiveData(LocalDateTime.now())
    private val _endDateLiveDate: MutableLiveData<LocalDateTime> =
        MutableLiveData(LocalDateTime.now())
    private val _titleLiveData: MutableLiveData<String> = MutableLiveData("")
    private val _descriptionLiveData: MutableLiveData<String> = MutableLiveData("")
    private val _locationLiveData: MutableLiveData<SelectedLocation> =
        MutableLiveData(SelectedLocation())
    private val _displayAlertField: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _alertTimeLiveDate: MutableLiveData<AlertTime> = MutableLiveData(
        AlertTime(
            hour = _calendar.get(Calendar.HOUR_OF_DAY),
            minute = _calendar.get(Calendar.MINUTE)
        )
    )

    val startDateLiveData: LiveData<LocalDateTime>
        get() = _startDateLiveDate
    val endDateLiveData: LiveData<LocalDateTime>
        get() = _endDateLiveDate
    val titleLiveData: LiveData<String>
        get() = _titleLiveData
    val descriptionLiveData: LiveData<String>
        get() = _descriptionLiveData
    val displayAlertField: LiveData<Boolean>
        get() = _displayAlertField
    val alertTimeLiveDate: LiveData<AlertTime>
        get() = _alertTimeLiveDate

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
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.insetReminder(ReminderForm(
                title = _titleLiveData.value ?: "",
                description = _descriptionLiveData.value ?: "",
                location = _locationLiveData.value,
                startDate = _startDateLiveDate.value,
                endDate = _endDateLiveDate.value,
                alertTime = _alertTimeLiveDate.value
            ))
        }
    }

    fun checkTitleValue(): String? {
        if (_titleLiveData.value.isNullOrEmpty()) {
            return "Please add Title"
        }
        if (_titleLiveData.value!![0] == ' ') {
            return "title should not start with space"
        }
        return null
    }

    fun checkDescriptionValue(): String? {
        if (_descriptionLiveData.value.isNullOrEmpty()) {
            return "Please add Description"
        }
        if (_descriptionLiveData.value!![0] == ' ') {
            return "Description should not start with space"
        }
        return null
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

    fun toggleAlertField() {
        _displayAlertField.value = !_displayAlertField.value!!
    }

    fun setAlertTime(hour: Int?, minute: Int?) {
        _alertTimeLiveDate.value = AlertTime(hour, minute)
    }
}