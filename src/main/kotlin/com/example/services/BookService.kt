package com.example.services

import com.example.models.*
import com.example.repositories.BookRepository

class BookService(private val repository: BookRepository) {

    fun getAllBooks(): ServiceResult<List<Book>> {
        return ServiceResult.Success(repository.findAll())
    }

    fun getBook(id: Int): ServiceResult<Book> {
        val book = repository.findById(id)
        return if (book != null) {
            ServiceResult.Success(book)
        } else {
            ServiceResult.NotFound("Book with id $id not found")
        }
    }

    fun createBook(request: CreateBookRequest): ServiceResult<Book> {
        val errors = validateBook(request)
        if (errors.isNotEmpty()) {
            return ServiceResult.ValidationFailure(errors)
        }

        if (repository.findByIsbn(request.isbn) != null) {
            return ServiceResult.ValidationFailure(
                listOf(ValidationError("isbn", "A book with this ISBN already exists"))
            )
        }

        return ServiceResult.Success(repository.create(request))
    }

    fun updateBook(id: Int, request: CreateBookRequest): ServiceResult<Book> {
        val errors = validateBook(request)
        if (errors.isNotEmpty()) {
            return ServiceResult.ValidationFailure(errors)
        }

        val updated = repository.update(id, request)
        return if (updated != null) {
            ServiceResult.Success(updated)
        } else {
            ServiceResult.NotFound("Book with id $id not found")
        }
    }

    fun deleteBook(id: Int): ServiceResult<Unit> {
        return if (repository.delete(id)) {
            ServiceResult.Success(Unit)
        } else {
            ServiceResult.NotFound("Book with id $id not found")
        }
    }

    fun searchByAuthor(author: String): ServiceResult<List<Book>> {
        return ServiceResult.Success(repository.findByAuthor(author))
    }

    private fun validateBook(request: CreateBookRequest): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()

        if (request.title.isBlank()) {
            errors.add(ValidationError("title", "Title cannot be blank"))
        }
        if (request.title.length > 200) {
            errors.add(ValidationError("title", "Title cannot exceed 200 characters"))
        }
        if (request.author.isBlank()) {
            errors.add(ValidationError("author", "Author cannot be blank"))
        }
        if (request.isbn.isBlank()) {
            errors.add(ValidationError("isbn", "ISBN cannot be blank"))
        }
        if (request.year < 1000 || request.year > 2100) {
            errors.add(ValidationError("year", "Year must be between 1000 and 2100"))
        }

        return errors
    }
}