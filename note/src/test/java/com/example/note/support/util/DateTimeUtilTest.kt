package com.example.note.support.util

import com.example.note.support.utils.DatetimeUtil
import org.junit.Assert.assertEquals
import org.junit.Test

class DatetimeUtilTest {

    @Test
    fun testFormatDateTime() {
        val timestamp = 1705944600000
        val expectedDate = "2024-01-23 02:30:00"

        val result = DatetimeUtil.formatDateTime(timestamp)
        assertEquals(expectedDate, result)
    }
}
