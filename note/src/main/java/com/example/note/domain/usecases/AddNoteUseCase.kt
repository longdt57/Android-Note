package com.example.note.domain.usecases

import com.example.note.domain.models.NoteD
import com.example.note.domain.repositories.NoteRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(note: String): NoteD {
        return noteRepository.addNote(note)
    }
}