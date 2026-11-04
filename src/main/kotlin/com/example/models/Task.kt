package com.example.models

import java.time.LocalDateTime

data class Task(
    val id: Int,
    val title: String,
    val description: String = "",
    val completed: Boolean = false,
    val priority: Int = 3,
    val createdAt: LocalDateTime = LocalDateTime.now()
)