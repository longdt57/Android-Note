package leegroup.module.compose.ui.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import leegroup.module.compose.ui.models.ErrorModel
import leegroup.module.compose.ui.models.ErrorState
import leegroup.module.compose.ui.theme.ComposeTheme

@Composable
fun ErrorView(
    error: ErrorState,
    onErrorConfirmation: (ErrorState) -> Unit = {},
    onErrorDismissRequest: (ErrorState) -> Unit = {},
) {
    when (error) {
        is ErrorState.MessageError -> {
            val message = when (error) {
                is ErrorState.Api -> error.customMessage ?: stringResource(error.messageRes)
                else -> stringResource(error.messageRes)
            }
            AlertDialogView(
                icon = {
                    error.iconRes?.let {
                        Icon(
                            painter = painterResource(id = it),
                            contentDescription = stringResource(id = error.titleRes)
                        )
                    }
                },
                title = stringResource(id = error.titleRes),
                text = message,
                confirmText = stringResource(id = error.primaryRes),
                dismissText = error.secondaryRes?.let { stringResource(id = it) },
                onConfirmation = { onErrorConfirmation(error) },
                onDismissRequest = { onErrorDismissRequest(error) })
        }

        is ErrorState.None -> {}
    }
}

@Preview(showBackground = true)
@Composable
private fun CommonErrorViewPreview() {
    ComposeTheme {
        ErrorView(ErrorState.Common)
    }
}

@Preview(showBackground = true)
@Composable
private fun NetworkErrorViewPreview() {
    ComposeTheme {
        ErrorView(ErrorState.Network)
    }
}

@Preview(showBackground = true)
@Composable
private fun ServerErrorViewPreview() {
    ComposeTheme {
        ErrorView(ErrorState.Server)
    }
}

@Preview(showBackground = true)
@Composable
private fun ApiErrorViewPreview() {
    ComposeTheme {
        ErrorView(ErrorState.Api())
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomApiErrorViewPreview() {
    ComposeTheme {
        ErrorView(
            ErrorState.Api(
                error = ErrorModel(
                    message = "Custom message"
                )
            )
        )
    }
}


