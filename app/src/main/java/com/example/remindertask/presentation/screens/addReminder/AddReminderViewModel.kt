package com.example.remindertask.presentation.screens.addReminder

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.remindertask.data.models.data.AlertTime
import com.example.remindertask.data.models.data.ReminderForm
import com.example.remindertask.data.models.data.SelectedLocation
import com.example.remindertask.data.source.repo.DatabaseRepository
import com.example.remindertask.data.source.repo.NotificationRepository
import com.example.remindertask.data.source.repo.channelID
import com.example.remindertask.data.source.repo.messageExtra
import com.example.remindertask.data.source.repo.titleExtra
import com.example.remindertask.presentation.BaseApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

class AddReminderViewModel(
    private val reminderRepository: DatabaseRepository,
    private val application: Application
) : AndroidViewModel(application) {

    override fun onCleared() {
        Log.d("onCleared", "why tho")
        super.onCleared()
    }

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
                    (application as BaseApplication).reminderRepository, application
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
            _calendar.get(Calendar.HOUR_OF_DAY),
            _calendar.get(Calendar.MINUTE)
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
            val primaryKey = reminderRepository.insetReminder(
                ReminderForm(
                    title = _titleLiveData.value ?: "",
                    description = _descriptionLiveData.value ?: "",
                    location = _locationLiveData.value,
                    startDate = _startDateLiveDate.value,
                    endDate = _endDateLiveDate.value,
                    alertTime = _alertTimeLiveDate.value
                )
            )
            if (_displayAlertField.value == true) {
                createNotification()
                scheduleNotification(primaryKey.first().toInt())
            }
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

    private fun createNotification() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, "chanel", importance)
        channel.description = "Description"
        val notificationManager =
            application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification(primaryKey: Int) {
        val intent = Intent(application.applicationContext, NotificationRepository::class.java)
        val title = _titleLiveData.value
        val message = _descriptionLiveData.value
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            application.applicationContext,
            primaryKey,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = LocalDateTime.of(
            _startDateLiveDate.value?.toLocalDate(),
            LocalTime.of(_alertTimeLiveDate.value?.hour!!, _alertTimeLiveDate.value?.minute!!)
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            pendingIntent
        )
    }
}