package com.example.note.ui.screens.note

import androidx.lifecycle.viewModelScope
import com.example.note.NoteConfig.NOTE_PER_PAGE
import com.example.note.di.NoteModule.NoteDispatcherProvider
import com.example.note.domain.models.NoteD
import com.example.note.domain.param.GetNoteParam
import com.example.note.domain.usecases.AddNoteUseCase
import com.example.note.domain.usecases.DeleteNoteUseCase
import com.example.note.domain.usecases.GetNotesUseCase
import com.example.note.domain.usecases.PeriodicSyncToCloudUseCase
import com.example.note.domain.usecases.SyncFromCloudUseCase
import com.example.note.ui.models.NoteUiState
import com.example.note.ui.models.mapToUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import leegroup.module.compose.support.util.DispatchersProvider
import leegroup.module.compose.ui.viewmodel.StateViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@Suppress("TooManyFunctions")
class NoteViewModel @Inject constructor(
    @NoteDispatcherProvider private val dispatchersProvider: DispatchersProvider,
    private val getNotesUseCase: GetNotesUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val syncFromCloudUseCase: SyncFromCloudUseCase,
    private val periodicSyncToCloudUseCase: PeriodicSyncToCloudUseCase,
) : StateViewModel<NoteUiState>(
    initialUiState = NoteUiState()
) {

    private var loadDataJob: Job? = null

    init {
        syncFromCloud()
        syncToCloudPeriodic()
    }

    fun onDeleteNote(noteId: Long) {
        viewModelScope.launch(dispatchersProvider.io) {
            deleteNoteUseCase(noteId)
            update { state -> state.removeNote(noteId) }
        }
    }

    fun onAddNote(note: String) {
        viewModelScope.launch(dispatchersProvider.io) {
            val noteModel = addNoteUseCase(note)
            handleAddNewNote(noteModel)
        }
    }

    fun onQueryChanged(query: String) {
        if (query == getUiState().query) return
        update { it.copy(query = query) }
        reload()
    }

    fun loadNotes() {
        if (canLoadMore().not()) return

        val param = buildGetNotesParam()
        loadDataJob = getNotesUseCase.invoke(param)
            .injectLoading()
            .onEach { notes -> handleLoadNotesSuccess(notes) }
            .flowOn(dispatchersProvider.io)
            .catch { handleError(it) }
            .launchIn(viewModelScope)
    }

    private fun handleLoadNotesSuccess(notes: List<NoteD>) {
        val newNotes = notes.mapToUiModel()
        update { state ->
            val newSinceTimeStamp =
                if (notes.isNotEmpty()) notes.last().timestamp else state.sinceTimeStamp
            state
                .addNotes(newNotes)
                .copy(
                    hasMore = notes.isNotEmpty(),
                    sinceTimeStamp = newSinceTimeStamp
                )
        }

    }

    private fun syncFromCloud() {
        viewModelScope.launch(dispatchersProvider.io) {
            try {
                if (syncFromCloudUseCase()) {
                    reload()
                }
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    private fun syncToCloudPeriodic() {
        viewModelScope.launch(dispatchersProvider.io) {
            try {
                periodicSyncToCloudUseCase()
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    private fun reload() {
        cancelCurrentLoading()
        update { uiState -> uiState.resetData() }
        loadNotes()
    }

    private fun buildGetNotesParam(): GetNoteParam {
        val state = getUiState()
        return GetNoteParam(
            query = state.query,
            limit = NOTE_PER_PAGE,
            fromDate = state.sinceTimeStamp
        )
    }

    private fun cancelCurrentLoading() {
        loadDataJob?.cancel()
    }

    private fun canLoadMore(): Boolean {
        return getUiState().hasMore && loadDataJob?.isActive != true
    }

    private fun handleAddNewNote(note: NoteD) {
        val currentQuery = getUiState().query
        if (note.content.contains(currentQuery, ignoreCase = true).not()) return

        update { state -> state.addNote(note.mapToUiModel()) }
    }
}