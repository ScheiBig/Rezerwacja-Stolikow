package com.rezerwacja_stolikow.serialization

import com.rezerwacja_stolikow.validation.PhoneNumber
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object PhoneNumberSerializer: KSerializer<String> {
    override val descriptor = PrimitiveSerialDescriptor("PhoneNumber", PrimitiveKind.LONG)
    
    override fun deserialize(decoder: Decoder) = PhoneNumber.toString(decoder.decodeLong())
    
    override fun serialize(
        encoder: Encoder,
        value: String
    ) = encoder.encodeLong(
        value
            .replace(" ", "")
            .toLong()
    )
}
