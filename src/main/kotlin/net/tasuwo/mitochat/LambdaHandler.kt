package net.tasuwo.mitochat

import com.amazonaws.serverless.proxy.jersey.JerseyLambdaContainerHandler
import com.amazonaws.serverless.proxy.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.model.AwsProxyResponse
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import org.apache.log4j.Logger
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.server.ResourceConfig

@Suppress("unused")
class LambdaHandler : RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    private val jerseyApplication = ResourceConfig()
        .packages("net.tasuwo.mitochat.resource")
        .register(JacksonFeature::class.java)
    private val handler = JerseyLambdaContainerHandler.getAwsProxyHandler(jerseyApplication)
    @Suppress("unused")
    private val logger = Logger.getLogger(LambdaHandler::class.java)

    override fun handleRequest(awsProxyRequest: AwsProxyRequest, context: Context): AwsProxyResponse {
        val response = handler.proxy(awsProxyRequest, context)

        // CORS 対応
        val headers: MutableMap<String, String> = response.headers
        headers["Access-Control-Allow-Origin"] = "*"
        response.headers = headers

        return response
    }
}