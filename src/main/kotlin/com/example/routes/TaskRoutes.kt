package com.example.routes

import com.example.models.Task
import com.example.services.TaskService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class TaskRequest(
    val title: String,
    val description: String = "",
    val priority: Int = 3
)

@Serializable
data class TaskResponse(
    val id: Int,
    val title: String,
    val description: String,
    val completed: Boolean,
    val priority: Int,
    val createdAt: String
)

private fun Task.toResponse() = TaskResponse(
    id = id,
    title = title,
    description = description,
    completed = completed,
    priority = priority,
    createdAt = createdAt.toString()
)

fun Route.taskRoutes(service: TaskService) {
    route("/api/tasks") {

        get {
            call.respond(service.getAllTasks().map { it.toResponse() })
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val task = service.getTaskById(id)
            if (task == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(task.toResponse())
            }
        }

        post {
            val request = call.receive<TaskRequest>()
            val task = service.createTask(
                request.title,
                request.description,
                request.priority
            )
            call.respond(HttpStatusCode.Created, task.toResponse())
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }

            val request = call.receive<TaskRequest>()
            val task = service.updateTask(
                id,
                request.title,
                request.description,
                request.priority
            )

            if (task == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(task.toResponse())
            }
        }

        patch("/{id}/toggle") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@patch
            }

            val task = service.toggleTask(id)
            if (task == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(task.toResponse())
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            if (service.deleteTask(id)) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}