package com.example.remindertask.models.interfaces

interface SharedPrefInterface {
    fun saveBoolean(key: String,value: Boolean)
    fun getBoolean(key:String, defaultValue:Boolean): Boolean
}