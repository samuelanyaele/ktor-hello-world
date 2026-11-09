
package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class HealthResponse(
    val status: String,
    val database: String,
    val timestamp: Long
)

fun Route.healthRoutes(
    cachedTaskService: com.example.services.CachedTaskService
) {
    get("/health") {
        val dbHealthy = try {
            transaction {
                exec("SELECT 1")
            }
            true
        } catch (e: Exception) {
            false
        }

        val status = if (dbHealthy) {
            HttpStatusCode.OK
        } else {
            HttpStatusCode.ServiceUnavailable
        }

        call.respond(
            status,
            HealthResponse(
                status = if (dbHealthy) "UP" else "DOWN",
                database = if (dbHealthy) "UP" else "DOWN",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    get("/metrics/cache") {
        call.respond(cachedTaskService.getCacheStats())
    }
}
