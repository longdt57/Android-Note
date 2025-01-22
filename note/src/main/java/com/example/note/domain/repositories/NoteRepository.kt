package com.example.note.domain.repositories

import com.example.note.domain.models.NoteD
import com.example.note.domain.param.GetNoteParam

interface NoteRepository {
    suspend fun addNote(note: String): NoteD
    suspend fun deleteNote(noteId: Long)
    suspend fun getNotes(param: GetNoteParam): List<NoteD>
}