package com.idragonpro.andmagnus.helpers

import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSource.Factory
import com.google.android.exoplayer2.upstream.DataSourceInputStream
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.TransferListener
import java.io.IOException
import javax.crypto.Cipher
import javax.crypto.CipherInputStream

class DecryptingDataSource(
    private val upstream: DataSource,
    private val cipher: Cipher
) : DataSource {

    private var dataSpec: DataSpec? = null
    private var cipherInputStream: CipherInputStream? = null
    private var bytesRemaining: Long = 0
    private var transferListener: TransferListener? = null

    @Throws(IOException::class)
    override fun open(dataSpec: DataSpec): Long {
        this.dataSpec = dataSpec
        bytesRemaining = upstream.open(dataSpec)

        val inputStream = DataSourceInputStream(upstream, dataSpec).apply {
            open()
        }
        cipherInputStream = CipherInputStream(inputStream, cipher)

        transferListener?.onTransferStart(this, dataSpec, false)
        return bytesRemaining
    }

    @Throws(IOException::class)
    override fun read(buffer: ByteArray, offset: Int, readLength: Int): Int {
        if (bytesRemaining == 0L) {
            return -1
        } else {
            val bytesRead = cipherInputStream?.read(buffer, offset, readLength) ?: -1
            if (bytesRead > 0) {
                bytesRemaining -= bytesRead.toLong()
                transferListener?.onBytesTransferred(this, dataSpec!!, false, bytesRead)
            }
            return bytesRead
        }
    }

    @Throws(IOException::class)
    override fun close() {
        try {
            cipherInputStream?.close()
        } finally {
            cipherInputStream = null
            upstream.close()
            transferListener?.onTransferEnd(this, dataSpec!!, false)
        }
    }

    override fun getUri() = dataSpec?.uri

    override fun addTransferListener(transferListener: TransferListener) {
        this.transferListener = transferListener
        upstream.addTransferListener(transferListener)
    }
}

class DecryptingDataSourceFactory(
    private val upstreamFactory: Factory,
    private val cipher: Cipher
) : Factory {

    override fun createDataSource(): DataSource {
        return DecryptingDataSource(upstreamFactory.createDataSource(), cipher)
    }
}