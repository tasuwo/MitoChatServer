package net.tasuwo.mitochat.model.json.events

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import net.tasuwo.mitochat.model.json.events.enums.EventType.Talk

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class TalkEvent(
    @Suppress("unused") val icon: String = "",
    @Suppress("unused") val text: String = "",
    @Suppress("unused") val right: Boolean = false
) : Event {
    @JsonProperty("type")
    override var type = Talk
}
