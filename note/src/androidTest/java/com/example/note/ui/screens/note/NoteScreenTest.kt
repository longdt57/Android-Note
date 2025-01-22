package com.example.note.ui.screens.note

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.note.R
import com.example.note.ui.models.NoteUiModel
import com.example.note.ui.models.NoteUiState
import kotlinx.collections.immutable.persistentListOf
import leegroup.module.compose.support.extensions.randomString
import leegroup.module.compose.ui.theme.ComposeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val notes = persistentListOf(
        NoteUiModel(
            id = 1,
            content = "First note",
            date = "2024-01-22 10:30:00"
        ),
        NoteUiModel(
            id = 2,
            content = "Second note: " + randomString(50),
            date = "2024-01-22 11:00:00"
        ),
        NoteUiModel(
            id = 3,
            content = "Third note",
            date = "2024-01-22 11:30:00"
        )
    )

    @Test
    fun test_screen_layout() {
        val title = context.getString(R.string.notes_screen_title)
        val addText = context.getString(R.string.add)
        val emptyNotesText = context.getString(R.string.note_empty_text)

        composeTestRule.setContent {
            ComposeTheme {
                NoteScreenContent(
                    uiState = NoteUiState(items = persistentListOf()),
                    showEmpty = true
                )
            }
        }

        composeTestRule.onNodeWithText(title).assertIsDisplayed()
        composeTestRule.onNodeWithText(addText).assertIsDisplayed()
        composeTestRule.onNodeWithText(emptyNotesText).assertIsDisplayed()
    }

    @Test
    fun test_screen_content() {
        val deleteText = context.getString(R.string.delete)
        composeTestRule.setContent {
            ComposeTheme {
                ComposeTheme {
                    NoteScreenContent(
                        uiState = NoteUiState(items = notes),
                        showEmpty = false
                    )
                }
            }
        }

        composeTestRule.onNodeWithText(notes.first().content).assertIsDisplayed()
        composeTestRule.onNodeWithText(notes.first().date).assertIsDisplayed()
        composeTestRule.onAllNodesWithContentDescription(label = deleteText)
            .assertCountEquals(notes.size)
    }

}