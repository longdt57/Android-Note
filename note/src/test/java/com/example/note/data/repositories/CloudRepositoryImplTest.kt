package com.example.note.data.repositories

import com.example.note.data.firestore.FireStoreNote
import com.example.note.data.firestore.FireStoreNoteManager
import com.example.note.data.firestore.mapToEntity
import com.example.note.data.firestore.mapToFireStoreNote
import com.example.note.data.local.datastore.NoteDataStore
import com.example.note.data.local.room.daos.NoteDao
import com.example.note.data.models.NoteEntity
import com.example.note.data.models.NoteStatus
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CloudRepositoryImplTest {

    private val noteDataStore: NoteDataStore = mockk()
    private val fireStoreNoteManager: FireStoreNoteManager = mockk()
    private val noteDao: NoteDao = mockk()

    private val repository =
        CloudRepositoryImpl(noteDataStore, fireStoreNoteManager, noteDao)

    @Test
    fun `isSyncedFromCloud returns true when datastore emits true`() = runTest {
        every { noteDataStore.hasSyncedFromCloud() } returns flowOf(true)

        val result = repository.isSyncedFromCloud()

        assert(result)
        verify { noteDataStore.hasSyncedFromCloud() }
    }

    @Test
    fun `isSyncedFromCloud returns false when datastore emits false`() = runTest {
        every { noteDataStore.hasSyncedFromCloud() } returns flowOf(false)

        val result = repository.isSyncedFromCloud()

        assert(!result)
        verify { noteDataStore.hasSyncedFromCloud() }
    }

    @Test
    fun `syncFromCloud retrieves notes and updates datastore`() = runTest {
        val notes = listOf(FireStoreNote(id = 1, content = "Test", timestamp = 123456789L))
        val notesEntity = notes.mapToEntity()
        coEvery { fireStoreNoteManager.getNotesFromFireStore() } returns notes
        coEvery { noteDao.upsertItems(notesEntity) } returns Unit
        coEvery { noteDataStore.setHasSyncedFromCloud(true) } returns Unit

        repository.syncFromCloud()

        coVerify { fireStoreNoteManager.getNotesFromFireStore() }
        coVerify { noteDao.upsertItems(notesEntity) }
        coVerify { noteDataStore.setHasSyncedFromCloud(true) }
    }

    @Test
    fun `syncToCloud handles empty unsynced notes`() = runTest {
        coEvery { noteDao.getUnSyncedNotes() } returns emptyList()

        repository.syncToCloud()

        coVerify { noteDao.getUnSyncedNotes() }
        coVerify(exactly = 0) { fireStoreNoteManager.deleteNoteFromFireStore(any()) }
    }

    @Test
    fun `syncToCloud processes deleted notes`() = runTest {
        val deletedNote = NoteEntity(id = 1, content = "Deleted Note", status = NoteStatus.DELETE)
        coEvery { noteDao.getUnSyncedNotes() } returns listOf(deletedNote)
        coEvery { fireStoreNoteManager.deleteNoteFromFireStore(deletedNote.id) } returns true
        coEvery { noteDao.deleteByIds(listOf(deletedNote.id)) } returns Unit

        repository.syncToCloud()

        coVerify { fireStoreNoteManager.deleteNoteFromFireStore(deletedNote.id) }
        coVerify { noteDao.deleteByIds(listOf(deletedNote.id)) }
    }

    @Test
    fun `syncToCloud processes added notes`() = runTest {
        val addedNotesEntity =
            listOf(NoteEntity(id = 2, content = "Added Note", status = NoteStatus.ADD))
        val addedNotes = addedNotesEntity.mapToFireStoreNote()
        coEvery { noteDao.getUnSyncedNotes() } returns addedNotesEntity
        coEvery { fireStoreNoteManager.addNoteToFireStore(addedNotes.first()) } returns true
        coEvery { noteDao.upsertItems(addedNotesEntity.map { it.copy(syncedCloud = true) }) } returns Unit

        repository.syncToCloud()

        coVerify { fireStoreNoteManager.addNoteToFireStore(addedNotes.first()) }
        coVerify { noteDao.upsertItems(addedNotesEntity.map { it.copy(syncedCloud = true) }) }
    }

    @Test
    fun `syncToCloud processes updated notes`() = runTest {
        val updatedNoteEntity =
            listOf(NoteEntity(id = 3, content = "Updated Note", status = NoteStatus.UPDATE))
        val updatedNote = updatedNoteEntity.mapToFireStoreNote()
        coEvery { noteDao.getUnSyncedNotes() } returns updatedNoteEntity
        coEvery { fireStoreNoteManager.updateNoteInFireStore(updatedNote.first()) } returns true
        coEvery { noteDao.upsertItems(updatedNoteEntity.map { it.copy(syncedCloud = true) }) } returns Unit

        repository.syncToCloud()

        coVerify { fireStoreNoteManager.updateNoteInFireStore(updatedNote.first()) }
        coVerify { noteDao.upsertItems(updatedNoteEntity.map { it.copy(syncedCloud = true) }) }
    }
}
