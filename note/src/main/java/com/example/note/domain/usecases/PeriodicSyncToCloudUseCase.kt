package com.example.note.domain.usecases

import com.example.note.NoteConfig.SYNC_TO_CLOUD_INTERVAL
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import javax.inject.Inject

class PeriodicSyncToCloudUseCase @Inject constructor(
    private val syncToCloudUseCase: SyncToCloudUseCase,
) {

    suspend operator fun invoke(interval: Long = SYNC_TO_CLOUD_INTERVAL) = coroutineScope {
        while (isActive) { // Ensures this stops when the coroutine scope is cancelled
            syncToCloudUseCase.invoke()
            delay(interval) // Waits for 1 minute
        }
    }
}