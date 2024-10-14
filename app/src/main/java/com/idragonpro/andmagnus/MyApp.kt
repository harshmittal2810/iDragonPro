package com.idragonpro.andmagnus

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import com.danikula.videocache.HttpProxyCacheServer
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.GsonBuilder
import com.idragonpro.andmagnus.api.API
import com.idragonpro.andmagnus.api.RetrofitWebApi
import com.idragonpro.andmagnus.api.WebApi
import com.idragonpro.andmagnus.helpers.SaveSharedPreference
import com.idragonpro.andmagnus.security.SecurityChecks
import com.idragonpro.andmagnus.work.CancelDecryptReceiver
import com.idragonpro.andmagnus.work.CancelReceiver
import com.idragonpro.andmagnus.work.PauseReceiver
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


class MyApp : Application() {
    private var proxy: HttpProxyCacheServer? = null
    private var downloadService: Intent? = null
    lateinit var applicationScope: CoroutineScope

    init {
        System.loadLibrary("frida-check")
    }

    external fun isFridaServerListening(): Boolean

    override fun onCreate() {
        super.onCreate()
        instance = this
        MobileAds.initialize(this)

        if (BuildConfig.DEBUG) {
            val requestConfig = RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("BF9ADC98002D7C9B2B76E17C12426780")).build()
            MobileAds.setRequestConfiguration(requestConfig)
        }

        SecurityChecks.initializeSecurityChecks()

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String?> ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            SaveSharedPreference.setFbId(instance, token)
        }

        applicationScope = CoroutineScope(SupervisorJob())
        applicationScope.launch((Dispatchers.IO)) {
            try {
                initLibraries()
            } catch (e: YoutubeDLException) {
                Log.e(TAG, "failed to initialize youtubedl-android", e)
                println("failed to initialize youtubedl-android ${e.printStackTrace()}, ${e.localizedMessage}")
            }
        }

        registerReceiver(CancelReceiver(), IntentFilter())
        registerReceiver(CancelDecryptReceiver(), IntentFilter())
        registerReceiver(PauseReceiver(), IntentFilter())
    }

    private fun newProxy(): HttpProxyCacheServer {
        return HttpProxyCacheServer(this)
    }

    fun createRetrofitNewInstance(): WebApi {
        val gson = GsonBuilder().setLenient().create()
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(loggingInterceptor)
            //            httpClient.addInterceptor(new ChuckerInterceptor(getApplicationContext()));
        }
        httpClient.readTimeout(120, TimeUnit.SECONDS)
        httpClient.connectTimeout(90, TimeUnit.SECONDS)
        httpClient.writeTimeout(90, TimeUnit.SECONDS)

        val retrofit = Retrofit.Builder().baseUrl(API.BASE_NEW_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create()).client(httpClient.build())
            .build()
        val webApi = retrofit.create(WebApi::class.java)
        return webApi
    }

    fun createRazorAppRetrofitNewInstance(): RetrofitWebApi {
        val gson = GsonBuilder().setLenient().create()
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(loggingInterceptor)
        }
        httpClient.readTimeout(120, TimeUnit.SECONDS)
        httpClient.connectTimeout(90, TimeUnit.SECONDS)
        httpClient.writeTimeout(90, TimeUnit.SECONDS)

        val retrofit = Retrofit.Builder().baseUrl(API.RETROFIT_BASE_NEW_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson)).client(httpClient.build())
            .build()
        val webApi = retrofit.create(RetrofitWebApi::class.java)
        return webApi
    }

    val versionCode: Int
        get() {
            var version = 0
            try {
                val pInfo = packageManager.getPackageInfo(packageName, 0)
                version = pInfo.versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return version
        }

    @Throws(YoutubeDLException::class)
    private fun initLibraries() {
        YoutubeDL.getInstance().init(this)
    }

    companion object {
        val TAG: String = MyApp::class.java.simpleName
        private var instance: MyApp? = null

        @JvmStatic
        fun getInstance(): MyApp? {
            return instance
        }

        fun getProxy(context: Context): HttpProxyCacheServer {
            val myApp = context.applicationContext as MyApp
            return if (myApp.proxy == null) (myApp.newProxy()
                .also { myApp.proxy = it }) else myApp.proxy!!
        }
    }

    fun getDownloadService(): Intent? {
        return downloadService
    }
}