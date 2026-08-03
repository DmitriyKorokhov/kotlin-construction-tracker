package com.yourname.tracker

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: String = "",
    val taskNumber: Int = 0,
    val title: String = "",
    val priority: String = "Обычный",
    val description: String = "",
    val dueDate: String = "",
    val status: String = "В процессе",
    val comments: String = "",
    val assignerName: String = "Александр Тарасов",
    val assigneeName: String = "",
    val category: String = "ЭО"
)