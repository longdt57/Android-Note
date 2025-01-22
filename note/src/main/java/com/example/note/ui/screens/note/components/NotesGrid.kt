package com.example.note.ui.screens.note.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.note.ui.models.NoteUiModel
import com.example.note.ui.models.ScreenSize
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import leegroup.module.compose.support.extensions.randomString
import leegroup.module.compose.ui.components.LoadMoreGrid
import leegroup.module.compose.ui.theme.ComposeTheme

@Composable
fun NotesGrid(
    notes: ImmutableList<NoteUiModel>,
    onLoadMore: () -> Unit = {},
    onDelete: (NoteUiModel) -> Unit = {},
    onClick: (NoteUiModel) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val screenSize = ScreenSize.fromWidth(LocalConfiguration.current.screenWidthDp)
    val columnCount = screenSize.columnCount

    val listState = rememberLazyGridState()
    LoadMoreGrid(gridState = listState, onLoadMore = onLoadMore)

    LazyVerticalGrid(
        modifier = modifier.padding(horizontal = 16.dp),
        state = listState,
        columns = GridCells.Fixed(columnCount),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item(span = { GridItemSpan(columnCount) }) {
            Spacer(modifier = Modifier.padding(top = 8.dp))
        }
        items(notes, key = { it.id }) { note ->
            NoteCard(
                modifier = Modifier.fillMaxWidth(),
                note = note,
                onDelete = onDelete,
                onClick = onClick
            )
        }
        item(span = { GridItemSpan(columnCount) }) {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
@Suppress("MagicNumber")
private fun PreviewNotesGrid() {
    val sampleNotes = persistentListOf(
        NoteUiModel(id = 1, content = "First note", date = "2024-01-22 10:30:00"),
        NoteUiModel(
            id = 2,
            content = "Second note: " + randomString(50),
            date = "2024-01-22 11:00:00"
        ),
        NoteUiModel(id = 3, content = "Third note", date = "2024-01-22 11:30:00")
    )

    ComposeTheme {
        NotesGrid(notes = sampleNotes, onDelete = {})
    }
}

@Preview(
    showBackground = true,
    device = Devices.PIXEL_FOLD
)
@Composable
@Suppress("MagicNumber")
private fun PreviewLandscapeNotesGrid() {
    val sampleNotes = persistentListOf(
        NoteUiModel(id = 1, content = "First note", date = "2024-01-22 10:30:00"),
        NoteUiModel(
            id = 2,
            content = "Second note: " + randomString(50),
            date = "2024-01-22 11:00:00"
        ),
        NoteUiModel(id = 3, content = "Third note", date = "2024-01-22 11:30:00")
    )

    ComposeTheme {
        NotesGrid(notes = sampleNotes, onDelete = {})
    }
}

@Preview(
    showBackground = true,
    device = Devices.PIXEL_TABLET
)
@Composable
@Suppress("MagicNumber")
private fun PreviewLargeScreenNotesGrid() {
    val sampleNotes = persistentListOf(
        NoteUiModel(id = 1, content = "First note", date = "2024-01-22 10:30:00"),
        NoteUiModel(
            id = 2,
            content = "Second note: " + randomString(50),
            date = "2024-01-22 11:00:00"
        ),
        NoteUiModel(id = 3, content = "Third note", date = "2024-01-22 11:30:00")
    )

    ComposeTheme {
        NotesGrid(notes = sampleNotes)
    }
}

