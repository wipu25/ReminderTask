package com.example.remindertask.data.source.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.remindertask.data.models.data.ReminderForm

@Dao
interface AddReminderDao {
    @Query("SELECT * FROM ReminderForm")
    fun getAll(): List<ReminderForm>

    @Query("SELECT * FROM ReminderForm WHERE uid = :uid")
    fun getByPrimaryKey(uid: Int): List<ReminderForm>

    @Insert
    fun insert(vararg addReminderForm: ReminderForm): List<Long>

    @Query("DELETE FROM ReminderForm WHERE uid = :uid")
    fun delete(uid: Int)
}