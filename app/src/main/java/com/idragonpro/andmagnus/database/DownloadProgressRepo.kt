package com.idragonpro.andmagnus.database

import androidx.lifecycle.LiveData

class DownloadProgressRepo(private val downloadsDao: DownloadProgressDao) {

    val allDownloads: LiveData<List<DownloadProgress>> = downloadsDao.getAllDownloads()

    suspend fun insert(download: DownloadProgress) {
        downloadsDao.insert(download)
    }

    suspend fun update(download: DownloadProgress) {
        downloadsDao.update(download)
    }

    fun getById(movieId: String): DownloadProgress? {
        return downloadsDao.getWithId(movieId)
    }


    suspend fun delete(download: DownloadProgress) {
        downloadsDao.delete(download)
    }

    suspend fun deleteWithId(videoId: String) {
        downloadsDao.deleteWithId(videoId)
    }

}