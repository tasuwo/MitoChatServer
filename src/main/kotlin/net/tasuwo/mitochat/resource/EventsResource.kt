package net.tasuwo.mitochat.resource

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import io.swagger.annotations.Extension
import io.swagger.annotations.ExtensionProperty
import io.swagger.annotations.ResponseHeader
import net.tasuwo.mitochat.model.ErrorResponse
import net.tasuwo.mitochat.model.events.Events
import net.tasuwo.mitochat.model.events.ProvideButtonEvent
import net.tasuwo.mitochat.model.events.ResetButtonsEvent
import net.tasuwo.mitochat.model.events.TalkEvent
import net.tasuwo.mitochat.model.events.TypingEvent
import net.tasuwo.mitochat.model.events.WaitingEvent
import org.apache.log4j.Logger
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.OPTIONS
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces

@Path("/events")
@Api(value = "会話中に発生するイベント")
class EventsResource {
    @Suppress("unused")
    private val logger = Logger.getLogger(EventsResource::class.java)

    @GET
    @Path("{key}")
    @ApiOperation(
        value = "イベント群を取得する",
        notes = "会話を進めるイベント群を取得する",
        extensions = arrayOf(
            Extension(
                name = "x-amazon-apigateway-integration",
                properties = arrayOf(
                    // Swagger 拡張を利用する場合は、必ず POST を指定する必要がある
                    ExtensionProperty(name = "httpMethod", value = "POST"),
                    ExtensionProperty(name = "type", value = "aws_proxy")
                )
            )
        )
    )
    @ApiResponses(
        ApiResponse(
            code = 200,
            message = "成功",
            responseHeaders = arrayOf(
                ResponseHeader(name = "Access-Control-Allow-Headers", response = String::class),
                ResponseHeader(name = "Access-Control-Allow-Methods", response = String::class),
                ResponseHeader(name = "Access-Control-Allow-Origin", response = String::class)
            )
        )
    )
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.WILDCARD)
    fun retrieveEvents(
        @ApiParam(value = "イベント群のID", required = true) @PathParam("key") key: String
    ): Response {
        return when (key) {
            "1" -> {
                val events = Events(listOf(
                    WaitingEvent(2.0),
                    TypingEvent(2.0),
                    TalkEvent("default", "起立！気をつけ！", false),
                    WaitingEvent(1.0),
                    TypingEvent(4.0),
                    TalkEvent("default", "こんにちは〜〜〜！月ノ美兎です〜〜〜！", false),
                    WaitingEvent(1.0),
                    TypingEvent(2.0),
                    TalkEvent("default", "見えてるかな？？", false),
                    WaitingEvent(1.5),
                    ProvideButtonEvent("おるやんけ！！", 2),
                    ProvideButtonEvent("みえてます！！", 3)
                ))
                Response
                    .status(200)
                    .entity(events)
                    .build()
            }
            "2" -> {
                val events = Events(listOf(
                    ResetButtonsEvent(),
                    TalkEvent("default", "おるやんけ！！", true),
                    WaitingEvent(3.0),
                    TypingEvent(3.0),
                    WaitingEvent(0.2),
                    TalkEvent("default", "は〜い、おりますよ〜w", false)
                ))
                Response
                    .status(200)
                    .entity(events)
                    .build()
            }
            else -> {
                Response
                    .status(404)
                    .entity(ErrorResponse("存在しないイベント ID です"))
                    .build()
            }
        }
    }

    @OPTIONS
    @Path("{key}")
    @ApiOperation(
        value = "CORS サポート",
        notes = "正しいヘッダーを返すことで CORS を有効にする",
        tags = arrayOf("CORS"),
        extensions = arrayOf(
            Extension(
                name = "x-amazon-apigateway-integration",
                properties = arrayOf(
                    ExtensionProperty(name = "type", value = "mock")
                )
            )
        )
    )
    @ApiResponses(
        ApiResponse(
            code = 200,
            message = "成功",
            responseHeaders = arrayOf(
                ResponseHeader(name = "Access-Control-Allow-Headers", response = String::class),
                ResponseHeader(name = "Access-Control-Allow-Methods", response = String::class),
                ResponseHeader(name = "Access-Control-Allow-Origin", response = String::class)
            )
        )
    )
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun forCORS(): Response {
        return Response
            .status(200)
            .build()
    }
}
