package com.example.remindertask.models.repo

import android.content.SharedPreferences
import com.example.remindertask.models.interfaces.SharedPrefInterface

class SharedPrefRepository(private val sharedPreferences: SharedPreferences) : SharedPrefInterface {

    override fun saveBoolean(key: String, value: Boolean) {
        val edit = sharedPreferences.edit()
        edit.putBoolean(key, value)
        edit.apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }
}