package com.idragonpro.andmagnus.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.idragonpro.andmagnus.MyApp.Companion.getInstance
import com.idragonpro.andmagnus.R
import com.idragonpro.andmagnus.activities.Home
import com.idragonpro.andmagnus.helpers.LocaleHelper
import com.idragonpro.andmagnus.helpers.SaveSharedPreference
import com.idragonpro.andmagnus.responseModels.AnalyticsResponseModel
import com.idragonpro.andmagnus.responseModels.SplashResponseModel
import com.idragonpro.andmagnus.utility.DeviceUtility
import com.idragonpro.andmagnus.utility.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class FlashActivity : AppCompatActivity() {
    private var videoView: PlayerView? = null
    private var getStartedBtn: Button? = null
    private var context: Context? = null
    var flashURL: String? = null
    private var progressBar: ProgressBar? = null
    private var adView: AdView? = null
    var adRequest: AdRequest? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var flashtime = 0
    private var ObjExoPlayer: SimpleExoPlayer? = null
    var isFirstSplashAnaSent: Boolean = false
    var isSecondSplashAnaSent: Boolean = false
    var isThirdSplashAnaSent: Boolean = false
    private var adsHandler = Handler()
    private var requestId: String? = null
    private var runnable: Runnable? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash)
        context = this
        videoView = findViewById(R.id.videoView)
        progressBar = findViewById(R.id.progressBar)
        getStartedBtn = findViewById(R.id.get_started_btn)
        progressBar?.visibility = View.VISIBLE

        if (SaveSharedPreference.getSplashData(context) != null) {
            playSplash()
        }

        callSplash()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        context = this

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Test Analytics")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "FlashActivity")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        recordScreenView()

        getStartedBtn?.setOnClickListener { startNextActivity() }

        val splashData = SaveSharedPreference.getSplashData(context)

        if (splashData != null && splashData.isShowGoogleAd != null && splashData.isShowGoogleAd.equals(
                "1", ignoreCase = true
            )
        ) {
            MobileAds.initialize(this) { }
            adView = findViewById(R.id.ad_view_flash)
            adRequest = AdRequest.Builder().build()
            adView?.loadAd(adRequest!!)
        }
        splashAnalytics()

        if (!NetworkUtils.isNetworkAvailable(this@FlashActivity)) {
            val intent = Intent(this@FlashActivity, AllDownloadActivity::class.java)
            intent.putExtra("from", "promo_activity")
            startActivity(intent)
            finish()
        }
    }

    private fun recordScreenView() {
        mFirebaseAnalytics!!.setCurrentScreen(this, null, null) // [END set_current_screen]
    }

    private fun callSplash() {
        val webApi = getInstance()!!.createRetrofitNewInstance()
        val call = webApi.splashData
        call.enqueue(object : Callback<SplashResponseModel?> {
            override fun onResponse(
                call: Call<SplashResponseModel?>, response: Response<SplashResponseModel?>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.promoflash != null) {
                        SaveSharedPreference.setSplashData(
                            context, Gson().toJson(response.body()!!.promoflash)
                        )
                        SaveSharedPreference.setSplashAdsSettings(
                            context, Gson().toJson(response.body()!!.promoflashAnalyticsSetting)
                        )
                        if (flashURL == null) {
                            playSplash()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SplashResponseModel?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun playSplash() {
        val splashData = SaveSharedPreference.getSplashData(context)
        val orientation = resources.configuration.orientation
        flashURL = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            splashData.flashVideoURLPC
        } else {
            // In portrait
            splashData.flashVideoURL
        }

        flashtime = splashData.flashTime * 1000

        runOnUiThread {
            if (ObjExoPlayer == null) {
                ObjExoPlayer = SimpleExoPlayer.Builder(this@FlashActivity).build()
                ObjExoPlayer!!.playWhenReady = true
            }
            setAdsHandler()
            videoView!!.player = ObjExoPlayer
            val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                this@FlashActivity, Util.getUserAgent(context!!, "iDragonPro")
            )
            val mediaSourceFactory = ProgressiveMediaSource.Factory(dataSourceFactory)
            val ObjMediaSource: MediaSource = mediaSourceFactory.createMediaSource(
                MediaItem.fromUri(
                    Uri.parse(flashURL)
                )
            )

            ObjExoPlayer!!.prepare(ObjMediaSource, true, true)
            ObjExoPlayer!!.addListener(object : Player.Listener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if (playWhenReady) {
                        when (playbackState) {
                            Player.STATE_READY -> {
                                if (progressBar != null) {
                                    progressBar!!.visibility = View.GONE
                                }
                                getStartedBtn!!.visibility = View.VISIBLE
                            }

                            Player.STATE_BUFFERING -> if (progressBar != null) {
                                progressBar!!.visibility = View.VISIBLE
                            }

                            else -> startNextActivity()
                        }
                    }
                }
            })
        }
    }

    private fun setAdsHandler() {
        adsHandler = Handler()

        runnable = object : Runnable {
            override fun run() {
                if (ObjExoPlayer != null) {
                    val duration = ObjExoPlayer!!.contentPosition
                    val secs = TimeUnit.MILLISECONDS.toSeconds(duration)

                    //Splash analytics
                    checkSendSplashAnalytics(secs, true)
                }
                adsHandler.postDelayed(this, 1000)
            }
        }

        adsHandler.post(runnable!!)
    }

    private fun checkSendSplashAnalytics(secs: Long, isSentFromTimer: Boolean) {
        val splashAdsSettings = SaveSharedPreference.getSplashAdsSettings(
            context
        )
        if (splashAdsSettings != null) {
            if (!isFirstSplashAnaSent) {
                val range1To = splashAdsSettings.range1To
                val range1From = splashAdsSettings.range1From
                if (secs == range1To.toLong() || secs > range1To) {
                    val i = range1To - range1From + 1
                    sendSplashAnalytics1(i.toLong())
                } else {
                    if (!isSentFromTimer) {
                        sendSplashAnalytics1(secs)
                    }
                }
            } else if (!isSecondSplashAnaSent) {
                val range2To = splashAdsSettings.range2To
                val range2From = splashAdsSettings.range2From
                if (secs == range2To.toLong() || secs > range2To) {
                    val i = range2To - range2From + 1
                    sendSplashAnalytics2(i.toLong())
                } else {
                    if (!isSentFromTimer) {
                        val range1To = splashAdsSettings.range1To
                        sendSplashAnalytics2(secs - range1To)
                    }
                }
            } else if (!isThirdSplashAnaSent) {
                val range3To = splashAdsSettings.range3To
                val range3From = splashAdsSettings.range3From
                if (secs == range3To.toLong() || secs > range3To) {
                    val i = range3To - range3From + 1
                    sendSplashAnalytics3(i.toLong())
                } else {
                    if (!isSentFromTimer) {
                        val range2To = splashAdsSettings.range2To
                        sendSplashAnalytics3(secs - range2To)
                    }
                }
            }
        }
    }

    private fun sendSplashAnalytics1(secs: Long) {
        isFirstSplashAnaSent = true
        val webApi = getInstance()!!.createRetrofitNewInstance()
        val call = webApi.promo_flash_analytics_range1(
            SaveSharedPreference.getUserId(context), secs
        )
        call.enqueue(object : Callback<AnalyticsResponseModel> {
            override fun onResponse(
                call: Call<AnalyticsResponseModel>, response: Response<AnalyticsResponseModel>
            ) {
                requestId = response.body()!!.getiRequestId()
            }

            override fun onFailure(call: Call<AnalyticsResponseModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun sendSplashAnalytics2(secs: Long) {
        isSecondSplashAnaSent = true
        val webApi = getInstance()!!.createRetrofitNewInstance()
        val call = webApi.promo_flash_analytics_range2(
            SaveSharedPreference.getUserId(context), secs, requestId
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

    private fun sendSplashAnalytics3(secs: Long) {
        isThirdSplashAnaSent = true
        val webApi = getInstance()!!.createRetrofitNewInstance()
        val call = webApi.promo_flash_analytics_range3(
            SaveSharedPreference.getUserId(context), secs, requestId
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

    override fun onDestroy() {
        if (ObjExoPlayer != null) {
            val duration = ObjExoPlayer!!.contentPosition
            val secs = TimeUnit.MILLISECONDS.toSeconds(duration)
            checkSendSplashAnalytics(secs, false)
        }
        try {
            adsHandler.removeCallbacksAndMessages(runnable)
        } catch (e: Exception) {
            Log.e(TAG, "Error: " + e.message)
        }
        Releaseplayer()
        super.onDestroy()
    }

    private fun Releaseplayer() {
        if (ObjExoPlayer != null) {
            ObjExoPlayer!!.stop()
            ObjExoPlayer!!.release()
            ObjExoPlayer = null
        }
        finish()
    }

    private fun startNextActivity() {
        if (isFinishing) return


        if (!NetworkUtils.isNetworkAvailable(this@FlashActivity)) {
            val intent = Intent(this@FlashActivity, AllDownloadActivity::class.java)
            intent.putExtra("from", "promo_activity")
            startActivity(intent)
            finish()
        } else {
            if (!SaveSharedPreference.getUserId(context).equals("", ignoreCase = true)) {
                if (SaveSharedPreference.isLanguageSet(context)) {
                    val intent = Intent(this@FlashActivity, Home::class.java)
                    intent.putExtra("from", "promo_activity")
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@FlashActivity, LanguageActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                val intent = Intent(this@FlashActivity, GoogleSignupActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


    override fun onPause() {
        super.onPause()
        if (ObjExoPlayer != null) {
            ObjExoPlayer!!.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()
        if (ObjExoPlayer != null) {
            ObjExoPlayer!!.playWhenReady = true
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"))
    }

    private fun splashAnalytics() {
        val webApi = getInstance()!!.createRetrofitNewInstance()
        val call = webApi.splash_screen_analytics_create(
            SaveSharedPreference.getUserId(context), DeviceUtility.getDeviceName()
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

    companion object {
        val TAG: String = FlashActivity::class.java.simpleName
    }
}

