package com.example.remindertask.models.database

import androidx.room.*
import com.example.remindertask.models.data.ReminderForm

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