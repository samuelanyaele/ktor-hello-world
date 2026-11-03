package com.example

import com.example.repositories.InMemoryBookRepository
import com.example.routes.bookRoutes
import com.example.services.BookService
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {

    @Test
    fun testGetAllBooks() = testApplication {
        application {
            install(ContentNegotiation) { json() }
            val repository = InMemoryBookRepository()
            val service = BookService(repository)
            routing {
                bookRoutes(service)
            }
        }

        val response = client.get("/api/books")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("The Pragmatic Programmer"))
    }

    @Test
    fun testGetBookById() = testApplication {
        application {
            install(ContentNegotiation) { json() }
            val repository = InMemoryBookRepository()
            val service = BookService(repository)
            routing {
                bookRoutes(service)
            }
        }

        val response = client.get("/api/books/1")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("The Pragmatic Programmer"))
    }
}