package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable

@Serializable
data class Message(val id: Int, val text: String, val author: String)

@Serializable
data class CreateMessageRequest(val text: String, val author: String)

val messages = mutableListOf(
    Message(1, "Hello, World!", "System"),
    Message(2, "Welcome to Ktor", "System")
)
var nextId = 3

fun Application.module() {
    install(ContentNegotiation) { json() }

    routing {
        get("/") {
            call.respondText("Hello, Ktor!")
        }

        get("/health") {
            call.respond(mapOf("status" to "UP"))
        }

        route("/api/messages") {
            get {
                call.respond(messages)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                if (id == null) {
                    call.respond(
                        io.ktor.http.HttpStatusCode.BadRequest,
                        mapOf("error" to "Invalid ID")
                    )
                    return@get
                }

                val message = messages.find { it.id == id }

                if (message == null) {
                    call.respond(
                        io.ktor.http.HttpStatusCode.NotFound,
                        mapOf("error" to "Message not found")
                    )
                } else {
                    call.respond(message)
                }
            }

            post {
                val request = call.receive<CreateMessageRequest>()
                val message = Message(nextId++, request.text, request.author)
                messages.add(message)

                call.respond(
                    io.ktor.http.HttpStatusCode.Created,
                    message
                )
            }
        }
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}