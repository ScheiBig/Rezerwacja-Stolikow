package com.rezerwacja_stolikow.routing

import com.rezerwacja_stolikow.HashGenerator
import com.rezerwacja_stolikow.util.invoke
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import com.rezerwacja_stolikow.util.resource as resourcePath

fun Routing.imageRoutes() {
    route(IMAGE) {
        get("restaurant_thumb" / "{$ID}") {
            val param = this.call.parameters
            val imageHash = param(ID)
            val imageName = try {
                HashGenerator.decodeString(imageHash)
            } catch (_: Exception) {
                throw NoSuchElementException("No such image with id: $imageHash")
            }
            val imagePath = resourcePath(imageName)
            val imageFile = File(imagePath)
            if (!imageFile.exists() || imageName.isBlank() || !("""thumb/[0-9a-zA-Z]+\.jpg"""
                    .toRegex()
                    .matches(imageName))
            ) {
                throw NoSuchElementException("No such image with id: $imageHash")
            }
            this.call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment
                    .withParameter(
                        ContentDisposition.Parameters.FileName, "restaurant_${imagePath.split("/")[1]}.jpg"
                    )
                    .toString()
            )
            this.call.respondFile(imageFile)
        }
        get("restaurant_map" / "{$ID}") {
            val param = this.call.parameters
            val imageHash = param(ID)
            val imageName = try {
                HashGenerator.decodeString(imageHash)
            } catch (_: Exception) {
                throw NoSuchElementException("No such image with id: $imageHash")
            }
            val imagePath = resourcePath(imageName)
            val imageFile = File(imagePath)
            if (!imageFile.exists() || imageName.isBlank() || !("""map/[0-9a-zA-Z]+\.svg"""
                    .toRegex()
                    .matches(imageName))
            ) {
                throw NoSuchElementException("No such image with id: $imageHash")
            }
            this.call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment
                    .withParameter(
                        ContentDisposition.Parameters.FileName, "map_${imagePath.split("/")[1]}.jpg"
                    )
                    .toString()
            )
            this.call.respondFile(imageFile)
        }
    }
}
