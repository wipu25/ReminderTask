package com.example.remindertask.data.source.repo

import com.example.remindertask.data.models.data.ReminderForm
import com.example.remindertask.data.source.database.AddReminderDao

class DatabaseRepository(private val addReminderDao: AddReminderDao) {
    fun insetReminder(addReminderForm: ReminderForm): List<Long> =
        addReminderDao.insert(addReminderForm)

    fun deleteReminder(uid: Int) = addReminderDao.delete(uid)

    fun getReminder(uid: Int) = addReminderDao.getByPrimaryKey(uid)

    fun getAll() = addReminderDao.getAll()
}