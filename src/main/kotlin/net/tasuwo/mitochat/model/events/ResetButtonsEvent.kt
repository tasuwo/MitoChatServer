package net.tasuwo.mitochat.model.events

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import net.tasuwo.mitochat.model.events.EventType.ResetButtons

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class ResetButtonsEvent : Event {
    @JsonProperty("type")
    override var type = ResetButtons
}
