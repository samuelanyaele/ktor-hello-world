package com.example.routes

import com.example.models.Task
import com.example.services.TaskService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.auth.JwtConfig

private fun Task.toSecuredResponse() = TaskResponse(
    id = id,
    title = title,
    description = description,
    completed = completed,
    priority = priority,
    createdAt = createdAt.toString()
)

fun Route.securedTaskRoutes(service: TaskService) {
    authenticate("auth-jwt") {
        route("/api/tasks") {

            get {
                call.respond(
                    service.getAllTasks().map { it.toSecuredResponse() }
                )
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
                    call.respond(task.toSecuredResponse())
                }
            }

            post {
                val request = call.receive<TaskRequest>()

                val task = service.createTask(
                    request.title,
                    request.description,
                    request.priority
                )

                call.respond(
                    HttpStatusCode.Created,
                    task.toSecuredResponse()
                )
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
                    call.respond(task.toSecuredResponse())
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
                    call.respond(task.toSecuredResponse())
                }
            }

            delete("/{id}") {
                val principal = call.principal<JWTPrincipal>()
                val role = principal?.payload
                    ?.getClaim("role")?.asString()

                if (role != "ADMIN") {
                    call.respond(
                        HttpStatusCode.Forbidden,
                        mapOf("error" to "Admin access required")
                    )
                    return@delete
                }

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
}