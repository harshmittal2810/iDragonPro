package com.idragonpro.andmagnus.utils

import com.idragonpro.andmagnus.BuildConfig
import java.io.FileInputStream
import java.io.IOException
import java.math.BigInteger
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.spec.IvParameterSpec

class FileCipherInputStream(
    private val upstream: FileInputStream,
    private var cipher: Cipher,
    private val ivParameterSpec: IvParameterSpec? = null,
) : CipherInputStream(upstream, cipher) {

    @Throws(IOException::class)
    override fun read(b: ByteArray?, off: Int, len: Int): Int {
        return super.read(b, off, len)
    }

    // in case of ECB mode, use this method
    fun forceSkip(bytesToSkip: Long): Long {
        val skipOverFlow = bytesToSkip % cipher.blockSize
        val skipBlockPosition = bytesToSkip - skipOverFlow
        try {
            if (skipBlockPosition <= 0) {
                TunedCipherInputStream(this, cipher).initCipher()
//                initCipher()
                return 0L
            }
            var upstreamSkipped = upstream.skip(skipBlockPosition)
            while (upstreamSkipped < skipBlockPosition) {
                upstream.read()
                upstreamSkipped++
            }
            val cipherBlockArray = ByteArray(cipher.blockSize)
            upstream.read(cipherBlockArray)
            TunedCipherInputStream(this, cipher).initCipher()
//            initCipher()
            val cipherSkipped = skip(skipBlockPosition)
            val negligibleBytes = ByteArray(skipOverFlow.toInt())
            read(negligibleBytes)
            return cipherSkipped
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
    }

    // in case of other modes with IV, use this method
    fun forceIvSkip(bytesToSkip: Long): Long {
        val skipped: Long = upstream.skip(bytesToSkip)
        try {
            val skipOverFlow = (bytesToSkip % cipher.blockSize).toInt()
            val skipBlockPosition = bytesToSkip - skipOverFlow
            val blocksNumber = skipBlockPosition / cipher.blockSize
            val ivOffset = BigInteger(1, ivParameterSpec!!.iv).add(
                BigInteger.valueOf(blocksNumber)
            )
            val ivOffsetBytes = ivOffset.toByteArray()
            val skippedIvSpec = if (ivOffsetBytes.size < cipher.blockSize) {
                val resizedIvOffsetBytes = ByteArray(cipher.blockSize)
                System.arraycopy(
                    ivOffsetBytes,
                    0,
                    resizedIvOffsetBytes,
                    cipher.blockSize - ivOffsetBytes.size,
                    ivOffsetBytes.size
                )
                IvParameterSpec(resizedIvOffsetBytes)
            } else {
                IvParameterSpec(
                    ivOffsetBytes,
                    ivOffsetBytes.size - cipher.blockSize,
                    cipher.blockSize
                )
            }

            cipher.init(
                Cipher.ENCRYPT_MODE,
                SecurityManger().keyGen(BuildConfig.KEYPASSWORD),
                skippedIvSpec
            )
            val skipBuffer = ByteArray(skipOverFlow)
            cipher.update(skipBuffer, 0, skipOverFlow, skipBuffer)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return 0
        }
        return skipped
    }


    @Throws(IOException::class)
    override fun available(): Int {
        return upstream.available()
    }
}