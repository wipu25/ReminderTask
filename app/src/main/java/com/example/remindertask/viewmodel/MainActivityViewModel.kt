package com.example.remindertask.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.remindertask.models.repo.SharedPrefRepository

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val _sharedPreferences =
        getApplication<Application>().getSharedPreferences("intro", Context.MODE_PRIVATE)
    private val _sharedPrefRepository = SharedPrefRepository(_sharedPreferences)

    fun getIsIntroSeen(): Boolean {
        return _sharedPrefRepository.getBoolean("isIntroSeen", false)
    }
}