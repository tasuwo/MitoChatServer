package net.tasuwo.mitochat.aws

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import net.tasuwo.mitochat.model.EnvironmentVariable

object DynamoDBClient {
    fun get(): AmazonDynamoDB {
        val endpoint = EnvironmentVariable.dynamoDBEndpoint

        if (endpoint.isEmpty())
            return AmazonDynamoDBClientBuilder.defaultClient()

        return AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(endpoint, Regions.AP_NORTHEAST_2.name)
            )
            .build()
    }
}