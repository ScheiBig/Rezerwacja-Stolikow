@file:OptIn(ExperimentalSerializationApi::class)

package com.rezerwacja_stolikow.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*

val PrettyJson = Json {
    prettyPrint = true
    explicitNulls = false
}
