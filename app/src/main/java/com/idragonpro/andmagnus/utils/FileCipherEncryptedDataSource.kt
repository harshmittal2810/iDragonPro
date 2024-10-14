package com.idragonpro.andmagnus.utils

import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.TransferListener
import com.idragonpro.andmagnus.BuildConfig
import java.io.EOFException
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import kotlin.math.min

class FileCipherEncryptedDataSource : DataSource {

    private var encryptedFileStream: FileInputStream? = null
    private var cipherInputStream: FileCipherInputStream? = null
    private var bytesToRead: Long = 0
    private var bytesRead: Int = 0
    private var isOpen = false
    private var dataSpec: DataSpec? = null

    @Throws(IOException::class)
    override fun open(dataSpec: DataSpec): Long {
        this.dataSpec = dataSpec
        if (isOpen) return bytesToRead
        try {
            setupCipherInputStream()
            cipherInputStream?.forceSkip(dataSpec.position)

            if (dataSpec.length != C.LENGTH_UNSET.toLong()) {
                bytesToRead = dataSpec.length
                return bytesToRead
            }
            if (bytesToRead == Int.MAX_VALUE.toLong()) {
                bytesToRead = C.LENGTH_UNSET.toLong()
                return bytesToRead
            }
            bytesToRead = cipherInputStream!!.available().toLong()
        } catch (e: IOException) {
            throw IOException(e)
        }
        isOpen = true
        return bytesToRead
    }

    private fun setupCipherInputStream() {
        val path = uri?.path
            ?: throw RuntimeException("Path can't be empty!")
        encryptedFileStream = File(path).inputStream()

        val encryptionCipher =
            Cipher.getInstance(BuildConfig.TRANSFORMATION)
        val iv = ByteArray(encryptionCipher.blockSize)
        val ivParams = IvParameterSpec(iv)

        val keySpec = SecurityManger().keyGen(BuildConfig.KEYPASSWORD)
        encryptionCipher.init(
            Cipher.DECRYPT_MODE,
            keySpec,
            ivParams
        )

        cipherInputStream = FileCipherInputStream(
            encryptedFileStream!!,
            encryptionCipher,
            ivParams, // if you are using ECB, remove this line.
        )
    }

    @Throws(IOException::class)
    override fun read(buffer: ByteArray, offset: Int, readLength: Int): Int {
        if (bytesToRead == 0L) {
            return C.RESULT_END_OF_INPUT
        }

        val mBytesToRead = getBytesToRead(readLength)

        bytesRead = try {
            cipherInputStream!!.read(buffer, offset, mBytesToRead)
        } catch (e: IOException) {
            throw IOException(e)
        }

        if (bytesRead < 0) {
            if (bytesToRead != C.LENGTH_UNSET.toLong())
                throw IOException(EOFException())
            return C.RESULT_END_OF_INPUT
        }

        if (bytesToRead != C.LENGTH_UNSET.toLong())
            bytesToRead -= bytesRead
        return bytesRead
    }

    private fun getBytesToRead(mBytesToRead: Int): Int {
        if (bytesToRead == C.LENGTH_UNSET.toLong()) {
            return bytesToRead.toInt()
        }
        return min(bytesToRead.toDouble(), mBytesToRead.toDouble()).toInt()
    }

    override fun addTransferListener(transferListener: TransferListener) {}

    override fun getUri(): Uri? = dataSpec?.uri

    @Throws(IOException::class)
    override fun close() {
        try {
            encryptedFileStream?.close()
            cipherInputStream?.close()
        } catch (e: IOException) {
            throw IOException(e)
        } finally {
            if (isOpen) {
                isOpen = false
            }
        }
    }
}
