package com.yourname.tracker

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: String = "",
    val taskNumber: Int = 0,
    val priority: String = "Обычный",
    val description: String = "",
    val status: String = "В процессе", // в процессе, выполнена, требует корректировки
    val comments: String = "",
    val assignerName: String = "",
    val assigneeName: String = ""
)