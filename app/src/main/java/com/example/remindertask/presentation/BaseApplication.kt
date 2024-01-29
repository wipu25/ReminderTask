package com.example.remindertask.presentation

import android.app.Application
import com.example.remindertask.data.source.database.AppDatabase
import com.example.remindertask.data.source.repo.DatabaseRepository

class BaseApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val reminderRepository by lazy { DatabaseRepository(database.addReminderDao()) }
}