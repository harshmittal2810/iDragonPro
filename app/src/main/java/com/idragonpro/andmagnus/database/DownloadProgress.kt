package com.idragonpro.andmagnus.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "download_progress")
@Parcelize
class DownloadProgress(
    var thumbnail: String,
    @PrimaryKey
    var taskId: String,
    var videoId: String,
    var name: String,
    var progress: Int,
    var size: Long,
    var line: String,
    var mobileNumber: String
) : Parcelable
