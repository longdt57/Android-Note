package com.example.note.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.note.domain.usecases.SyncToCloudUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class SyncToCloudWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    @Assisted private val syncToCloudUseCase: SyncToCloudUseCase
) : CoroutineWorker(
    context,
    params
) {

    override suspend fun doWork(): Result {
        return try {
            Timber.d("Syncing notes with server...")
            syncToCloudUseCase.invoke()
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "Error syncing notes")
            Result.retry()
        }
    }
}