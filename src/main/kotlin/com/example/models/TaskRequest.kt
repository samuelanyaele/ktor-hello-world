
package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class TaskRequest(
    val title: String,
    val description: String = "",
    val priority: Int = 3
)
