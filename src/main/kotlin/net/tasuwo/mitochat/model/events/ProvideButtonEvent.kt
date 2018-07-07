package net.tasuwo.mitochat.model.events

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import net.tasuwo.mitochat.model.events.EventType.ProvideButton

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class ProvideButtonEvent(
    @Suppress("unused") val text: String = "",
    @Suppress("unused") val nextEventsId: Int = 0
) : Event {
    @JsonProperty("type")
    override val type = ProvideButton
}
