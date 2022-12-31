import com.rezerwacja_stolikow.HashGenerator
import com.rezerwacja_stolikow.util.invoke
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import com.rezerwacja_stolikow.util.resource as resourcePath

fun Routing.imageRoutes() {
    static("image") {
        get("{id?}") {
            val param = this.call.parameters
            val imageHash = param("id")
            val imageName = HashGenerator.decodeString(imageHash)
            val imagePath = resourcePath(imageName)
            val imageFile = File(imagePath)
            if (!imageFile.exists() || imageName.isBlank() || !imagePath.matches("restaurant/[0-9a-zA-Z]+\\.jpg".toRegex())) {
                throw NoSuchElementException("No such image with id: $imageHash")
            }
            this.call.response.header(
                HttpHeaders.ContentDisposition, ContentDisposition.Attachment.withParameter(
                    ContentDisposition.Parameters.FileName, "restaurant_${imagePath.split("/")[1]}.jpg"
                ).toString()
            )
            this.call.respondFile(imageFile)
        }
    }
}
