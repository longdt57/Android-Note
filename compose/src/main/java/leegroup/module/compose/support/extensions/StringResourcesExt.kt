package leegroup.module.compose.support.extensions

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource

@Composable
@ReadOnlyComposable
fun stringResourceOrNull(@StringRes id: Int?): String? {
    return when (id) {
        null, 0, -1 -> null
        else -> stringResource(id)
    }
}