package com.example

import com.example.repositories.InMemoryBookRepository
import com.example.routes.bookRoutes
import com.example.services.BookService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*

fun main() {
    val repository = InMemoryBookRepository()
    val service = BookService(repository)

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) { json() }
        routing {
            bookRoutes(service)
        }
    }.start(wait = true)
}