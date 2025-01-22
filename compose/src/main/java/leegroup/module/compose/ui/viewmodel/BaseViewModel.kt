package leegroup.module.compose.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import leegroup.module.compose.support.extensions.mapApiError
import leegroup.module.compose.ui.models.ErrorModel
import leegroup.module.compose.ui.models.ErrorState
import leegroup.module.compose.ui.models.LoadingState

@Suppress("PropertyName", "MemberVisibilityCanBePrivate")
abstract class BaseViewModel : ViewModel() {

    private val _loading: MutableStateFlow<LoadingState> = MutableStateFlow(LoadingState.None)
    val loading = _loading.asStateFlow()

    protected val _error = MutableStateFlow<ErrorState>(ErrorState.None)
    val error = _error.asStateFlow()

    protected val _navigator = MutableSharedFlow<Any>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigator = _navigator.asSharedFlow()

    protected open fun showLoading() {
        _loading.value = LoadingState.Loading()
    }

    protected open fun isLoading(): Boolean {
        return _loading.value is LoadingState.Loading
    }

    protected open fun hideLoading() {
        _loading.value = LoadingState.None
    }

    protected open fun handleError(e: Throwable) {
        val error = e.mapApiError<ErrorModel>()
        _error.tryEmit(error)
    }

    open fun onErrorConfirmation(errorState: ErrorState) {
        hideError()
    }

    open fun onErrorDismissClick(errorState: ErrorState) {
        hideError()
    }

    fun hideError() {
        _error.tryEmit(ErrorState.None)
    }

    protected fun <T> Flow<T>.injectLoading(): Flow<T> = this
        .onStart { showLoading() }
        .onCompletion { hideLoading() }
}
