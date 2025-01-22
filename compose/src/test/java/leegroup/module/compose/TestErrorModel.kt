package leegroup.module.compose

import io.mockk.every
import io.mockk.mockk
import leegroup.module.compose.support.extensions.mapApiError
import leegroup.module.compose.ui.models.ErrorModel
import leegroup.module.compose.ui.models.ErrorState
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

class MapApiErrorTest {

    @Test
    fun `mapApiError returns Network ErrorState for network-related exceptions`() {
        val networkExceptions = listOf(
            UnknownHostException(),
            SSLException("SSL error"),
            InterruptedIOException()
        )

        networkExceptions.forEach { exception ->
            val result = exception.mapApiError<ErrorModel>()
            assertEquals(ErrorState.Network, result)
        }
    }

    @Test
    fun `mapApiError returns Server ErrorState for ConnectException`() {
        val exception = ConnectException()
        val result = exception.mapApiError<ErrorModel>()
        assertEquals(ErrorState.Server, result)
    }

    @Test
    fun `mapApiError returns Api ErrorState for HttpException`() {
        val mockResponse: Response<Unit> = mockk()
        every { mockResponse.errorBody()?.string() } returns """{"message":"API error occurred"}"""
        every { mockResponse.code() } returns 400

        val httpException = MockUtil.apiError
        val result = httpException.mapApiError<ErrorModel>()

        assert(result is ErrorState.Api)
        result as ErrorState.Api
        assertEquals(ErrorModel("message").message, result.customMessage)
    }

    @Test
    fun `mapApiError returns Common ErrorState for other exceptions`() {
        val exception = IllegalArgumentException("Some error")
        val result = exception.mapApiError<ErrorModel>()
        assertEquals(ErrorState.Common, result)
    }

    @Test
    fun `assert error common is similar to message error`() {
        val messageError = object : ErrorState.MessageError {}
        assertEquals(ErrorState.Common.iconRes, messageError.iconRes)
        assertEquals(ErrorState.Common.messageRes, messageError.messageRes)
        assertEquals(ErrorState.Common.titleRes, messageError.titleRes)
        assertEquals(ErrorState.Common.primaryRes, messageError.primaryRes)
        assertEquals(ErrorState.Common.secondaryRes, messageError.secondaryRes)

    }

    @Test
    fun `mapApiError returns Api ErrorState with null error for HttpException with invalid body`() {

        val httpException = MockUtil.apiErrorInvalidMessage
        val result = httpException.mapApiError<ErrorModel>()

        assert(result is ErrorState.Api)
        result as ErrorState.Api
        assertEquals(null, result.error)
    }
}
