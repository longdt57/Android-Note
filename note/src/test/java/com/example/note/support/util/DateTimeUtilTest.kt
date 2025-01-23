package com.example.note.support.util

import com.example.note.support.utils.DatetimeUtil
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.TimeZone

class DatetimeUtilTest {

    @Test
    fun testFormatDateTime() {
        val timestamp = 1705944600000
        val expectedDate = "2024-01-22 17:30:00"

        val result = DatetimeUtil.formatDateTime(timestamp, TimeZone.getTimeZone("GMT"))
        assertEquals(expectedDate, result)
    }
}
