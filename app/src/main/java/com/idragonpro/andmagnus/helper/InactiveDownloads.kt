package com.idragonpro.andmagnus.helper

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class InactiveDownloads : Serializable {
    private val list: MutableList<DownloadVideo> = ArrayList()

    fun add(context: Context, inactiveDownload: DownloadVideo) {
        list.add(inactiveDownload)
        save(context)
    }

    fun save(context: Context) {
        try {
            val file = File(context.filesDir, "inactive.dat")
            val fileOutputStream = FileOutputStream(file)
            ObjectOutputStream(fileOutputStream).use { objectOutputStream ->
                objectOutputStream.writeObject(
                    this
                )
            }
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getList(): List<DownloadVideo> {
        return list
    }

    companion object {
        fun load(context: Context): InactiveDownloads? {
            val file = File(context.filesDir, "inactive.dat")
            var inactiveDownloads: InactiveDownloads? = InactiveDownloads()
            if (file.exists()) {
                try {
                    val fileInputStream = FileInputStream(file)
                    ObjectInputStream(fileInputStream).use { objectInputStream ->
                        inactiveDownloads = objectInputStream.readObject() as InactiveDownloads?
                    }
                    fileInputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }
            }
            return inactiveDownloads
        }
    }
}