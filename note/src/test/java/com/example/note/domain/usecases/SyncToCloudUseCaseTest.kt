package com.example.note.domain.usecases

import com.example.note.domain.repositories.CloudRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SyncToCloudUseCaseTest {

    private lateinit var syncToCloudUseCase: SyncToCloudUseCase
    private val cloudRepository: CloudRepository = mockk()

    @Before
    fun setUp() {
        syncToCloudUseCase = SyncToCloudUseCase(cloudRepository)
    }

    @Test
    fun `test syncToCloud is called when invoke is executed`() = runTest {
        coEvery { cloudRepository.syncToCloud() } returns Unit
        syncToCloudUseCase.invoke()
        coVerify { cloudRepository.syncToCloud() }
    }
}
