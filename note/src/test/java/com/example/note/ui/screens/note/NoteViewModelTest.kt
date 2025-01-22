package com.example.note.ui.screens.note

import app.cash.turbine.test
import com.example.note.BaseUnitTest
import com.example.note.domain.models.NoteD
import com.example.note.domain.usecases.AddNoteUseCase
import com.example.note.domain.usecases.DeleteNoteUseCase
import com.example.note.domain.usecases.GetNotesUseCase
import com.example.note.domain.usecases.PeriodicSyncToCloudUseCase
import com.example.note.domain.usecases.SyncFromCloudUseCase
import com.example.note.ui.models.mapToUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import leegroup.module.compose.ui.models.ErrorState
import leegroup.module.compose.ui.models.LoadingState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NoteViewModelTest : BaseUnitTest() {

    private lateinit var viewModel: NoteViewModel
    private lateinit var getNotesUseCase: GetNotesUseCase
    private lateinit var addNoteUseCase: AddNoteUseCase
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase
    private lateinit var syncFromCloudUseCase: SyncFromCloudUseCase
    private lateinit var periodicSyncToCloudUseCase: PeriodicSyncToCloudUseCase

    private val query = "Note"

    private val noteD1 = NoteD(1L, "Note 1", System.currentTimeMillis())
    private val noteD2 = NoteD(2L, "Note 2", System.currentTimeMillis() - 60_000)

    private val notes = listOf(noteD1, noteD2)

    @Before
    fun setUp() {
        getNotesUseCase = mockk()
        addNoteUseCase = mockk()
        deleteNoteUseCase = mockk()
        syncFromCloudUseCase = mockk()
        periodicSyncToCloudUseCase = mockk()

        coEvery { syncFromCloudUseCase() } returns false
        coEvery { periodicSyncToCloudUseCase() } returns Unit
    }

    private fun initViewModel() {
        viewModel = NoteViewModel(
            dispatchersProvider = testDispatcherProvider,
            getNotesUseCase = getNotesUseCase,
            addNoteUseCase = addNoteUseCase,
            deleteNoteUseCase = deleteNoteUseCase,
            syncFromCloudUseCase = syncFromCloudUseCase,
            periodicSyncToCloudUseCase = periodicSyncToCloudUseCase,
        )
    }

    @Test
    fun `loadNotes should fetch and update notes`() = runTest {
        coEvery { getNotesUseCase.invoke(any()) } returns flowOf(notes)

        initViewModel()
        viewModel.loadNotes()

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertEquals(2, state.items.size)
        }
        coVerify { getNotesUseCase.invoke(any()) }
    }

    @Test
    fun `loadNotes return error should show error`() = runTest {
        coEvery { getNotesUseCase.invoke(any()) } returns flow {
            error("Error fetching notes")
        }

        initViewModel()
        viewModel.loadNotes()

        viewModel.error.test {
            val error = expectMostRecentItem()
            assertEquals(error, ErrorState.Common)
        }
    }

    @Test
    fun `loadNotes shouldn't execute if there is no more notes`() = runTest {
        coEvery { getNotesUseCase.invoke(any()) } returns flowOf(emptyList())

        initViewModel()
        viewModel.loadNotes()
        delay(10)
        viewModel.loadNotes()

        viewModel.uiState.test {
            val item = expectMostRecentItem()
            assertEquals(item.hasMore, false)
        }

        verify(exactly = 1) { getNotesUseCase.invoke(any()) }
    }

    @Test
    fun `loadNotes shouldn't execute if there is active job`() = runTest {
        coEvery { getNotesUseCase.invoke(any()) } returns flow {
            delay(100)
            emit(notes)
        }

        initViewModel()
        viewModel.loadNotes()
        delay(10)
        assertTrue(viewModel.loading.value is LoadingState.Loading)

        viewModel.loadNotes()

        verify(exactly = 1) { getNotesUseCase.invoke(any()) }
    }

    @Test
    fun `onAddNote should add note to state`() = runTest {
        val noteContent = "New note"
        coEvery { addNoteUseCase(noteContent) } returns noteD1

        initViewModel()
        viewModel.onAddNote(noteContent)

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            val expected = noteD1.mapToUiModel()
            assertEquals(expected, state.items.firstOrNull())
        }
        coVerify { addNoteUseCase(noteContent) }
    }

    @Test
    fun `onAddNote but not show on UI if not contains query`() = runTest {
        val query = "query"

        every { getNotesUseCase.invoke(any()) } returns flowOf()
        initViewModel()
        viewModel.onQueryChanged(query)

        val noteContent = "New note"
        coEvery { addNoteUseCase(noteContent) } returns noteD1

        viewModel.onAddNote(noteContent)

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertEquals(0, state.items.size)
        }
        coVerify { addNoteUseCase(noteContent) }
    }

    @Test
    fun `onDeleteNote should remove note from state`() = runTest {
        val deleteNoteId = noteD1.id
        every { getNotesUseCase.invoke(any()) } returns flowOf(notes)
        coEvery { deleteNoteUseCase(deleteNoteId) } returns Unit

        initViewModel()
        viewModel.loadNotes()
        delay(10)
        viewModel.onDeleteNote(deleteNoteId)

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertEquals(1, state.items.size)
        }
        coVerify { deleteNoteUseCase(deleteNoteId) }
    }

    @Test
    fun `onQueryChanged should cancel current job reload notes`() = runTest {
        coEvery { getNotesUseCase.invoke(any()) } returns flow {
            delay(100)
            emit(notes)
        }

        initViewModel()
        viewModel.loadNotes()

        val queryNote1 = "Note 1"
        coEvery { getNotesUseCase.invoke(any()) } returns flowOf(listOf(noteD1))
        viewModel.onQueryChanged(queryNote1)

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertEquals(queryNote1, state.query)
            assertEquals(1, state.items.size)
        }
    }

    @Test
    fun `onQueryChanged should update query and reload notes`() = runTest {
        coEvery { getNotesUseCase.invoke(any()) } returns flow { emit(notes) }

        initViewModel()
        viewModel.onQueryChanged(query)

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertEquals(query, state.query)
            assertEquals(2, state.items.size)
        }
    }

    @Test
    fun `query not changed should avoid updating query`() = runTest {
        coEvery { getNotesUseCase.invoke(any()) } returns flow { emit(notes) }

        initViewModel()
        viewModel.onQueryChanged(query)
        delay(10)
        viewModel.onQueryChanged(query)

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertEquals(query, state.query)
            assertEquals(2, state.items.size)
        }
        verify(exactly = 1) { getNotesUseCase.invoke(any()) }
    }

    @Test
    fun `loadMore should fetch additional notes and update sinceTimeStamp`() = runTest {
        every { getNotesUseCase.invoke(any()) } returns flowOf(listOf(noteD1))

        initViewModel()
        viewModel.loadNotes()

        val newNotes = listOf(noteD2)
        every { getNotesUseCase.invoke(any()) } returns flowOf(newNotes)
        viewModel.loadNotes()

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertEquals(2, state.items.size)
            assertEquals(noteD2.timestamp, state.sinceTimeStamp)
        }
        coVerify { getNotesUseCase.invoke(any()) }
    }

    @Test
    fun `syncFromCloud successfully should reload notes`() = runTest {
        coEvery { syncFromCloudUseCase() } returns true
        initViewModel()

        coVerify { syncFromCloudUseCase() }

        verify { getNotesUseCase.invoke(any()) }
    }

    @Test
    fun `syncFromCloud failed should not reload notes`() = runTest {
        coEvery { syncFromCloudUseCase() } returns false
        initViewModel()

        verify(exactly = 0) { getNotesUseCase.invoke(any()) }
    }

    @Test
    fun `syncToCloud should invoke`() = runTest {
        initViewModel()

        coVerify { periodicSyncToCloudUseCase() }
    }

    @Test
    fun `syncToCloud error should be handled`() = runTest {
        coEvery { periodicSyncToCloudUseCase() } throws RuntimeException()
        initViewModel()

        coVerify { periodicSyncToCloudUseCase() }
    }
}
