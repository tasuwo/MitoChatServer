package net.tasuwo.mitochat.model.json.events.enums

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = EventTypeDeserializer::class)
sealed class EventType {
    object Waiting : EventType() {
        @JsonValue
        override fun toString() = "WAITING"
    }
    object Typing : EventType() {
        @JsonValue
        override fun toString() = "TYPING"
    }
    object Talk : EventType() {
        @JsonValue
        override fun toString() = "TALK"
    }
    object ProvideButton : EventType() {
        @JsonValue
        override fun toString() = "PROVIDE_BUTTON"
    }
    object ResetButtons : EventType() {
        @JsonValue
        override fun toString() = "RESET_BUTTONS"
    }

    companion object {
        fun fromString(@JsonProperty("type") str: String): EventType? {
            return when (str) {
                "WAITING" -> Waiting
                "TYPING" -> Typing
                "TALK" -> Talk
                "PROVIDE_BUTTON" -> ProvideButton
                "RESET_BUTTONS" -> ResetButtons
                else -> null
            }
        }
    }
}
