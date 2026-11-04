package com.example.services

import com.example.models.Task
import com.example.repositories.TaskRepository

class TaskService(private val repository: TaskRepository) {

    fun getAllTasks(): List<Task> = repository.findAll()

    fun getTaskById(id: Int): Task? = repository.findById(id)

    fun createTask(
        title: String,
        description: String,
        priority: Int
    ): Task = repository.create(title, description, priority)

    fun updateTask(
        id: Int,
        title: String,
        description: String,
        priority: Int
    ): Task? = repository.update(id, title, description, priority)

    fun toggleTask(id: Int): Task? = repository.toggleCompleted(id)

    fun deleteTask(id: Int): Boolean = repository.delete(id)

    fun getTasksByCompletion(completed: Boolean): List<Task> =
        repository.findByCompleted(completed)

    fun getTasksByPriority(priority: Int): List<Task> =
        repository.findByPriorityGreaterThan(priority)
}