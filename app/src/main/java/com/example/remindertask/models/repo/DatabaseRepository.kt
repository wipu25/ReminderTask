package com.example.remindertask.models.repo

import com.example.remindertask.models.data.ReminderForm
import com.example.remindertask.models.database.AddReminderDao

class DatabaseRepository(private val addReminderDao: AddReminderDao) {
    fun insetReminder(addReminderForm: ReminderForm) = addReminderDao.insert(addReminderForm)

    fun deleteReminder(removeReminderForm: ReminderForm) = addReminderDao.delete(removeReminderForm)

    fun getReminder(uid: Int) = addReminderDao.getByPrimaryKey(uid)

    fun getAll() = addReminderDao.getAll()
}