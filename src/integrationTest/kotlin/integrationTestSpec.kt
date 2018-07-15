import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper
import io.restassured.RestAssured
import io.restassured.RestAssured.`when`
import io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema
import net.tasuwo.mitochat.model.json.ErrorResponse
import net.tasuwo.mitochat.model.json.Scenario
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

    describe("/chat/{id}") {
        on("チャットイベント群を取得する") {
            it("ID1の会話イベント群を取得する") {
                `when`().get("/chat/1")
                    .then().statusCode(200)
            }
        }
        on("存在しないチャットイベント群を取得する") {
            it("500 エラーが返ってくる") {
                `when`().get("/chat/0")
                    .then().statusCode(500)
                    .body(matchesJsonSchema(pojo2JsonSchema(ErrorResponse::class.java)))
            }
        }
    }
})
