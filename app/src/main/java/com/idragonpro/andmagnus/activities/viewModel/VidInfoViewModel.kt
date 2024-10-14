package com.idragonpro.andmagnus.activities.viewModel

import android.app.Activity
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.idragonpro.andmagnus.MyApp
import com.idragonpro.andmagnus.beans.Movies
import com.idragonpro.andmagnus.database.AppDatabase
import com.idragonpro.andmagnus.database.DownloadProgress
import com.idragonpro.andmagnus.database.DownloadProgressRepo
import com.idragonpro.andmagnus.helpers.SaveSharedPreference
import com.idragonpro.andmagnus.models.VidInfoItem
import com.idragonpro.andmagnus.utils.Utils
import com.idragonpro.andmagnus.work.DownloadWorker
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.mapper.VideoInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VidInfoViewModel(val context: Application) : AndroidViewModel(context) {
    val vidFormats: MutableLiveData<VideoInfo?> = MutableLiveData()
    val loadState: MutableLiveData<LoadState> = MutableLiveData(LoadState.INITIAL)
    val thumbnail: MutableLiveData<String> = MutableLiveData()
    private val progressRepo: DownloadProgressRepo

    init {
        val downloadProgress = AppDatabase.getDatabase(context).downloadProgressDao()
        progressRepo = DownloadProgressRepo(downloadProgress)
    }

    fun insert(word: DownloadProgress) = viewModelScope.launch(Dispatchers.IO) {
        progressRepo.insert(word)
    }

    fun update(word: DownloadProgress) = viewModelScope.launch(Dispatchers.IO) {
        progressRepo.update(word)
    }

    fun delete(word: DownloadProgress) = viewModelScope.launch(Dispatchers.IO) {
        progressRepo.delete(word)
    }

    fun deleteWithId(videoId: String) = viewModelScope.launch(Dispatchers.IO) {
        progressRepo.deleteWithId(videoId)
    }


    lateinit var selectedItem: VidInfoItem.VidFormatItem
    private lateinit var movieDetails: Movies

    private fun submit(vidInfoItems: VideoInfo?) {
        vidFormats.postValue(vidInfoItems)
    }

    private fun updateLoading(loadState: LoadState) {
        this.loadState.postValue(loadState)
    }

    private fun updateThumbnail(thumbnail: String?) {
        this.thumbnail.postValue(thumbnail)
    }

    var vidInfo: VideoInfo? = null

    private fun fetchInfo(url: String) {
        viewModelScope.launch {
            updateLoading(LoadState.LOADING)
            Log.d(MyApp.TAG, "fetchInfo: In Progress")
            try {
                withContext(Dispatchers.IO) {
                    val downloadUrl = Utils.checkForPlaylist(url)
                    vidInfo = YoutubeDL.getInstance().getInfo(downloadUrl)
                    Log.d(MyApp.TAG, "fetchInfo: $vidInfo")
                    updateLoading(LoadState.LOADED)
                    if (vidInfo != null) {
                        updateThumbnail(vidInfo!!.thumbnail)
                        submit(vidInfo)
                    }
                }
            } catch (e: Exception) {
                updateLoading(LoadState.FAILED)
                Log.d(
                    MyApp.TAG,
                    "fetchInfo: Failed " + e.message + "  " + e.cause + "  " + e.localizedMessage
                )
                return@launch
            }
        }
    }


    fun startDownload(
        vidFormatItem: VidInfoItem.VidFormatItem,
        downloadDir: String,
        activity: Activity,
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            val vidInfo = vidFormatItem.vidInfo
            val vidFormat = vidFormatItem.vidFormat
            val workTag = vidInfo.id
            val workManager = WorkManager.getInstance(activity.applicationContext!!)
            val state =
                workTag?.let { workManager.getWorkInfosByTag(it).get()?.getOrNull(0)?.state }
            val running =
                state === WorkInfo.State.RUNNING || state === WorkInfo.State.ENQUEUED || ((progressRepo.getById(
                    movieId = movieDetails.getsVedioId()
                ) != null))

            if (running) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        activity, "Download Already Running", Toast.LENGTH_LONG
                    ).show()
                }
                return@launch
            }

            insert(
                DownloadProgress(
                    vidFormat.url!!,
                    vidInfo.id!!,
                    movieDetails.getsVedioId(),
                    vidInfo.title!!,
                    0,
                    vidInfo.fileSizeApproximate,
                    "Download waiting.....",
                    SaveSharedPreference.getMobileNumber(activity)
                )
            )

            val workData = workDataOf(
                DownloadWorker.urlKey to vidInfo.webpageUrl,
                DownloadWorker.nameKey to vidInfo.title,
                DownloadWorker.ext to vidInfo.ext,
                DownloadWorker.formatIdKey to vidFormat.formatId,
                DownloadWorker.acodecKey to vidFormat.acodec,
                DownloadWorker.vcodecKey to vidFormat.vcodec,
                DownloadWorker.downloadDirKey to downloadDir,
                DownloadWorker.sizeKey to vidFormat.fileSizeApproximate,
                DownloadWorker.taskIdKey to vidInfo.id,
                DownloadWorker.videoId to movieDetails.getsVedioId(),
                DownloadWorker.duration to vidInfo.duration,
                DownloadWorker.mobileNumber to SaveSharedPreference.getMobileNumber(context),
                DownloadWorker.thumbnail to (vidInfo.thumbnail ?: movieDetails.getsSmallBanner())
            )

            println("WorkManager:: $workData")

            val workRequest = workTag.let {
                OneTimeWorkRequestBuilder<DownloadWorker>()
                    .addTag(it!!)
                    .setInputData(workData)
                    .build()
            }

            workManager.enqueueUniqueWork(
                workTag!!, ExistingWorkPolicy.KEEP, workRequest
            )
        }
    }

    fun updateMovieData(sMovie: Movies?) {
        if (sMovie != null) {
            movieDetails = sMovie
            fetchInfo(movieDetails.downloadVideoUrl)
        }
    }
}

enum class LoadState {
    INITIAL, LOADING, LOADED, FAILED
}
