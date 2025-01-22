package com.example.note.domain.usecases

import com.example.note.domain.models.NoteD
import com.example.note.domain.param.GetNoteParam
import com.example.note.domain.repositories.NoteRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetNotesUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var getNotesUseCase: GetNotesUseCase

    @Before
    fun setUp() {
        noteRepository = mockk()
        getNotesUseCase = GetNotesUseCase(noteRepository)
    }

    @Test
    fun `invoke should return a flow of notes`() = runTest {
        val param = GetNoteParam("query", 10, System.currentTimeMillis())
        val expectedNotes = listOf(
            NoteD(1L, "Note 1", 1705944600000),
            NoteD(2L, "Note 2", 1705954600000)
        )
        coEvery { noteRepository.getNotes(param) } returns expectedNotes

        val result = getNotesUseCase(param).first()

        assertEquals(expectedNotes, result)
    }
}
