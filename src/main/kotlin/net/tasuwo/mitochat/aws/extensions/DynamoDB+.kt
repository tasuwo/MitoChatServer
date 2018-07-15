package net.tasuwo.mitochat.aws.extensions

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.PrimaryKey

fun DynamoDB.retrieveChatDataPrefix(chatId: Int, version: Int) : String {
    return this
        .getTable("Chat")
        .getItem(PrimaryKey("Id", chatId, "Version", version))["Source"] as String
}
