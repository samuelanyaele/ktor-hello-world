
package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class TaskResponse(
    val id: Int,
    val title: String,
    val description: String,
    val completed: Boolean,
    val priority: Int,
    val createdAt: String
)
