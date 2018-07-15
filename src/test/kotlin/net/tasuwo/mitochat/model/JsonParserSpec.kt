package net.tasuwo.mitochat.model

import net.tasuwo.mitochat.SpecUtil.Companion.getFile
import net.tasuwo.mitochat.model.json.Scenario
import net.tasuwo.mitochat.model.json.events.Events
import net.tasuwo.mitochat.model.json.events.ProvideButtonEvent
import net.tasuwo.mitochat.model.json.events.ResetButtonsEvent
import net.tasuwo.mitochat.model.json.events.TalkEvent
import net.tasuwo.mitochat.model.json.events.TypingEvent
import net.tasuwo.mitochat.model.json.events.WaitingEvent
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.io.FileReader
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Matchers.hasKey
import org.junit.Assert.fail

object JsonParserSpec : Spek({
   describe("JSON 文字列のパース") {
       on("JSON 文字列を Kotlin オブジェクトにパースする") {
           it("Scenario オブジェクトにパースできる") {
               val scenario = JsonParser().parse(
                   FileReader(getFile(JsonParser::class, "parse_scenario.json")).readText()
               )

               assertThat(scenario, `is`(instanceOf(Scenario::class.java)))
               assertThat(scenario.scenario, hasKey("0"))
               assertThat(scenario.scenario["0"], `is`(instanceOf(Events::class.java)))
               assertThat((scenario.scenario["0"] as Events).events, `is`(emptyList()))
           }

           it("Provide Button イベントをパースできる") {
               val scenario = JsonParser().parse(
                   FileReader(getFile(JsonParser::class, "parse_provide_button.json")).readText()
               )

               val event = (scenario.scenario["0"] as Events).events[0]
               assertThat(event, `is`(instanceOf(ProvideButtonEvent::class.java)))
               val provideButtonEvent = event as ProvideButtonEvent
               assertThat(provideButtonEvent.text, `is`("text"))
               assertThat(provideButtonEvent.nextChatId, `is`(10))
           }

           it("Reset Buttons イベントをパースできる") {
               val scenario = JsonParser().parse(
                   FileReader(getFile(JsonParser::class, "parse_reset_buttons.json")).readText()
               )

               val event = (scenario.scenario["0"] as Events).events[0]
               assertThat(event, `is`(instanceOf(ResetButtonsEvent::class.java)))
           }

           it("Talk イベントをパースできる") {
               val scenario = JsonParser().parse(
                   FileReader(getFile(JsonParser::class, "parse_talk.json")).readText()
               )

               val event = (scenario.scenario["0"] as Events).events[0]
               assertThat(event, `is`(instanceOf(TalkEvent::class.java)))
               val talkEvent = event as TalkEvent
               assertThat(talkEvent.icon, `is`("icon"))
               assertThat(talkEvent.text, `is`("text"))
               assertThat(talkEvent.right, `is`(true))
           }

           it("Typing イベントをパースできる") {
               val scenario = JsonParser().parse(
                   FileReader(getFile(JsonParser::class, "parse_typing.json")).readText()
               )

               val event = (scenario.scenario["0"] as Events).events[0]
               assertThat(event, `is`(instanceOf(TypingEvent::class.java)))
               val typingEvent = event as TypingEvent
               assertThat(typingEvent.time, `is`(10.0))
           }

           it("Waiting イベントをパースできる") {
               val scenario = JsonParser().parse(
                   FileReader(getFile(JsonParser::class, "parse_waiting.json")).readText()
               )

               val event = (scenario.scenario["0"] as Events).events[0]
               assertThat(event, `is`(instanceOf(WaitingEvent::class.java)))
               val waitingEvent = event as WaitingEvent
               assertThat(waitingEvent.time, `is`(10.0))
           }
       }

       on("不正な文字列ならエラーとする") {
           it("JSON 文字列でない") {
               val text = FileReader(getFile(JsonParser::class, "no_json.json")).readText()
               try {
                   JsonParser().parse(text)

                   fail("")
               } catch (e: RuntimeException) {
                   assertThat(e.message, `is`(containsString(text)))
               }
           }

           it("不正な Event Type") {
               val text = FileReader(getFile(JsonParser::class, "invalid_event_type.json")).readText()
               try {
                   JsonParser().parse(text)

                   fail("")
               } catch (e: RuntimeException) {
                   assertThat(e.message, `is`(containsString(text)))
                   assertThat(e.message, `is`(containsString("INVALID")))
                   assertThat(e.message, `is`(containsString("Event Type")))
               }
           }

           it("キー Event Type が含まれていない") {
               val text = FileReader(getFile(JsonParser::class, "no_event_type.json")).readText()
               try {
                   JsonParser().parse(text)

                   fail("")
               } catch (e: RuntimeException) {
                   assertThat(e.message, `is`(containsString(text)))
                   assertThat(e.message, `is`(containsString("Event Type")))
               }
           }
       }
   }
})