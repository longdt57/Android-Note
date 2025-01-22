package com.example.note

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import leegroup.module.compose.support.util.DispatchersProvider
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineTestRule(
    var testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {

    val testDispatcherProvider = object : DispatchersProvider {

        override val io: CoroutineDispatcher
            get() = testDispatcher

        override val main: CoroutineDispatcher
            get() = testDispatcher

        override val default: CoroutineDispatcher
            get() = testDispatcher
    }

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
        super.finished(description)
    }
}
