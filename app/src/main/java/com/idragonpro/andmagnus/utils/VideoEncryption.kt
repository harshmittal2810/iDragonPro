package com.idragonpro.andmagnus.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.File
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


object VideoEncryption {

    const val GCM_IV_LENGTH = 12 // GCM recommended IV length
    const val GCM_TAG_LENGTH = 16 // Authentication tag length
    const val AES_TRANSFORMATION = "AES/GCM/NoPadding" // Authentication tag length

    fun encryptFile(secretKey: SecretKey, inputFile: File, outputFile: File) {
        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        val iv = ByteArray(GCM_IV_LENGTH)
        SecureRandom().nextBytes(iv)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH * 8, iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec)
        outputFile.outputStream().use { fileOut ->
            fileOut.write(iv)
            CipherOutputStream(fileOut, cipher).use { cipherOut ->
                inputFile.inputStream().use { it.copyTo(cipherOut) }
            }
        }
    }

    fun generateAndStoreKey(alias: String): SecretKey {
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
        )
        return keyGenerator.generateKey()
    }

}