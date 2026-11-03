package com.example.repositories

import com.example.models.Book
import com.example.models.CreateBookRequest

interface BookRepository {
    fun findAll(): List<Book>
    fun findById(id: Int): Book?
    fun findByIsbn(isbn: String): Book?
    fun findByAuthor(author: String): List<Book>
    fun create(request: CreateBookRequest): Book
    fun update(id: Int, request: CreateBookRequest): Book?
    fun delete(id: Int): Boolean
}