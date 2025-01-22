package com.example.note.data.repositories

import com.example.note.data.firestore.FireStoreNoteManager
import com.example.note.data.firestore.mapToEntity
import com.example.note.data.firestore.mapToFireStoreNote
import com.example.note.data.local.datastore.NoteDataStore
import com.example.note.data.local.room.daos.NoteDao
import com.example.note.data.models.NoteEntity
import com.example.note.di.NoteModule.NoteDispatcherProvider
import com.example.note.domain.repositories.CloudRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import leegroup.module.compose.support.util.DispatchersProvider
import javax.inject.Inject

class CloudRepositoryImpl @Inject constructor(
    @NoteDispatcherProvider private val dispatchersProvider: DispatchersProvider,
    private val noteDataStore: NoteDataStore,
    private val fireStoreNoteManager: FireStoreNoteManager,
    private val noteDao: NoteDao,
) : CloudRepository {

    override suspend fun isSyncedFromCloud(): Boolean {
        return noteDataStore.hasSyncedFromCloud().first()
    }

    override suspend fun syncFromCloud(): Boolean {
        val notes = fireStoreNoteManager.getNotesFromFireStore()
        noteDao.upsertItems(notes.mapToEntity())
        noteDataStore.setHasSyncedFromCloud(true)
        return notes.isNotEmpty()
    }

    override suspend fun syncToCloud() {
        val notes = noteDao.getUnSyncedNotes()
        if (notes.isEmpty()) return

        val deletedNotes = notes.filter { it.isStatusDelete() }
        val addNotes = notes.filter { it.isStatusAdd() }
        val updateNotes = notes.filter { it.isStatusUpdate() }

        deleteNotesFromCloud(deletedNotes)
        addNotesToCloud(addNotes)
        updateNotesToCloud(updateNotes)
    }

    private suspend fun deleteNotesFromCloud(notes: List<NoteEntity>) {
        val results = notes.filter { note ->
            withContext(dispatchersProvider.io) {
                fireStoreNoteManager.deleteNoteFromFireStore(note.id)
            }
        }
        if (results.isNotEmpty()) {
            noteDao.deleteByIds(results.map { it.id })
        }
    }

    private suspend fun addNotesToCloud(notes: List<NoteEntity>) {
        val results = notes.filter { note ->
            withContext(dispatchersProvider.io) {
                fireStoreNoteManager.addNoteToFireStore(note.mapToFireStoreNote())
            }
        }
        if (results.isNotEmpty()) {
            noteDao.upsertItems(results.map { it.copy(syncedCloud = true) })
        }
    }

    private suspend fun updateNotesToCloud(notes: List<NoteEntity>) {
        val results = notes.filter { note ->
            withContext(dispatchersProvider.io) {
                fireStoreNoteManager.updateNoteInFireStore(note.mapToFireStoreNote())
            }
        }
        if (results.isNotEmpty()) {
            noteDao.upsertItems(results.map { it.copy(syncedCloud = true) })
        }
    }
}
