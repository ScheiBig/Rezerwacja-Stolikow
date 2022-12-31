package com.rezerwacja_stolikow.util

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.util.*

/**
 * Returns path to [resource][path] relative to project root
 */
fun resource(path: String) = "src\\main\\resources\\$path"

/**
 * Get parameters value associated with this [name] or fail with [MissingRequestParameterException]
 * @throws MissingRequestParameterException if no values associated with this [name]
 */
operator fun Parameters.invoke(name: String) = this.getOrFail(name)
