package com.example.routes

import com.example.repositories.InMemoryTaskRepository
import com.example.services.TaskService
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.server.routing.*

class TaskRoutesTest {

    private fun ApplicationTestBuilder.setupApplication() {
        application {
            install(ContentNegotiation) {
                json()
            }

            val repository = InMemoryTaskRepository()
            val service = TaskService(repository)

            routing {
                taskRoutes(service)
            }
        }
    }

    @Test
    fun `GET all tasks should return empty list initially`() = testApplication {
        setupApplication()

        val response = client.get("/api/tasks")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("[]", response.bodyAsText())
    }

    @Test
    fun `POST task should create task`() = testApplication {
        setupApplication()

        val response = client.post("/api/tasks") {
            contentType(ContentType.Application.Json)
            setBody(
                """{"title":"Test Task","description":"Test Description","priority":3}"""
            )
        }

        assertEquals(HttpStatusCode.Created, response.status)
        assertTrue(response.bodyAsText().contains("Test Task"))
    }

    @Test
    fun `GET task by id should return 404 when not found`() = testApplication {
        setupApplication()

        val response = client.get("/api/tasks/999")

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `full CRUD lifecycle should work`() = testApplication {
        setupApplication()

        // Create
        val createResponse = client.post("/api/tasks") {
            contentType(ContentType.Application.Json)
            setBody(
                """{"title":"Lifecycle Task","description":"Test","priority":3}"""
            )
        }

        assertEquals(HttpStatusCode.Created, createResponse.status)

        // Read
        val getResponse = client.get("/api/tasks/1")
        assertEquals(HttpStatusCode.OK, getResponse.status)
        assertTrue(getResponse.bodyAsText().contains("Lifecycle Task"))

        // Update
        val updateResponse = client.put("/api/tasks/1") {
            contentType(ContentType.Application.Json)
            setBody(
                """{"title":"Updated Task","description":"Updated","priority":5}"""
            )
        }

        assertEquals(HttpStatusCode.OK, updateResponse.status)
        assertTrue(updateResponse.bodyAsText().contains("Updated Task"))

        // Delete
        val deleteResponse = client.delete("/api/tasks/1")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        // Verify deletion
        val verifyResponse = client.get("/api/tasks/1")
        assertEquals(HttpStatusCode.NotFound, verifyResponse.status)
    }
}