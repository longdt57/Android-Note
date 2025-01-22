package com.example.note.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.note.domain.usecases.SyncFromCloudUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class SyncFromCloudWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val syncFromCloudUseCase: SyncFromCloudUseCase
) : CoroutineWorker(
    context,
    params
) {

    override suspend fun doWork(): Result {
        return try {
            Timber.d("Syncing notes with server...")
            syncFromCloudUseCase.invoke()
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "Error syncing notes")
            Result.retry()
        }
    }
}