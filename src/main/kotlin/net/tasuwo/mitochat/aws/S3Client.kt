package net.tasuwo.mitochat.aws

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import net.tasuwo.mitochat.model.EnvironmentVariable

object S3Client {
    fun get(): AmazonS3 {
        val endpoint = EnvironmentVariable.s3BucketEndPoint

        if (endpoint.isEmpty())
            return AmazonS3ClientBuilder.defaultClient()

        val builder = AmazonS3ClientBuilder.standard()
            .withPathStyleAccessEnabled(true)
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(endpoint, Regions.AP_NORTHEAST_2.name)
            )
        builder.isChunkedEncodingDisabled = true
        return builder.build()
    }
}
