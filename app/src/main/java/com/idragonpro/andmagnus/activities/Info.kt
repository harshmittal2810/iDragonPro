package com.idragonpro.andmagnus.activities

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.lzyzsd.circleprogress.DonutProgress
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.analytics.AnalyticsListener.EventTime
import com.google.android.exoplayer2.ext.ima.BuildConfig
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.GsonBuilder
import com.google.mlkit.nl.translate.Translator
import com.idragonpro.andmagnus.MyApp.Companion.getInstance
import com.idragonpro.andmagnus.R
import com.idragonpro.andmagnus.activities.Info.MoreBottomDialogFragment.Companion.DOWNLOAD_FOLDER_NAME
import com.idragonpro.andmagnus.activities.Info.MoreBottomDialogFragment.Companion.convertSolution
import com.idragonpro.andmagnus.activities.viewModel.DownloadsViewModel
import com.idragonpro.andmagnus.activities.viewModel.VidInfoViewModel
import com.idragonpro.andmagnus.adapters.EpisodeAdapter
import com.idragonpro.andmagnus.adapters.InfoAdapter
import com.idragonpro.andmagnus.api.API
import com.idragonpro.andmagnus.api.WebApi
import com.idragonpro.andmagnus.beans.Movies
import com.idragonpro.andmagnus.database.Download
import com.idragonpro.andmagnus.database.DownloadProgress
import com.idragonpro.andmagnus.helpers.GlobalModule
import com.idragonpro.andmagnus.helpers.LocaleHelper
import com.idragonpro.andmagnus.helpers.NonSwipeableViewPager
import com.idragonpro.andmagnus.helpers.SaveSharedPreference
import com.idragonpro.andmagnus.models.Review
import com.idragonpro.andmagnus.models.VidInfoItem
import com.idragonpro.andmagnus.radapters.ReviewRvAdapter
import com.idragonpro.andmagnus.responseModels.AddReviewResponseModel
import com.idragonpro.andmagnus.responseModels.AnalyticsResponseModel
import com.idragonpro.andmagnus.responseModels.BundlePackageResponseModel
import com.idragonpro.andmagnus.responseModels.GeneralResponseModel
import com.idragonpro.andmagnus.responseModels.IPResponseModel
import com.idragonpro.andmagnus.responseModels.ReviewResponseModels
import com.idragonpro.andmagnus.responseModels.VideoResponseModel
import com.idragonpro.andmagnus.security.SecurityChecks
import com.idragonpro.andmagnus.utility.CustomOnScaleGestureListener
import com.idragonpro.andmagnus.utility.DeviceUtility
import com.idragonpro.andmagnus.utility.StringUtility
import com.idragonpro.andmagnus.utility.TrackSelectionDialog
import com.idragonpro.andmagnus.utility.UtilityInterface
import com.idragonpro.andmagnus.utils.Utils.Companion.getDaysDifference
import com.idragonpro.andmagnus.work.CancelReceiver
import com.yausername.youtubedl_android.mapper.VideoFormat
import com.yausername.youtubedl_android.mapper.VideoInfo
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.Date
import java.util.Formatter
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min

class Info : AppCompatActivity(), UtilityInterface, View.OnTouchListener, View.OnClickListener,
    OnTabSelectedListener {

    @JvmField
    var isLoginAfterGoogle: Boolean = false
    var isDownloadLoginAfterGoogle: Boolean = false

    private var imgBanner: ImageView? = null
    private var tvName: TextView? = null
    private var tvSummary: TextView? = null
    private var llWatchList: LinearLayout? = null
    private var llShare: LinearLayout? = null
    private var sMovie: Movies? = null
    private var sPlay: String? = null
    private var llPlayMovie: TextView? = null
    private var btnTrailer: TextView? = null
    private var dialog: Dialog? = null
    private var episodeList: MutableList<Movies>? = null
    private var sFor: String? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    private var bannerAdView: FrameLayout? = null
    private var stickybannerAdView: FrameLayout? = null
    private var mPublisherInterstitialAdNew: AdManagerInterstitialAd? = null
    private var mPublisherInterstitialAd: InterstitialAd? = null
    private var mPublisherInterstitialAd2: InterstitialAd? = null
    private var mInstreamAd: InterstitialAd? = null
    private var mInstreamAd2: InterstitialAd? = null
    private var mInstreamAd3: InterstitialAd? = null
    private var mInstreamAd4: InterstitialAd? = null
    private var mCancelAd: InterstitialAd? = null
    private var mCancelAd2: InterstitialAd? = null
    private var isLoadedSecondTime = false
    private var shouldLoadAd = false
    private var dialogProgress: ProgressDialog? = null
    private var loading = false
    private var isContentAvailable: Boolean = false
    private var ObjPlayerView: PlayerView? = null
    private var ObjProgressBar: ProgressBar? = null
    private var tvPlayerCurrentTime: TextView? = null
    private var tvPlayerEndTime: TextView? = null
    private var tvQuality: TextView? = null
    private var imgFullScreenEnterExit: ImageView? = null
    private var imgBwd: ImageView? = null
    private var imgFwd: ImageView? = null
    private var img_setting: ImageView? = null
    private var imgBackPlayer: ImageView? = null
    private var exo_play1: ImageButton? = null
    private var exo_pause1: ImageButton? = null
    private var exo_play_down: ImageButton? = null
    private var handler: Handler? = null
    private var adsHandler: Handler? = null
    private var VideoURL: String? = null
    private var ObjExoPlayer: SimpleExoPlayer? = null
    private var mFormatBuilder: StringBuilder? = null
    private var mFormatter: Formatter? = null
    private var adFrameLayout: FrameLayout? = null
    private var frameLayout_top: FrameLayout? = null
    private var adView: AdView? = null
    private var adRequest: AdRequest? = null
    private var gamadView: AdManagerAdView? = null
    private var countDownTimer: CountDownTimer? = null
    private var rlPlayerPArent: RelativeLayout? = null
    private var realPArent: RelativeLayout? = null
    private var mExoPlayerFullscreen = false
    private var mFullScreenDialog: Dialog? = null
    private var llBottomLayout: LinearLayout? = null
    private var llTrack: LinearLayout? = null
    private var progress_bar: ProgressBar? = null
    private var nestedScrollView: NestedScrollView? = null
    private var main_info: RelativeLayout? = null
    private var rlViewMore: RelativeLayout? = null
    private var isLoadinInstream = false
    private var isLoadinCancelInstream = false
    private var isForwardButtonClicked = false
    private var trackSelector: DefaultTrackSelector? = null
    private var bandwidthMeter: DefaultBandwidthMeter = DefaultBandwidthMeter()
    private var isShowingTrackSelectionDialog = false
    private var ism3u8 = false
    private var isTrailer = false
    private var tempPosition = 0
    private var episodePosition = 0
    private var isPremium = false
    private var isVideoCompleted = false
    private var root_layout: RelativeLayout? = null
    private var rlDownload: RelativeLayout? = null
    private var gesture_volume_layout: RelativeLayout? = null
    private var gesture_bright_layout: RelativeLayout? = null
    private var gesture_progress_layout: RelativeLayout? = null
    private var gesture_iv_progress: ImageView? = null
    private var gesture_tv_progress_time: TextView? = null
    private var screenWidth = 0
    private var screenHeight = 0
    private var audioManager: AudioManager? = null
    private var maxVolume = 0
    private var currentBrightness = 0
    private var currentVolume = 0
    private var startX = 0f
    private var startY = 0f
    private var moveY = 0f
    private var distanceY = 0f
    private var pbVolume: ProgressBar? = null
    private var pbBrigtness: ProgressBar? = null
    private var nextvideolayout: RelativeLayout? = null
    private var txtNextTitle: TextView? = null
    private var txtCancel: TextView? = null
    private var donutProgress: DonutProgress? = null
    private var imgViewNextPlay: ImageView? = null
    private var isLocked = false
    private var nextVideoTimer: CountDownTimer? = null
    private var tvTitlePlayer: TextView? = null
    private var btnDownload: TextView? = null
    private var btnDownloadProgress: CircularProgressIndicator? = null
    private var englishGermanTranslator: Translator? = null
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var totalTimeWatched: Long = 0
    private var startDuration: Long = 0
    private var endDuration: Long = 0
    private var isClickProg = false
    private var videoType: String? = null
    private var iIsPaidUser = "1"
    private var preroll1 = ""
    private var preroll2 = ""
    private var preroll3 = ""
    private var midroll1 = ""
    private var midroll2 = ""
    private var midroll_3 = ""
    private var midroll4 = ""
    private var skip1 = ""
    private var skip2 = ""
    private var isPreRollThird = false
    private var tabLayout: TabLayout? = null
    private var viewPager: NonSwipeableViewPager? = null
    private var adapter: InfoAdapter? = null
    private var etReview: EditText? = null
    private var btnSubmit: Button? = null
    private var rvReviews: RecyclerView? = null
    private var reviewAdapter: ReviewRvAdapter? = null
    private val reviewList: MutableList<Review> = ArrayList()
    private var pageNumber = 1
    private val pageSize = 50
    private var loadMore = true
    private var showEpisodes: Boolean = false
    private var moreBottomDialogFragment: MoreBottomDialogFragment? = null
    private var llQuality: RelativeLayout? = null
    private var imgBack: ImageView? = null
    private var shadow: View? = null
    private var isManual = false
    private var isShownOnFullscreen = false
    private var isObscuredTouch: Boolean = false
    private var isAdforTrailer = false
    private var tempVideoURL: String? = null
    private var tempIsTrailer = false
    private var tempIsPremium = false
    private var isEpisodeAdPlayed = false
    private var isWebAdRequested = false
    private var isAfterPayment = false
    private var isLandscape = false
    private var bundleResponse: BundlePackageResponseModel? = null
    private var viewModel: VidInfoViewModel? = null
    private var downloadsViewModel: DownloadsViewModel? = null
    private var downloadVideo: Download? = null
    private var downloadVideoProgress: DownloadProgress? = null
    private var progressReceiver: BroadcastReceiver? = null
    private var deleteProgressReceiver: BroadcastReceiver? = null
    private var isDownloadProgress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        viewModel = ViewModelProvider(this)[VidInfoViewModel::class.java]
        downloadsViewModel = ViewModelProvider(this)[DownloadsViewModel::class.java]

        if (!BuildConfig.DEBUG) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE
            )
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        recordScreenView()
        initializeBrightnessManager()

        val intent = intent
        val data = intent.data
        if (data?.getQueryParameter("play") != null) {
            sPlay = data.getQueryParameter("play")
        }

        if (sPlay == null) {
            sMovie = getIntent().getSerializableExtra("movie") as Movies?
            viewModel?.updateMovieData(sMovie)
        }

        if (getIntent().hasExtra("isAfterPayment")) {
            isAfterPayment = getIntent().getBooleanExtra("isAfterPayment", false)
        }

        imgBanner = findViewById(R.id.imgBanner)
        llBottomLayout = findViewById(R.id.llBottomLayout)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.pager)
        tvName = findViewById(R.id.tvName)
        tvSummary = findViewById(R.id.tvSummary)
        llWatchList = findViewById(R.id.llWatchList)
        llShare = findViewById(R.id.llShare)
        rlPlayerPArent = findViewById(R.id.rlPlayerPArent)
        realPArent = findViewById(R.id.realPArent)
        llPlayMovie = findViewById(R.id.btnPlay)
        btnTrailer = findViewById(R.id.btnTrailer)
        progress_bar = findViewById(R.id.progress_bar)
        llTrack = findViewById(R.id.llTrack)
        btnDownload = findViewById(R.id.btnDownload)
        btnDownloadProgress = findViewById(R.id.btnDownloadProgress)
        main_info = findViewById(R.id.main_info)
        rlViewMore = findViewById(R.id.rlViewMore)
        nextvideolayout = findViewById(R.id.nextvideolayout)
        rlDownload = findViewById(R.id.rlDownload)

        txtNextTitle = nextvideolayout?.findViewById(R.id.txtNextTitle)
        txtCancel = nextvideolayout?.findViewById(R.id.txtCancel)
        donutProgress = nextvideolayout?.findViewById(R.id.donutProgress)
        imgViewNextPlay = nextvideolayout?.findViewById(R.id.imgvwNextPlay)

        nestedScrollView = findViewById(R.id.nestedScrollView)
        bannerAdView = findViewById(R.id.bannerAdView)
        etReview = findViewById(R.id.etReview)
        btnSubmit = findViewById(R.id.btnSubmit)
        shadow = findViewById(R.id.shadow)
        imgBack = findViewById(R.id.imgBack)
        rvReviews = findViewById(R.id.rvReviews)

        rlViewMore?.setOnClickListener { showMoreBottomSheet(sMovie) }

        nextvideolayout?.visibility = View.GONE
        txtCancel?.setOnClickListener(this)
        imgViewNextPlay?.setOnClickListener(this)


        btnSubmit?.setOnClickListener {
            if (etReview != null && etReview!!.text.toString().equals("", ignoreCase = true)) {
                val adExp = AlertDialog.Builder(this@Info).create()
                adExp.setMessage(getString(R.string.please_enter_valid_review))
                adExp.setButton(
                    DialogInterface.BUTTON_POSITIVE, "OK"
                ) { _: DialogInterface?, _: Int -> adExp.dismiss() }
                adExp.show()
            } else {
                dialog =
                    GlobalModule.showProgressDialog(getString(R.string.adding_review), this@Info)
                addReview()
            }
        }

        imgBack?.setOnClickListener { onBackPressed() }

        rvReviews?.isNestedScrollingEnabled = false

        val llmanager = LinearLayoutManager(applicationContext)
        rvReviews?.setLayoutManager(llmanager)
        nestedScrollView?.setOnScrollChangeListener { v: NestedScrollView, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY > oldScrollY) {
                    if (scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) {
                        loadComments()
                        loading = true
                    }
                }
            }
        }

        reviewAdapter = ReviewRvAdapter(this, reviewList)
        rvReviews?.setAdapter(reviewAdapter)
        sFor = getIntent().getStringExtra("from")

        if (SaveSharedPreference.getCountry(this@Info) == null || SaveSharedPreference.getCountry(
                this@Info
            ).isEmpty()
        ) {
            countryFromIp
        }

        getMovie()
        loadBannerAd()

        stickybannerAdView = findViewById(R.id.stickybannerAdView)

        // Add a global layout listener to detect configuration changes
        val layoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            val orientation = resources.configuration.orientation
            isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE
            initializeBrightnessManager()
            setBannerHeight()
            if (sMovie != null && imgBanner != null) {
                if (sMovie!!.getsBigBanner() != null && !sMovie!!.getsBigBanner()
                        .equals("", ignoreCase = true)
                ) {
                    Glide.with(imgBanner!!.context).load(sMovie!!.getsBigBanner()).centerCrop()
                        .into(imgBanner!!)
                } else {
                    Glide.with(imgBanner!!.context).load(R.drawable.not_img).centerCrop()
                        .into(imgBanner!!)
                }

                tvName?.text = sMovie!!.getsName()
            }
        }

        btnDownloadProgress?.visibility = View.GONE
        btnDownloadProgress?.setProgress(0)

        // Register the layout listener
        window.decorView.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)

        checkDownloadedView()

        downloadsViewModel?.allProgress?.observe(this@Info) {
            val list = ArrayList(it)
            downloadVideoProgress = list.firstOrNull { it.videoId == sMovie!!.getsVedioId() }
            if (downloadVideoProgress != null
                && downloadVideoProgress!!.mobileNumber.equals(
                    SaveSharedPreference.getMobileNumber(
                        this@Info
                    ), true
                )
            ) {
                if (downloadVideoProgress!!.progress == 100) {
                    isDownloadProgress = false
                    btnDownload?.text = "Downloaded"
                    btnDownloadProgress?.visibility = View.GONE
                    checkDownloadedView()
                } else {
                    isDownloadProgress = true
                    btnDownload?.text = "Downloading..."
                    btnDownloadProgress?.visibility = View.VISIBLE
                    btnDownloadProgress?.setProgress(downloadVideoProgress!!.progress)
                }
            } else {
                isDownloadProgress = false
                btnDownloadProgress?.visibility = View.GONE
                if (downloadVideo == null)
                    initialDownloadButton()
            }
        }

        rlDownload?.setOnClickListener {
            SecurityChecks.getInstance().checkSecurity(this) { result: Boolean, _: String? ->
                if (!result) {
                    //don't make download file
                    showAlertDialog(null)
                    return@checkSecurity
                } else {
                    lifecycleScope.launch(Dispatchers.Main) {
                        if (downloadVideo != null || isDownloadProgress) {
                            showDownloadPopup()
                        } else {
                            checkForDownload()
                        }
                    }
                }
            }
        }

        if (sMovie!!.downloadVideoUrl.isNullOrEmpty()) {
            rlDownload?.visibility = View.GONE
        }

        progressReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (intent.action == "DOWNLOAD_PROGRESS") {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        handleDownloadProgress(intent)
                    } else {
                        handleDownloadProgressIn(intent)
                    }
                }
            }
        }

        deleteProgressReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (intent.action == "DELETE_PROGRESS") {
                    CoroutineScope(Dispatchers.IO).launch {
                        val downloadProgressList = downloadsViewModel?.allProgress?.value
                        if (!downloadProgressList.isNullOrEmpty()) {
                            val taskId = intent.getStringExtra("taskId")
                            val videoId = intent.getStringExtra("videoId")
                            val downloadInfo =
                                downloadProgressList.firstOrNull { it.taskId == taskId || it.videoId == videoId }
                            if (downloadInfo != null) {
                                viewModel!!.delete(downloadInfo)
                            }
                        }
                    }
                }
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            progressReceiver as BroadcastReceiver, IntentFilter("DOWNLOAD_PROGRESS")
        )

        LocalBroadcastManager.getInstance(this).registerReceiver(
            deleteProgressReceiver as BroadcastReceiver, IntentFilter("DELETE_PROGRESS")
        )
    }

    private fun showAlertDialog(message: String?) {
        runOnUiThread {
            val adExp = AlertDialog.Builder(this@Info).create()
            adExp.setTitle(getString(R.string.common_permission_alert))
            adExp.setMessage(message ?: getString(R.string.root_check_failure))
            adExp.setButton(
                DialogInterface.BUTTON_POSITIVE, "OK"
            ) { _: DialogInterface?, _: Int -> adExp.dismiss() }
            adExp.show()
        }
    }

    private fun checkDownloadedView(isFromDownloadPlay: Boolean = false) {
        lifecycleScope.launch {
            downloadsViewModel?.downloadedList?.collect { downloads: List<Download>? ->
                val list = ArrayList(downloads!!)
                val download =
                    list.firstOrNull { it.videoId == sMovie?.getsVedioId() && it.downloadedPath.isNotEmpty() && it.downloadedPath != "null" }

                println("Download List:: $downloads")
                println("Downloads:: $download")

                if (download != null) {
                    val file = File(download.downloadedPath)
                    if (file.exists()
                        && download.mobileNumber.equals(
                            SaveSharedPreference.getMobileNumber(this@Info),
                            true
                        )
                    ) {
                        val days = getDaysDifference(Date().time, download.expiryDate)
                        if (days <= 0) {
                            downloadsViewModel!!.startDelete(download.id, this@Info)
                            this@Info.downloadVideo = null
                        } else {

                            this@Info.downloadVideo = download
                            if (isVideoAvailableForUser()) {
                                btnDownloadProgress?.visibility = View.GONE
                                isDownloadProgress = false
                                btnDownload?.text = "Downloaded"
                                if (isFromDownloadPlay) {
                                    rlDownload?.performClick()
                                }
                            } else {
                                this@Info.downloadVideo = null
                            }
                        }
                    }
                } else {
                    this@Info.downloadVideo = null
                }

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun handleDownloadProgress(intent: Intent) {
        val downloadInfoArrayList: ArrayList<DownloadProgress>? =
            intent.getParcelableArrayListExtra("downloadList", DownloadProgress::class.java)
        downloadInfoArrayList?.forEach { progress ->
            CoroutineScope(Dispatchers.IO).launch {
                viewModel?.update(progress)
            }
        }
    }

    @Suppress("DEPRECATION")
    fun handleDownloadProgressIn(intent: Intent) {
        val downloadInfoArrayList: ArrayList<DownloadProgress>? =
            intent.getParcelableArrayListExtra("downloadList")
        downloadInfoArrayList?.forEach { progress ->
            CoroutineScope(Dispatchers.IO).launch {
                viewModel?.update(progress)
            }
        }
    }

    private fun showDownloadPopup() {
        val popup = PopupMenu(this@Info, btnDownload!!)
        popup.menuInflater.inflate(R.menu.menu_download, popup.menu)
        if (isDownloadProgress) {
            popup.menu.findItem(R.id.menu_play).setVisible(false)
            popup.menu.findItem(R.id.menu_delete).setVisible(false)
            popup.menu.findItem(R.id.menu_cancel).setVisible(true)
        } else {
            popup.menu.findItem(R.id.menu_cancel).setVisible(false)
            popup.menu.findItem(R.id.menu_play).setVisible(true)
            popup.menu.findItem(R.id.menu_delete).setVisible(true)
        }



        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_delete -> {
                    if (downloadVideo != null) {
                        val dialog = Dialog((this@Info))
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog.setContentView(R.layout.dialog_confirmation)

                        val txtTitle: TextView = dialog.findViewById(R.id.txtTitle)
                        val txtDesc: TextView = dialog.findViewById(R.id.txtDesc)
                        txtTitle.text = "Confirm"
                        txtDesc.text = "Are you sure you want to delete this video?"
                        val txtNO: TextView = dialog.findViewById(R.id.txtNO)
                        val txtOK: TextView = dialog.findViewById(R.id.txtOK)
                        txtNO.setOnClickListener { dialog.dismiss() }
                        txtOK.setOnClickListener {
                            downloadsViewModel?.startDelete(downloadVideo!!.id, dialog.context)
                            initialDownloadButton()
                            dialog.dismiss()
                        }

                        dialog.show()
                        dialog.window?.setLayout(
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT
                        )
                        dialog.window?.setBackgroundDrawable(
                            ColorDrawable(
                                Color.TRANSPARENT
                            )
                        )
                    }
                }

                R.id.menu_cancel -> {
                    if (downloadVideoProgress != null) {
                        val cancelIntent = Intent(this@Info, CancelReceiver::class.java)
                        cancelIntent.putExtra("taskId", downloadVideoProgress!!.taskId)
                        cancelIntent.putExtra(
                            "notificationId",
                            downloadVideoProgress!!.taskId.hashCode()
                        )
                        sendBroadcast(cancelIntent)
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel!!.delete(downloadVideoProgress!!)
                        }
                        initialDownloadButton()
                    }
                }

                R.id.menu_play -> {
                    if (isVideoAvailableForUser()) {
                        DownloadedVideoInfo.playDownloadVideo(
                            this,
                            videoURL = downloadVideo!!.downloadedPath,
                            ext = downloadVideo!!.ext,
                            videoTitle = downloadVideo!!.name,
                        )
                    }
                }
            }
            true
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true)
        }
        popup.show()
    }

    private fun isVideoAvailableForUser(): Boolean {
        if (downloadVideo != null) {
            val file = File(downloadVideo!!.downloadedPath)
            return file.exists() && downloadVideo!!.mobileNumber == SaveSharedPreference.getMobileNumber(
                this
            )
        }
        return false
    }

    private fun initialDownloadButton() {
        this.downloadVideo = null
        btnDownload?.text = "Download"
        btnDownloadProgress?.visibility = View.GONE
    }

    private var mVideoInfo: VideoInfo? = null

    private fun initDownload() {
        showDownloadToast("Please wait", false)

        viewModel!!.vidFormats.observe(this@Info) { videoInfo ->
            if (videoInfo?.formats == null) {
                return@observe
            }

            videoInfo.formats!!.removeIf {
                !it.ext!!.contains("mp4") || it.format!!.contains("audio")
            }

            val namesAlreadySeen: MutableSet<String> = HashSet()
            videoInfo.formats!!.removeIf { p: VideoFormat ->
                !namesAlreadySeen.add(convertSolution(p.format!!))
            }

            mVideoInfo = videoInfo

            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    viewModel?.selectedItem = VidInfoItem.VidFormatItem(
                        mVideoInfo!!, mVideoInfo!!.formats?.first()?.formatId!!
                    )

                    val path: String? =
                        PreferenceManager.getDefaultSharedPreferences(this@Info)
                            .getString(getString(R.string.download_location_key), null)

                    Log.d(TAG, "Download Path $path")

                    if (path == null) {
                        val customDir =
                            File(applicationContext.cacheDir, DOWNLOAD_FOLDER_NAME)
                        if (!customDir.exists()) {
                            customDir.mkdirs()
                        }

                        if (customDir.exists()) {
                            Log.d(TAG, "Directory Exists ${customDir.path}")

                            setDefaultDownloadLocation(customDir.path)
                            showDownloadToast(
                                "Starting Download, Don't Disconnect to Internet !!",
                                true
                            )
                            viewModel?.selectedItem?.let {
                                viewModel!!.startDownload(
                                    it, customDir.path, this@Info
                                )
                            }
                        }
                    } else {
                        Log.d(TAG, "Starting Download")
                        showDownloadToast(
                            "Starting Download, Don't Disconnect to Internet !!",
                            true
                        )
                        viewModel?.selectedItem?.let {
                            viewModel!!.startDownload(it, path, this@Info)
                        }
                    }
                }
            }
        }
    }

    private fun showDownloadToast(message: String, changeButton: Boolean) {
        val inflater: LayoutInflater = layoutInflater
        val layout: View = inflater.inflate(
            R.layout.toast_download, findViewById<View>(R.id.toast_layout_root) as ViewGroup?
        )

        val tvToast = layout.findViewById<TextView>(R.id.tvToast)
        tvToast.text = message

        val toast = Toast(this@Info)
        toast.setGravity(Gravity.BOTTOM, 0, 250)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()

        if (changeButton) {
            btnDownload?.text = "Download Started"
            btnDownloadProgress!!.setProgress(0)
        }
    }

    private fun checkForDownload() {

        if (sMovie!!.downloadVideoUrl.isNullOrEmpty()) {
            Toast.makeText(this@Info, "Video not allowed to download", Toast.LENGTH_LONG).show()
            return
        }

        val listOfStringFromString = StringUtility.getListOfStringFromString(
            sMovie!!.getsRegion()
        )

        for (i in listOfStringFromString.indices) {
            if (SaveSharedPreference.getCountry(this)
                    .isNotEmpty() && (SaveSharedPreference.getCountry(this).equals(
                    listOfStringFromString[i], ignoreCase = true
                ) || listOfStringFromString[i].equals(
                    "Worldwide", ignoreCase = true
                ) || isContentAvailable)
            ) {
                isContentAvailable = true

                if (sMovie!!.getsComingSoon() != null && sMovie!!.getsComingSoon()
                        .equals("yes", ignoreCase = true)
                ) {
                    val adExp = AlertDialog.Builder(this@Info).create()
                    adExp.setMessage(getString(R.string.movie_coming_soon))
                    adExp.setButton(
                        DialogInterface.BUTTON_POSITIVE, getString(R.string.ok)
                    ) { _: DialogInterface?, _: Int -> adExp.dismiss() }
                    adExp.show()

                } else if (SaveSharedPreference.getLoginFromGoogle(this@Info)) {
                    //disable button
                    GlobalModule.startActivity = Info::class.java.simpleName
                    isDownloadLoginAfterGoogle = true
                    val intent = Intent(this, Register::class.java)
                    startActivity(intent)
                } else {
                    if (sMovie!!.getsAllowedInPackage().equals(API.ACTIVE, ignoreCase = true)) {
                        if (sMovie!!.getsType() != null && sMovie!!.getsType() == "5") {
                            if (SaveSharedPreference.getWebRemDays(this) > 0 || SaveSharedPreference.getWebTimeDiff(
                                    this
                                ) > 0
                            ) {
                                initDownload()
                            } else if (sMovie!!.subscriptions != null && (sMovie!!.daysdiff > 0 || sMovie!!.timediff > 0)) {
                                initDownload()
                            } else {
                                showSubsBottomSheet(sMovie)
                            }
                        } else {
                            if (SaveSharedPreference.getRemDays(this) > 0 || SaveSharedPreference.getTimeDiff(
                                    this
                                ) > 0
                            ) {
                                initDownload()
                            } else if (sMovie!!.subscriptions != null && (sMovie!!.daysdiff > 0 || sMovie!!.timediff > 0)) {
                                initDownload()
                            } else {
                                showSubsBottomSheet(sMovie)
                            }
                        }
                    } else {
                        if (sMovie!!.subscriptions != null && (sMovie!!.daysdiff > 0 || sMovie!!.timediff > 0)) {
                            initDownload()
                        } else {
                            showSubsBottomSheet(sMovie)
                        }
                    }
                }

                break
            }
        }

        if (!isContentAvailable) {
            showContentNotAvailableDialog()
        }
    }

    private fun showMoreBottomSheet(sMovie: Movies?) {
        if (moreBottomDialogFragment == null) {
            moreBottomDialogFragment = MoreBottomDialogFragment.newInstance(sMovie)
            moreBottomDialogFragment!!.show(supportFragmentManager, MoreBottomDialogFragment.TAG)
        } else if (!moreBottomDialogFragment!!.isVisible) {
            moreBottomDialogFragment!!.show(supportFragmentManager, MoreBottomDialogFragment.TAG)
        }
    }

    fun showSubsBottomSheet(sMovie: Movies?) {
        val intent = Intent(this, Subscription::class.java)
        intent.putExtra(MOVIESLIST, sMovie)
        if (bundleResponse != null) {
            intent.putExtra(BUNDLEPACKAGE, bundleResponse)
        }
        startActivity(intent)
        finish()
    }

    private fun setBannerHeight() {
        val width = screenWidth
        val height: Int

        if (isLandscape) {
            height = (screenHeight * 0.8).toInt()
            val layoutParams = llBottomLayout!!.layoutParams as RelativeLayout.LayoutParams
            layoutParams.topMargin = (screenHeight * 0.5).toInt()
            llBottomLayout!!.requestLayout()
        } else {
            height = ((screenWidth / 100) * 158)
        }

        val parms = RelativeLayout.LayoutParams(width, height)
        imgBanner!!.layoutParams = parms
    }

    private fun onPlay() {
        val listOfStringFromString = StringUtility.getListOfStringFromString(
            sMovie!!.getsRegion()
        )

        for (i in listOfStringFromString.indices) {

            if (SaveSharedPreference.getCountry(this)
                    .isNotEmpty() && (SaveSharedPreference.getCountry(this).equals(
                    listOfStringFromString[i], ignoreCase = true
                ) || listOfStringFromString[i].equals(
                    "Worldwide", ignoreCase = true
                ) || isContentAvailable)
            ) {

                isContentAvailable = true

                if (mPublisherInterstitialAd != null) {
                    googleAdAnalyticsLoad(preroll1)
                    mPublisherInterstitialAd!!.show(this@Info)
                } else if (mPublisherInterstitialAdNew != null) {
                    mPublisherInterstitialAdNew!!.show(this@Info)
                } else {

                    if (shouldLoadAd) {
                        showProgressDialog()
                    } else {
                        if (sMovie!!.getsComingSoon() != null && sMovie!!.getsComingSoon()
                                .equals("yes", ignoreCase = true)
                        ) {
                            val adExp = AlertDialog.Builder(this@Info).create()
                            adExp.setMessage(getString(R.string.movie_coming_soon))
                            adExp.setButton(
                                DialogInterface.BUTTON_POSITIVE, getString(R.string.ok)
                            ) { _: DialogInterface?, _: Int -> adExp.dismiss() }
                            adExp.show()
                        } else if (sMovie!!.getiSfree() != null && sMovie!!.getiSfree()
                                .equals("yes", ignoreCase = true)
                        ) {
                            init(sMovie!!.videoUrl.replace(" ".toRegex(), "%20"), false, 0, false)
                        } else if (SaveSharedPreference.getLoginFromGoogle(this@Info)) {
                            GlobalModule.startActivity = Info::class.java.simpleName
                            isLoginAfterGoogle = true
                            val intent = Intent(this@Info, Register::class.java)
                            startActivity(intent)
                        } else {
                            if (sMovie!!.getsAllowedInPackage()
                                    .equals(API.ACTIVE, ignoreCase = true)
                            ) {
                                if (sMovie!!.getsType() != null && sMovie!!.getsType() == "5") {
                                    if (SaveSharedPreference.getWebRemDays(this@Info) > 0 || SaveSharedPreference.getWebTimeDiff(
                                            this@Info
                                        ) > 0
                                    ) {
                                        init(
                                            sMovie!!.videoUrl.replace(" ".toRegex(), "%20"),
                                            false,
                                            0,
                                            true
                                        )
                                    } else if (sMovie!!.subscriptions != null && (sMovie!!.daysdiff > 0 || sMovie!!.timediff > 0)) {
                                        init(
                                            sMovie!!.videoUrl.replace(" ".toRegex(), "%20"),
                                            false,
                                            0,
                                            true
                                        )
                                    } else {
                                        showSubsBottomSheet(sMovie)
                                    }
                                } else {
                                    if (SaveSharedPreference.getRemDays(this@Info) > 0 || SaveSharedPreference.getTimeDiff(
                                            this@Info
                                        ) > 0
                                    ) {
                                        init(
                                            sMovie!!.videoUrl.replace(" ".toRegex(), "%20"),
                                            false,
                                            0,
                                            true
                                        )
                                    } else if (sMovie!!.subscriptions != null && (sMovie!!.daysdiff > 0 || sMovie!!.timediff > 0)) {
                                        init(
                                            sMovie!!.videoUrl.replace(" ".toRegex(), "%20"),
                                            false,
                                            0,
                                            true
                                        )
                                    } else {
                                        showSubsBottomSheet(sMovie)
                                    }
                                }
                            } else {
                                if (sMovie!!.subscriptions != null && (sMovie!!.daysdiff > 0 || sMovie!!.timediff > 0)) {
                                    init(
                                        sMovie!!.videoUrl.replace(" ".toRegex(), "%20"),
                                        false,
                                        0,
                                        true
                                    )
                                } else {
                                    showSubsBottomSheet(sMovie)
                                }
                            }
                        }
                    }
                }
                break
            }
        }
        if (!isContentAvailable) {
            showContentNotAvailableDialog()
        }
    }

    private fun showContentNotAvailableDialog() {
        val adExp = AlertDialog.Builder(this@Info).create()
        val msg =
            getString(R.string.sorry) + sMovie!!.getsName() + getString(R.string.not_available) + " - " + SaveSharedPreference.getCountry(
                this@Info
            )
        adExp.setMessage(msg)
        adExp.setCancelable(true)
        adExp.setCanceledOnTouchOutside(true)
        adExp.setButton(
            DialogInterface.BUTTON_POSITIVE, getString(R.string.ok)
        ) { _, _ -> adExp.dismiss() }
        adExp.show()
    }

    private val countryFromIp: Unit
        get() {
            val gson = GsonBuilder().setLenient().create()
            val httpClient = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                httpClient.addInterceptor(loggingInterceptor)
            }
            httpClient.readTimeout(90, TimeUnit.SECONDS)
            httpClient.connectTimeout(90, TimeUnit.SECONDS)
            val retrofit = Retrofit.Builder().baseUrl("http://ip-api.com/")
                .addConverterFactory(GsonConverterFactory.create(gson)).client(httpClient.build())
                .build()

            val webApi = retrofit.create(WebApi::class.java)
            val call = webApi.ipDetails
            call.enqueue(object : Callback<IPResponseModel?> {
                override fun onResponse(
                    call: Call<IPResponseModel?>, response: Response<IPResponseModel?>
                ) {
                    if (response.body() != null) {
                        if (response.body()!!.country != null) {
                            SaveSharedPreference.setCountry(this@Info, response.body()!!.country)
                        }
                    }
                }

                override fun onFailure(call: Call<IPResponseModel?>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }

    private fun showProgressDialog() {
        dialogProgress = ProgressDialog(this)
        dialogProgress!!.setMessage(getString(R.string.loading_movie))

        try {
            dialogProgress!!.show()
        } catch (e: WindowManager.BadTokenException) {
            e.printStackTrace()
        }

        val progressRunnable = Runnable {
            dialogProgress!!.cancel()
            llPlayMovie!!.performClick()
        }

        val pdCanceller = Handler()
        pdCanceller.postDelayed(progressRunnable, 7000)
    }

    private fun showRewardedVideo() {
        if (mPublisherInterstitialAd2 != null) {
            googleAdAnalyticsLoad(preroll2)
            mPublisherInterstitialAd2!!.show(this@Info)
        } else {
            if (isEpisodeAdPlayed) {
                init(tempVideoURL, tempIsTrailer, tempPosition, tempIsPremium)
                isWebAdRequested = false
            } else {
                isClickProg = true
                llPlayMovie!!.performClick()
            }
        }
    }

    private fun loadRewardedVideo() {
        if (SaveSharedPreference.getAdsData(this@Info) != null) {
            if (SaveSharedPreference.getAdsData(this@Info).gStatus != null && SaveSharedPreference.getAdsData(
                    this@Info
                ).gStatus == API.ACTIVE
            ) {
                googleAdAnalyticsRequest(
                    SaveSharedPreference.getAdsData(this@Info).infoInterstial2, API.PRE_ROLL_2
                )
                val adRequest = AdRequest.Builder().build()
                InterstitialAd.load(this,
                    SaveSharedPreference.getAdsData(this@Info).infoInterstial2,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            super.onAdFailedToLoad(loadAdError)
                            shouldLoadAd = false
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            super.onAdLoaded(interstitialAd)

                            shouldLoadAd = false
                            googleAdAnalyticsResponse(preroll2)
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mPublisherInterstitialAd2 = interstitialAd
                            mPublisherInterstitialAd2!!.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        shouldLoadAd = false
                                        if (mPublisherInterstitialAd != null) {
                                            googleAdAnalyticsLoad(preroll3)
                                            mPublisherInterstitialAd!!.show(this@Info)
                                        } else if (mPublisherInterstitialAdNew != null) {
                                            mPublisherInterstitialAdNew!!.show(this@Info)
                                        } else {
                                            if (isEpisodeAdPlayed) {
                                                init(
                                                    tempVideoURL,
                                                    tempIsTrailer,
                                                    tempPosition,
                                                    tempIsPremium
                                                )
                                                isWebAdRequested = false
                                            } else {
                                                isClickProg = true
                                                llPlayMovie!!.performClick()
                                            }
                                        }
                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                        shouldLoadAd = false
                                    }

                                    override fun onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mPublisherInterstitialAd2 = null
                                        shouldLoadAd = false
                                        googleAdAnalyticsResponse(preroll2)
                                    }
                                }
                        }
                    })
            } else {
                shouldLoadAd = false
            }
        } else {
            shouldLoadAd = false
        }
    }

    fun loadInterstitialAd() {
        if (SaveSharedPreference.getAdsData(this@Info) != null) {
            if (SaveSharedPreference.getAdsData(this@Info).gStatus != null && SaveSharedPreference.getAdsData(
                    this@Info
                ).gStatus == API.ACTIVE
            ) {
                isPreRollThird = false
                googleAdAnalyticsRequest(
                    SaveSharedPreference.getAdsData(this@Info).gInterstial, API.PRE_ROLL_1
                )
                val adRequest = AdRequest.Builder().build()

                InterstitialAd.load(this,
                    SaveSharedPreference.getAdsData(this@Info).gInterstial,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            mPublisherInterstitialAd = interstitialAd
                            mPublisherInterstitialAd!!.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        if (isAdforTrailer) {
                                            isAdforTrailer = false
                                            //                                            loadInterstitialAd();
                                            init(
                                                sMovie!!.getsTrailer()
                                                    .replace(" ".toRegex(), "%20"), true, 0, false
                                            )
                                        } else {
                                            shouldLoadAd = false
                                            if (!isLoadedSecondTime) {
                                                isLoadedSecondTime = true
                                                showRewardedVideo()
                                                isPreRollThird = true
                                                googleAdAnalyticsRequest(
                                                    SaveSharedPreference.getAdsData(this@Info).gInterstial,
                                                    API.PRE_ROLL_3
                                                )
                                                loadInterstitialAd()
                                            } else {
                                                if (isEpisodeAdPlayed) {
                                                    init(
                                                        tempVideoURL,
                                                        tempIsTrailer,
                                                        tempPosition,
                                                        tempIsPremium
                                                    )
                                                    isWebAdRequested = false
                                                } else {
                                                    isClickProg = true
                                                    llPlayMovie!!.performClick()
                                                }
                                            }
                                        }
                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                        shouldLoadAd = false
                                        if (dialogProgress!!.isShowing) {
                                            dialogProgress!!.dismiss()
                                            isClickProg = true
                                            llPlayMovie!!.performClick()
                                        }
                                    }

                                    override fun onAdShowedFullScreenContent() {
                                        mPublisherInterstitialAd = null
                                        shouldLoadAd = false
                                        if (isPreRollThird) {
                                            googleAdAnalyticsResponse(preroll3)
                                        } else {
                                            googleAdAnalyticsResponse(preroll1)
                                        }
                                        if (dialogProgress!!.isShowing) {
                                            dialogProgress!!.dismiss()
                                            isClickProg = true
                                            llPlayMovie!!.performClick()
                                        }
                                    }
                                }
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            mPublisherInterstitialAd = null
                            shouldLoadAd = false
                            if (dialogProgress!!.isShowing) {
                                dialogProgress!!.dismiss()
                                isClickProg = true
                                llPlayMovie!!.performClick()
                            }
                        }
                    })
            } else if (SaveSharedPreference.getAdsData(this@Info).mopubStatus != null && (SaveSharedPreference.getAdsData(
                    this@Info
                ).mopubStatus == API.ACTIVE)
            ) {
                val adRequest = AdManagerAdRequest.Builder().build()

                AdManagerInterstitialAd.load(this,
                    SaveSharedPreference.getAdsData(this@Info).mopubInterstial,
                    adRequest,
                    object : AdManagerInterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: AdManagerInterstitialAd) {
                            // The mAdManagerInterstitialAd reference will be null until
                            // an ad is loaded.
                            Log.i(TAG, "onAdLoaded")
                            mPublisherInterstitialAdNew = interstitialAd
                            mPublisherInterstitialAd!!.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        shouldLoadAd = false
                                        if (!isLoadedSecondTime) {
                                            isLoadedSecondTime = true
                                            showRewardedVideo()
                                            loadInterstitialAd()
                                        } else {
                                            isClickProg = true
                                            llPlayMovie!!.performClick()
                                        }
                                    }
                                }
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            // Handle the error
                            Log.d(TAG, loadAdError.toString())
                            shouldLoadAd = false
                        }
                    })
            } else {
                shouldLoadAd = false
            }
        } else {
            shouldLoadAd = false
        }
    }

    fun loadMovieInterstitial() {
        isLoadinInstream = true
        if (SaveSharedPreference.getAdsData(this@Info) != null) {
            if (SaveSharedPreference.getAdsData(this@Info).gStatus != null && SaveSharedPreference.getAdsData(
                    this@Info
                ).gStatus == API.ACTIVE
            ) {
                googleAdAnalyticsRequest(
                    SaveSharedPreference.getAdsData(this@Info).gMovieInterstial, API.MID_ROLL_1
                )

                val adRequest = AdRequest.Builder().build()

                InterstitialAd.load(this,
                    SaveSharedPreference.getAdsData(this@Info).gMovieInterstial,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            mInstreamAd = interstitialAd
                            mInstreamAd!!.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        mInstreamAd = null
                                        isLoadinInstream = false
                                        showMovieRewardedVideo()
                                        if (sMovie != null && sMovie!!.getiSfree() != null) {
                                            if (sMovie!!.getiSfree().equals(
                                                    "yes", ignoreCase = true
                                                ) && SaveSharedPreference.getLoginFromGoogle(
                                                    this@Info
                                                )
                                            ) {
                                                loadMovieInterstitial3()
                                            } else if (sMovie!!.getiSfree()
                                                    .equals("yes", ignoreCase = true)
                                            ) {
                                                if (!(SaveSharedPreference.getWebRemDays(this@Info) > 0 || SaveSharedPreference.getWebTimeDiff(
                                                        this@Info
                                                    ) > 0)
                                                ) {
                                                    loadMovieInterstitial3()
                                                } else if (sMovie!!.ads_for_paid_movie != null && sMovie!!.ads_for_paid_movie.equals(
                                                        "yes", ignoreCase = true
                                                    )
                                                ) {
                                                    if (sMovie!!.ads_paid_movie_count != null) {
                                                        if (sMovie!!.ads_paid_movie_count.equals(
                                                                "3", ignoreCase = true
                                                            ) || sMovie!!.ads_paid_movie_count.equals(
                                                                "4", ignoreCase = true
                                                            )
                                                        ) {
                                                            loadMovieInterstitial3()
                                                        }
                                                    }
                                                }
                                            } else if (sMovie!!.getiSfree().equals(
                                                    "no", ignoreCase = true
                                                ) && sMovie!!.ads_for_paid_movie != null && sMovie!!.ads_for_paid_movie.equals(
                                                    "yes", ignoreCase = true
                                                )
                                            ) {
                                                if (sMovie!!.ads_paid_movie_count != null) {
                                                    if (sMovie!!.ads_paid_movie_count.equals(
                                                            "3", ignoreCase = true
                                                        ) || sMovie!!.ads_paid_movie_count.equals(
                                                            "4", ignoreCase = true
                                                        )
                                                    ) {
                                                        loadMovieInterstitial3()
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                        isLoadinInstream = false
                                    }

                                    override fun onAdShowedFullScreenContent() {
                                        mInstreamAd = null
                                        googleAdAnalyticsResponse(midroll1)
                                    }
                                }
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            mInstreamAd = null
                        }
                    })
            } else if (SaveSharedPreference.getAdsData(this@Info).mopubStatus != null && (SaveSharedPreference.getAdsData(
                    this@Info
                ).mopubStatus == API.ACTIVE)
            ) {
                val adRequest = AdManagerAdRequest.Builder().build()

                AdManagerInterstitialAd.load(this,
                    SaveSharedPreference.getAdsData(this@Info).mopubInterstial,
                    adRequest,
                    object : AdManagerInterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: AdManagerInterstitialAd) {
                            // The mAdManagerInterstitialAd reference will be null until
                            // an ad is loaded.
                            mPublisherInterstitialAdNew = interstitialAd
                            mPublisherInterstitialAd!!.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        shouldLoadAd = false
                                        if (!isLoadedSecondTime) {
                                            isLoadedSecondTime = true
                                            showRewardedVideo()
                                            //                                            loadMovieInterstitial();
                                        } else {
                                            isClickProg = true
                                            llPlayMovie!!.performClick()
                                        }
                                    }
                                }
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            // Handle the error
                            Log.d(TAG, loadAdError.toString())
                            shouldLoadAd = false
                        }
                    })
            }
        }
    }

    fun loadMovieInterstitial3() {
        if (SaveSharedPreference.getAdsData(this@Info) != null) {
            if (SaveSharedPreference.getAdsData(this@Info).gStatus != null && SaveSharedPreference.getAdsData(
                    this@Info
                ).gStatus == API.ACTIVE
            ) {
                googleAdAnalyticsRequest(
                    SaveSharedPreference.getAdsData(this@Info).gMovieInterstial3, API.MID_ROLL_3
                )
                val adRequest = AdRequest.Builder().build()

                InterstitialAd.load(this,
                    SaveSharedPreference.getAdsData(this@Info).gMovieInterstial3,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInstreamAd3 = interstitialAd
                            mInstreamAd3!!.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        mInstreamAd3 = null
                                        if (mInstreamAd4 != null) {
                                            googleAdAnalyticsLoad(midroll4)
                                            mInstreamAd4!!.show(this@Info)
                                        }
                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    }

                                    override fun onAdShowedFullScreenContent() {
                                        mInstreamAd3 = null
                                        googleAdAnalyticsResponse(midroll_3)
                                    }
                                }
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            mInstreamAd3 = null
                        }
                    })
            }
        }
    }

    fun loadMovieInterstitial4() {
        if (SaveSharedPreference.getAdsData(this@Info) != null) {
            if (SaveSharedPreference.getAdsData(this@Info).gStatus != null && SaveSharedPreference.getAdsData(
                    this@Info
                ).gStatus == API.ACTIVE
            ) {
                googleAdAnalyticsRequest(
                    SaveSharedPreference.getAdsData(this@Info).gMovieInterstial4, API.MID_ROLL_4
                )

                val adRequest = AdRequest.Builder().build()

                InterstitialAd.load(this,
                    SaveSharedPreference.getAdsData(this@Info).gMovieInterstial4,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInstreamAd4 = interstitialAd
                            mInstreamAd4!!.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        mInstreamAd4 = null
                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    }

                                    override fun onAdShowedFullScreenContent() {
                                        mInstreamAd4 = null
                                        googleAdAnalyticsResponse(midroll4)
                                    }
                                }
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            mInstreamAd4 = null
                        }
                    })
            }
        }
    }

    private fun loadMovieRewardedVideo() {
        if (SaveSharedPreference.getAdsData(this@Info) != null) {
            if (SaveSharedPreference.getAdsData(this@Info).gStatus != null && SaveSharedPreference.getAdsData(
                    this@Info
                ).gStatus == API.ACTIVE
            ) {
                googleAdAnalyticsRequest(
                    SaveSharedPreference.getAdsData(this@Info).gMovieInterstial2, API.MID_ROLL_2
                )
                val adRequest = AdRequest.Builder().build()

                InterstitialAd.load(this,
                    SaveSharedPreference.getAdsData(this@Info).gMovieInterstial2,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            mInstreamAd2 = interstitialAd
                            mInstreamAd2!!.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        mInstreamAd2 = null
                                        if (mInstreamAd3 != null) {
                                            googleAdAnalyticsLoad(midroll_3)
                                            mInstreamAd3!!.show(this@Info)
                                        }
                                        if (SaveSharedPreference.getAdsData(this@Info) != null) {
                                            if (SaveSharedPreference.getAdsData(this@Info).gStatus != null && (SaveSharedPreference.getAdsData(
                                                    this@Info
                                                ).gAactivateFourAds == API.ACTIVE)
                                            ) {
                                                if (sMovie != null && sMovie!!.getiSfree() != null) {
                                                    if (sMovie!!.getiSfree().equals(
                                                            "yes", ignoreCase = true
                                                        ) && SaveSharedPreference.getLoginFromGoogle(
                                                            this@Info
                                                        )
                                                    ) {
                                                        loadMovieInterstitial4()
                                                    } else if (sMovie!!.getiSfree()
                                                            .equals("yes", ignoreCase = true)
                                                    ) {
                                                        if (!(SaveSharedPreference.getWebRemDays(
                                                                this@Info
                                                            ) > 0 || SaveSharedPreference.getWebTimeDiff(
                                                                this@Info
                                                            ) > 0)
                                                        ) {
                                                            loadMovieInterstitial4()
                                                        } else if (sMovie!!.ads_for_paid_movie != null && sMovie!!.ads_for_paid_movie.equals(
                                                                "yes", ignoreCase = true
                                                            )
                                                        ) {
                                                            if (sMovie!!.ads_paid_movie_count != null) {
                                                                if (sMovie!!.ads_paid_movie_count.equals(
                                                                        "4", ignoreCase = true
                                                                    )
                                                                ) {
                                                                    loadMovieInterstitial4()
                                                                }
                                                            }
                                                        }
                                                    } else if (sMovie!!.getiSfree().equals(
                                                            "no", ignoreCase = true
                                                        ) && sMovie!!.ads_for_paid_movie != null && sMovie!!.ads_for_paid_movie.equals(
                                                            "yes", ignoreCase = true
                                                        )
                                                    ) {
                                                        if (sMovie!!.ads_paid_movie_count != null) {
                                                            if (sMovie!!.ads_paid_movie_count.equals(
                                                                    "4", ignoreCase = true
                                                                )
                                                            ) {
                                                                loadMovieInterstitial4()
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    }

                                    override fun onAdShowedFullScreenContent() {
                                        mInstreamAd2 = null
                                        googleAdAnalyticsResponse(midroll2)
                                    }
                                }
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            mInstreamAd2 = null
                        }
                    })
            }
        }
    }

    private fun showMovieRewardedVideo() {
        if (mInstreamAd2 != null) {
            googleAdAnalyticsLoad(midroll2)
            mInstreamAd2!!.show(this@Info)
        }
    }

    fun loadCancelInterstitial() {
        isLoadinCancelInstream = true
        if (SaveSharedPreference.getAdsData(this@Info) != null) {
            if (SaveSharedPreference.getAdsData(this@Info).gStatus != null && SaveSharedPreference.getAdsData(
                    this@Info
                ).gStatus == API.ACTIVE
            ) {
                googleAdAnalyticsRequest(
                    SaveSharedPreference.getAdsData(this@Info).gCancelInterstial, API.SKIP_1
                )
                val adRequest = AdRequest.Builder().build()

                InterstitialAd.load(this,
                    SaveSharedPreference.getAdsData(this@Info).gCancelInterstial,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            mCancelAd = interstitialAd
                            mCancelAd!!.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        mCancelAd = null
                                        if (!isVideoCompleted) {
                                            showCancelRewardedVideo()
                                            loadCancelInterstitial()
                                        }
                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    }

                                    override fun onAdShowedFullScreenContent() {
                                        mCancelAd = null
                                        googleAdAnalyticsResponse(skip1)
                                    }
                                }
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            mCancelAd = null
                        }
                    })
            } else if (SaveSharedPreference.getAdsData(this@Info).mopubStatus != null && (SaveSharedPreference.getAdsData(
                    this@Info
                ).mopubStatus == API.ACTIVE)
            ) {
                val adRequest = AdManagerAdRequest.Builder().build()

                AdManagerInterstitialAd.load(this,
                    SaveSharedPreference.getAdsData(this@Info).mopubInterstial,
                    adRequest,
                    object : AdManagerInterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: AdManagerInterstitialAd) {
                            // The mAdManagerInterstitialAd reference will be null until
                            // an ad is loaded.
                            Log.i(TAG, "onAdLoaded")
                            mPublisherInterstitialAdNew = interstitialAd
                            mPublisherInterstitialAd!!.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        shouldLoadAd = false
                                        if (!isLoadedSecondTime) {
                                            isLoadedSecondTime = true
                                            showRewardedVideo()
                                            loadCancelInterstitial()
                                        } else {
                                            isClickProg = true
                                            llPlayMovie!!.performClick()
                                        }
                                    }
                                }
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            // Handle the error
                            Log.d(TAG, loadAdError.toString())
                            shouldLoadAd = false
                        }
                    })
            }
        }
    }

    private fun loadCancelRewardedVideo() {
        if (SaveSharedPreference.getAdsData(this@Info) != null) {
            if (SaveSharedPreference.getAdsData(this@Info).gStatus != null && SaveSharedPreference.getAdsData(
                    this@Info
                ).gStatus == API.ACTIVE
            ) {
                googleAdAnalyticsRequest(
                    SaveSharedPreference.getAdsData(this@Info).gCancelInterstial2, API.SKIP_2
                )
                val adRequest = AdRequest.Builder().build()

                InterstitialAd.load(this,
                    SaveSharedPreference.getAdsData(this@Info).gCancelInterstial2,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            mCancelAd2 = interstitialAd
                            mCancelAd2!!.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        mCancelAd2 = null
                                        loadCancelRewardedVideo()
                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    }

                                    override fun onAdShowedFullScreenContent() {
                                        mCancelAd2 = null
                                        googleAdAnalyticsResponse(skip2)
                                    }
                                }
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            mCancelAd2 = null
                        }
                    })
            }
        }
    }

    private fun showCancelRewardedVideo() {
        if (mCancelAd2 != null) {
            googleAdAnalyticsLoad(skip2)
            mCancelAd2!!.show(this@Info)
        }
    }

    private fun loadTopBannerAd() {
        if (SaveSharedPreference.getAdsData(this@Info) != null) {
            if (SaveSharedPreference.getAdsData(this@Info).gStatus != null && SaveSharedPreference.getAdsData(
                    this@Info
                ).gStatus == API.ACTIVE
            ) {
                stickybannerAdView!!.removeAllViews()
                val adView = AdView(this@Info)
                adView.setAdSize(AdSize.BANNER)
                adView.adUnitId = SaveSharedPreference.getAdsData(this@Info).getgStickyBanner()
                val adRequest = AdRequest.Builder().build()
                stickybannerAdView!!.addView(adView)
                adView.loadAd(adRequest)
            }
        }
    }

    private fun recordScreenView() {
        mFirebaseAnalytics!!.setCurrentScreen(this, null, null) // [END set_current_screen]
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imgvwNextPlay -> {
                nextVideoTimer!!.cancel()
                playNext()
            }

            R.id.txtCancel -> {
                isLocked = false
                ObjPlayerView!!.visibility = View.VISIBLE
                nextvideolayout!!.visibility = View.GONE
            }
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        viewPager!!.currentItem = tab.position
        Log.d(TAG, "onTabSelected: " + showEpisodes + " " + tab.position)
        if (showEpisodes && tab.position == 0) {
            rlViewMore!!.visibility = View.VISIBLE
        } else {
            rlViewMore!!.visibility = View.GONE
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!mExoPlayerFullscreen) {
            val intent = Intent(this@Info, Home::class.java)
            startActivity(intent)
            finish()
        } else {
            isManual = true
            closeFullscreenDialog()
        }
    }

    override fun onPause() {
        if (ObjExoPlayer != null) {
            ObjExoPlayer!!.playWhenReady = false
        }
        validateDeviceAccessInfo(API.DELETE_DEVICE)
        if (totalTimeWatched != 0L) {
            videoAnalyticsCreate(videoType, totalTimeWatched)
        }
        totalTimeWatched = 0
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (ObjExoPlayer != null) {
            ObjExoPlayer!!.playWhenReady = true
            validateDeviceAccessInfo(API.ADD_DEVICE)
            networkStatus
        } else {
            if (isLoginAfterGoogle) {
                isLoginAfterGoogle = false
                if (!SaveSharedPreference.getLoginFromGoogle(this@Info)) {
                    llPlayMovie!!.performClick()
                }
            } else if (isDownloadLoginAfterGoogle) {
                isDownloadLoginAfterGoogle = false
                checkDownloadedView(true)
//                if (!SaveSharedPreference.getLoginFromGoogle(this@Info)) {
//                    rlDownload!!.performClick()
//                }
            }
        }
    }

    public override fun onDestroy() {
        if (stickybannerAdView != null) {
            stickybannerAdView!!.removeAllViews()
        }
        if (bannerAdView != null) {
            bannerAdView!!.removeAllViews()
        }
        destroyBannerAds()
        releasePlayer()
        finish()
        super.onDestroy()
    }

    private fun addWishList() {
        val webApi = getInstance()!!.createRetrofitNewInstance()
        val call = webApi.addWishList(
            SaveSharedPreference.getUserId(this@Info), sMovie!!.getsVedioId()
        )
        call.enqueue(object : Callback<GeneralResponseModel?> {
            override fun onResponse(
                call: Call<GeneralResponseModel?>, response: Response<GeneralResponseModel?>
            ) {
                if (!this@Info.isFinishing) {
                    if (dialog != null && dialog!!.isShowing) {
                        dialog!!.dismiss()
                    }
                }
                if (response.body() != null && response.body()!!.status != null) {
                    if (response.body()!!.status.equals(
                            API.SUCCESSFULL_STATUS, ignoreCase = true
                        )
                    ) {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.added_to_watchlist),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.already_in_watchlist),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.already_in_watchlist),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<GeneralResponseModel?>, t: Throwable) {
                t.printStackTrace()
                if (!this@Info.isFinishing) {
                    if (dialog != null && dialog!!.isShowing) {
                        dialog!!.dismiss()
                    }
                }
                Toast.makeText(
                    applicationContext, getString(R.string.already_in_watchlist), Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    override fun finishActivity(activity: Activity) {
        if (!activity.isDestroyed) {
            activity.finish()
        }
    }

    private fun releasePlayer() {
        if (ObjExoPlayer != null) {
            ObjExoPlayer!!.stop()
            ObjExoPlayer!!.release()
            ObjExoPlayer = null
        }
        if (handler != null) {
            handler = null
        }
        finish()
    }

    private fun addHistory() {
        val webApi = getInstance()!!.createRetrofitNewInstance()
        val call = webApi.addHistory(
            SaveSharedPreference.getUserId(this@Info), sMovie!!.getsVedioId(), "0"
        )
        call.enqueue(object : Callback<GeneralResponseModel?> {
            override fun onResponse(
                call: Call<GeneralResponseModel?>, response: Response<GeneralResponseModel?>
            ) {
                if (dialog != null && dialog!!.isShowing) {
                    dialog!!.dismiss()
                }
                if (response.body() != null && response.body()!!.status != null) {
                    if (response.body()!!.status.equals(
                            API.SUCCESSFULL_STATUS, ignoreCase = true
                        )
                    ) {
                        //Toast.makeText(getApplicationContext(),"Added To WatchList",Toast.LENGTH_LONG).show();
                    } else {
                        // Toast.makeText(getApplicationContext(),"Already In Wishlist",Toast.LENGTH_LONG).show();
                    }
                } else {
                    //Toast.makeText(getApplicationContext(),"Already In Wishlist",Toast.LENGTH_LONG).show();
                }
            }

            override fun onFailure(call: Call<GeneralResponseModel?>, t: Throwable) {
                t.printStackTrace()
                if (dialog != null && dialog!!.isShowing) {
                    dialog!!.dismiss()
                }
                // Toast.makeText(getApplicationContext(),"Already In Wishlist",Toast.LENGTH_LONG).show();
            }
        })
    }

    private fun getMovie() {
        val webApi = getInstance()!!.createRetrofitNewInstance()
        val call = webApi.getMovie(
            sMovie!!.getsVedioId(),
            SaveSharedPreference.getUserId(this@Info),
            getInstance()!!.versionCode
        )

        call.enqueue(object : Callback<VideoResponseModel?> {
            override fun onResponse(
                call: Call<VideoResponseModel?>, response: Response<VideoResponseModel?>
            ) {
                if (response.body() != null && response.body()!!.video != null && !isFinishing) {
                    sMovie = response.body()!!.video
                    viewModel?.updateMovieData(sMovie)
                    setData()
                    progress_bar!!.visibility = View.GONE
                    llBottomLayout!!.visibility = View.VISIBLE
                    markPaidUser()
                    callBundleDetails()
                    if (isAfterPayment) {
                        isAfterPayment = false
                        llPlayMovie!!.performClick()
                    }
                } else {
                    progress_bar!!.visibility = View.GONE
                    Toast.makeText(
                        applicationContext, getString(R.string.failed), Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<VideoResponseModel?>, t: Throwable) {
                t.printStackTrace()
                progress_bar!!.visibility = View.GONE
                Toast.makeText(
                    applicationContext, getString(R.string.failed), Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun callBundleDetails() {
        if (sMovie != null && sMovie!!.getiSfree() != null && sMovie!!.getiSfree().equals(
                "no", ignoreCase = true
            ) && sMovie!!.getsComingSoon() != null && sMovie!!.getsComingSoon()
                .equals("no", ignoreCase = true)
        ) {
            val webApi = getInstance()!!.createRetrofitNewInstance()
            val call = webApi.getBundlePackages(sMovie!!.getsVedioId())
            call.enqueue(object : Callback<BundlePackageResponseModel?> {
                override fun onResponse(
                    call: Call<BundlePackageResponseModel?>,
                    response: Response<BundlePackageResponseModel?>
                ) {
                    if (response.body() != null && response.body()!!.status) {
                        bundleResponse = response.body()
                    }
                }

                override fun onFailure(call: Call<BundlePackageResponseModel?>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    private fun setData() {
        if (sMovie!!.getsComingSoon() != null && sMovie!!.getsComingSoon()
                .equals("yes", ignoreCase = true)
        ) {
            llPlayMovie!!.text = getString(R.string.coming_soon)
            llPlayMovie!!.isEnabled = true
        } else {
            llPlayMovie!!.text = getString(R.string.play)
            llPlayMovie!!.isEnabled = true
        }
        if (sMovie!!.getsType() != null && sMovie!!.getsType() == "5") {
            tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.episodes)))
            tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.more_details)))
            tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
            showEpisodes = true
        } else {
            tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.more_details)))
            tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
            showEpisodes = false
        }
        adapter = InfoAdapter(supportFragmentManager, tabLayout!!.tabCount, sMovie, showEpisodes)
        viewPager!!.adapter = adapter
        if (showEpisodes) {
            rlViewMore!!.visibility = View.VISIBLE
        }

        tabLayout!!.setOnTabSelectedListener(this@Info)
        addPreRollAds()

        loadComments()

        if (sMovie != null) {
            if (sMovie!!.getsBigBanner() != null && !sMovie!!.getsBigBanner()
                    .equals("", ignoreCase = true)
            ) {
                Glide.with(imgBanner!!.context).load(sMovie!!.getsBigBanner()).centerCrop()
                    .centerCrop().into(imgBanner!!)
            } else {
                Glide.with(imgBanner!!.context).load(R.drawable.not_img).centerCrop().centerCrop()
                    .into(
                        imgBanner!!
                    )
            }

            //dynamic Price Set
            if (sMovie!!.movieprices != null && sMovie!!.packageModels != null && sMovie!!.packageModels.isNotEmpty()) {
                for (i in sMovie!!.packageModels.indices) {
                    if (!sMovie!!.packageModels[i].isPackage.equals(
                            API.ACTIVE, ignoreCase = true
                        )
                    ) {
                        sMovie!!.packageModels[i].price = sMovie!!.movieprices.sAmount
                        sMovie!!.packageModels[i].setiPriceWithPackage(sMovie!!.movieprices.getiPriceWithPackage())
                    }
                }
            }

            tvName!!.text = sMovie!!.getsName()
            tvSummary!!.text = sMovie!!.getsSummary()

            llPlayMovie!!.setOnClickListener {
                if (!isClickProg) {
                    videoAnalyticsCreate(API.VIDEO_ANALYTICS_PLAY, 0)
                }
                SaveSharedPreference.getCountry(this@Info)
                if (SaveSharedPreference.getCountry(this@Info).isNotEmpty()) {
                    onPlay()
                }
                isClickProg = false
            }

            btnTrailer!!.setOnClickListener {
                if (VideoURL == null || VideoURL!!.isEmpty()) {
                    if (sMovie!!.getsTrailer() != null) {
                        if (mPublisherInterstitialAd != null) {
                            isAdforTrailer = true
                            googleAdAnalyticsLoad(preroll1)
                            mPublisherInterstitialAd!!.show(this@Info)
                        } else {
                            init(
                                sMovie!!.getsTrailer().replace(" ".toRegex(), "%20"), true, 0, false
                            )
                        }
                    }
                } else {
                    Toast.makeText(this@Info, getString(R.string.video_playing), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            llWatchList!!.setOnClickListener {
                dialog =
                    GlobalModule.showProgressDialog(getString(R.string.add_to_watch), this@Info)
                addWishList()
            }

            llShare!!.setOnClickListener {
                val sUrl =
                    "https://play.google.com/store/apps/details?id=com.idragonpro.andmagnus&hl=en" + sMovie!!.getsVedioId()

                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.setType("text/plain")
                sharingIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.hey_watch) + sMovie!!.getsName() + getString(R.string.check_out_idragon) + sUrl
                )
                sharingIntent.putExtra("from", "info")
                startActivity(Intent.createChooser(sharingIntent, "Share via"))
            }
            if (sMovie != null && sMovie!!.allepisodes != null && sMovie!!.allepisodes.size > 0) {
                episodeList = ArrayList()
                for (i in sMovie!!.allepisodes.indices) {
                    val movies = Movies.cloneMovies(sMovie)
                    movies.videoUrl = sMovie!!.allepisodes[i].link
                    movies.setsEpisode(sMovie!!.allepisodes[i].episodes_no)
                    episodeList?.add(movies)
                }
            }
        }
    }

    private fun addPreRollAds() {
        if (SaveSharedPreference.getLoginFromGoogle(this@Info)) {
            if (sMovie != null && sMovie!!.getiSfree() != null && sMovie!!.getiSfree()
                    .equals("yes", ignoreCase = true)
            ) {
                shouldLoadAd = true
                loadInterstitialAd()
                //                Toast.makeText(context, "PlayFree", Toast.LENGTH_SHORT).show();
                loadRewardedVideo()
            } else {
                //                Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "addPrerollAds: $sMovie")

                //                shouldLoadAd = true;
                //                loadInterstitialAd();
            }
        } else if (sMovie != null && sMovie!!.getsComingSoon() != null && sMovie!!.getsComingSoon()
                .equals("yes", ignoreCase = true)
        ) {
            shouldLoadAd = true
            loadInterstitialAd()
        } else if (sMovie != null) {
            if (sMovie!!.getsAllowedInPackage().equals(API.ACTIVE, ignoreCase = true)) {
                if (sMovie!!.getsType() != null && sMovie!!.getsType() == "5") {
                    if (SaveSharedPreference.getWebRemDays(this@Info) > 0 || SaveSharedPreference.getWebTimeDiff(
                            this@Info
                        ) > 0
                    ) {
                        shouldLoadAd = true
                        loadInterstitialAd()
                    } else if (sMovie!!.subscriptions != null && (sMovie!!.daysdiff > 0 || sMovie!!.timediff > 0)) {
                        //                        Toast.makeText(context, "PlayS", Toast.LENGTH_SHORT).show();
                        shouldLoadAd = true
                        loadInterstitialAd()
                    } else if (sMovie!!.getiSfree() != null && sMovie!!.getiSfree()
                            .equals("yes", ignoreCase = true)
                    ) {
                        shouldLoadAd = true
                        loadInterstitialAd()
                    } else {
                        //                        Toast.makeText(context, "PlaySelse", Toast.LENGTH_SHORT).show();
                        shouldLoadAd = true
                        loadInterstitialAd()
                    }
                } else {
                    if (SaveSharedPreference.getRemDays(this@Info) > 0 || SaveSharedPreference.getTimeDiff(
                            this@Info
                        ) > 0
                    ) {
                        shouldLoadAd = true
                        loadInterstitialAd()
                    } else if (sMovie!!.subscriptions != null && (sMovie!!.daysdiff > 0 || sMovie!!.timediff > 0)) {
                        //                        Toast.makeText(context, "PlaySo", Toast.LENGTH_SHORT).show();
                        shouldLoadAd = true
                        loadInterstitialAd()
                    } else if (sMovie!!.getiSfree() != null && sMovie!!.getiSfree()
                            .equals("yes", ignoreCase = true)
                    ) {
                        shouldLoadAd = true
                        loadInterstitialAd()
                    } else {
                        //                        Toast.makeText(context, "PlaySoelse", Toast.LENGTH_SHORT).show();
                        //                        shouldLoadAd = true;
                        //                        loadInterstitialAd();
                    }
                }
            } else {
                if (sMovie!!.subscriptions != null && (sMovie!!.daysdiff > 0 || sMovie!!.timediff > 0)) {
                    //                    Toast.makeText(context, "PlaySub", Toast.LENGTH_SHORT).show();
                    shouldLoadAd = true
                    loadInterstitialAd()
                } else if (sMovie!!.getiSfree() != null && sMovie!!.getiSfree()
                        .equals("yes", ignoreCase = true)
                ) {
                    shouldLoadAd = true
                    loadInterstitialAd()
                } else {
                    //                    Toast.makeText(context, "PlaySubelse", Toast.LENGTH_SHORT).show();
                    shouldLoadAd = true
                    loadInterstitialAd()
                }
            }
        }
    }

    fun init(videoURL: String?, isTrailer: Boolean, position: Int, isPremium: Boolean) {
        if (ObjExoPlayer != null) {
            endDuration = TimeUnit.MILLISECONDS.toSeconds(ObjExoPlayer!!.currentPosition)
            totalTimeWatched += (endDuration - startDuration)
            startDuration = endDuration
            endDuration = 0
            if (totalTimeWatched != 0L) {
                videoAnalyticsCreate(videoType, totalTimeWatched)
            }
            totalTimeWatched = 0
        }

        this.isTrailer = isTrailer
        episodePosition = position
        this.isPremium = isPremium
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
        imgFullScreenEnterExit = findViewById(R.id.img_full_screen_enter_exit)
        imgBwd = findViewById(R.id.img_bwd)
        imgFwd = findViewById(R.id.img_fwd)
        tvTitlePlayer = findViewById(R.id.tvTitlePlayer)
        if (sMovie!!.getsType() != null && sMovie!!.getsType() == "5") {
            tvTitlePlayer?.text =
                sMovie!!.getsName() + ": " + getString(R.string.episode) + " " + (episodePosition + 1)
        } else {
            tvTitlePlayer?.text = sMovie!!.getsName()
        }
        root_layout = findViewById(R.id.root_layout)
        root_layout?.isLongClickable = true
        root_layout?.setOnTouchListener(this)

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
        adFrameLayout = findViewById(R.id.ad_view_exoplayer)
        frameLayout_top = findViewById(R.id.fl_adplaceholder_exoplayer)
        adFrameLayout?.visibility = View.VISIBLE
        handler = Handler(Looper.getMainLooper())
        adsHandler = Handler(Looper.getMainLooper())
        VideoURL = videoURL
        realPArent!!.visibility = View.VISIBLE
        imgBanner!!.visibility = View.GONE
        imgBack!!.visibility = View.GONE
        shadow!!.visibility = View.GONE
        nestedScrollView!!.smoothScrollTo(0, 0)
        val layoutParams = llBottomLayout!!.layoutParams as RelativeLayout.LayoutParams
        layoutParams.topMargin = DeviceUtility.dpToPx(5, this)
        llBottomLayout!!.requestLayout()
        initPlayer()
        if (ObjPlayerView != null) {
            ObjPlayerView!!.onResume()
            validateDeviceAccessInfo(API.ADD_DEVICE)
        }
        if (ObjExoPlayer != null) {
            if (isObscuredTouch) {
                ObjExoPlayer!!.volume = 0f
            } else {
                ObjExoPlayer!!.volume = 1f
            }
        }
    }

    fun setTempData(
        tempVideoURL: String?, tempIsTrailer: Boolean, tempPosition: Int, tempIsPremium: Boolean
    ) {
        isEpisodeAdPlayed = true
        this.tempVideoURL = tempVideoURL
        this.tempIsTrailer = tempIsTrailer
        this.tempPosition = tempPosition
        this.tempIsPremium = tempIsPremium
        if (mPublisherInterstitialAd != null) {
            googleAdAnalyticsLoad(preroll1)
            mPublisherInterstitialAd!!.show(this@Info)
        } else {
            init(tempVideoURL, tempIsTrailer, tempPosition, tempIsPremium)
            isWebAdRequested = false
        }
    }

    private fun initPlayer() {
        if (VideoURL != null) {
            var mediaUrl = VideoURL
            if (VideoURL!!.endsWith("m3u8")) {
                ism3u8 = true
            }

            if (downloadVideo != null && !isTrailer) {
                ism3u8 = false
                mediaUrl = downloadVideo?.downloadedPath
            }

            if (ism3u8) {
                val userAgent = Util.getUserAgent(this, "iDragonPro")
                val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                    this, userAgent, bandwidthMeter
                )
                val daUri = Uri.parse(mediaUrl)
                val mVideoSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(
                    MediaItem.fromUri(
                        daUri
                    )
                )
                val combinedMediaSources = ArrayList<MediaSource>()
                combinedMediaSources.add(mVideoSource)
                val mergedSource =
                    MergingMediaSource(*combinedMediaSources.toTypedArray<MediaSource>())

                val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
                trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
                val loadControl: LoadControl = DefaultLoadControl()
                if (ObjExoPlayer == null) {
                    ObjExoPlayer = SimpleExoPlayer.Builder(this).setTrackSelector(
                        trackSelector!!
                    ).setLoadControl(loadControl).build()
                    ObjPlayerView!!.player = ObjExoPlayer
                }
                ObjPlayerView!!.controllerAutoShow = false
                ObjPlayerView!!.controllerShowTimeoutMs = 2000
                ObjExoPlayer!!.prepare(mergedSource)
                ObjExoPlayer!!.playWhenReady = true
                validateDeviceAccessInfo(API.ADD_DEVICE)
            } else {
                if (ObjExoPlayer == null) {
                    ObjExoPlayer = SimpleExoPlayer.Builder(this).build()
                    ObjExoPlayer!!.playWhenReady = true
                    validateDeviceAccessInfo(API.ADD_DEVICE)
                } else if (isTrailer) {
                    ObjExoPlayer = SimpleExoPlayer.Builder(this).build()
                    ObjExoPlayer!!.playWhenReady = true
                }
                ObjPlayerView!!.controllerAutoShow = false
                ObjPlayerView!!.controllerShowTimeoutMs = 2000
                ObjPlayerView!!.player = ObjExoPlayer
                val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                    this, Util.getUserAgent(this, "iDragonPro")
                )
                val mediaSourceFactory = ProgressiveMediaSource.Factory(dataSourceFactory)
                val mediaSource: MediaSource = mediaSourceFactory.createMediaSource(
                    MediaItem.fromUri(
                        Uri.parse(mediaUrl)
                    )
                )
                ObjExoPlayer!!.prepare(mediaSource, true, true)
            }
            if (ObjProgressBar != null) {
                ObjProgressBar!!.visibility = View.VISIBLE
            }
            setProgress()
            if (!isTrailer) {
                setAdsHandler()
                videoType = API.VIDEO_ANALYTICS_VIEW
            } else {
                videoType = API.VIDEO_ANALYTICS_TRAILER
            }
            initBwd()
            initFwd()
            initSetting()
            initPlayPause()
            setOnClickListener()
            initFullscreenDialog()
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
                        Log.d(TAG, "playertrack: startDuration$startDuration")
                        frameLayout_top!!.visibility = View.GONE
                        exo_play1!!.visibility = View.GONE
                        exo_play_down!!.visibility = View.GONE
                        exo_pause1!!.visibility = View.VISIBLE

                        when (playbackState) {
                            Player.STATE_READY -> if (ObjProgressBar != null) {
                                ObjProgressBar!!.visibility = View.GONE
                            }

                            Player.STATE_BUFFERING -> {}
                            Player.STATE_ENDED -> {
                                if ((sMovie!!.getsType() != null && (sMovie!!.getsType() == "5") && sMovie!!.allepisodes != null) && (episodePosition + 1) < sMovie!!.allepisodes.size) {
                                    if (!isLocked && !isTrailer) {
                                        isLocked = true
                                        setNextVideoScreen()
                                    }
                                } else {
                                    if (mCancelAd != null) {
                                        googleAdAnalyticsLoad(skip1)
                                        mCancelAd!!.show(this@Info)
                                    }
                                    if (ObjProgressBar != null) {
                                        ObjProgressBar!!.visibility = View.VISIBLE
                                    }
                                }
                                isVideoCompleted = true
                            }
                        }
                    } else {
                        endDuration = TimeUnit.MILLISECONDS.toSeconds(
                            ObjExoPlayer!!.currentPosition
                        )
                        Log.d(TAG, "playertrack: endDuration$endDuration")
                        totalTimeWatched += (endDuration - startDuration)
                        startDuration = endDuration
                        endDuration = 0

                        if (mExoPlayerFullscreen) {
                            refreshAd()
                            frameLayout_top!!.visibility = View.VISIBLE
                        }
                        exo_play1!!.visibility = View.VISIBLE
                        exo_play_down!!.visibility = View.VISIBLE
                        exo_pause1!!.visibility = View.GONE

                        //refreshAdTimer();
                    }
                }

                override fun onSeekProcessed() {
                    Log.d(
                        TAG, "playertrack: onSeekProcessed-- " + TimeUnit.MILLISECONDS.toSeconds(
                            ObjExoPlayer!!.currentPosition
                        )
                    )
                    if (!isForwardButtonClicked) {
                        if (mCancelAd != null) {
                            googleAdAnalyticsLoad(skip1)
                            mCancelAd!!.show(this@Info)
                        }
                    }
                    isForwardButtonClicked = false
                }
            })

            ObjExoPlayer!!.addAnalyticsListener(object : AnalyticsListener {
                override fun onPlayerStateChanged(
                    eventTime: EventTime, playWhenReady: Boolean, playbackState: Int
                ) {

                }

                override fun onPositionDiscontinuity(eventTime: EventTime, reason: Int) {
                    Log.d(
                        TAG,
                        "playertrack onPositionDiscontinuity: " + TimeUnit.MILLISECONDS.toSeconds(
                            ObjExoPlayer!!.currentPosition
                        )
                    )
                }

                override fun onSeekStarted(eventTime: EventTime) {
                    Log.d(
                        TAG, "playertrack onSeekStarted: " + TimeUnit.MILLISECONDS.toSeconds(
                            ObjExoPlayer!!.currentPosition
                        )
                    )
                    endDuration = TimeUnit.MILLISECONDS.toSeconds(ObjExoPlayer!!.currentPosition)
                    Log.d(
                        TAG,
                        "playertrack onSeekStarted$totalTimeWatched $endDuration $startDuration"
                    )
                    totalTimeWatched += (endDuration - startDuration)
                    Log.d(TAG, "playertrack onSeekStarted$totalTimeWatched")
                    startDuration = endDuration
                    endDuration = 0
                }

                override fun onSeekProcessed(eventTime: EventTime) {
                    Log.d(
                        TAG, "playertrack onSeekProcessed: " + TimeUnit.MILLISECONDS.toSeconds(
                            ObjExoPlayer!!.currentPosition
                        )
                    )
                    startDuration = TimeUnit.MILLISECONDS.toSeconds(
                        ObjExoPlayer!!.currentPosition
                    )
                }
            })
        }
    }

    private fun setNextVideoScreen() {
        val nextEpisode = episodeList!![episodePosition + 1]
        txtNextTitle!!.text = getString(R.string.episode) + " " + nextEpisode.getsEpisode()
        ObjPlayerView!!.visibility = View.GONE
        nextvideolayout!!.visibility = View.VISIBLE
        nextVideoTimer =
            object : CountDownTimer(NEXT_VIDEO_SCREEN_TIME.toLong(), COUNTDOWN_INTERVAL.toLong()) {
                override fun onTick(millisUntilFinished: Long) {
                    //Log.e(TAG, "onTick: jww" );
                    val progress =
                        (100 - Math.round(((millisUntilFinished * 100) / NEXT_VIDEO_SCREEN_TIME).toFloat())).toDouble()
                    donutProgress!!.progress = progress.toInt()
                    //Log.d(TAG," playing value"+saranyuVideoPlayer.isPlaying());
                }

                override fun onFinish() {
                    playNext()
                }
            }.start()
    }

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
        imgBackPlayer!!.setOnClickListener { this@Info.onBackPressed() }
    }

    private fun playNext() {
        isLocked = false
        val pos = episodePosition + 1
        val nextEpisode = episodeList!![episodePosition + 1]
        ObjPlayerView!!.visibility = View.VISIBLE
        nextvideolayout!!.visibility = View.GONE
        val premium =
            sMovie!!.getiSfree() == null || !sMovie!!.getiSfree().equals("yes", ignoreCase = true)
        setTempData(nextEpisode.videoUrl.replace(" ".toRegex(), "%20"), false, pos, premium)
    }

    private fun setAdsHandler() {
        adsHandler = Handler()
        //Make sure you update Seekbar on UI thread
        adsHandler!!.post(object : Runnable {
            override fun run() {
                if (ObjExoPlayer != null) {
                    if (!isLoadinCancelInstream) {
                        if (sMovie != null && sMovie!!.getiSfree() != null) {
                            if (sMovie!!.getiSfree().equals(
                                    "yes", ignoreCase = true
                                ) && SaveSharedPreference.getLoginFromGoogle(
                                    this@Info
                                )
                            ) {
//                                loadCancelInterstitial()
//                                loadCancelRewardedVideo()
                            } else if (sMovie!!.getiSfree().equals("yes", ignoreCase = true)) {
                                if (sMovie!!.getsType() != null && sMovie!!.getsType() == "5") {
                                    if (SaveSharedPreference.getWebRemDays(this@Info) > 0 || SaveSharedPreference.getWebTimeDiff(
                                            this@Info
                                        ) > 0
                                    ) {
                                        //nothing
                                    } else if (sMovie!!.subscriptions != null && (sMovie!!.daysdiff > 0 || sMovie!!.timediff > 0)) {
                                        //nothing
                                    } else {
//                                        loadCancelInterstitial()
//                                        loadCancelRewardedVideo()
                                    }
                                } else {
                                    if (SaveSharedPreference.getRemDays(this@Info) > 0 || SaveSharedPreference.getTimeDiff(
                                            this@Info
                                        ) > 0
                                    ) {
                                        //nothing
                                    } else if (sMovie!!.subscriptions != null && (sMovie!!.daysdiff > 0 || sMovie!!.timediff > 0)) {
                                        //nothing
                                    } else {
//                                        loadCancelInterstitial()
//                                        loadCancelRewardedVideo()
                                    }
                                }
                            }
                        }
                    }
                    val duration = ObjExoPlayer!!.contentPosition
                    val time = TimeUnit.MILLISECONDS.toMinutes(duration)
                    val secs = TimeUnit.MILLISECONDS.toSeconds(duration)
                    if (sMovie!!.getsType() != null && sMovie!!.getsType() == "5") {
                        if (time > 0 && ((time + 1) % 6 == 0L)) {
                            if (!isLoadinInstream) {
                                if (sMovie != null && sMovie!!.getiSfree() != null) {
                                    if (sMovie!!.getiSfree().equals(
                                            "yes", ignoreCase = true
                                        ) && SaveSharedPreference.getLoginFromGoogle(
                                            this@Info
                                        )
                                    ) {
                                        loadMovieInterstitial()
                                        loadMovieRewardedVideo()
                                    } else if (sMovie!!.getiSfree()
                                            .equals("yes", ignoreCase = true)
                                    ) {
                                        if (!(SaveSharedPreference.getWebRemDays(this@Info) > 0 || SaveSharedPreference.getWebTimeDiff(
                                                this@Info
                                            ) > 0)
                                        ) {
                                            loadMovieInterstitial()
                                            loadMovieRewardedVideo()
                                        } else if (sMovie!!.ads_for_paid_movie != null && sMovie!!.ads_for_paid_movie.equals(
                                                "yes", ignoreCase = true
                                            )
                                        ) {
                                            if (sMovie!!.ads_paid_movie_count != null) {
                                                if (sMovie!!.ads_paid_movie_count.equals(
                                                        "1", ignoreCase = true
                                                    )
                                                ) {
                                                    loadMovieInterstitial()
                                                } else {
                                                    loadMovieInterstitial()
                                                    loadMovieRewardedVideo()
                                                }
                                            }
                                        }
                                    } else if (sMovie!!.getiSfree().equals(
                                            "no", ignoreCase = true
                                        ) && sMovie!!.ads_for_paid_movie != null && sMovie!!.ads_for_paid_movie.equals(
                                            "yes", ignoreCase = true
                                        )
                                    ) {
                                        if (sMovie!!.ads_paid_movie_count != null) {
                                            if (sMovie!!.ads_paid_movie_count.equals(
                                                    "1", ignoreCase = true
                                                )
                                            ) {
                                                loadMovieInterstitial()
                                            } else {
                                                loadMovieInterstitial()
                                                loadMovieRewardedVideo()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (time > 0 && (time % 6 == 0L)) {
                            if (mInstreamAd != null) {
                                googleAdAnalyticsLoad(midroll1)
                                mInstreamAd!!.show(this@Info)
                            }
                        }
                    } else {
                        if (time > 0 && ((time + 1) % 8 == 0L)) {
                            if (!isLoadinInstream) {
                                if (sMovie != null && sMovie!!.getiSfree() != null) {
                                    if (sMovie!!.getiSfree().equals(
                                            "yes", ignoreCase = true
                                        ) && SaveSharedPreference.getLoginFromGoogle(
                                            this@Info
                                        )
                                    ) {
                                        loadMovieInterstitial()
                                        loadMovieRewardedVideo()
                                    } else if (sMovie!!.getiSfree()
                                            .equals("yes", ignoreCase = true)
                                    ) {
                                        if (!(SaveSharedPreference.getRemDays(this@Info) > 0 || SaveSharedPreference.getTimeDiff(
                                                this@Info
                                            ) > 0)
                                        ) {
                                            loadMovieInterstitial()
                                            loadMovieRewardedVideo()
                                        } else if (sMovie!!.ads_for_paid_movie != null && sMovie!!.ads_for_paid_movie.equals(
                                                "yes", ignoreCase = true
                                            )
                                        ) {
                                            if (sMovie!!.ads_paid_movie_count != null) {
                                                if (sMovie!!.ads_paid_movie_count.equals(
                                                        "1", ignoreCase = true
                                                    )
                                                ) {
                                                    loadMovieInterstitial()
                                                } else {
                                                    loadMovieInterstitial()
                                                    loadMovieRewardedVideo()
                                                }
                                            }
                                        }
                                    } else if (sMovie!!.getiSfree().equals(
                                            "no", ignoreCase = true
                                        ) && sMovie!!.ads_for_paid_movie != null && sMovie!!.ads_for_paid_movie.equals(
                                            "yes", ignoreCase = true
                                        )
                                    ) {
                                        if (sMovie!!.ads_paid_movie_count != null) {
                                            if (sMovie!!.ads_paid_movie_count.equals(
                                                    "1", ignoreCase = true
                                                )
                                            ) {
                                                loadMovieInterstitial()
                                            } else {
                                                loadMovieInterstitial()
                                                loadMovieRewardedVideo()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (time > 0 && (time % 8 == 0L)) {
                            if (mInstreamAd != null) {
                                googleAdAnalyticsLoad(midroll1)
                                mInstreamAd!!.show(this@Info)
                            }
                        }
                    }
                    handler!!.postDelayed(this, 1000)
                }
            }
        })
    }

    private fun markPaidUser() {
        iIsPaidUser = if (SaveSharedPreference.getLoginFromGoogle(this@Info)) {
            "0"
        } else {
            if (sMovie!!.getsType() != null && sMovie!!.getsType() == "5") {
                if (SaveSharedPreference.getWebRemDays(this@Info) > 0 || SaveSharedPreference.getWebTimeDiff(
                        this@Info
                    ) > 0
                ) {
                    "1"
                } else if (sMovie!!.subscriptions != null && (sMovie!!.daysdiff > 0 || sMovie!!.timediff > 0)) {
                    "1"
                } else {
                    "0"
                }
            } else {
                if (SaveSharedPreference.getRemDays(this@Info) > 0 || SaveSharedPreference.getTimeDiff(
                        this@Info
                    ) > 0
                ) {
                    "1"
                } else if (sMovie!!.subscriptions != null && (sMovie!!.daysdiff > 0 || sMovie!!.timediff > 0)) {
                    "1"
                } else {
                    "0"
                }
            }
        }
        videoAnalyticsCreate(API.VIDEO_ANALYTICS_VISIT, 0)
    }

    private fun initSetting() {
        if (ism3u8) {
            img_setting!!.visibility = View.VISIBLE
            tvQuality!!.visibility = View.VISIBLE
            img_setting!!.setOnClickListener {
                if (!isShowingTrackSelectionDialog && TrackSelectionDialog.willHaveContent(
                        trackSelector
                    )
                ) {
                    isShowingTrackSelectionDialog = true
                    val trackSelectionDialog = TrackSelectionDialog.createForTrackSelector(
                        trackSelector,  /* onDismissListener= */
                        { _: DialogInterface? ->
                            isShowingTrackSelectionDialog = false
                        }, 0, ObjProgressBar
                    )
                    trackSelectionDialog.show(supportFragmentManager,  /* tag= */null)
                }
            }
        } else {
            img_setting!!.visibility = View.GONE
            tvQuality!!.visibility = View.GONE
        }
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

    private fun setOnClickListener() {
        imgFullScreenEnterExit!!.setOnClickListener {
            isManual = true
            if (!mExoPlayerFullscreen) openFullscreenDialog()
            else closeFullscreenDialog()
        }
        if (ObjExoPlayer != null) {
            if (Settings.System.getInt(
                    contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0
                ) == 1
            ) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER
            }
        }
    }

    private fun initPlayPause() {
        exo_play1!!.setOnClickListener {
            if (ObjExoPlayer != null) {
                ObjExoPlayer!!.playWhenReady = true
                validateDeviceAccessInfo(API.ADD_DEVICE)
            }
        }
        exo_play_down!!.setOnClickListener {
            if (ObjExoPlayer != null) {
                ObjExoPlayer!!.playWhenReady = true
                validateDeviceAccessInfo(API.ADD_DEVICE)
            }
        }
        exo_pause1!!.setOnClickListener {
            if (ObjExoPlayer != null) {
                ObjExoPlayer!!.playWhenReady = false
            }
        }
    }

    private fun initFullscreenDialog() {
        mFullScreenDialog =
            object : Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
                override fun onBackPressed() {
                    if (mExoPlayerFullscreen) {
                        isManual = true
                        closeFullscreenDialog()
                    }
                    super.onBackPressed()
                }
            }
    }

    private fun openFullscreenDialog() {
        try {
            Log.d(TAG, "Open Full Screen Dialog")
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            (rlPlayerPArent!!.parent as RelativeLayout).removeView(rlPlayerPArent)

            main_info!!.addView(
                rlPlayerPArent, RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
            )

            imgFullScreenEnterExit!!.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.ic_full_screen_exit
                )
            )
            mExoPlayerFullscreen = true
            llBottomLayout!!.visibility = View.GONE
            nestedScrollView!!.visibility = View.GONE
            stickybannerAdView!!.visibility = View.GONE
            bannerAdsShow()
            initializeBrightnessManager()
            if (!isShownOnFullscreen) {
                isShownOnFullscreen = true
                networkStatus
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun closeFullscreenDialog() {
        try {
            Log.d(TAG, "Close Full Screen Dialog")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            (rlPlayerPArent!!.parent as ViewGroup).removeView(rlPlayerPArent)
            val height = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 240f, resources.displayMetrics
            ).toInt()
            mExoPlayerFullscreen = false
            (findViewById<View>(R.id.realPArent) as RelativeLayout).addView(
                rlPlayerPArent,
                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
            )
            imgFullScreenEnterExit!!.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.ic_full_screen
                )
            )
            frameLayout_top!!.visibility = View.GONE
            adFrameLayout!!.visibility = View.GONE
            destroyBannerAds()
            llBottomLayout!!.visibility = View.VISIBLE
            nestedScrollView!!.visibility = View.VISIBLE
            stickybannerAdView!!.visibility = View.VISIBLE
            initializeBrightnessManager()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun destroyBannerAds() {
        if (adFrameLayout != null) {
            if (adView != null) {
                adFrameLayout!!.removeView(adView)
            }
            if (gamadView != null) {
                adFrameLayout!!.removeView(gamadView)
            }
        }
    }

    private fun setProgress() {
        handler = Handler()
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

    fun refreshAd() {
        if (SaveSharedPreference.getAdsData(this) != null) {
            if (SaveSharedPreference.getAdsData(this).gStatus != null && (SaveSharedPreference.getAdsData(
                    this
                ).gStatus == API.ACTIVE)
            ) {
                val adLoader = AdLoader.Builder(
                    this, SaveSharedPreference.getAdsData(this).gNative
                ).forNativeAd { nativeAd: NativeAd? ->
                    val styles = NativeTemplateStyle.Builder().build()
                    val adviewTop =
                        layoutInflater.inflate(R.layout.ad_unified, null) as LinearLayout
                    val template = adviewTop.findViewById<TemplateView>(R.id.my_template)
                    template.setStyles(styles)
                    template.setNativeAd(nativeAd)
                    frameLayout_top!!.removeAllViews()
                    frameLayout_top!!.addView(adviewTop)
                }.build()
                adLoader.loadAd(AdRequest.Builder().build())
            } else if (SaveSharedPreference.getAdsData(this).mopubStatus != null && (SaveSharedPreference.getAdsData(
                    this
                ).mopubStatus == API.ACTIVE)
            ) {
                val adLoader = AdLoader.Builder(
                    this, SaveSharedPreference.getAdsData(this).mopubNative
                ).forNativeAd { nativeAd: NativeAd? ->
                    val styles = NativeTemplateStyle.Builder().build()
                    val adviewTop =
                        layoutInflater.inflate(R.layout.ad_unified, null) as LinearLayout
                    val template = adviewTop.findViewById<TemplateView>(R.id.my_template)
                    template.setStyles(styles)
                    template.setNativeAd(nativeAd)
                    frameLayout_top!!.removeAllViews()
                    frameLayout_top!!.addView(adviewTop)
                }.build()
                adLoader.loadAd(AdManagerAdRequest.Builder().build())
            }
        }
    }

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

    private fun bannerAdsShow() {
        if (SaveSharedPreference.getAdsData(this@Info) != null) {
            if (SaveSharedPreference.getAdsData(this@Info).gStatus != null && SaveSharedPreference.getAdsData(
                    this@Info
                ).gStatus == API.ACTIVE
            ) {
                val handler = Handler(Looper.getMainLooper())
                val timer = Timer()
                val doAsynchronousTask: TimerTask = object : TimerTask() {
                    override fun run() {
                        handler.post {
                            try {
                                if (adView != null) {
                                    adFrameLayout!!.removeView(adView)
                                    adView!!.destroy()
                                }

                                adView = AdView(this@Info)
                                adView!!.setAdSize(AdSize.BANNER)
                                adView!!.adUnitId =
                                    SaveSharedPreference.getAdsData(this@Info).gBanner
                                adRequest = AdRequest.Builder().build()
                                adFrameLayout!!.addView(adView)
                                adView!!.loadAd(adRequest!!)
                                startTimer()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
                timer.schedule(
                    doAsynchronousTask, 0, 300000
                ) //600000  300000 execute in every 10 minutes
            } else if (SaveSharedPreference.getAdsData(this@Info).mopubStatus != null && (SaveSharedPreference.getAdsData(
                    this@Info
                ).mopubStatus == API.ACTIVE)
            ) {
                val handler = Handler()
                val timer = Timer()
                val doAsynchronousTask: TimerTask = object : TimerTask() {
                    override fun run() {
                        handler.post {
                            try {
                                if (gamadView != null) {
                                    adFrameLayout!!.removeView(gamadView)
                                    gamadView!!.destroy()
                                }

                                gamadView = AdManagerAdView(this@Info)
                                gamadView!!.setAdSizes(AdSize.BANNER)
                                gamadView!!.adUnitId =
                                    SaveSharedPreference.getAdsData(this@Info).mopubBanner
                                val adRequest = AdManagerAdRequest.Builder().build()
                                adFrameLayout!!.addView(gamadView)
                                gamadView!!.loadAd(adRequest)
                                startTimer()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
                timer.schedule(doAsynchronousTask, 0, 300000)
            }
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (mExoPlayerFullscreen) {
                    adFrameLayout!!.visibility = View.VISIBLE
                }
            }

            override fun onFinish() {
                //  tvTime.setText(" "); //On finish change timer text
                adFrameLayout!!.visibility = View.GONE
                countDownTimer = null //set CountDownTimer to null
            }
        }.start()
    }

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

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (ObjExoPlayer != null) {
                openFullscreenDialog()
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (ObjExoPlayer != null && !isManual) {
                closeFullscreenDialog()
            }
        }
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

    private fun translateInLanguage(text: String?, tv: TextView?) {
        englishGermanTranslator!!.translate(text!!)
            .addOnSuccessListener { translatedText: String? ->
                // Translation successful.
                tv!!.text = translatedText
            }.addOnFailureListener { }
    }

    private fun validateDeviceAccessInfo(accessType: String) {
        if (isPremium) {
            val webApi = getInstance()!!.createRetrofitNewInstance()
            val call = webApi.validate_device_access_info(
                SaveSharedPreference.getUserId(
                    this@Info
                ), DeviceUtility.getDeviceName(), sMovie!!.getsVedioId(), accessType
            )
            call.enqueue(object : Callback<AnalyticsResponseModel?> {
                override fun onResponse(
                    call: Call<AnalyticsResponseModel?>, response: Response<AnalyticsResponseModel?>
                ) {
                    if (accessType.equals(API.ADD_DEVICE, ignoreCase = true)) {
                        if (response.body() != null) {
                            if (!response.body()!!.isStatus) {
                                if (ObjExoPlayer != null) {
                                    ObjExoPlayer!!.playWhenReady = false
                                }
                                showPremiumContentDialog()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<AnalyticsResponseModel?>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    private fun videoAnalyticsCreate(accessType: String?, totalTimeWatched: Long) {
        val webApi = getInstance()!!.createRetrofitNewInstance()
        val call = webApi.video_analytics_create(
            SaveSharedPreference.getUserId(this@Info),
            sMovie!!.getsVedioId(),
            accessType,
            totalTimeWatched,
            iIsPaidUser
        )
        call.enqueue(object : Callback<AnalyticsResponseModel?> {
            override fun onResponse(
                call: Call<AnalyticsResponseModel?>, response: Response<AnalyticsResponseModel?>
            ) {
            }

            override fun onFailure(call: Call<AnalyticsResponseModel?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun showPremiumContentDialog() {
        val adExp = AlertDialog.Builder(
            this@Info
        ).create()
        adExp.setMessage(getString(R.string.premium_content_block))
        adExp.setCancelable(false)
        adExp.setButton(
            DialogInterface.BUTTON_POSITIVE, getString(R.string.ok)
        ) { _: DialogInterface?, _: Int ->
            this@Info.finish()
            adExp.dismiss()
        }
        adExp.show()
    }

    private fun googleAdAnalyticsRequest(iGoogleAdId: String, iPosition: String) {
        val webApi = getInstance()!!.createRetrofitNewInstance()
        val call = webApi.google_ad_analytics_request(
            SaveSharedPreference.getUserId(this@Info),
            sMovie!!.getsVedioId(),
            iGoogleAdId,
            iPosition
        )
        call.enqueue(object : Callback<AnalyticsResponseModel?> {
            override fun onResponse(
                call: Call<AnalyticsResponseModel?>, response: Response<AnalyticsResponseModel?>
            ) {
                if (response.body() != null) {
                    val requestId = response.body()!!.getiRequestId()
                    if (iPosition.equals(API.PRE_ROLL_1, ignoreCase = true)) {
                        preroll1 = requestId
                    } else if (iPosition.equals(API.PRE_ROLL_2, ignoreCase = true)) {
                        preroll2 = requestId
                    } else if (iPosition.equals(API.PRE_ROLL_3, ignoreCase = true)) {
                        preroll3 = requestId
                    } else if (iPosition.equals(API.MID_ROLL_1, ignoreCase = true)) {
                        midroll1 = requestId
                    } else if (iPosition.equals(API.MID_ROLL_2, ignoreCase = true)) {
                        midroll2 = requestId
                    } else if (iPosition.equals(API.MID_ROLL_3, ignoreCase = true)) {
                        midroll_3 = requestId
                    } else if (iPosition.equals(API.MID_ROLL_4, ignoreCase = true)) {
                        midroll4 = requestId
                    } else if (iPosition.equals(API.SKIP_1, ignoreCase = true)) {
                        skip1 = requestId
                    } else if (iPosition.equals(API.SKIP_2, ignoreCase = true)) {
                        skip2 = requestId
                    }
                }
            }

            override fun onFailure(call: Call<AnalyticsResponseModel?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun googleAdAnalyticsResponse(iRequestId: String) {
        val webApi = getInstance()!!.createRetrofitNewInstance()
        val call = webApi.google_ad_analytics_response(
            SaveSharedPreference.getUserId(this@Info), sMovie!!.getsVedioId(), iRequestId
        )
        call.enqueue(object : Callback<AnalyticsResponseModel?> {
            override fun onResponse(
                call: Call<AnalyticsResponseModel?>, response: Response<AnalyticsResponseModel?>
            ) {
            }

            override fun onFailure(call: Call<AnalyticsResponseModel?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun googleAdAnalyticsLoad(iRequestId: String) {
        val webApi = getInstance()!!.createRetrofitNewInstance()
        val call = webApi.google_ad_analytics_load(
            SaveSharedPreference.getUserId(this@Info), sMovie!!.getsVedioId(), iRequestId
        )
        call.enqueue(object : Callback<AnalyticsResponseModel?> {
            override fun onResponse(
                call: Call<AnalyticsResponseModel?>, response: Response<AnalyticsResponseModel?>
            ) {
            }

            override fun onFailure(call: Call<AnalyticsResponseModel?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"))
    }

    private fun loadComments() {
        val webApi = getInstance()!!.createRetrofitNewInstance()
        val call = webApi.getReviews(sMovie!!.getsVedioId(), pageSize, pageNumber)
        call.enqueue(object : Callback<ReviewResponseModels?> {
            override fun onResponse(
                call: Call<ReviewResponseModels?>, response: Response<ReviewResponseModels?>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.review != null) {
                        if (response.body()!!.review.size > 0) {
                            reviewList.addAll(response.body()!!.review)
                            reviewAdapter!!.notifyDataSetChanged()
                            pageNumber += 1
                        } else {
                            loadMore = false
                            pageNumber += 1
                        }
                    }
                }
                loading = false
            }

            override fun onFailure(call: Call<ReviewResponseModels?>, t: Throwable) {
                t.printStackTrace()
                loading = false
            }
        })
    }

    private fun addReview() {
        val webApi = getInstance()!!.createRetrofitNewInstance()
        val call = webApi.addReview(sMovie!!.getsVedioId(),
            SaveSharedPreference.getUserId(this@Info),
            etReview!!.text.toString().trim { it <= ' ' })
        call.enqueue(object : Callback<AddReviewResponseModel?> {
            override fun onResponse(
                call: Call<AddReviewResponseModel?>, response: Response<AddReviewResponseModel?>
            ) {
                if (response.body() != null && response.body()!!.comments != null) {
                    val inflater1 = (this@Info as Activity?)!!.layoutInflater
                    val chlidView = inflater1.inflate(R.layout.review_item, null)

                    val tvName = chlidView.findViewById<TextView>(R.id.tvName)
                    val tvReview = chlidView.findViewById<TextView>(R.id.tvReview)
                    val tvTime = chlidView.findViewById<TextView>(R.id.tvTime)

                    val toServerUnicodeEncoded = etReview!!.text.toString()
                        .trim { it <= ' ' } //StringEscapeUtils.escapeJava(etReview.getText().toString());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvReview.text =
                            Html.fromHtml(toServerUnicodeEncoded, Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        tvReview.text = Html.fromHtml(toServerUnicodeEncoded)
                    }
                    tvName.text =
                        SaveSharedPreference.getFirstName(this@Info) + " " + SaveSharedPreference.getLastName(
                            this@Info
                        )
                    tvTime.text = getString(R.string.now)
                    etReview!!.setText("")
                    val review = Review()
                    review.sFirstName = SaveSharedPreference.getFirstName(this@Info)
                    review.sLastName = SaveSharedPreference.getLastName(this@Info)
                    review.sCreatedTimeStamp = "NOW"
                    review.sReview = toServerUnicodeEncoded
                    reviewList.add(0, review)
                    reviewAdapter!!.notifyDataSetChanged()
                    if (!this@Info.isFinishing) {
                        if (dialog != null && dialog!!.isShowing) {
                            dialog!!.dismiss()
                        }
                    }
                    runOnUiThread {
                        val adExp = AlertDialog.Builder(
                            this@Info
                        ).create()
                        adExp.setMessage((getString(R.string.review_added)))
                        adExp.setButton(
                            DialogInterface.BUTTON_POSITIVE, getString(R.string.ok)
                        ) { _, _ -> adExp.dismiss() }
                        adExp.show()
                    }
                } else {
                    if (!this@Info.isFinishing) {
                        if (dialog != null && dialog!!.isShowing) {
                            dialog!!.dismiss()
                        }
                    }
                    Toast.makeText(
                        applicationContext, getString(R.string.failed), Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<AddReviewResponseModel?>, t: Throwable) {
                if (!this@Info.isFinishing) {
                    if (dialog != null && dialog!!.isShowing) {
                        dialog!!.dismiss()
                    }
                }
                t.printStackTrace()
                Toast.makeText(applicationContext, getString(R.string.failed), Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun loadBannerAd() {
        if (SaveSharedPreference.getAdsData(this@Info) != null) {
            if (SaveSharedPreference.getAdsData(this@Info).gStatus != null && SaveSharedPreference.getAdsData(
                    this@Info
                ).gStatus == API.ACTIVE
            ) {
                bannerAdView!!.removeAllViews()
                val adView = AdView(this@Info)
                adView.setAdSize(AdSize.LARGE_BANNER)
                adView.adUnitId = SaveSharedPreference.getAdsData(this@Info).getgAdaptiveBanner()
                val adRequest = AdRequest.Builder().build()
                bannerAdView!!.addView(adView)
                adView.loadAd(adRequest)
            }
        }
    }

    private val networkStatus: Unit
        get() {
            val conManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = conManager.activeNetworkInfo
            if (activeNetwork != null) {
                // connected to the internet
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                    showToast()
                }
            } else {
                // not connected to the internet
            }
        }

    private fun showToast() {
        // Set the toast and duration
        val toastDurationInMilliSeconds = 10000
        val custom = Toasty.custom(
            this@Info,
            getString(R.string.data_msg),
            R.drawable.chat_icon,
            R.color.themered,
            8000,
            false,
            true
        )
        // Set the countdown to display the toast
        val toastCountDown: CountDownTimer =
            object : CountDownTimer(toastDurationInMilliSeconds.toLong(), 500 /*Tick duration*/) {
                override fun onTick(millisUntilFinished: Long) {
                    custom.show()
                }

                override fun onFinish() {
                    custom.cancel()
                }
            }

        // Show the toast and starts the countdown
        custom.show()
        toastCountDown.start()
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

    class MoreBottomDialogFragment : BottomSheetDialogFragment() {
        private var mContext: Context? = null
        private var episodeList: ArrayList<Movies>? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.episodes_fragment_bottomsheet, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
            if (sMovie != null && sMovie!!.allepisodes != null && sMovie!!.allepisodes.size > 0) {
                episodeList = ArrayList()
                for (i in sMovie!!.allepisodes.indices) {
                    val movies = Movies.cloneMovies(sMovie)
                    movies.videoUrl = sMovie!!.allepisodes[i].link
                    movies.setsEpisode(sMovie!!.allepisodes[i].episodes_no)
                    movies.setsSmallBanner(sMovie!!.allepisodes[i].banner)
                    episodeList!!.add(movies)
                }
                val mAdapter = EpisodeAdapter(mContext, episodeList, mContext as Info?, this)
                recyclerView.adapter = mAdapter
                recyclerView.setHasFixedSize(true)

                // use a linear layout manager
                recyclerView.layoutManager = GridLayoutManager(mContext, 2)
            }
        }

        override fun onAttach(context: Context) {
            super.onAttach(context)
            mContext = context
        }

        override fun onDetach() {
            super.onDetach()
            mContext = null
        }

        companion object {
            const val TAG: String = "MoreBottomDialogFragment"
            const val DOWNLOAD_FOLDER_NAME: String = "iDragonPro"
            private var sMovie: Movies? = null
            fun newInstance(sMovie: Movies?): MoreBottomDialogFragment {
                Companion.sMovie = sMovie
                return MoreBottomDialogFragment()
            }

            fun convertSolution(str: String): String {
                if (!str.contains(Regex("\\d"))) {
                    return "114P"
                }
                if (str.contains("low", true) || str.contains("sd") || str.contains(
                        "unknown", true
                    )
                ) {
                    return "114P"
                }
                val split: List<String> = str.split("[^\\d]+".toRegex())
                val str2 = if (split.size >= 2) {
                    split[1]
                } else {
                    split[0]
                }
                try {
                    val parseLong: Long = str2.toLong()
                    return when {
                        parseLong < 240 -> "144P"
                        parseLong < 360 -> "240P"
                        parseLong < 480 -> "360P"
                        parseLong < 720 -> "480P"
                        parseLong < 1080 -> "720P"
                        parseLong < 1440 -> "1080P"
                        parseLong < 2160 -> "1440P"
                        parseLong < 4320 -> "4K"
                        else -> "8K"
                    }
                } catch (unused: NumberFormatException) {
                    return str2
                }
            }

            fun estimateVideoSize(durationInSeconds: Int, resolution: Int): String {
                val bitRate: Double = when {
                    resolution <= 240 -> 200.0
                    resolution <= 360 -> 500.0
                    resolution <= 480 -> 1000.0
                    resolution <= 720 -> 2500.0
                    else -> 6000.0
                }
                val videoSizeInMb =
                    bitRate / 8.0 * durationInSeconds / 60.0 * resolution * resolution / (640.0 * 480.0)
                return if (videoSizeInMb >= 1000.0) {
                    String.format("%.2f GB", videoSizeInMb / 1000.0)
                } else {
                    String.format("%.2f MB", videoSizeInMb)
                }
            }
        }
    }

    private fun setDefaultDownloadLocation(path: String) {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@Info)
        if ((prefs.getString(getString(R.string.download_location_key), null) == null)) {
            prefs.edit().putString(getString(R.string.download_location_key), path).apply()
        }
    }

    companion object {
        private val TAG: String = Info::class.java.simpleName
        private val OPEN_DIRECTORY_REQUEST_CODE: Int = 42069

        private const val MAX_SCREEN_BRIGHTNESS = 255
        private const val SLIDE_MULTIPLE = 1f
        private const val NEXT_VIDEO_SCREEN_TIME = 4000
        private const val COUNTDOWN_INTERVAL = 100
        const val MOVIESLIST: String = "movieList"
        const val BUNDLEPACKAGE: String = "bundlepackage"
    }
}
