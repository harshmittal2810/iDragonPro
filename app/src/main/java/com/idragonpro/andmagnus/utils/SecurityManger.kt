package com.idragonpro.andmagnus.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.idragonpro.andmagnus.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class SecurityManger {

    fun generateKey(): ByteArray {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128)
        return keyGenerator.generateKey().encoded
    }

    fun aesCipherInStreamToOutStream(
        context: Context,
        password: String? = BuildConfig.KEYPASSWORD,
        fileUri: Uri,
        file: File?
    ): Uri? {
        // Generate secret from password by calling the keyGen function
        try {
            val key = keyGen(password)
            val c = Cipher.getInstance(BuildConfig.TRANSFORMATION)
            val iv = ByteArray(c.blockSize)
            val ivParams = IvParameterSpec(iv)
            c.init(Cipher.ENCRYPT_MODE, key, ivParams)

            // Crypt and write directly in to file
            val inStream = context.contentResolver.openInputStream(fileUri)
            CipherOutputStream(FileOutputStream(file), c).use { outputStream ->
                inStream!!.copyTo(outputStream)
                inStream.close()
                outputStream.close()
            }
            return if (file != null) {
                File(fileUri.toString()).deleteRecursively()
                FileProvider.getUriForFile(
                    context,
                    context.applicationContext.packageName.toString() + ".provider",
                    file
                )
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun aesDecipherInStreamToOutStream(
        context: Context,
        password: String? = BuildConfig.KEYPASSWORD,
        fileInUri: Uri,
        file: File?,
    ): Uri? {
        // Generate secret from password by calling the keyGen function
        try {

            val key = keyGen(password)
            val c = Cipher.getInstance(BuildConfig.TRANSFORMATION)
            val iv = ByteArray(c.blockSize)
            val ivParams = IvParameterSpec(iv)
            c.init(Cipher.DECRYPT_MODE, key, ivParams)

            val outputStream = FileOutputStream(file)
            // Decrypt and write directly in to file
            CipherInputStream(
                context.contentResolver.openInputStream(fileInUri),
                c
            ).use { inputStream ->
                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()
            }

            return if (file != null) {
                FileProvider.getUriForFile(
                    context,
                    context.applicationContext.packageName.toString() + ".provider",
                    file
                )
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun keyGen(password: String? = BuildConfig.KEYPASSWORD): SecretKey {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = password!!.toByteArray()
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()

        return SecretKeySpec(key, BuildConfig.ALGORITHM)
    }
}