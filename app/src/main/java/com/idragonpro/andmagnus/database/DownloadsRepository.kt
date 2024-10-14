package com.idragonpro.andmagnus.database

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class DownloadsRepository(private val downloadsDao: DownloadsDao) {

    val allDownloads: Flow<List<Download>> = downloadsDao.getAllDownloads()

    fun getDownloadVideoId(videoId: String): Download {
        return downloadsDao.getByVideoId(videoId)
    }

    suspend fun insert(download: Download) {
        downloadsDao.insert(download)
    }

    suspend fun update(download: Download) {
        downloadsDao.update(download)
    }

    suspend fun delete(download: Download) {
        downloadsDao.delete(download)
    }

}
