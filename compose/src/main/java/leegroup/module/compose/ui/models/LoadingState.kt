package leegroup.module.compose.ui.models

import androidx.annotation.StringRes
import leegroup.module.compose.R

sealed interface LoadingState {
    data object None : LoadingState
    data class Loading(@StringRes val messageRes: Int = R.string.loading) : LoadingState
}