package net.tasuwo.mitochat.model

import com.fasterxml.jackson.databind.ObjectMapper
import net.tasuwo.mitochat.model.json.Scenario
import net.tasuwo.mitochat.model.json.events.Event
import net.tasuwo.mitochat.model.json.events.enums.EventType
import net.tasuwo.mitochat.model.json.events.Events
import net.tasuwo.mitochat.model.json.events.ProvideButtonEvent
import net.tasuwo.mitochat.model.json.events.ResetButtonsEvent
import net.tasuwo.mitochat.model.json.events.TalkEvent
import net.tasuwo.mitochat.model.json.events.TypingEvent
import net.tasuwo.mitochat.model.json.events.WaitingEvent
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException

// TODO: Json Schema による Validation
class JsonParser {
    fun parse(jsonStr: String): Scenario {
        val parser = JSONParser()

        try {
            val json = parser.parse(jsonStr) as JSONObject

            val eventsIds = json.keys.iterator()
            val eventsById = mapOf<String, Events>().toMutableMap()
            while (eventsIds.hasNext()) {
                val eventsId = eventsIds.next() as String
                val events = (json[eventsId] as JSONObject)["events"] as JSONArray?
                events ?: throw RuntimeException("キー events が格納されていません")

                val eventList = listOf<Event>().toMutableList()
                for (i in 0..(events.size - 1)) {
                    val obj = events[i] as JSONObject
                    val event = generateEvent(obj)
                    event ?: throw RuntimeException( "Event Type (${obj["type"]}) が不正です")

                    eventList.add(event)
                }

                eventsById[eventsId] = Events(eventList)
            }

            return Scenario(eventsById)
        } catch (e: ParseException) {
            throw RuntimeException("JSON フォーマットが正しくありません: $jsonStr", e)
        } catch (e: RuntimeException) {
            throw RuntimeException("${e.message}: $jsonStr", e)
        }
    }

    private fun generateEvent(e: JSONObject) : Event? {
        val mapper = ObjectMapper()
        val type = e["type"] as String?
        type ?: throw RuntimeException( "キー Event Type が格納されていません")

        return when (EventType.fromString(type)) {
            EventType.Waiting ->
                mapper.readValue(e.toJSONString(), WaitingEvent::class.java)
            EventType.Talk ->
                mapper.readValue(e.toJSONString(), TalkEvent::class.java)
            EventType.ResetButtons ->
                mapper.readValue(e.toJSONString(), ResetButtonsEvent::class.java)
            EventType.Typing ->
                mapper.readValue(e.toJSONString(), TypingEvent::class.java)
            EventType.ProvideButton ->
                mapper.readValue(e.toJSONString(), ProvideButtonEvent::class.java)
            null -> null
        }
    }
}