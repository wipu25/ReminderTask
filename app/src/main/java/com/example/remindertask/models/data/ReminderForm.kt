package com.example.remindertask.models.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Entity
data class ReminderForm(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "location") var location: SelectedLocation,
    @ColumnInfo(name = "start_date") var startDate: LocalDateTime? = null,
    @ColumnInfo(name = "end_date") var endDate: LocalDateTime? = null
)

class DateTimeConverter {
    @TypeConverter
    fun StringToDateTime(epochMilli: Long?): LocalDateTime? {
        return if (epochMilli != null)
            LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneOffset.UTC)
        else
            null
    }

    @TypeConverter
    fun DateTimeToString(localDateTime: LocalDateTime?): Long? {
        return localDateTime?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }
}
