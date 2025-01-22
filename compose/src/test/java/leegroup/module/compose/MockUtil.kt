package leegroup.module.compose

import io.mockk.every
import io.mockk.mockk
import leegroup.module.compose.ui.models.ErrorState
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

object MockUtil {
    val serverException: Throwable = ConnectException()
    val noConnectivityException: Throwable = UnknownHostException()
    val apiErrorState = ErrorState.Api()

    val apiError: HttpException
        get() {
            val response = mockk<Response<Any>>()
            val httpException = mockk<HttpException>()
            val responseBody = mockk<ResponseBody>()
            every { response.code() } returns 500
            every { response.message() } returns "message"
            every { response.errorBody() } returns responseBody
            every { httpException.code() } returns response.code()
            every { httpException.message() } returns response.message()
            every { httpException.response() } returns response
            every { responseBody.string() } returns
                    """
                    {
                        "message": "message"
                    }
                """.trimIndent()
            return httpException
        }

    val apiErrorInvalidMessage: HttpException
        get() {
            val response = mockk<Response<Any>>()
            val httpException = mockk<HttpException>()
            val responseBody = mockk<ResponseBody>()
            every { response.code() } returns 500
            every { response.message() } returns "message"
            every { response.errorBody() } returns responseBody
            every { httpException.code() } returns response.code()
            every { httpException.message() } returns response.message()
            every { httpException.response() } returns response
            every { responseBody.string() } returns
                    """
                    {
                        "message": "message", Invalid Json
                    }
                """.trimIndent()
            return httpException
        }
}