package com.example.note.domain.usecases

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PeriodicSyncToCloudUseCaseTest {

    private val syncToCloudUseCase: SyncToCloudUseCase = mockk(relaxed = true)
    private val periodicSyncToCloudUseCase = PeriodicSyncToCloudUseCase(syncToCloudUseCase)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `invoke should call syncToCloudUseCase periodically`() = runTest {

        // Launch the periodic sync in a separate coroutine
        val job = launch {
            periodicSyncToCloudUseCase.invoke(10)
        }

        // Advance time to simulate the periodic interval
        advanceTimeBy(22) // 100 ms

        // Cancel the coroutine to stop the periodic sync
        job.cancel()

        // Verify that syncToCloudUseCase.invoke() was called 3 times
        coVerify(exactly = 3) { syncToCloudUseCase.invoke() }
    }
}
