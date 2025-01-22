package com.example.note

import org.junit.Rule

abstract class BaseUnitTest {

    @get:Rule
    val coroutinesRule = CoroutineTestRule()

    val testDispatcherProvider = coroutinesRule.testDispatcherProvider
}