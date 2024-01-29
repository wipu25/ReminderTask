package com.example.remindertask.presentation.screens.main

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.remindertask.presentation.BaseApplication
import com.example.remindertask.data.models.data.ReminderForm
import com.example.remindertask.data.source.repo.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val reminderRepository: DatabaseRepository) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return HomeViewModel(
                    (application as BaseApplication).reminderRepository
                ) as T
            }
        }
    }

    private val _allReminder: MutableLiveData<List<ReminderForm>> = MutableLiveData(listOf())
    private var _removeIndex: Int? = null
    val allReminder: LiveData<List<ReminderForm>>
        get() = _allReminder
    val removeIndex: Int?
        get() = _removeIndex

    fun getAllReminder() {
        _removeIndex = null
        viewModelScope.launch(Dispatchers.IO) {
            _allReminder.postValue(reminderRepository.getAll())
        }
    }

    fun removeItem(uid: Int, position: Int) {
        _removeIndex = position
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.deleteReminder(uid)
            _allReminder.postValue(reminderRepository.getAll())
        }
    }
}