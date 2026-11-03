package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val isbn: String,
    val year: Int,
    val available: Boolean = true
)

@Serializable
data class CreateBookRequest(
    val title: String,
    val author: String,
    val isbn: String,
    val year: Int
)

@Serializable
data class UpdateBookRequest(
    val title: String? = null,
    val author: String? = null,
    val isbn: String? = null,
    val year: Int? = null,
    val available: Boolean? = null
)
@Serializable
data class ValidationError(val field: String, val message: String)

sealed class ServiceResult<out T> {
    data class Success<T>(val data: T) : ServiceResult<T>()
    data class ValidationFailure(val errors: List<ValidationError>) : ServiceResult<Nothing>()
    data class NotFound(val message: String) : ServiceResult<Nothing>()
    data class Error(val message: String) : ServiceResult<Nothing>()
}