
package com.example

import com.example.auth.configureJwt
import com.example.auth.authRoutes
import com.example.cache.TaskCache
import com.example.concurrent.DashboardService
import com.example.concurrent.dashboardRoutes
import com.example.database.DatabaseFactory
import com.example.plugins.configureErrorHandling
import com.example.plugins.configureLogging
import com.example.plugins.healthRoutes
import com.example.repositories.InMemoryBookRepository
import com.example.repositories.PostgresTaskRepository
import com.example.routes.bookRoutes
import com.example.routes.securedTaskRoutes
import com.example.services.BookService
import com.example.services.CachedTaskService

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*

fun main() {
    // Connect to PostgreSQL
    DatabaseFactory.init()

    // Existing Book API
    val bookRepository = InMemoryBookRepository()
    val bookService = BookService(bookRepository)

    // PostgreSQL Task API with caching
    val taskRepository = PostgresTaskRepository()
    val taskCache = TaskCache()
    val cachedTaskService = CachedTaskService(
        taskRepository,
        taskCache
    )

    // Dashboard service
    val dashboardService = DashboardService()

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {

        configureLogging()
        configureErrorHandling()

        install(ContentNegotiation) {
            json()
        }

        configureJwt()

        routing {
            bookRoutes(bookService)
            authRoutes()
            securedTaskRoutes(cachedTaskService)
            dashboardRoutes(dashboardService)
            healthRoutes(cachedTaskService)
        }
    }.start(wait = true)
}
