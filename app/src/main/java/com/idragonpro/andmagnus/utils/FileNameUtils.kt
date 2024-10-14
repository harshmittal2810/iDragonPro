package com.idragonpro.andmagnus.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Base64
import com.idragonpro.andmagnus.BuildConfig
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object FileNameUtils {
    fun createFilename(title: String): String {
        val cleanFileName = title.replace("[\\\\><\"|*?'%:#/]".toRegex(), " ")
        var fileName = cleanFileName.trim { it <= ' ' }.replace(" +".toRegex(), " ")
        if (fileName.length > 127) fileName = fileName.substring(0, 127)
        return fileName
    }

    fun getFileSize(filePath: String): Long {
        val file = File(filePath)
        return if (file.exists()) file.length() else 0
    }

    fun String?.asUri(): Uri? {
        try {
            return Uri.parse(this)
        } catch (e: Exception) {
        }
        return null
    }

    val File.uri get() = this.absolutePath.asUri()

    @Throws(Exception::class)
    fun File.getMediaDuration(context: Context): Long {
        if (!exists()) return 0
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        retriever.release()

        return (duration?.toLongOrNull() ?: 0) / 1000
    }

    val Uri?.exists get() = if (this == null) false else asFile().exists()

    fun Uri.asFile(): File = File(toString())


    fun encryptFile(secretKey: SecretKey, inputFile: String, outputFile: String) {
        val cipher = Cipher.getInstance(BuildConfig.TRANSFORMATION)
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)

        FileInputStream(inputFile).use { fis ->
            FileOutputStream(outputFile).use { fos ->
                fos.write(iv) // Write the IV to the beginning of the output file
                CipherOutputStream(fos, cipher).use { cos ->
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while (fis.read(buffer).also { bytesRead = it } != -1) {
                        cos.write(buffer, 0, bytesRead)
                    }
                }
            }
        }
    }

    fun createDecryptCipher(secretKey: SecretKey, inputFile: String): Cipher {
        val iv = ByteArray(16)
        FileInputStream(inputFile).use { fis ->
            fis.read(iv) // Read the IV from the beginning of the file
        }
        val ivSpec = IvParameterSpec(iv)
        val cipher = Cipher.getInstance(BuildConfig.TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
        return cipher
    }


    fun createCipher(secretKey: SecretKey, mode: Int): Cipher {
        val cipher = Cipher.getInstance(BuildConfig.ALGORITHM)
        cipher.init(mode, secretKey)
        return cipher
    }

    fun generateSecretKey(): SecretKey {
        val keyGen = KeyGenerator.getInstance(BuildConfig.ALGORITHM)
        keyGen.init(256) // Use 256-bit AES
        return keyGen.generateKey()
    }

    fun encodeToBase64(input: String): String {
        val encodedBytes = Base64.encode(input.toByteArray(), Base64.DEFAULT)
        return String(encodedBytes)
    }
}