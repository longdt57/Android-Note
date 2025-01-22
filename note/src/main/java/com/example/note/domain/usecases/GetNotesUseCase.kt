package com.example.note.domain.usecases

import com.example.note.domain.models.NoteD
import com.example.note.domain.param.GetNoteParam
import com.example.note.domain.repositories.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(param: GetNoteParam): Flow<List<NoteD>> {
        return flow {
            emit(noteRepository.getNotes(param))
        }
    }
}