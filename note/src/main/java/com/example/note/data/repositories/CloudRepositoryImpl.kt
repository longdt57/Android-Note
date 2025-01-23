package com.example.note.data.repositories

import com.example.note.data.firestore.FireStoreNoteManager
import com.example.note.data.firestore.mapToEntity
import com.example.note.data.firestore.mapToFireStoreNote
import com.example.note.data.local.datastore.NoteDataStore
import com.example.note.data.local.room.daos.NoteDao
import com.example.note.data.models.NoteEntity
import com.example.note.domain.repositories.CloudRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CloudRepositoryImpl @Inject constructor(
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
        coroutineScope {
            val results = notes.map { note ->
                async {
                    fireStoreNoteManager.deleteNoteFromFireStore(note.id)
                }
            }.awaitAll()
            val noteResults = notes.filterIndexed { index, _ -> results[index] }
            if (noteResults.isNotEmpty()) {
                noteDao.deleteByIds(noteResults.map { it.id })
            }
        }
    }

    private suspend fun addNotesToCloud(notes: List<NoteEntity>) {
        coroutineScope {
            val results = notes.map { note ->
                async {
                    fireStoreNoteManager.addNoteToFireStore(note.mapToFireStoreNote())
                }
            }.awaitAll()

            val noteResults = notes.filterIndexed { index, _ -> results[index] }
            if (noteResults.isNotEmpty()) {
                noteDao.upsertItems(noteResults.map { it.copy(syncedCloud = true) })
            }
        }
    }

    private suspend fun updateNotesToCloud(notes: List<NoteEntity>) {
        coroutineScope {
            val results = notes.map { note ->
                async {
                    fireStoreNoteManager.updateNoteInFireStore(note.mapToFireStoreNote())
                }
            }.awaitAll()

            val noteResults = notes.filterIndexed { index, _ -> results[index] }
            if (noteResults.isNotEmpty()) {
                noteDao.upsertItems(noteResults.map { it.copy(syncedCloud = true) })
            }
        }
    }
}
