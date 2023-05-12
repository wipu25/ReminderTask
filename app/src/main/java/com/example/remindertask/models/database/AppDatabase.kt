package com.example.remindertask.models.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.remindertask.models.data.AlertTimeConverter
import com.example.remindertask.models.data.DateTimeConverter
import com.example.remindertask.models.data.ReminderForm
import com.example.remindertask.models.data.SelectedLocationConverter

@Database(entities = [ReminderForm::class], version = 3)
@TypeConverters(
    SelectedLocationConverter::class,
    DateTimeConverter::class,
    AlertTimeConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun addReminderDao(): AddReminderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "addReminder"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
