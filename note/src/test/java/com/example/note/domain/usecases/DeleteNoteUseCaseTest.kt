package com.example.note.domain.usecases

import com.example.note.domain.repositories.NoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteNoteUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase

    @Before
    fun setUp() {
        noteRepository = mockk()
        deleteNoteUseCase = DeleteNoteUseCase(noteRepository)
    }

    @Test
    fun `invoke should call deleteNote with correct noteId`() = runTest {
        val noteId = 123L
        coEvery { noteRepository.deleteNote(noteId) } returns Unit

        deleteNoteUseCase(noteId)

        coVerify { noteRepository.deleteNote(noteId) }
    }
}
