package net.tasuwo.mitochat.model.json.events.enums

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

class EventTypeDeserializer : JsonDeserializer<Any>() {
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): EventType? {
        val jsonValue = jp.text
        return EventType.fromString(jsonValue)
    }
}
