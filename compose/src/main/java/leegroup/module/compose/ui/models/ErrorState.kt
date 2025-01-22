package leegroup.module.compose.ui.models

import leegroup.module.compose.R

sealed interface ErrorState {
    data object None : ErrorState

    interface MessageError : ErrorState {
        val iconRes: Int? get() = null
        val titleRes: Int get() = R.string.popup_error_unknown_title
        val messageRes: Int get() = R.string.popup_error_unknown_body
        val primaryRes: Int get() = R.string.common_close
        val secondaryRes: Int? get() = null
    }

    data object Common : MessageError

    data object Network : MessageError {
        override val titleRes: Int = R.string.popup_error_no_connection_title
        override val messageRes: Int = R.string.popup_error_no_connection_body
        override val primaryRes: Int = R.string.common_retry
        override val secondaryRes: Int = R.string.common_close
    }

    data class Api(
        val error: ErrorModel? = null,
    ) : MessageError {
        val customMessage get() = error?.message

        override val titleRes: Int = R.string.popup_error_unknown_title
        override val messageRes: Int = R.string.popup_error_unknown_body
        override val primaryRes: Int = R.string.common_retry
        override val secondaryRes: Int = R.string.common_close
    }

    data object Server : MessageError {
        override val titleRes: Int = R.string.popup_error_timeout_title
        override val messageRes: Int = R.string.popup_error_timeout_body
        override val primaryRes: Int = R.string.common_close
    }
}
