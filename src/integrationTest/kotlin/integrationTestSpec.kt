import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper
import io.restassured.RestAssured
import io.restassured.RestAssured.`when`
import io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema
import net.tasuwo.mitochat.model.ErrorResponse
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.lang.reflect.Type

object IntegrationTestSpec : Spek({
    RestAssured.baseURI = "http://localhost"
    RestAssured.port = 3000

    fun pojo2JsonSchema(c: Type) : String {
        val mapper = ObjectMapper()
        val visitor = SchemaFactoryWrapper()
        mapper.acceptJsonFormatVisitor(mapper.constructType(c), visitor)
        val jsonSchema = visitor.finalSchema()
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema)
    }

    describe("/events/{id}") {
        on("チャットイベント群を取得する") {
            it("ID1の会話イベント群を取得する") {
                `when`().get("/events/1")
                    .then().statusCode(200)
            }
            it("ID2の会話イベント群を取得する") {
                `when`().get("/events/2")
                    .then().statusCode(200)
            }
        }
        on("存在しないチャットイベント群を取得する") {
            it("404 エラーが返ってくる") {
                `when`().get("/events/0")
                    .then().statusCode(404)
                    .body(matchesJsonSchema(pojo2JsonSchema(ErrorResponse::class.java)))
            }
        }
    }
})
