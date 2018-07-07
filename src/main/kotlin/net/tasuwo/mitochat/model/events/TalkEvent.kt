package net.tasuwo.mitochat.model.events

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import net.tasuwo.mitochat.model.events.EventType.Talk

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class TalkEvent(
    @Suppress("unused") val icon: String = "",
    @Suppress("unused") val text: String = "",
    @Suppress("unused") val isRight: Boolean = false
) : Event {
    @JsonProperty("type")
    override var type = Talk
}
