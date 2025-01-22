package leegroup.module.compose.support.extensions

import leegroup.module.compose.support.util.JsonUtil
import leegroup.module.compose.ui.models.ErrorModel
import leegroup.module.compose.ui.models.ErrorState
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

inline fun <reified Model : ErrorModel> Throwable.mapApiError(): ErrorState {
    return when (this) {
        is UnknownHostException,
        is SSLException,
        is InterruptedIOException -> ErrorState.Network

        is ConnectException -> ErrorState.Server
        is HttpException -> mapHttpException<Model>(this)
        else -> ErrorState.Common
    }
}

inline fun <reified Model : ErrorModel> mapHttpException(
    exception: HttpException
): ErrorState {
    val errorResponse = exception.response()?.let { response ->
        val jsonString = response.errorBody()?.string() ?: return@let null
        JsonUtil.decodeFromString<Model>(jsonString)
    }
    return ErrorState.Api(error = errorResponse)
}
