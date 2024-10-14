package com.idragonpro.andmagnus.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ShortcutDao {
    @Insert
    fun insert(shortcutTable: ShortcutTable?)

    @Delete
    fun delete(shortcutTable: ShortcutTable?)

    @get:Query("SELECT * FROM ShortcutTable")
    val allShortcut: LiveData<List<ShortcutTable?>?>?

    @get:Query("SELECT * FROM ShortcutTable")
    val allShortcutList: List<ShortcutTable?>?
}