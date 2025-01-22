package com.example.note.data.repositories

import com.example.note.data.local.room.daos.NoteDao
import com.example.note.data.models.NoteEntity
import com.example.note.data.models.NoteStatus
import com.example.note.data.models.mapToDomainModel
import com.example.note.domain.models.NoteD
import com.example.note.domain.param.GetNoteParam
import com.example.note.domain.repositories.NoteRepository
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override suspend fun addNote(note: String): NoteD {
        val noteEntity = NoteEntity(content = note, status = NoteStatus.ADD)
        val noteId = noteDao.upsert(noteEntity)
        return noteEntity.copy(id = noteId)
            .mapToDomainModel()
    }

    override suspend fun deleteNote(noteId: Long) {
        return noteDao.markAsDeletedById(noteId)
    }

    override suspend fun getNotes(param: GetNoteParam): List<NoteD> {
        return noteDao.getNotes(
            query = param.query,
            limit = param.limit,
            fromDate = param.fromDate
        )
            .filter { it.isStatusDelete().not() }
            .mapToDomainModel()
    }
}
