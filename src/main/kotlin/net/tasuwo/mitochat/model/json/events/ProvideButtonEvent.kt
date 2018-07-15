package net.tasuwo.mitochat.model.json.events

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import net.tasuwo.mitochat.model.json.events.enums.EventType.ProvideButton

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class ProvideButtonEvent(
    @Suppress("unused") val text: String = "",
    @Suppress("unused") val nextChatId: Int = 0
) : Event {
    @JsonProperty("type")
    override val type = ProvideButton
}
