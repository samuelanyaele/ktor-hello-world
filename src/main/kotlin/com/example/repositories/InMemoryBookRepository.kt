package com.example.repositories

import com.example.models.Book
import com.example.models.CreateBookRequest

class InMemoryBookRepository : BookRepository {
    private val books = mutableListOf(
        Book(1, "The Pragmatic Programmer", "Hunt & Thomas", "978-0135957059", 2019),
        Book(2, "Clean Code", "Robert C. Martin", "978-0132350884", 2008)
    )
    private var nextId = 3

    override fun findAll(): List<Book> = books.toList()

    override fun findById(id: Int): Book? = books.find { it.id == id }

    override fun findByIsbn(isbn: String): Book? = books.find { it.isbn == isbn }

    override fun findByAuthor(author: String): List<Book> =
        books.filter { it.author.contains(author, ignoreCase = true) }

    override fun create(request: CreateBookRequest): Book {
        val book = Book(
            id = nextId++,
            title = request.title,
            author = request.author,
            isbn = request.isbn,
            year = request.year
        )
        books.add(book)
        return book
    }

    override fun update(id: Int, request: CreateBookRequest): Book? {
        val index = books.indexOfFirst { it.id == id }
        if (index == -1) return null

        val updated = books[index].copy(
            title = request.title,
            author = request.author,
            isbn = request.isbn,
            year = request.year
        )
        books[index] = updated
        return updated
    }

    override fun delete(id: Int): Boolean = books.removeIf { it.id == id }
}