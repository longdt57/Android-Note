package leegroup.module.compose.ui.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class ErrorModel(
    @SerialName("message")
    val message: String,
)