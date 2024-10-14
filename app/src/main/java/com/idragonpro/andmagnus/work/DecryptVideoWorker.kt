package com.idragonpro.andmagnus.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.preference.PreferenceManager
import android.util.Log
import androidx.core.net.toUri
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.idragonpro.andmagnus.R
import com.idragonpro.andmagnus.activities.Info.MoreBottomDialogFragment.Companion.DOWNLOAD_FOLDER_NAME
import com.idragonpro.andmagnus.utils.SecurityManger
import java.io.File


class DecryptVideoWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

    override suspend fun doWork(): Result {

        val encryptedFilePath = inputData.getString(encryptedFilePath)!!
        val name = inputData.getString(nameKey) ?: ""
        val ext = inputData.getString(ext) ?: ""

        createNotificationChannelForDownloadProgress()

        val path: String? = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .getString(applicationContext.getString(R.string.download_location_key), null)

        val startIndex = encryptedFilePath.indexOf(DOWNLOAD_FOLDER_NAME)
        val substring =
            encryptedFilePath.substring(startIndex, encryptedFilePath.length).substringAfter(
                DOWNLOAD_FOLDER_NAME.plus(File.separator)
            )

        Log.d(TAG, "Encoded String: $substring")
        var filePath = "$path${File.separator}${substring}"

        if (!filePath.endsWith(ext)) {
            filePath = filePath.plus(".").plus(ext)
        }

        val decryptedFile = File(filePath)
        if (decryptedFile.exists()) {
            decryptedFile.delete()
        }

        Log.d(TAG, "Decrypted URI:: $decryptedFile")

        try {
            val encryptedUri =
                SecurityManger().aesDecipherInStreamToOutStream(
                    context = applicationContext,
                    fileInUri = File(encryptedFilePath).toUri(), file = decryptedFile
                )

            Log.d(TAG, "Decrypted URI from AES:: $encryptedUri")

            val progressIntent = Intent(ENCRYPT_PROGRESS)
            progressIntent.putExtra(decryptFile, decryptedFile.path)
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(progressIntent)
        } catch (e: Exception) {
            e.printStackTrace()
//            Toast.makeText(applicationContext, "${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }

        return Result.success()
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
        const val TAG = "DecryptVideoWorker"
        const val ENCRYPT_PROGRESS = "ENCRYPT_PROGRESS"
        const val channelIdProgress = "idragon_decrypt_progress"
        const val encryptedFilePath = "encryptedFilePath"
        const val decryptFile = "decryptFile"
        const val nameKey = "name"
        const val ext = "ext"
        const val formatIdKey = "formatId"
        const val videoId = "videoId"
    }
}

