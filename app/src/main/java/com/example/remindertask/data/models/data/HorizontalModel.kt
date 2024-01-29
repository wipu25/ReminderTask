package com.example.remindertask.models

import com.example.remindertask.data.models.data.ReminderForm

enum class Type { horizontal, vertical }

abstract class RecyclerItem {
    abstract var type: Type
}

data class HorizontalModel(
    val list: List<String>
) : RecyclerItem() {
    override var type: Type = Type.horizontal
}

data class VerticalItem(
    val reminderForm: ReminderForm
) : RecyclerItem() {
    override var type: Type = Type.vertical
}