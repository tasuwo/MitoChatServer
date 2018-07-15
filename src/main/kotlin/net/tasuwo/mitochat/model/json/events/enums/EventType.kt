package net.tasuwo.mitochat.model.json.events.enums

import com.fasterxml.jackson.annotation.JsonValue

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
}