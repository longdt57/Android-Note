package com.example.note.ui.models

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class NoteUiState(
    val query: String = "",
    val items: ImmutableList<NoteUiModel> = persistentListOf(),
    val sinceTimeStamp: Long = System.currentTimeMillis(),
    val hasMore: Boolean = true,
) {
    fun resetData() = copy(
        items = persistentListOf(),
        sinceTimeStamp = System.currentTimeMillis(),
        hasMore = true
    )

    fun addNotes(items: List<NoteUiModel>): NoteUiState {
        val newItems = this.items.plus(items).sortByDateDesc()
        return copy(items = newItems)
    }

    fun addNote(item: NoteUiModel) = copy(
        items = this.items.toMutableList()
            .apply { add(0, item) }
            .toImmutableList()
    )

    fun removeNote(noteId: Long) = copy(
        items = this.items.filter { it.id != noteId }.toImmutableList()

    )

    private fun List<NoteUiModel>.sortByDateDesc() =
        sortedByDescending { it.date }.toImmutableList()
}