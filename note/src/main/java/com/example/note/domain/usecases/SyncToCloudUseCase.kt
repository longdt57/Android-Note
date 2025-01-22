package com.example.note.domain.usecases

import com.example.note.domain.repositories.CloudRepository
import javax.inject.Inject

class SyncToCloudUseCase @Inject constructor(
    private val cloudRepository: CloudRepository
) {

    suspend operator fun invoke() {
        cloudRepository.syncToCloud()
    }
}