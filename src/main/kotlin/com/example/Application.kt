package com.example

import com.example.database.DatabaseFactory
import com.example.repositories.InMemoryBookRepository
import com.example.repositories.PostgresTaskRepository
import com.example.routes.bookRoutes
import com.example.routes.taskRoutes
import com.example.services.BookService
import com.example.services.TaskService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import com.example.concurrent.DashboardService
import com.example.concurrent.dashboardRoutes

fun main() {
    // Connect to PostgreSQL
    DatabaseFactory.init()

    // Existing Book API
    val bookRepository = InMemoryBookRepository()
    val bookService = BookService(bookRepository)

    // New PostgreSQL Task API
    val taskRepository = PostgresTaskRepository()
    val taskService = TaskService(taskRepository)
    val dashboardService = DashboardService()

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) {
            json()
        }

        routing {
            bookRoutes(bookService)
            taskRoutes(taskService)
            dashboardRoutes(dashboardService)
        }
    }.start(wait = true)
}