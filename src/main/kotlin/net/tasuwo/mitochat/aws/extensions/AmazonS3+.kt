package net.tasuwo.mitochat.aws.extensions

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GetObjectRequest
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.Path

fun AmazonS3.download(bucketName: String, objectKey: String, filePath: Path) {
    val obj = this.getObject(GetObjectRequest(bucketName, objectKey))
    val inputStream: InputStream = obj.objectContent

    val saveFile = File(filePath.toUri())

    inputStream.use { input ->
        val fileOS = FileOutputStream(saveFile)
        fileOS.use { fileOut ->
            input.copyTo(fileOut)
        }
    }
}
