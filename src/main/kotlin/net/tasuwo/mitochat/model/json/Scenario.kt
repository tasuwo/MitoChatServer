package net.tasuwo.mitochat.model.json

import net.tasuwo.mitochat.model.json.events.Events

data class Scenario(
    @Suppress("unused") val scenario: Map<String, Events> = mapOf()
)
