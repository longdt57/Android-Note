package leegroup.module.compose.support.extensions

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import leegroup.module.compose.ui.models.BaseDestination

fun NavGraphBuilder.composable(
    destination: BaseDestination,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = destination.route,
        arguments = destination.arguments,
        deepLinks = destination.deepLinks.map {
            navDeepLink {
                uriPattern = it
            }
        },
        content = content
    )
}

fun NavHostController.appNavigate(destination: Any) {
    when (destination) {
        is BaseDestination -> navigateWithDestination(destination)
        else -> navigate(destination)
    }
}


fun NavHostController.navigateWithDestination(destination: BaseDestination) {
    when (destination) {
        is BaseDestination.Up -> {
            destination.results.forEach { (key, value) ->
                previousBackStackEntry?.savedStateHandle?.set(key, value)
            }
            navigateUp()
        }

        else -> navigate(route = destination.destination)
    }
}

