package com.idragonpro.andmagnus.work

import android.content.Context
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.idragonpro.andmagnus.database.AppDatabase
import com.idragonpro.andmagnus.database.DownloadsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DeleteWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val fileId = inputData.getLong(fileIdKey, 0)

        val downloadsDao = AppDatabase.getDatabase(applicationContext).downloadsDao()
        val repository = DownloadsRepository(downloadsDao)
        val download = downloadsDao.getById(fileId)

        val fileName = download.name
        val fileUri = download.downloadedPath

        repository.delete(download)

        val file = File(fileUri)
        if (file.exists()) {
            file.delete()
            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "Deleted $fileName", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return Result.success()
    }

    companion object {
        const val fileIdKey = "id"
    }

}