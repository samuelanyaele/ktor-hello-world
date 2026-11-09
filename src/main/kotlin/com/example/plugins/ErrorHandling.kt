package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: String,
    val timestamp: Long = System.currentTimeMillis()
)

fun Application.configureErrorHandling() {
    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(cause.message ?: "Invalid argument")
            )
        }

        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled exception", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse("Internal server error")
            )
        }

        status(HttpStatusCode.NotFound) { call, _ ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse("Resource not found")
            )
        }
    }
}