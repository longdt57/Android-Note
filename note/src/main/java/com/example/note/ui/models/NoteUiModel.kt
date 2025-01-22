package com.example.note.ui.models

import androidx.compose.runtime.Immutable
import com.example.note.domain.models.NoteD
import com.example.note.support.utils.DatetimeUtil
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class NoteUiModel(
    val id: Long = 0,
    val content: String,
    val date: String,
)

fun NoteD.mapToUiModel() = NoteUiModel(
    id = id,
    content = content,
    date = DatetimeUtil.formatDateTime(timestamp)
)

fun List<NoteD>.mapToUiModel() = map { it.mapToUiModel() }.toImmutableList()
