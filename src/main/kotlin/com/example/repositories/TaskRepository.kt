package com.example.repositories

import com.example.models.Task

interface TaskRepository {
    fun findAll(): List<Task>
    fun findById(id: Int): Task?
    fun create(title: String, description: String, priority: Int): Task
    fun update(id: Int, title: String, description: String, priority: Int): Task?
    fun toggleCompleted(id: Int): Task?
    fun delete(id: Int): Boolean
    fun findByCompleted(completed: Boolean): List<Task>
    fun findByPriorityGreaterThan(priority: Int): List<Task>
}