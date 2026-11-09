package com.example.cache

import com.example.models.Task
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import java.util.concurrent.TimeUnit

class TaskCache {
    private val cache: Cache<Int, Task> = Caffeine.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .maximumSize(1000)
        .recordStats()
        .build()

    fun get(id: Int): Task? = cache.getIfPresent(id)

    fun put(id: Int, task: Task) {
        cache.put(id, task)
    }

    fun invalidate(id: Int) {
        cache.invalidate(id)
    }

    fun invalidateAll() {
        cache.invalidateAll()
    }

    fun getStats(): CacheStats = CacheStats(
        hitCount = cache.stats().hitCount(),
        missCount = cache.stats().missCount(),
        hitRate = cache.stats().hitRate(),
        size = cache.estimatedSize()
    )
}