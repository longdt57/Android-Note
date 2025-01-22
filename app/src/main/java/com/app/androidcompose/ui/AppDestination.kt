package com.app.androidcompose.ui

import leegroup.module.compose.ui.models.BaseDestination

sealed class AppDestination {

    object RootNavGraph : BaseDestination("rootNavGraph")

}
