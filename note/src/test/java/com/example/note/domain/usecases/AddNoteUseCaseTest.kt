package com.example.note.domain.usecases

import com.example.note.domain.models.NoteD
import com.example.note.domain.repositories.NoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AddNoteUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var addNoteUseCase: AddNoteUseCase

    @Before
    fun setUp() {
        noteRepository = mockk()
        addNoteUseCase = AddNoteUseCase(noteRepository)
    }

    @Test
    fun `invoke should add note and return NoteD`() = runTest {
        val note = "Test note"
        val expectedNoteD = NoteD(1L, "Test note", timestamp = 1705944600000)
        coEvery { noteRepository.addNote(note) } returns expectedNoteD

        val result = addNoteUseCase(note)

        assertEquals(expectedNoteD, result)
        coVerify { noteRepository.addNote(note) }
    }
}
