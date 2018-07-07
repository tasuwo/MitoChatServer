package net.tasuwo.mitochat.model.events

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import net.tasuwo.mitochat.model.events.EventType.Waiting

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class WaitingEvent(@Suppress("unused") val time: Double = 0.0) : Event {
    @JsonProperty("type")
    override val type = Waiting
}