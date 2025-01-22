package com.example.note.data.repositories

import com.example.note.data.local.room.daos.NoteDao
import com.example.note.data.models.NoteEntity
import com.example.note.data.models.mapToDomainModel
import com.example.note.domain.param.GetNoteParam
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NoteRepositoryImplTest {

    private lateinit var noteDao: NoteDao
    private lateinit var noteRepository: NoteRepositoryImpl

    @Before
    fun setUp() {
        noteDao = mockk()
        noteRepository = NoteRepositoryImpl(noteDao)
    }

    @Test
    fun `addNote should save note and return NoteD`() = runTest {
        val noteContent = "Test note"
        val currentTimeStamp = System.currentTimeMillis()
        val noteId = 1L
        coEvery { noteDao.upsert(any()) } returns noteId

        val result = noteRepository.addNote(noteContent)

        assertEquals(noteContent, result.content)
        assertEquals(noteId, result.id)
        assertTrue(result.timestamp >= currentTimeStamp)
        coVerify { noteDao.upsert(any()) }
    }

    @Test
    fun `deleteNote should call deleteById`() = runTest {
        val noteId = 1L
        coEvery { noteDao.markAsDeletedById(noteId) } returns Unit

        noteRepository.deleteNote(noteId)

        coVerify { noteDao.markAsDeletedById(noteId) }
    }

    @Test
    fun `getNotes should fetch and map notes correctly`() = runTest {
        val param = GetNoteParam("query", 10, System.currentTimeMillis())
        val noteEntities = listOf(
            NoteEntity(1L, "Note 1", System.currentTimeMillis()),
            NoteEntity(2L, "Note 2", System.currentTimeMillis())
        )
        coEvery {
            noteDao.getNotes(query = param.query, limit = param.limit, fromDate = param.fromDate)
        } returns noteEntities

        val result = noteRepository.getNotes(param)

        val expected = noteEntities.map { it.mapToDomainModel() }
        assertEquals(expected, result)
        coVerify {
            noteDao.getNotes(query = param.query, limit = param.limit, fromDate = param.fromDate)
        }
    }
}
