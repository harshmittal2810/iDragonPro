package com.idragonpro.andmagnus.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.idragonpro.andmagnus.MyApp.Companion.getInstance
import com.idragonpro.andmagnus.R
import com.idragonpro.andmagnus.adapters.HomeTabAdapter
import com.idragonpro.andmagnus.api.API
import com.idragonpro.andmagnus.fragments.NewHomeFragment
import com.idragonpro.andmagnus.helpers.GlobalModule
import com.idragonpro.andmagnus.helpers.LocaleHelper
import com.idragonpro.andmagnus.helpers.NonSwipeableViewPager
import com.idragonpro.andmagnus.helpers.SaveSharedPreference
import com.idragonpro.andmagnus.helpers.WebService
import com.idragonpro.andmagnus.responseModels.AdsResponseModel
import com.idragonpro.andmagnus.responseModels.GeneralResponseModel
import com.idragonpro.andmagnus.responseModels.ProfileResponseModel
import com.idragonpro.andmagnus.utility.DeviceUtility
import com.idragonpro.andmagnus.utils.PermissionInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Home : AppCompatActivity(), OnTabSelectedListener {
    private var tabLayout: TabLayout? = null
    private var viewPager: NonSwipeableViewPager? = null
    var progressBar: ProgressBar? = null
    var sFrom: String? = null

    //By Bushra
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var adapter: HomeTabAdapter? = null
    private var mPublisherInterstitialAd: InterstitialAd? = null
    private var mPublisherInterstitialAdNew: AdManagerInterstitialAd? = null
    private var updateBottomDialogFragment: UpdateBottomDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Test Analytics")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Home")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

        recordScreenView()

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        sFrom = intent.getStringExtra("from")
        progressBar = findViewById(R.id.progressBar)

        val llHome = findViewById<LinearLayout>(R.id.llHome)
        val llSearch = findViewById<LinearLayout>(R.id.llSearch)
        val llWatchlist = findViewById<LinearLayout>(R.id.llWatchList)
        val llProfile = findViewById<LinearLayout>(R.id.llProfile)
        val llDownload = findViewById<LinearLayout>(R.id.llDownload)

        llHome?.setOnClickListener {
            if (tabLayout != null && viewPager != null && viewPager!!.childCount > 0) {
                tabLayout!!.getTabAt(0)!!.select()
            }
        }

        llDownload.setOnClickListener {
            val intent = Intent(this@Home, AllDownloadActivity::class.java)
            startActivity(intent)
        }

        llSearch.setOnClickListener {
            val intent = Intent(this@Home, SearchActivity::class.java)
            startActivity(intent)
        }

        llProfile?.setOnClickListener {
            val intent = Intent(this@Home, UserProfileActivity::class.java)
            startActivity(intent)
        }

        llWatchlist.setOnClickListener {
            val intent = Intent(this@Home, WatchList::class.java)
            startActivity(intent)
        }

        val imgShop = findViewById<ImageView>(R.id.imgShop)
        Glide.with(this@Home).load(R.drawable.vip_image) // or url
            .into(imgShop)
        imgShop.setOnClickListener { view: View? ->
            val intent = Intent(this@Home, Register::class.java)
            startActivity(intent)
        }

        val analyticBundle = Bundle()
        analyticBundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Home")
        analyticBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, sHomeData)
        analyticBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, analyticBundle)
        adsDetails
        if (SaveSharedPreference.getAdsData(this@Home) != null) {
            if (SaveSharedPreference.getAdsData(this@Home).gStatus != null && SaveSharedPreference.getAdsData(
                    this@Home
                ).gStatus == API.ACTIVE
            ) {
                //                loadInterstitialAd();
            } else if (SaveSharedPreference.getAdsData(this@Home).mopubStatus != null && (SaveSharedPreference.getAdsData(
                    this@Home
                ).mopubStatus == API.ACTIVE)
            ) {
                loadAdManagerInterstitialAd()
            }
        }

        // Check notification permission on Android 13 and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkAndRequestNotificationPermission()
        }
    }

    private fun checkAndRequestNotificationPermission() {
        XXPermissions.with(this).permission(Permission.POST_NOTIFICATIONS)
            .interceptor(PermissionInterceptor()).request(object : OnPermissionCallback {
                override fun onGranted(permissions: List<String>, all: Boolean) {
                    if (!all) {
                        return
                    }
                }

                override fun onDenied(permissions: List<String>, never: Boolean) {
                    super.onDenied(permissions, never)
                    Log.d(TAG, "onDenied: =====")
                }
            })
    }

    private fun loadAdManagerInterstitialAd() {
        val adRequest = AdManagerAdRequest.Builder().build()

        AdManagerInterstitialAd.load(this,
            SaveSharedPreference.getAdsData(this@Home).mopubInterstial,
            adRequest,
            object : AdManagerInterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: AdManagerInterstitialAd) {
                    mPublisherInterstitialAdNew = interstitialAd
                    mPublisherInterstitialAdNew!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                mPublisherInterstitialAdNew = null
                                loadAdManagerInterstitialAd()
                            }

                            override fun onAdShowedFullScreenContent() {
                                mPublisherInterstitialAd = null
                            }
                        }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    mPublisherInterstitialAdNew = null
                }
            })
    }

    private val adsDetails: Unit
        get() {
            val webApi = getInstance()!!.createRetrofitNewInstance()
            val call = webApi.adsData
            call.enqueue(object : Callback<AdsResponseModel?> {
                override fun onResponse(
                    call: Call<AdsResponseModel?>, response: Response<AdsResponseModel?>
                ) {
                    if (response.body() != null) {
                        if (response.body()!!.adsData != null) {
                            SaveSharedPreference.setAdsContent(
                                this@Home, Gson().toJson(response.body()!!.adsData)
                            )
                            getVastTag()
                        }
                    }
                }

                override fun onFailure(call: Call<AdsResponseModel?>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }

    private fun getVastTag() {
        if (SaveSharedPreference.getAdsData(this@Home) != null) {
            if (SaveSharedPreference.getAdsData(this@Home).gStatus != null && SaveSharedPreference.getAdsData(
                    this@Home
                ).gStatus == API.ACTIVE
            ) {
                val clsGetVastTag =
                    ClassGetVastTag(this@Home, SaveSharedPreference.getAdsData(this@Home).gVast)
                clsGetVastTag.execute()
            } else if (SaveSharedPreference.getAdsData(this@Home).mopubStatus != null && (SaveSharedPreference.getAdsData(
                    this@Home
                ).mopubStatus == API.ACTIVE)
            ) {
                val clsGetVastTag =
                    ClassGetVastTag(this@Home, SaveSharedPreference.getAdsData(this@Home).mopubVast)
                clsGetVastTag.execute()
            } else if (SaveSharedPreference.getAdsData(this@Home).sdkStatus != null && SaveSharedPreference.getAdsData(
                    this@Home
                ).sdkStatus == API.ACTIVE
            ) {
                val clsGetVastTag =
                    ClassGetVastTag(this@Home, SaveSharedPreference.getAdsData(this@Home).sdkVast)
                clsGetVastTag.execute()
            }
        }
    }

    private fun callHome() {
        if (adapter == null) {
            tabLayout = findViewById(R.id.tabLayout)
            viewPager = findViewById(R.id.pager)
            val tab1 = tabLayout!!.newTab()
            tab1.setCustomView(R.layout.custom_tab)
            val tabName1 = tab1.customView!!.findViewById<TextView>(R.id.txt_tab_name)
            tabName1.text = getString(R.string.home)
            tabLayout!!.addTab(tab1)

            val tab2 = tabLayout!!.newTab()
            tab2.setCustomView(R.layout.custom_tab)
            val tabName2 = tab2.customView!!.findViewById<TextView>(R.id.txt_tab_name)
            tabName2.text = getString(R.string.web_series)
            tabLayout!!.addTab(tab2)

            val tab3 = tabLayout!!.newTab()
            tab3.setCustomView(R.layout.custom_tab)
            val tabName3 = tab3.customView!!.findViewById<TextView>(R.id.txt_tab_name)
            tabName3.text = getString(R.string.movies)
            tabLayout!!.addTab(tab3)

            val tab4 = tabLayout!!.newTab()
            tab4.setCustomView(R.layout.custom_tab)
            val tabName4 = tab4.customView!!.findViewById<TextView>(R.id.txt_tab_name)
            tabName4.text = getString(R.string.kids)
            tabLayout!!.addTab(tab4)

            adapter = HomeTabAdapter(0, supportFragmentManager, tabLayout!!.tabCount)
            viewPager!!.setOffscreenPageLimit(0)
            viewPager!!.setAdapter(adapter)

            tabLayout!!.addOnTabSelectedListener(this@Home)

            viewPager!!.visibility = View.VISIBLE
            tabLayout!!.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        setLocation()

        try {
            showAppUpdateDialog()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun recordScreenView() {
        mFirebaseAnalytics!!.setCurrentScreen(this, null, null)
    }

    private fun setLocation() {
        if (adapter == null) {
            callProfile()
        }
    }

    override fun onRestart() {
        super.onRestart()
        setLocation()
    }

    private fun callProfile() {
        val progressDialog =
            GlobalModule.showProgressDialog(getString(R.string.magic_home), this@Home)
        val webApi = getInstance()!!.createRetrofitNewInstance()
        val call = webApi.getProfile(
            SaveSharedPreference.getUserId(this@Home),
            DeviceUtility.getDeviceName(),
            SaveSharedPreference.getFbId(this@Home),
            getInstance()!!.versionCode
        )
        call.enqueue(object : Callback<ProfileResponseModel?> {
            override fun onResponse(
                call: Call<ProfileResponseModel?>, response: Response<ProfileResponseModel?>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.user != null) {
                        val userModel = response.body()!!.user
                        SaveSharedPreference.setUserId(this@Home, userModel.id.toString())
                        SaveSharedPreference.setFirstName(this@Home, userModel.name)
                        SaveSharedPreference.setLastName(this@Home, userModel.lastname)
                        SaveSharedPreference.setEmail(this@Home, userModel.email)
                        SaveSharedPreference.setProfilePic(this@Home, userModel.profilePic)
                        SaveSharedPreference.setNotification(
                            this@Home, userModel.isNotificationActive
                        )

                        if (!SaveSharedPreference.getLoginFromGoogle(this@Home)) {
                            if (userModel.mobileno != null && !userModel.mobileno.isEmpty() && !userModel.mobileno.equals(
                                    "null", ignoreCase = true
                                )
                            ) {
                                SaveSharedPreference.setMobileNumber(this@Home, userModel.mobileno)
                            }
                            if (userModel.subscriptions != null && userModel.subscriptions.getPackage() != null && userModel.subscriptions.getPackage()
                                    .isNotEmpty() && !userModel.subscriptions.getPackage()
                                    .equals("null", ignoreCase = true)
                            ) {
                                SaveSharedPreference.setSubTitle(
                                    this@Home, userModel.subscriptions.getPackage()
                                )
                            }
                            if (userModel.daysdiff >= 0) {
                                SaveSharedPreference.setRemDays(this@Home, userModel.daysdiff)
                            }
                            if (userModel.timediff >= 0) {
                                SaveSharedPreference.setTimeDiff(this@Home, userModel.timediff)
                            }
                            if (userModel.subscriptions_web != null && userModel.subscriptions_web.getPackage() != null && userModel.subscriptions_web.getPackage()
                                    .isNotEmpty() && !userModel.subscriptions_web.getPackage()
                                    .equals("null", ignoreCase = true)
                            ) {
                                SaveSharedPreference.setWebSubTitle(
                                    this@Home, userModel.subscriptions_web.getPackage()
                                )
                            }
                            if (userModel.daysdiff_web >= 0) {
                                SaveSharedPreference.setWebRemDays(
                                    this@Home, userModel.daysdiff_web
                                )
                            }
                            if (userModel.timediff_web >= 0) {
                                SaveSharedPreference.setWebTimeDiff(
                                    this@Home, userModel.timediff_web
                                )
                            }
                        }
                        callHome()
                        updateFirebaseId()
                    }
                }
                if (!this@Home.isFinishing) {
                    if (progressDialog!!.isShowing) {
                        progressDialog.dismiss()
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponseModel?>, t: Throwable) {
                t.printStackTrace()
                //hideProgressBar();
                if (!this@Home.isFinishing) {
                    if (progressDialog != null && progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                }
            }
        })
    }

    private fun updateFirebaseId() {
        val webApi = getInstance()!!.createRetrofitNewInstance()
        val call = webApi.addFirebaseId(
            SaveSharedPreference.getUserId(this@Home), SaveSharedPreference.getFbId(this@Home)
        )
        call.enqueue(object : Callback<GeneralResponseModel?> {
            override fun onResponse(
                call: Call<GeneralResponseModel?>, response: Response<GeneralResponseModel?>
            ) {
            }

            override fun onFailure(call: Call<GeneralResponseModel?>, t: Throwable) {
            }
        })
    }

    fun onRefresh() {
        for (i in 0 until adapter!!.count) {
            val newHomeFragment = adapter!!.getRegisteredFragment(i) as NewHomeFragment
            newHomeFragment.onRefreshCalled()
        }
    }

    fun showAppUpdateDialog() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { result: AppUpdateInfo ->
            if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && !isFinishing) {
                if (updateBottomDialogFragment == null || !updateBottomDialogFragment!!.isVisible) {
                    if (result.clientVersionStalenessDays() != null) {
                        if (result.clientVersionStalenessDays()!! >= 2) {
                            updateBottomDialogFragment = UpdateBottomDialogFragment.newInstance()
                            updateBottomDialogFragment!!.isCancelable = false
                            updateBottomDialogFragment!!.show(
                                supportFragmentManager, UpdateBottomDialogFragment.TAG
                            )
                        }
                    } else {
                        updateBottomDialogFragment = UpdateBottomDialogFragment.newInstance()
                        updateBottomDialogFragment!!.isCancelable = false
                        updateBottomDialogFragment!!.show(
                            supportFragmentManager, UpdateBottomDialogFragment.TAG
                        )
                    }
                }
            }
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        viewPager!!.currentItem = tab.position
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
    }


    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (GlobalModule.startActivity != null && GlobalModule.startActivity.equals(
                Login::class.java.simpleName, ignoreCase = true
            )
        ) {
            GlobalModule.startActivity = ""
            recreate()
        }
        if (GlobalModule.startActivity != null && GlobalModule.startActivity.equals(
                LanguageActivity::class.java.simpleName, ignoreCase = true
            )
        ) {
            GlobalModule.startActivity = ""
            recreate()
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"))
    }

    /*private val pariSectionDetails: Unit
        get() {
            val webApi = getInstance()!!.createRetrofitNewInstance()
            val call = webApi.pariSectionDetails
            call.enqueue(object : Callback<PariSectionDetailResponse?> {
                override fun onResponse(
                    call: Call<PariSectionDetailResponse?>,
                    response: Response<PariSectionDetailResponse?>
                ) {
                    if (response.body() != null) {
                        val setting = response.body()!!.pariSection.setting[0]
                        if (setting.isButtonShow.equals("1", ignoreCase = true) && !isFinishing) {
                            loadButterflyImage(
                                setting.getiButtonImageUrl(),
                                setting.getiAudioUrl(),
                                response.body()!!.pariSection.section
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<PariSectionDetailResponse?>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }

    private fun loadButterflyImage(imageUrl: String, audioUrl: String, section: List<Section>) {
        imgHome!!.visibility = View.GONE
        Glide.with(imgButterFly!!.context).load(imageUrl).into(imgButterFly!!)
        imgButterFly!!.setOnClickListener { v: View? ->
            val bundle = Bundle()
            bundle.putString("MP3_URL", audioUrl)
            bundle.putSerializable("SECTION", section as Serializable)
            val intent = Intent(this@Home, PariSectionDetailActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }*/

    class UpdateBottomDialogFragment : BottomSheetDialogFragment() {
        private var mContext: Context? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.bottom_sheet_update, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            view.findViewById<View>(R.id.tvUpdate).setOnClickListener { v: View? ->
                this@UpdateBottomDialogFragment.dismiss()
                try {
                    startActivity(
                        Intent(
                            "android.intent.action.VIEW",
                            Uri.parse("market://details?id=" + mContext!!.packageName)
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            "android.intent.action.VIEW",
                            Uri.parse("https://play.google.com/store/apps/details?id=" + mContext!!.packageName)
                        )
                    )
                }
            }
            view.findViewById<View>(R.id.imgCancel)
                .setOnClickListener { v: View? -> (mContext as Activity?)!!.finishAffinity() }
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
            const val TAG: String = "UpdateBottomDialogFragment"
            fun newInstance(): UpdateBottomDialogFragment {
                return UpdateBottomDialogFragment()
            }
        }
    }

    private inner class ClassGetVastTag(private val context: Context?, private val url: String) :
        AsyncTask<String?, Void?, String>() {

        override fun doInBackground(vararg strings: String?): String {
            var aResponse = ""
            val webService = WebService()
            aResponse = webService.getVast(url, context)
            GlobalModule.vmap = aResponse
            return aResponse
        }

        override fun onPostExecute(aResponse: String?) {
            super.onPostExecute(aResponse)
        }
    }

    companion object {
        private val TAG: String = Home::class.java.simpleName
        var sHomeData: String = ""
    }
}
