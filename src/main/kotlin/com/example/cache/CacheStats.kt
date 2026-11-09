
package com.example.cache

import kotlinx.serialization.Serializable

@Serializable
data class CacheStats(
    val hitCount: Long,
    val missCount: Long,
    val hitRate: Double,
    val size: Long
)
