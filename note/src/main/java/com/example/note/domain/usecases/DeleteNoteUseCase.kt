package com.example.note.domain.usecases

import com.example.note.domain.repositories.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(noteId: Long) {
        noteRepository.deleteNote(noteId)
    }
}