package net.tasuwo.mitochat.model

object EnvironmentVariable {
    val dynamoDBEndpoint: String = System.getenv("DYNAMO_DB_ENDPOINT")
    val s3BucketEndPoint: String = System.getenv("S3_BUCKET_ENDPOINT")
    val s3BucketName: String = System.getenv("S3_BUCKET_NAME")
}
