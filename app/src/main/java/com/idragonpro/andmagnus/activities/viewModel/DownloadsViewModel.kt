package com.idragonpro.andmagnus.activities.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.await
import androidx.work.workDataOf
import com.idragonpro.andmagnus.database.AppDatabase
import com.idragonpro.andmagnus.database.Download
import com.idragonpro.andmagnus.database.DownloadProgress
import com.idragonpro.andmagnus.database.DownloadProgressRepo
import com.idragonpro.andmagnus.database.DownloadsRepository
import com.idragonpro.andmagnus.work.DeleteWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DownloadsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DownloadsRepository
    private val progressRepo: DownloadProgressRepo
    val allProgress: LiveData<List<DownloadProgress>>
    val loadState: MutableLiveData<WorkInfo.State?> = MutableLiveData(WorkInfo.State.SUCCEEDED)

    private val _downloadedList = MutableStateFlow<List<Download>>(emptyList())
    val downloadedList: StateFlow<List<Download>> = _downloadedList

    init {
        val downloadsDao = AppDatabase.getDatabase(application).downloadsDao()
        val downloadProgress = AppDatabase.getDatabase(application).downloadProgressDao()
        repository = DownloadsRepository(downloadsDao)
        progressRepo = DownloadProgressRepo(downloadProgress)
        allProgress = progressRepo.allDownloads

        viewModelScope.launch {
            repository.allDownloads.collect { examples ->
                _downloadedList.value = examples
            }
        }
    }


    fun getDownloadedVideoId(videoId: String) = liveData(Dispatchers.IO) {
        val download = repository.getDownloadVideoId(videoId = videoId)
        emit(download)
    }

    fun insert(word: Download) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(word)
    }

    fun update(word: Download) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(word)
    }

    fun delete(word: Download) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(word)
    }

    fun startDelete(id: Long, context: Context) {
        val workTag = "tag_$id"
        val workManager = WorkManager.getInstance(context.applicationContext!!)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Use withContext to switch to a background thread for the blocking call
                val workInfos = withContext(Dispatchers.IO) {
                    workManager.getWorkInfosByTag(workTag).await()
                }

                val state = workInfos.firstOrNull()?.state

                if (state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED) {
                    return@launch
                }

                val workData = workDataOf(
                    DeleteWorker.fileIdKey to id
                )

                val workRequest =
                    OneTimeWorkRequestBuilder<DeleteWorker>().addTag(workTag).setInputData(workData)
                        .build()

                workManager.enqueueUniqueWork(
                    workTag, ExistingWorkPolicy.KEEP, workRequest
                )

            } catch (e: Exception) {
                // Handle exceptions
            }
        }
    }
}