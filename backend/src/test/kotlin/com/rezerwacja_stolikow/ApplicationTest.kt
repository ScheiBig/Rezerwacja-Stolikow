package com.rezerwacja_stolikow

import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.rezerwacja_stolikow.routing.configureRouting

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        client
            .get("/")
            .apply {
                assertEquals(HttpStatusCode.OK, this.status)
                assertEquals("Hello World!", this.bodyAsText())
            }
    }
}
