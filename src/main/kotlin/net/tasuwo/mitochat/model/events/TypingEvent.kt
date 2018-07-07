package net.tasuwo.mitochat.model.events

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import net.tasuwo.mitochat.model.events.EventType.Typing

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class TypingEvent(@Suppress("unused") val time: Double = 0.0) : Event {
    @JsonProperty("type")
    override val type = Typing
}
