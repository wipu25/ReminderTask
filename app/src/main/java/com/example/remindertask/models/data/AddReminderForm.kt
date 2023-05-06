package com.example.remindertask.models.data

import java.time.LocalDateTime

data class AddReminderForm(
    var title: String = "",
    var description: String = "",
    var location: SelectedLocation,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null
)
