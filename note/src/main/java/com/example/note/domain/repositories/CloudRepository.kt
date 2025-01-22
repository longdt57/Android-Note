package com.example.note.domain.repositories

interface CloudRepository {
    suspend fun isSyncedFromCloud(): Boolean
    suspend fun syncFromCloud(): Boolean
    suspend fun syncToCloud()
}