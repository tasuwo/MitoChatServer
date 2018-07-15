package net.tasuwo.mitochat.resource

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import io.swagger.annotations.Extension
import io.swagger.annotations.ExtensionProperty
import io.swagger.annotations.ResponseHeader
import net.tasuwo.mitochat.aws.DynamoDBClient
import net.tasuwo.mitochat.model.EnvironmentVariable
import net.tasuwo.mitochat.aws.S3Client
import net.tasuwo.mitochat.aws.extensions.download
import net.tasuwo.mitochat.aws.extensions.retrieveChatDataPrefix
import net.tasuwo.mitochat.model.JsonParser
import net.tasuwo.mitochat.model.json.ErrorResponse
import org.apache.log4j.Logger
import java.io.FileReader
import java.nio.file.Paths
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.OPTIONS
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces

@Path("/chat")
@Api(value = "会話中に発生するイベント")
class ChatResource {
    private val logger = Logger.getLogger(ChatResource::class.java)

    private inline fun catchAll(logger: Logger, callback: () -> Response): Response {
        return try {
            callback()
        } catch (e: Exception) {
            logger.error(e.localizedMessage, e)

            return Response
                .status(500)
                .entity(ErrorResponse("サーバ内部のエラーです : ${e.localizedMessage}"))
                .build()
        }
    }

    @GET
    @Path("{chat_id}")
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
        @ApiParam(value = "チャットのID", required = true) @PathParam("chat_id") chat_id: Int
    ): Response {
        return catchAll(logger) {
            val s3Client = S3Client.get()
            val dbClient = DynamoDBClient.get()
            val db = DynamoDB(dbClient)
            val tmpJsonPath = Paths.get("/tmp/tmp.json")

            val prefix = db.retrieveChatDataPrefix(chat_id, 0)
            s3Client.download(EnvironmentVariable.s3BucketName, prefix, tmpJsonPath)

            return Response
                .status(200)
                .entity(JsonParser().parse(FileReader(tmpJsonPath.toFile()).readText()))
                .build()
        }
    }

    @OPTIONS
    @Path("{chat_id}")
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
