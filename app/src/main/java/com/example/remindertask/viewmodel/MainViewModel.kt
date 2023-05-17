package com.example.remindertask.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.remindertask.MainApplication
import com.example.remindertask.models.data.ReminderForm
import com.example.remindertask.models.repo.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val reminderRepository: DatabaseRepository) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return MainViewModel(
                    (application as MainApplication).reminderRepository
                ) as T
            }
        }
    }

    private val _allReminder: MutableLiveData<List<ReminderForm>> = MutableLiveData(listOf())
    val allReminder: LiveData<List<ReminderForm>>
        get() = _allReminder

    fun getAllReminder() {
        viewModelScope.launch(Dispatchers.IO) {
            _allReminder.postValue(reminderRepository.getAll())
        }
    }

    fun removeItem(uid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.deleteReminder(uid)
            _allReminder.postValue(reminderRepository.getAll())
        }
    }
}