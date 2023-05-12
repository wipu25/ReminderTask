package com.example.remindertask.models.data

import androidx.room.TypeConverter
import com.google.gson.Gson

data class AlertTime(
    var hour: Int?,
    var minute: Int?
)

class AlertTimeConverter {
    @TypeConverter
    fun StringToAlertTime(string: String?): AlertTime? {
        return Gson().fromJson(string, AlertTime::class.java)
    }

    @TypeConverter
    fun AlertTimeToString(alertTime: AlertTime?): String? {
        return Gson().toJson(alertTime)
    }
}