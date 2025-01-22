package leegroup.module.compose.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import leegroup.module.compose.ui.models.LoadingState
import leegroup.module.compose.ui.theme.ComposeTheme

@Composable
fun LoadingView(loading: LoadingState) {
    when (loading) {
        is LoadingState.Loading -> LoadingProgress(loading)
        else -> {}
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingViewPreview() {
    ComposeTheme {
        LoadingView(LoadingState.Loading())
    }
}