package leegroup.module.compose.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import leegroup.module.compose.ui.viewmodel.BaseViewModel

@Composable
fun BaseScreen(
    viewModel: BaseViewModel,
    content: @Composable () -> Unit,
) {
    content()
    LoadingView(viewModel)
    ErrorView(viewModel)
}

@Composable
fun LoadingView(viewModel: BaseViewModel) {
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    LoadingView(loading = loading)
}


@Composable
fun ErrorView(viewModel: BaseViewModel) {
    val error by viewModel.error.collectAsStateWithLifecycle()
    ErrorView(
        error = error,
        onErrorConfirmation = { viewModel.onErrorConfirmation(it) },
        onErrorDismissRequest = { viewModel.onErrorDismissClick(it) }
    )
}
