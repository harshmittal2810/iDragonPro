package com.idragonpro.andmagnus.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.preference.PreferenceManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.idragonpro.andmagnus.BuildConfig
import com.idragonpro.andmagnus.R
import com.idragonpro.andmagnus.database.AppDatabase
import com.idragonpro.andmagnus.database.Download
import com.idragonpro.andmagnus.database.DownloadProgress
import com.idragonpro.andmagnus.database.DownloadsRepository
import com.idragonpro.andmagnus.utils.FileNameUtils
import com.idragonpro.andmagnus.utils.FileNameUtils.getMediaDuration
import com.idragonpro.andmagnus.utils.VideoEncryption
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import okhttp3.internal.closeQuietly
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date


class DownloadWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

    override suspend fun doWork(): Result {
        val videoId = inputData.getString(videoId)!!
        val url = inputData.getString(urlKey)!!
        val name = FileNameUtils.createFilename(inputData.getString(nameKey)!!)
        val extensionName = inputData.getString(ext) ?: ""
        val formatId = inputData.getString(formatIdKey)!!
        val acodec = inputData.getString(acodecKey)
        val vcodec = inputData.getString(vcodecKey)
        val downloadDir = inputData.getString(downloadDirKey)
        val size = inputData.getLong(sizeKey, 0L)
        val taskId = inputData.getString(taskIdKey)!!
        val duration = inputData.getInt(duration, 0)
        val thumbnail = inputData.getString(thumbnail)!!
        val mobileNumber = inputData.getString(mobileNumber)!!

        createNotificationChannel()
        createNotificationChannelForDownloadProgress()
        val notificationId = taskId.hashCode()
        val notification =
            NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(name).setOngoing(true)
                .setContentText(applicationContext.getString(R.string.download_start)).build()

        val foregroundInfo = if (SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                notificationId, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(notificationId, notification)
        }
        setForeground(foregroundInfo)

        val request = YoutubeDLRequest(url)

        val tmpFile = File.createTempFile("iDragon_DL", null, applicationContext.externalCacheDir)
        tmpFile.delete()
        tmpFile.mkdir()
        tmpFile.deleteOnExit()
        request.addOption("-o", "${tmpFile.absolutePath}/${name}.%(ext)s")

        val videoOnly = vcodec != "none" && acodec == "none"
        if (videoOnly) {
            request.addOption("-f", "${formatId}+bestaudio")
        } else {
            request.addOption("-f", formatId)
        }

        var destUri: Uri? = null
        var totalSize = 0L
        var totalDuration = 0L
        var encryptDownloadedPath = ""

        try {
            YoutubeDL.getInstance().execute(request, taskId) { progress, _, line ->
                val index = downloadList.indexOfFirst { it.name == name && it.taskId == taskId }
                if (index == -1) {
                    downloadList.add(
                        DownloadProgress(
                            thumbnail,
                            taskId,
                            videoId,
                            name,
                            progress.toInt(),
                            size,
                            replaceDoubleSpaces(line),
                            mobileNumber,
                        )
                    )
                } else {
                    downloadList[index].progress = progress.toInt()
                    downloadList[index].line = replaceDoubleSpaces(line)
                }

                val progressIntent = Intent("DOWNLOAD_PROGRESS")
                progressIntent.putExtra("downloadList", downloadList)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(progressIntent)
                showProgress(
                    taskId.hashCode(),
                    taskId,
                    name,
                    progress.toInt(),
                    replaceDoubleSpaces(line),
                    tmpFile
                )
            }


            /*val encryptionCipher =
                Cipher.getInstance(BuildConfig.TRANSFORMATION)
            val iv = ByteArray(encryptionCipher.blockSize)
            val ivParams = IvParameterSpec(iv)

            encryptionCipher.init(
                Cipher.ENCRYPT_MODE,
                SecurityManger().keyGen(BuildConfig.KEYPASSWORD),
                ivParams,
            )*/

            tmpFile.listFiles()?.forEach { file ->
                val destFile = File(File(downloadDir!!), file.name)
                destUri = Uri.parse(destFile.path)
                try {
                    /*FileInputStream(file).use { fis ->
                        FileOutputStream(destFile).use { fos ->
                            CipherOutputStream(fos, encryptionCipher).use { cipherOut ->
                                val buffer = ByteArray(1024)
                                var bytesRead: Int
                                while (fis.read(buffer).also { bytesRead = it } != -1) {
                                    cipherOut.write(buffer, 0, bytesRead)
                                }
                            }
                        }
                    }*/
                    FileInputStream(file).use { fis ->
                        FileOutputStream(destFile).use { fos ->
                            val buffer = ByteArray(1024)
                            var bytesRead: Int
                            while ((fis.read(buffer).also { bytesRead = it }) != -1) {
                                fos.write(buffer, 0, bytesRead)
                            }

                            fis.closeQuietly()
                            fos.closeQuietly()/*
                            CipherOutputStream(fos, cipher).use { cos ->
                                val buffer = ByteArray(1024)
                                var bytesRead: Int
                                while (fis.read(buffer).also { bytesRead = it } != -1) {
                                    cos.write(buffer, 0, bytesRead)
                                }
                            }*/
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            totalSize = FileNameUtils.getFileSize(destUri.toString())
            totalDuration = try {
                File(destUri.toString()).getMediaDuration(applicationContext)
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
            encryptDownloadedPath = destUri?.toString() ?: ""

            Log.d(TAG, "EncryptedVideo $encryptDownloadedPath")
            /*encryptDownloadedPath = encryptVideoFile(
                context = applicationContext,
                fileName = name,
                downloadedPath = destUri?.toString() ?: "",
                extensionName = extensionName,
            )*/

            Log.d(TAG, "Encrypted Download Path $encryptDownloadedPath")

        } catch (e: Exception) {
            e.printStackTrace()
//            Toast.makeText(applicationContext, "${e.localizedMessage}", Toast.LENGTH_LONG).show()
            val cancelIntent = Intent(applicationContext, CancelReceiver::class.java)
            cancelIntent.putExtra("taskId", taskId)
            cancelIntent.putExtra("notificationId", taskId.hashCode())
            applicationContext.sendBroadcast(cancelIntent)

            val progressIntent = Intent("DELETE_PROGRESS")
            progressIntent.putExtra("taskId", taskId)
            progressIntent.putExtra("videoId", videoId)
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(progressIntent)
        } finally {
            tmpFile.deleteRecursively()
        }


        val downloadsDao = AppDatabase.getDatabase(
            applicationContext
        ).downloadsDao()

        val repository = DownloadsRepository(downloadsDao)
        val download = Download(
            name = name,
            ext = extensionName,
            timestamp = Date().time,
            totalSize = totalSize,
            duration = totalDuration,
            videoId = videoId,
            mobileNumber = mobileNumber,
            expiryDate = getExpiryDate(),
        )

        println("Download Path:: $destUri")
        println("Encoded Download Path:: $encryptDownloadedPath")

        if (encryptDownloadedPath.isEmpty()) {
            encryptDownloadedPath = destUri.toString()
        }

        download.downloadedPath = encryptDownloadedPath
        download.downloadedPercent = 100.00
        download.downloadedSize = size
        download.thumbnail = thumbnail
        download.url = url
        download.mediaType = if (vcodec == "none" && acodec != "none") "audio" else "video"
        repository.insert(download)
        completeDownload(name, taskId)

        return Result.success()
    }

    private fun encryptVideoFile(
        context: Context, downloadedPath: String, fileName: String, extensionName: String,
    ): String {

        if (fileName.isEmpty() && downloadedPath.isEmpty()) {
            return ""
        }

        val path: String? = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(context.getString(R.string.download_location_key), null)

        val originalFile = File(downloadedPath)

        val encryptFile = File("$path${File.separator}${System.currentTimeMillis()}")
        if (!encryptFile.parentFile.exists())
            encryptFile.parentFile.mkdirs()

        if (encryptFile.exists()) {
            encryptFile.delete()
        }
//        encryptFile.createNewFile()

        Log.d(TAG, "Encrypted file ${encryptFile.path}")

//        val encryptedUri = SecurityManger().aesCipherInStreamToOutStream(
//            context = applicationContext,
//            fileUri = originalFile.toUri(), file = encryptFile
//        )

        VideoEncryption.encryptFile(
            VideoEncryption.generateAndStoreKey(BuildConfig.KEYPASSWORD),
            originalFile,
            encryptFile
        )

//        if (encryptedUri != null) {
//            Log.d(TAG, "Completed Encrypted file ${encryptFile.path}")
//            originalFile.delete()
//            return encryptFile.path ?: ""
//        } else {
//            Log.d(TAG, "Not Complete Encrypted file ${encryptFile.path}")
//            return ""
//        }
        return encryptFile.absolutePath
    }

    private fun getExpiryDate(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, 30)
        return calendar.time.time
    }


    private fun completeDownload(text: String, taskId: String) {
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle("Download Completed").setTicker("Download Completed")
            .setStyle(NotificationCompat.BigTextStyle().bigText(text)).build()
        setForegroundAsync(ForegroundInfo(taskId.hashCode(), notification))
    }

    private fun showProgress(
        id: Int, taskId: String, name: String, progress: Int, line: String, tmpFile: File
    ) {
        val text = line.replace(tmpFile.toString(), "")
        val intent =
            Intent(applicationContext, CancelReceiver::class.java).putExtra("taskId", taskId)
                .putExtra("notificationId", id)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, channelIdProgress)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(name).setTicker(name)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text)).setOngoing(true)
            .setProgress(100, progress, progress == -1).addAction(
                R.drawable.ic_baseline_stop_24,
                applicationContext.getString(R.string.cancel_download),
                pendingIntent
            ).build()

//        notificationManager?.notify(id, notification)
        setForegroundAsync(ForegroundInfo(id, notification))
    }


    private fun createNotificationChannel() {
        if (SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel = notificationManager?.getNotificationChannel(channelId)
            if (notificationChannel == null) {
                val channelName = applicationContext.getString(R.string.download_noti_channel_name)
                notificationChannel = NotificationChannel(
                    channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationChannel.description = channelName
                notificationManager?.createNotificationChannel(notificationChannel)
            }
        }
    }

    private fun createNotificationChannelForDownloadProgress() {
        if (SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel = notificationManager?.getNotificationChannel(channelIdProgress)
            if (notificationChannel == null) {
                val channelName =
                    applicationContext.getString(R.string.download_noti_progress_channel_name)
                notificationChannel = NotificationChannel(
                    channelIdProgress, channelName, NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationChannel.description = channelName
                notificationChannel.setSound(null, null)

                notificationManager?.createNotificationChannel(notificationChannel)
            }
        }
    }


    companion object {
        const val TAG = "DownloadedWorker"
        const val channelId = "idragon_download"
        const val channelIdProgress = "idragon_download_progress"
        const val urlKey = "url"
        const val nameKey = "name"
        const val ext = "ext"
        const val formatIdKey = "formatId"
        const val acodecKey = "acodec"
        const val vcodecKey = "vcodec"
        const val downloadDirKey = "downloadDir"
        const val sizeKey = "size"
        const val taskIdKey = "taskId"
        const val videoId = "videoId"
        const val duration = "duration"
        const val thumbnail = "thumbnail"
        const val mobileNumber = "mobile_number"
        const val DATE_FORMAT = "YYYY-MM-dd HH:mm:ss"
        val downloadList = ArrayList<DownloadProgress>()

        fun replaceDoubleSpaces(input: String): String {
            return input.replace(Regex("\\s{2,}"), " ")
        }
    }
}

