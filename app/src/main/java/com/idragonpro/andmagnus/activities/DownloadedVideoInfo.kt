package com.idragonpro.andmagnus.activities

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.analytics.AnalyticsListener.EventTime
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.firebase.analytics.FirebaseAnalytics
import com.idragonpro.andmagnus.R
import com.idragonpro.andmagnus.helpers.GlobalModule
import com.idragonpro.andmagnus.helpers.LocaleHelper
import com.idragonpro.andmagnus.helpers.SaveSharedPreference
import com.idragonpro.andmagnus.utility.CustomOnScaleGestureListener
import com.idragonpro.andmagnus.utility.UtilityInterface
import com.idragonpro.andmagnus.work.CancelDecryptReceiver
import com.idragonpro.andmagnus.work.DecryptVideoWorker
import com.idragonpro.andmagnus.work.DecryptVideoWorker.Companion.ENCRYPT_PROGRESS
import com.idragonpro.andmagnus.work.DecryptVideoWorker.Companion.decryptFile
import kotlinx.coroutines.launch
import java.io.File
import java.util.Formatter
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min

class DownloadedVideoInfo : AppCompatActivity(), UtilityInterface, View.OnTouchListener {

    private var encryptedFilePath: String? = null
    private var fileName: String? = null
    private var ext: String? = null
    private var ObjExoPlayer: ExoPlayer? = null
    private var tvPlayerCurrentTime: TextView? = null
    private var tvPlayerEndTime: TextView? = null

    private var mainInfo: RelativeLayout? = null
    private var endDuration: Long = 0
    private var startDuration: Long = 0
    private var ObjPlayerView: PlayerView? = null
    private var ObjProgressBar: ProgressBar? = null

    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var isManul = false
    private var totalTimeWatched: Long = 0

    private var isObscuredTouch: Boolean = false
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var progressReceiver: BroadcastReceiver? = null

    private var img_setting: ImageView? = null
    private var imgBwd: ImageView? = null
    private var imgFwd: ImageView? = null
    private var llQuality: RelativeLayout? = null
    private var tvQuality: TextView? = null
    private var imgBackPlayer: ImageView? = null
    private var rlFullScreen: RelativeLayout? = null
    private var tvTitlePlayer: TextView? = null
    private var root_layout: RelativeLayout? = null
    private var gesture_volume_layout: RelativeLayout? = null
    private var gesture_bright_layout: RelativeLayout? = null
    private var gesture_progress_layout: RelativeLayout? = null
    private var pbVolume: ProgressBar? = null
    private var pbBrigtness: ProgressBar? = null
    private var gesture_iv_progress: ImageView? = null
    private var gesture_tv_progress_time: TextView? = null
    private var isVideoCompleted = false
    private var exo_play1: ImageButton? = null
    private var exo_pause1: ImageButton? = null
    private var exo_play_down: ImageButton? = null
    private var isForwardButtonClicked = false
    private var maxVolume = 0
    private var decryptedFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_download_video_info)

        if (SaveSharedPreference.getLoginFromGoogle(this)) {
            GlobalModule.startActivity = DownloadedVideoInfo::class.java.simpleName
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        mainInfo = findViewById(R.id.main_info)

        recordScreenView()
        initializeBrightnessManager()

        if (intent.getStringExtra(VIDEO_URL) != null) {
            encryptedFilePath = Uri.parse(intent.getStringExtra(VIDEO_URL) ?: "").toString()
            fileName = Uri.parse(intent.getStringExtra(VIDEO_TITLE) ?: "").toString()
            ext = Uri.parse(intent.getStringExtra(EXT) ?: "").toString()
        }

        if (encryptedFilePath.isNullOrEmpty() || fileName.isNullOrEmpty()) {
            Toast.makeText(this, "Required data is missing !!", Toast.LENGTH_SHORT).show()
            finish()
        }

        Log.d(TAG, "Encrypted:: $encryptedFilePath, $fileName, $ext")

//        startDecryptWorker(fileName!!, ext ?: "")
//        observerDecryption()

//        LocalBroadcastManager.getInstance(this).registerReceiver(
//            progressReceiver as BroadcastReceiver, IntentFilter(ENCRYPT_PROGRESS)
//        )

        init(Uri.parse(encryptedFilePath ?: "").toString())
    }


    private fun observerDecryption() {
        lifecycleScope.launch {
            progressReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
                    if (intent.action == ENCRYPT_PROGRESS && !isFinishing) {
                        handleDownloadProgress(intent)
                    }
                }
            }
        }

    }

    private fun startDecryptWorker(fileName: String, ext: String) {
        val workTag = fileName.hashCode().toString()
        val workManager = WorkManager.getInstance(this@DownloadedVideoInfo)

        val workData = workDataOf(
            DecryptVideoWorker.encryptedFilePath to encryptedFilePath,
            DecryptVideoWorker.nameKey to fileName,
            DecryptVideoWorker.ext to ext,
        )

        println("WorkManager:: $workData")

        val workRequest = workTag.let {
            OneTimeWorkRequestBuilder<DecryptVideoWorker>()
                .addTag(it)
                .setInputData(workData)
                .build()
        }

        workManager.enqueueUniqueWork(
            workTag, ExistingWorkPolicy.KEEP, workRequest
        )

    }

    private fun deleteDecryptWorker() {
        val workTag = fileName.hashCode().toString()
        val cancelIntent = Intent(this@DownloadedVideoInfo, CancelDecryptReceiver::class.java)
        cancelIntent.putExtra("taskId", workTag)
        cancelIntent.putExtra(DecryptVideoWorker.encryptedFilePath, encryptedFilePath)
        cancelIntent.putExtra(DecryptVideoWorker.nameKey, fileName)
        cancelIntent.putExtra(DecryptVideoWorker.ext, ext)
        sendBroadcast(cancelIntent)
    }

    fun handleDownloadProgress(intent: Intent) {
        val decryptFilePath = intent.getStringExtra(decryptFile) ?: ""
        if (decryptFilePath.isEmpty()) {
            finish()
        }

        decryptedFile = File(decryptFilePath)
        init(decryptedFile.toString())
    }

    private fun recordScreenView() {
        mFirebaseAnalytics!!.setCurrentScreen(this, null, null) // [END set_current_screen]
    }

    override fun onPause() {
        if (ObjExoPlayer != null) {
            ObjExoPlayer!!.playWhenReady = false
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (ObjExoPlayer != null) {
            ObjExoPlayer!!.playWhenReady = true
        }
    }

    public override fun onDestroy() {
        releasePlayer()
        finish()
        super.onDestroy()
    }

    override fun finishActivity(activity: Activity) {
        releaseFile()
        if (!activity.isDestroyed) {
            activity.finish()
        }
    }

    private fun releaseFile() {
        if (decryptedFile != null && decryptedFile!!.exists()) {
            decryptedFile!!.delete()
        }
//        deleteDecryptWorker()
    }

    private fun releasePlayer() {
        if (ObjExoPlayer != null) {
            ObjExoPlayer!!.stop()
            ObjExoPlayer!!.release()
            ObjExoPlayer = null
        }
        releaseFile()
        finish()
    }

    fun init(videoURL: String = "") {
        if (ObjExoPlayer != null) {
            endDuration = TimeUnit.MILLISECONDS.toSeconds(ObjExoPlayer!!.currentPosition)
            totalTimeWatched += (endDuration - startDuration)
            startDuration = endDuration
            endDuration = 0
            totalTimeWatched = 0
        }

        ObjPlayerView = findViewById(R.id.exo_player)
        scaleGestureDetector =
            ScaleGestureDetector(this, CustomOnScaleGestureListener(ObjPlayerView!!))
        ObjProgressBar = findViewById(R.id.progress)
        tvPlayerCurrentTime = findViewById(R.id.tv_player_current_time)
        tvPlayerEndTime = findViewById(R.id.tv_player_end_time)
        img_setting = findViewById(R.id.img_setting)
        llQuality = findViewById(R.id.llQuality)
        tvQuality = findViewById(R.id.tvQuality)
        imgBackPlayer = findViewById(R.id.imgBackPlayer)
        rlFullScreen = findViewById(R.id.rlFullScreen)
        imgBwd = findViewById(R.id.img_bwd)
        imgFwd = findViewById(R.id.img_fwd)
        tvTitlePlayer = findViewById(R.id.tvTitlePlayer)
        root_layout = findViewById(R.id.root_layout)
        root_layout?.isLongClickable = true
        root_layout?.setOnTouchListener(this)
        rlFullScreen?.visibility = View.INVISIBLE

        gesture_volume_layout = findViewById(R.id.gesture_volume_layout)
        pbVolume = findViewById(R.id.pb_volume)

        gesture_bright_layout = findViewById(R.id.gesture_bright_layout)
        pbBrigtness = findViewById(R.id.pb_brigtness)

        gesture_progress_layout = findViewById(R.id.gesture_progress_layout)
        gesture_iv_progress = findViewById(R.id.gesture_iv_progress)
        gesture_tv_progress_time = findViewById(R.id.gesture_tv_progress_time)
        exo_play1 = findViewById(R.id.exo_play1)
        exo_play_down = findViewById(R.id.exo_play_down)
        exo_pause1 = findViewById(R.id.exo_pause1)

        initPlayer(videoURL)

//        if (ObjPlayerView != null) {
//            ObjPlayerView!!.onResume()
//        }
//
//        if (ObjExoPlayer != null) {
//            if (isObscuredTouch) {
//                ObjExoPlayer!!.volume = 0f
//            } else {
//                ObjExoPlayer!!.volume = 1f
//            }
//        }
    }

    private fun initPlayer(videoURL: String) {
        if (isFinishing && videoURL.isEmpty()) {
            return
        }

        if (ObjExoPlayer == null) {
            ObjExoPlayer = ExoPlayer.Builder(this).build()
            ObjExoPlayer!!.playWhenReady = lifecycle.currentState == Lifecycle.State.RESUMED
        }

        ObjPlayerView!!.controllerAutoShow = false
        ObjPlayerView!!.controllerShowTimeoutMs = 2000
        ObjPlayerView!!.player = ObjExoPlayer

        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            this, Util.getUserAgent(this, "iDragonPro")
        )
        val mediaSourceFactory = ProgressiveMediaSource.Factory(dataSourceFactory)

        // Use FileProvider to get the URI
        val file = File(videoURL)
        val uri =
            FileProvider.getUriForFile(this, "${applicationContext.packageName}.provider", file)


        val mediaSource: MediaSource = mediaSourceFactory.createMediaSource(
            MediaItem.fromUri(uri)
        )
//        ObjExoPlayer!!.prepare(mediaSource, true, true)

        ObjExoPlayer!!.apply {
            setMediaSource(mediaSource)
            prepare()
            playWhenReady = lifecycle.currentState == Lifecycle.State.RESUMED
        }

        if (ObjProgressBar != null) {
            ObjProgressBar!!.visibility = View.VISIBLE
        }

        setProgress()
        initBwd()
        initFwd()
        initSetting()
        initPlayPause()
        initBack()
        setMaxBrightness()

        ObjExoPlayer!!.addListener(object : Player.Listener {
            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playWhenReady) {
                    startDuration = TimeUnit.MILLISECONDS.toSeconds(
                        ObjExoPlayer!!.currentPosition
                    )
                    Log.d(TAG, "player track: startDuration$startDuration")
                    exo_play1!!.visibility = View.GONE
                    exo_play_down!!.visibility = View.GONE
                    exo_pause1!!.visibility = View.VISIBLE

                    when (playbackState) {
                        Player.STATE_READY -> if (ObjProgressBar != null) {
                            ObjProgressBar!!.visibility = View.GONE
                        }

                        Player.STATE_BUFFERING -> {}
                        Player.STATE_ENDED -> {
                            isVideoCompleted = true
                        }
                    }
                } else {
                    endDuration = TimeUnit.MILLISECONDS.toSeconds(
                        ObjExoPlayer!!.currentPosition
                    )
                    Log.d(TAG, "player track: endDuration$endDuration")
                    totalTimeWatched += (endDuration - startDuration)
                    startDuration = endDuration
                    endDuration = 0

                    exo_play1!!.visibility = View.VISIBLE
                    exo_play_down!!.visibility = View.VISIBLE
                    exo_pause1!!.visibility = View.GONE

                    //refreshAdTimer();
                }
            }

            override fun onSeekProcessed() {
                Log.d(
                    TAG, "player track: onSeekProcessed-- " + TimeUnit.MILLISECONDS.toSeconds(
                        ObjExoPlayer!!.currentPosition
                    )
                )
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Log.e(TAG, error.localizedMessage, error)
                Log.e("ExoPlayer", "Player error: ${error.message}")
            }
        })

        ObjExoPlayer!!.addAnalyticsListener(object : AnalyticsListener {
            override fun onPlayerStateChanged(
                eventTime: EventTime, playWhenReady: Boolean, playbackState: Int
            ) {

            }

            override fun onPositionDiscontinuity(eventTime: EventTime, reason: Int) {
                Log.d(
                    TAG, "player track onPositionDiscontinuity: " + TimeUnit.MILLISECONDS.toSeconds(
                        ObjExoPlayer!!.currentPosition
                    )
                )
            }

            override fun onSeekStarted(eventTime: EventTime) {
                Log.d(
                    TAG, "player track onSeekStarted: " + TimeUnit.MILLISECONDS.toSeconds(
                        ObjExoPlayer!!.currentPosition
                    )
                )
                endDuration = TimeUnit.MILLISECONDS.toSeconds(ObjExoPlayer!!.currentPosition)
                Log.d(
                    TAG, "player track onSeekStarted$totalTimeWatched $endDuration $startDuration"
                )
                totalTimeWatched += (endDuration - startDuration)
                Log.d(TAG, "player track onSeekStarted$totalTimeWatched")
                startDuration = endDuration
                endDuration = 0
            }

            override fun onSeekProcessed(eventTime: EventTime) {
                Log.d(
                    TAG, "player track onSeekProcessed: " + TimeUnit.MILLISECONDS.toSeconds(
                        ObjExoPlayer!!.currentPosition
                    )
                )
                startDuration = TimeUnit.MILLISECONDS.toSeconds(
                    ObjExoPlayer!!.currentPosition
                )
            }
        })
    }

    /*private fun playEncryptedVideo(
        context: Context,
        encryptedFile: File,
        secretKey: SecretKey
    ): ExoPlayer {
        val player = ExoPlayer.Builder(context).build()
        val dataSourceFactory = DataSource.Factory {
            DecryptingDataSource(secretKey, encryptedFile)
        }

        val uri = Uri.fromFile(encryptedFile)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri))

        player.setMediaSource(mediaSource)
        player.prepare()
        player.playWhenReady = true

        return player
    }*/

    private var currentBrightness = 0

    private fun setMaxBrightness() {
        //Set Maximum brigtness
        currentBrightness = 255
        val lp = window.attributes
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1f
        } else {
            lp.screenBrightness = (currentBrightness / MAX_SCREEN_BRIGHTNESS).toFloat()
        }
        window.attributes = lp
    }

    private fun initBack() {
        imgBackPlayer!!.setOnClickListener { this@DownloadedVideoInfo.onBackPressed() }
    }

    override fun onBackPressed() {
        if (decryptedFile != null && decryptedFile!!.exists()) {
            decryptedFile!!.delete()
        }
        super.onBackPressed()
    }


    private fun initSetting() {
        img_setting!!.visibility = View.GONE
        tvQuality!!.visibility = View.GONE
    }

    private fun initBwd() {
        imgBwd!!.requestFocus()
        imgBwd!!.setOnClickListener {
            isForwardButtonClicked = true
            ObjExoPlayer!!.seekTo(ObjExoPlayer!!.currentPosition - 10000)
        }
    }

    private fun initFwd() {
        imgFwd!!.requestFocus()
        imgFwd!!.setOnClickListener {
            isForwardButtonClicked = true
            ObjExoPlayer!!.seekTo(ObjExoPlayer!!.currentPosition + 10000)
        }
    }

    private fun initPlayPause() {
        exo_play1!!.setOnClickListener {
            if (ObjExoPlayer != null) {
                ObjExoPlayer!!.playWhenReady = true
            }
        }
        exo_play_down!!.setOnClickListener {
            if (ObjExoPlayer != null) {
                ObjExoPlayer!!.playWhenReady = true
            }
        }
        exo_pause1!!.setOnClickListener {
            if (ObjExoPlayer != null) {
                ObjExoPlayer!!.playWhenReady = false
            }
        }
    }

    private var handler: Handler? = null

    private fun setProgress() {
        handler = Handler(Looper.getMainLooper())
        //Make sure you update Seekbar on UI thread
        handler!!.post(object : Runnable {
            override fun run() {
                if (ObjExoPlayer != null) {
                    tvPlayerCurrentTime!!.text =
                        stringForTime(ObjExoPlayer!!.currentPosition.toInt())
                    tvPlayerEndTime!!.text = stringForTime(ObjExoPlayer!!.duration.toInt())

                    handler!!.postDelayed(this, 1000)
                }
            }
        })
    }

    private var mFormatBuilder: StringBuilder? = null
    private var mFormatter: Formatter? = null

    private fun stringForTime(timeMs: Int): String {
        mFormatBuilder = StringBuilder()
        mFormatter = Formatter(mFormatBuilder, Locale.getDefault())
        val totalSeconds = timeMs / 1000

        val seconds = totalSeconds % 60
        val minutes = (totalSeconds / 60) % 60
        val hours = totalSeconds / 3600

        mFormatBuilder!!.setLength(0)
        return if (hours > 0) {
            mFormatter!!.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter!!.format("%02d:%02d", minutes, seconds).toString()
        }
    }

    private var startX = 0f
    private var startY = 0f
    private var moveY = 0f
    private var distanceY = 0f
    private var screenWidth = 0
    private var screenHeight = 0
    private var audioManager: AudioManager? = null
    private var currentVolume = 0

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.pointerCount > 1) {
            scaleGestureDetector!!.onTouchEvent(event)
            return false
        } else {
            if (event.action == MotionEvent.ACTION_UP) {
                gesture_volume_layout!!.visibility = View.GONE
                gesture_bright_layout!!.visibility = View.GONE
                gesture_progress_layout!!.visibility = View.GONE
                currentBrightness = min(
                    max(
                        (distanceY * SLIDE_MULTIPLE / screenHeight * MAX_SCREEN_BRIGHTNESS + currentBrightness).toDouble(),
                        0.0
                    ), MAX_SCREEN_BRIGHTNESS.toDouble()
                ).toInt()
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                startX = event.x
                startY = event.y
                // Get the current system sound
                currentVolume = audioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC)
                if (startX > screenWidth.toFloat() / 2) {
                    // Set the maximum value of seekBar according to the maximum value of the system volume
                    // seekBar.setMax(maxVolume);
                    pbVolume!!.max = maxVolume
                } else {
                    // Set the maximum value of seekBar according to the maximum value of the system brightness
                    //  seekBar.setMax(MAX_SCREEN_BRIGHTNESS);
                    pbBrigtness!!.max = MAX_SCREEN_BRIGHTNESS
                }
            } else if (event.action == MotionEvent.ACTION_MOVE) {
                moveY = event.y
                distanceY = startY - moveY
                if (startX > screenWidth.toFloat() / 2) {
                    // On the right side of the screen, adjust the sound
                    updateVoice(
                        min(
                            max(
                                (distanceY * SLIDE_MULTIPLE / screenHeight * maxVolume + currentVolume).toDouble(),
                                0.0
                            ), maxVolume.toDouble()
                        ).toInt()
                    )
                } else {
                    // On the left side of the screen, adjust the brightness
                    setBrightness(
                        min(
                            max(
                                (distanceY * SLIDE_MULTIPLE / screenHeight * MAX_SCREEN_BRIGHTNESS + currentBrightness).toDouble(),
                                0.0
                            ), MAX_SCREEN_BRIGHTNESS.toDouble()
                        ).toFloat()
                    )
                }
                return true
            }
        }
        return false
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (ObjExoPlayer != null) {
            if (Settings.System.getInt(
                    contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0
                ) == 1
            ) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER
            }
        }
    }

    private fun initializeBrightnessManager() {
        screenWidth = Resources.getSystem().displayMetrics.widthPixels
        screenHeight = Resources.getSystem().displayMetrics.heightPixels

        // Get system volume
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (audioManager != null) {
            // AudioManager.STREAM_MUSIC is the media volume
            maxVolume = audioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        }

        // Get the current screen brightness
        try {
            currentBrightness =
                Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
            currentBrightness = 255
        }
    }

    private fun setBrightness(brightness: Float) {
        gesture_bright_layout!!.visibility = View.VISIBLE
        val lp = window.attributes
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1f
        } else {
            lp.screenBrightness = brightness / MAX_SCREEN_BRIGHTNESS
        }
        window.attributes = lp
        pbBrigtness!!.progress = brightness.toInt()
    }

    private fun updateVoice(index: Int) {
        gesture_volume_layout!!.visibility = View.VISIBLE
        if (audioManager != null) {
            audioManager!!.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0)
            pbVolume!!.progress = index
        }
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"))
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        isObscuredTouch = (event.flags and MotionEvent.FLAG_WINDOW_IS_OBSCURED) != 0
        if (ObjExoPlayer != null) {
            if (isObscuredTouch) {
                ObjExoPlayer!!.volume = 0f
            } else {
                ObjExoPlayer!!.volume = 1f
            }
        }
        return super.dispatchTouchEvent(event)
    }


    companion object {
        private val TAG: String = DownloadedVideoInfo::class.java.simpleName
        private const val MAX_SCREEN_BRIGHTNESS = 255
        private const val SLIDE_MULTIPLE = 1f
        private const val VIDEO_URL = "videoUrl"
        private const val VIDEO_TITLE = "videoTitle"
        private const val EXT = "ext"

        fun playDownloadVideo(
            activity: Activity,
            videoURL: String,
            videoTitle: String,
            ext: String
        ) {
            Log.d(TAG, "Play video:: $videoURL")
            val intent = Intent(activity, DownloadedVideoInfo::class.java)
            intent.putExtra(VIDEO_URL, videoURL)
            intent.putExtra(VIDEO_TITLE, videoTitle)
            intent.putExtra(EXT, ext)
            activity.startActivity(intent)
        }

    }
}