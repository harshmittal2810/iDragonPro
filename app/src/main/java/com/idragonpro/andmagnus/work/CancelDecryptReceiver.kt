package com.idragonpro.andmagnus.work

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import androidx.work.WorkManager
import com.idragonpro.andmagnus.R
import com.idragonpro.andmagnus.activities.Info.MoreBottomDialogFragment.Companion.DOWNLOAD_FOLDER_NAME
import java.io.File

private const val TAG = "CancelDecryptReceiver"

class CancelDecryptReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return
        val taskId = intent.getStringExtra("taskId")
        val fileName = intent.getStringExtra(DecryptVideoWorker.nameKey)
        val ext = intent.getStringExtra(DecryptVideoWorker.ext) ?: ""
        val encryptedFilePath = intent.getStringExtra(DecryptVideoWorker.encryptedFilePath) ?: ""

        if (taskId.isNullOrEmpty()) return

        Log.d(TAG, "Task (id:$taskId) was killed.")
        WorkManager.getInstance(context!!).cancelAllWorkByTag(taskId)

        val path: String? = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(context.getString(R.string.download_location_key), null)

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
        Log.d(TAG, "file path: $filePath")
        val decryptedFile = File(filePath)
        if (decryptedFile.exists()) {
            decryptedFile.delete()
        }
    }
}