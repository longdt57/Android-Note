package leegroup.module.compose.support.util

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DispatchersProviderImpl @Inject constructor() : DispatchersProvider {

    override val io = Dispatchers.IO

    override val main = Dispatchers.Main

    override val default = Dispatchers.Default
}
