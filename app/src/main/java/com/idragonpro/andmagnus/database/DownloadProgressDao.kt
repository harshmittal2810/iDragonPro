package com.idragonpro.andmagnus.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DownloadProgressDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: DownloadProgress)

    @Update
    fun update(item: DownloadProgress)

    @Delete
    fun delete(item: DownloadProgress)

    @Query("DELETE FROM download_progress WHERE videoId = :videoId")
    fun deleteWithId(videoId: String)

    @Query("SELECT * FROM download_progress WHERE videoId = :videoId LIMIT 1")
    fun getWithId(videoId: String): DownloadProgress?

    @Query("SELECT * from download_progress")
    fun getAllDownloads(): LiveData<List<DownloadProgress>>
}