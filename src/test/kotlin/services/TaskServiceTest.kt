package com.example.services

import com.example.models.Task
import com.example.repositories.TaskRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.*

class TaskServiceTest {

    private lateinit var repository: TaskRepository
    private lateinit var service: TaskService

    @BeforeEach
    fun setup() {
        repository = mockk()
        service = TaskService(repository)
    }

    @Test
    fun `getAllTasks should return all tasks`() {
        val tasks = listOf(
            Task(1, "Learn Kotlin", "Practice", priority = 3),
            Task(2, "Learn Ktor", "Build API", priority = 4)
        )

        every { repository.findAll() } returns tasks

        val result = service.getAllTasks()

        assertEquals(tasks, result)
        verify(exactly = 1) { repository.findAll() }
    }

    @Test
    fun `getTaskById should return existing task`() {
        val task = Task(1, "Learn Kotlin", "Practice", priority = 3)

        every { repository.findById(1) } returns task

        assertEquals(task, service.getTaskById(1))
    }

    @Test
    fun `getTaskById should return null when task does not exist`() {
        every { repository.findById(999) } returns null

        assertNull(service.getTaskById(999))
    }

    @Test
    fun `createTask should create and return a task`() {
        val task = Task(1, "Learn Testing", "Write tests", priority = 3)

        every {
            repository.create("Learn Testing", "Write tests", 3)
        } returns task

        val result = service.createTask("Learn Testing", "Write tests", 3)

        assertEquals(task, result)
    }

    @Test
    fun `deleteTask should return true when task is deleted`() {
        every { repository.delete(1) } returns true

        assertTrue(service.deleteTask(1))
    }

    @Test
    fun `deleteTask should return false when task is not found`() {
        every { repository.delete(999) } returns false

        assertFalse(service.deleteTask(999))
    }
}