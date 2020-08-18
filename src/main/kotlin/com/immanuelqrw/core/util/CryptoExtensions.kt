package com.immanuelqrw.core.util

import java.io.File
import java.security.MessageDigest
import java.util.Base64

/**
 * Creates SHA256 hash of the given text.
 *
 * @param textToHash Text input
 * @return SHA 256 hash
 */
fun hashWith256(textToHash: String): String = hashWith256(textToHash.toByteArray(Charsets.UTF_8))

/**
 * Creates SHA256 hash of the given file.
 *
 * @param bytes File input
 * @return SHA 256 hash
 */
fun hashWith256(fileToHash: File): String = hashWith256(fileToHash.readBytes())

/**
 * Creates SHA256 hash of the given byte array.
 *
 * @param bytes Byte Array input
 * @return SHA 256 hash
 */
fun hashWith256(bytes: ByteArray): String {
    val hashedArray = MessageDigest
        .getInstance("SHA-256")
        .digest(bytes)
    return Base64.getEncoder().encodeToString(hashedArray)
}


//fun String.hashWith256(): String = hashWith256(this)
//fun File.hashWith256(): String = hashWith256(this)
//fun ByteArray.hashWith256(): String = hashWith256(this)
