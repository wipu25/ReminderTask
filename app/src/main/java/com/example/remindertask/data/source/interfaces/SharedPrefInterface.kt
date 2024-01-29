package com.example.remindertask.data.source.interfaces

interface SharedPrefInterface {
    fun saveBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
}