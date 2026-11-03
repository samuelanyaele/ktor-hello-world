package com.example.routes

import com.example.models.*
import com.example.services.BookService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class ValidationErrorResponse(val errors: List<ValidationError>)

@Serializable
data class ErrorResponse(val message: String)

fun Route.bookRoutes(bookService: BookService) {
    route("/api/books") {
        get {
            when (val result = bookService.getAllBooks()) {
                is ServiceResult.Success -> call.respond(result.data)
                is ServiceResult.ValidationFailure -> call.respond(
                    HttpStatusCode.BadRequest,
                    ValidationErrorResponse(result.errors)
                )
                is ServiceResult.NotFound -> call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse(result.message)
                )
                is ServiceResult.Error -> call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(result.message)
                )
            }
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("Invalid ID")
                )
                return@get
            }

            when (val result = bookService.getBook(id)) {
                is ServiceResult.Success -> call.respond(result.data)
                is ServiceResult.ValidationFailure -> call.respond(
                    HttpStatusCode.BadRequest,
                    ValidationErrorResponse(result.errors)
                )
                is ServiceResult.NotFound -> call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse(result.message)
                )
                is ServiceResult.Error -> call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(result.message)
                )
            }
        }

        post {
            val request = call.receive<CreateBookRequest>()
            when (val result = bookService.createBook(request)) {
                is ServiceResult.Success -> call.respond(
                    HttpStatusCode.Created,
                    result.data
                )
                is ServiceResult.ValidationFailure -> call.respond(
                    HttpStatusCode.BadRequest,
                    ValidationErrorResponse(result.errors)
                )
                is ServiceResult.NotFound -> call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse(result.message)
                )
                is ServiceResult.Error -> call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(result.message)
                )
            }
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("Invalid ID")
                )
                return@put
            }

            val request = call.receive<CreateBookRequest>()
            when (val result = bookService.updateBook(id, request)) {
                is ServiceResult.Success -> call.respond(result.data)
                is ServiceResult.ValidationFailure -> call.respond(
                    HttpStatusCode.BadRequest,
                    ValidationErrorResponse(result.errors)
                )
                is ServiceResult.NotFound -> call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse(result.message)
                )
                is ServiceResult.Error -> call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(result.message)
                )
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("Invalid ID")
                )
                return@delete
            }

            when (val result = bookService.deleteBook(id)) {
                is ServiceResult.Success -> call.respond(HttpStatusCode.NoContent)
                is ServiceResult.ValidationFailure -> call.respond(
                    HttpStatusCode.BadRequest,
                    ValidationErrorResponse(result.errors)
                )
                is ServiceResult.NotFound -> call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse(result.message)
                )
                is ServiceResult.Error -> call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(result.message)
                )
            }
        }

        get("/search") {
            val author = call.request.queryParameters["author"]
            if (author.isNullOrBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("Author query parameter is required")
                )
                return@get
            }

            when (val result = bookService.searchByAuthor(author)) {
                is ServiceResult.Success -> call.respond(result.data)
                is ServiceResult.ValidationFailure -> call.respond(
                    HttpStatusCode.BadRequest,
                    ValidationErrorResponse(result.errors)
                )
                is ServiceResult.NotFound -> call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse(result.message)
                )
                is ServiceResult.Error -> call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(result.message)
                )
            }
        }
    }
}