package com.idragonpro.andmagnus.helper

import android.app.IntentService
import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import com.idragonpro.andmagnus.MyApp
import com.idragonpro.andmagnus.R
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.channels.FileChannel

class DownloadManager : IntentService("DownloadManager") {
    override fun onHandleIntent(intent: Intent?) {
        stop = false
        downloadThread = Thread.currentThread()
        if (intent != null) {
            chunked = intent.getBooleanExtra(CHUNCKED, false)
            if (chunked) {
                downloadFile = null
                prevDownloaded = 0
                downloadSpeed = 0
                totalSize = 0
                handleChunkedDownload(intent)
            } else {
                prevDownloaded = 0
                val connection: URLConnection?
                try {
                    totalSize = intent.getStringExtra("size")!!.toLong()
                    connection = (URL(intent.getStringExtra("link"))).openConnection()

                    val filename =
                        intent.getStringExtra("name") + "." + intent.getStringExtra("type")

                    val directory =
                        Environment.getExternalStoragePublicDirectory(getString(R.string.app_name))

                    val directoryExists: Boolean =
                        directory.exists() || directory.mkdir() || directory.createNewFile()

                    if (directoryExists) {
                        downloadFile = File(
                            Environment.getExternalStoragePublicDirectory(getString(R.string.app_name)),
                            filename
                        )
                        if (connection != null) {
                            var out: FileOutputStream? = null
                            if (downloadFile!!.exists()) {
                                prevDownloaded = downloadFile!!.length()
                                connection.setRequestProperty(
                                    "Range", "bytes=" + downloadFile!!.length() + "-"
                                )
                                connection.connect()
                                out = FileOutputStream(
                                    Environment.getExternalStoragePublicDirectory(getString(R.string.app_name))
                                        .toString() + "/" + filename, true
                                )
                            } else {
                                connection.connect()
                                if (downloadFile!!.createNewFile()) {
                                    out = FileOutputStream(
                                        downloadFile!!.absolutePath, true
                                    )
                                }
                            }
                            if (out != null && downloadFile!!.exists()) {
                                val `in` = connection.getInputStream()
                                var fileChannel: FileChannel
                                Channels.newChannel(`in`).use { readableByteChannel ->
                                    fileChannel = out.channel
                                    while (downloadFile!!.length() < totalSize) {
                                        if (stop) return
                                        fileChannel.transferFrom(readableByteChannel, 0, 1024)
                                    }
                                }
                                `in`.close()
                                out.flush()
                                out.close()
                                fileChannel.close()
                                downloadFinished(filename)
                            }
                        }
                    }
                } catch (e: FileNotFoundException) {
                    linkNotFound(intent)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun downloadFinished(filename: String) {
        if (onDownloadFinishedListener != null) {
            onDownloadFinishedListener!!.onDownloadFinished()
        } else {
            val queues: DownloadQueues = DownloadQueues.Companion.load(applicationContext)
            queues.deleteTopVideo(applicationContext)
            val completedVideos: CompletedVideos? = CompletedVideos.Companion.load(
                applicationContext
            )
            completedVideos!!.addVideo(applicationContext, filename)
            val topVideo = queues.topVideo
            if (topVideo != null) {
                val downloadService = MyApp.getInstance()!!.getDownloadService()
                downloadService!!.putExtra("link", topVideo.link)
                downloadService.putExtra("name", topVideo.name)
                downloadService.putExtra("type", topVideo.type)
                downloadService.putExtra("size", topVideo.size)
                downloadService.putExtra("page", topVideo.page)
                downloadService.putExtra(CHUNCKED, topVideo.chunked)
                downloadService.putExtra(WEBSITE, topVideo.website)
                onHandleIntent(downloadService)
            }
        }
    }

    private fun linkNotFound(intent: Intent) {
        if (onLinkNotFoundListener != null) {
            onLinkNotFoundListener!!.onLinkNotFound()
        } else {
            val queues: DownloadQueues = DownloadQueues.load(applicationContext)
            queues.deleteTopVideo(applicationContext)
            val inactiveDownload = DownloadVideo()
            inactiveDownload.name = intent.getStringExtra("name")
            inactiveDownload.link = intent.getStringExtra("link")
            inactiveDownload.type = intent.getStringExtra("type")
            inactiveDownload.size = intent.getStringExtra("size")
            inactiveDownload.page = intent.getStringExtra("page")
            inactiveDownload.website = intent.getStringExtra(WEBSITE)
            inactiveDownload.chunked = intent.getBooleanExtra(CHUNCKED, false)
            val inactiveDownloads: InactiveDownloads? = InactiveDownloads.Companion.load(
                applicationContext
            )
            inactiveDownloads!!.add(applicationContext, inactiveDownload)
            val topVideo = queues.topVideo
            if (topVideo != null) {
                val downloadService = MyApp.getInstance()!!.getDownloadService()
                downloadService!!.putExtra("link", topVideo.link)
                downloadService.putExtra("name", topVideo.name)
                downloadService.putExtra("type", topVideo.type)
                downloadService.putExtra("size", topVideo.size)
                downloadService.putExtra("page", topVideo.page)
                downloadService.putExtra(CHUNCKED, topVideo.chunked)
                downloadService.putExtra(WEBSITE, topVideo.website)
                onHandleIntent(downloadService)
            }
        }
    }

    private fun handleChunkedDownload(intent: Intent) {
        try {
            val name = intent.getStringExtra("name")
            val type = intent.getStringExtra("type")
            val directory =
                Environment.getExternalStoragePublicDirectory(getString(R.string.app_name))
            val directotryExists: Boolean =
                directory.exists() || directory.mkdir() || directory.createNewFile()
            if (directotryExists) {
                val progressFile = File(cacheDir, "$name.dat")
                val videoFile = File(
                    Environment.getExternalStoragePublicDirectory(getString(R.string.app_name)),
                    "$name.$type"
                )
                var totalChunks: Long = 0
                if (progressFile.exists()) {
                    val `in` = FileInputStream(progressFile)
                    val data = DataInputStream(`in`)
                    totalChunks = data.readLong()
                    data.close()
                    `in`.close()
                } else if (videoFile.exists()) {
                    downloadFinished("$name.$type")
                }
                if (videoFile.exists() && progressFile.exists()) {
                    while (true) {
                        prevDownloaded = 0
                        val website = intent.getStringExtra(WEBSITE)
                        var chunkUrl: String? = null
                        when (website) {
                            "dailymotion.com" -> chunkUrl =
                                getNextChunkWithDailymotionRule(intent, totalChunks)

                            "vimeo.com" -> chunkUrl = getNextChunkWithVimeoRule(intent, totalChunks)
                            TWITTER, METACAFE, MYSPACE -> chunkUrl =
                                getNextChunkWithM3U8Rule(intent, totalChunks)

                            else -> {}
                        }
                        if (chunkUrl == null) {
                            downloadFinished("$name.$type")
                        }
                        bytesOfChunk = ByteArrayOutputStream()
                        try {
                            val uCon = URL(chunkUrl).openConnection()
                            if (uCon != null) {
                                val `in` = uCon.getInputStream()
                                Channels.newChannel(`in`).use { readableByteChannel ->
                                    Channels.newChannel(
                                        bytesOfChunk
                                    ).use { writableByteChannel ->
                                        var read: Int
                                        while (true) {
                                            if (stop) return
                                            val buffer: ByteBuffer = ByteBuffer.allocateDirect(1024)
                                            read = readableByteChannel.read(buffer)
                                            if (read != -1) {
                                                buffer.flip()
                                                writableByteChannel.write(buffer)
                                            } else {
                                                FileOutputStream(videoFile, true).use { vAddChunk ->
                                                    vAddChunk.write(
                                                        bytesOfChunk!!.toByteArray()
                                                    )
                                                }
                                                val outputStream: FileOutputStream =
                                                    FileOutputStream(progressFile, false)
                                                DataOutputStream(outputStream).use { dataOutputStream ->
                                                    dataOutputStream.writeLong(
                                                        ++totalChunks
                                                    )
                                                }
                                                outputStream.close()
                                                break
                                            }
                                        }
                                    }
                                }
                                `in`.close()
                                bytesOfChunk!!.close()
                            }
                        } catch (e: FileNotFoundException) {
                            downloadFinished("$name.$type")
                            break
                        } catch (e: IOException) {
                            e.printStackTrace()
                            break
                        }
                    }
                }
                MediaScannerConnection.scanFile(
                    applicationContext, arrayOf(videoFile.absolutePath), null
                ) { path, uri ->
                    //nada
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getNextChunkWithDailymotionRule(intent: Intent, totalChunks: Long): String {
        val link = intent.getStringExtra("link")
        return link!!.replace("FRAGMENT", "frag(" + (totalChunks + 1) + ")")
    }

    private fun getNextChunkWithVimeoRule(intent: Intent, totalChunks: Long): String {
        val link = intent.getStringExtra("link")
        return link!!.replace("SEGMENT", "segment-" + (totalChunks + 1))
    }

    private fun getNextChunkWithM3U8Rule(intent: Intent, totalChunks: Long): String? {
        val link = intent.getStringExtra("link")
        val website = intent.getStringExtra(WEBSITE)
        var line: String? = null
        try {
            val m3u8Con = URL(link).openConnection()
            val `in` = m3u8Con.getInputStream()
            val inReader = InputStreamReader(`in`)
            val buffReader = BufferedReader(inReader)
            while ((buffReader.readLine().also { line = it }) != null) {
                if (((website == TWITTER) || (website == MYSPACE)) && line!!.endsWith(".ts") || (website == METACAFE) && line!!.endsWith(
                        ".mp4"
                    )
                ) {
                    break
                }
            }
            if (line != null) {
                var l: Long = 1
                while (l < (totalChunks + 1)) {
                    line = buffReader.readLine()
                    l++
                }
            }
            buffReader.close()
            inReader.close()
            `in`.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (line != null) {
            val prefix: String
            when (website) {
                TWITTER -> {
                    Log.i(
                        "VDInfo",
                        ("downloading chunk " + (totalChunks + 1) + ": " + "https://video.twimg.com" + line)
                    )
                    return "https://video.twimg.com$line"
                }

                METACAFE, MYSPACE -> {
                    prefix = link!!.substring(0, link.lastIndexOf("/") + 1)
                    Log.i(
                        "VDInfo", ("downloading chunk " + (totalChunks + 1) + ": " + prefix + line)
                    )
                    return prefix + line
                }

                else -> return null
            }
        } else {
            return null
        }
    }

    interface OnDownloadFinishedListener {
        fun onDownloadFinished()
    }

    interface OnLinkNotFoundListener {
        fun onLinkNotFound()
    }

    override fun onDestroy() {
        downloadFile = null
        Thread.currentThread().interrupt()
        super.onDestroy()
    }

    companion object {
        private val CHUNCKED = "chunked"
        private var downloadFile: File? = null
        private var prevDownloaded: Long = 0

        /**
         * Should be called every second
         *
         * @return download speed in bytes per second
         */
        var downloadSpeed: Long = 0
            get() {
                if (!chunked) {
                    if (downloadFile != null) {
                        val downloaded: Long = downloadFile!!.length()
                        downloadSpeed = downloaded - prevDownloaded
                        prevDownloaded = downloaded
                        return downloadSpeed
                    }
                    return 0
                } else {
                    if (bytesOfChunk != null) {
                        val downloaded: Long = bytesOfChunk!!.size().toLong()
                        downloadSpeed = downloaded - prevDownloaded
                        prevDownloaded = downloaded
                        return downloadSpeed
                    }
                    return 0
                }
            }
        private var totalSize: Long = 0
        private val WEBSITE = "website"
        private val TWITTER = "twitter.com"
        private val METACAFE = "metacafe.com"
        private val MYSPACE = "myspace.com"
        private var chunked = false
        private var bytesOfChunk: ByteArrayOutputStream? = null
        private var stop = false
        private var downloadThread: Thread? = null
        private var onDownloadFinishedListener: OnDownloadFinishedListener? = null
        fun setOnDownloadFinishedListener(listener: OnDownloadFinishedListener) {
            onDownloadFinishedListener = listener
        }

        private var onLinkNotFoundListener: OnLinkNotFoundListener? = null
        fun setOnLinkNotFoundListener(listener: OnLinkNotFoundListener) {
            onLinkNotFoundListener = listener
        }

        fun stop() {
            Log.d("debug", "stop: called")
            val downloadService = MyApp.getInstance()!!.getDownloadService()
            MyApp.getInstance()!!.stopService(downloadService)
            forceStopIfNecessary()
        }

        fun forceStopIfNecessary() {
            if (downloadThread != null) {
                Log.d("debug", "force: called")
                downloadThread = Thread.currentThread()
                if (downloadThread!!.isAlive) {
                    stop = true
                }
            }
        }

        /**
         * @return remaining time to download video in milliseconds
         */
        val remaining: Long
            get() {
                if (!chunked && (downloadFile != null)) {
                    val remainingLength: Long = totalSize - prevDownloaded
                    return (1000 * (remainingLength / downloadSpeed))
                } else {
                    return 0
                }
            }
    }
}