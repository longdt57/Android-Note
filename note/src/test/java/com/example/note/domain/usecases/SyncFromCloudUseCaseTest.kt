package com.example.note.domain.usecases

import com.example.note.domain.repositories.CloudRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SyncFromCloudUseCaseTest {

    private lateinit var syncFromCloudUseCase: SyncFromCloudUseCase
    private val cloudRepository: CloudRepository = mockk()

    @Before
    fun setUp() {
        syncFromCloudUseCase = SyncFromCloudUseCase(cloudRepository)
    }

    @Test
    fun `test syncFromCloud is called when isSyncedFromCloud returns false`() = runTest {
        // Arrange
        coEvery { cloudRepository.isSyncedFromCloud() } returns false
        coEvery { cloudRepository.syncFromCloud() } returns true

        // Act
        syncFromCloudUseCase.invoke()

        // Assert
        coVerify { cloudRepository.syncFromCloud() }
    }

    @Test
    fun `test syncFromCloud is not called when isSyncedFromCloud returns true`() = runTest {
        // Arrange
        coEvery { cloudRepository.isSyncedFromCloud() } returns true

        // Act
        syncFromCloudUseCase.invoke()

        // Assert
        coVerify(exactly = 0) { cloudRepository.syncFromCloud() }
    }
}
