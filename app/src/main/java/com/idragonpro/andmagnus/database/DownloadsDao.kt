package com.idragonpro.andmagnus.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadsDao {
    @Insert
    fun insert(item: Download)

    @Update
    fun update(item: Download)

    @Delete
    fun delete(item: Download)

    @Query("SELECT * from downloads_table WHERE id = :id")
    fun getById(id: Long): Download

    @Query("SELECT * from downloads_table WHERE videoId = :videoId AND downloaded_path IS NOT NULL AND downloaded_path <> ''")
    fun getByVideoId(videoId: String): Download

    @Query("SELECT * from downloads_table ORDER BY timestamp DESC")
    fun getAllDownloads(): Flow<List<Download>>
}