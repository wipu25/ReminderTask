package com.example.remindertask

import android.app.Application
import com.example.remindertask.models.database.AppDatabase
import com.example.remindertask.models.repo.DatabaseRepository

class MainApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val reminderRepository by lazy { DatabaseRepository(database.addReminderDao()) }
}