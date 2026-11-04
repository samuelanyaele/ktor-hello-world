package com.example.repositories

import com.example.models.Task

class InMemoryTaskRepository : TaskRepository {

    private val tasks = mutableListOf<Task>()
    private var nextId = 1

    override fun findAll(): List<Task> = tasks.toList()

    override fun findById(id: Int): Task? =
        tasks.find { it.id == id }

    override fun create(
        title: String,
        description: String,
        priority: Int
    ): Task {
        val task = Task(
            id = nextId++,
            title = title,
            description = description,
            priority = priority
        )
        tasks.add(task)
        return task
    }

    override fun update(
        id: Int,
        title: String,
        description: String,
        priority: Int
    ): Task? {
        val index = tasks.indexOfFirst { it.id == id }
        if (index == -1) return null

        val updatedTask = tasks[index].copy(
            title = title,
            description = description,
            priority = priority
        )

        tasks[index] = updatedTask
        return updatedTask
    }

    override fun toggleCompleted(id: Int): Task? {
        val index = tasks.indexOfFirst { it.id == id }
        if (index == -1) return null

        val updatedTask = tasks[index].copy(
            completed = !tasks[index].completed
        )

        tasks[index] = updatedTask
        return updatedTask
    }

    override fun delete(id: Int): Boolean =
        tasks.removeIf { it.id == id }

    override fun findByCompleted(completed: Boolean): List<Task> =
        tasks.filter { it.completed == completed }

    override fun findByPriorityGreaterThan(priority: Int): List<Task> =
        tasks.filter { it.priority > priority }
}