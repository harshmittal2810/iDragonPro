package com.idragonpro.andmagnus.utils

import com.google.android.exoplayer2.upstream.DataSource

class EncryptedDataSourceFactory : DataSource.Factory {

    override fun createDataSource(): DataSource =
        FileCipherEncryptedDataSource()
}