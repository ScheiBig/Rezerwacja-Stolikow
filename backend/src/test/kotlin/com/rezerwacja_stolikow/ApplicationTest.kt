package com.rezerwacja_stolikow

import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

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
