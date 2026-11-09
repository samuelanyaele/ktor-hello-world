
package com.example.services

import com.example.cache.CacheStats
import com.example.cache.TaskCache
import com.example.models.Task
import com.example.repositories.TaskRepository

class CachedTaskService(
    private val repository: TaskRepository,
    private val cache: TaskCache
) {

    fun getAllTasks(): List<Task> = repository.findAll()

    fun getTaskById(id: Int): Task? {
        val cached = cache.get(id)

        if (cached != null) {
            return cached
        }

        val task = repository.findById(id)

        if (task != null) {
            cache.put(id, task)
        }

        return task
    }

    fun createTask(
        title: String,
        description: String,
        priority: Int
    ): Task {
        val task = repository.create(title, description, priority)
        cache.put(task.id, task)
        return task
    }

    fun updateTask(
        id: Int,
        title: String,
        description: String,
        priority: Int
    ): Task? {
        val updated = repository.update(id, title, description, priority)

        if (updated != null) {
            cache.put(id, updated)
        }

        return updated
    }

    fun toggleTask(id: Int): Task? {
        val updated = repository.toggleCompleted(id)

        if (updated != null) {
            cache.put(id, updated)
        }

        return updated
    }

    fun deleteTask(id: Int): Boolean {
        val deleted = repository.delete(id)

        if (deleted) {
            cache.invalidate(id)
        }

        return deleted
    }

    fun getTasksByCompletion(completed: Boolean): List<Task> =
        repository.findByCompleted(completed)

    fun getTasksByPriority(priority: Int): List<Task> =
        repository.findByPriorityGreaterThan(priority)

    fun getCacheStats(): CacheStats = cache.getStats()
}
