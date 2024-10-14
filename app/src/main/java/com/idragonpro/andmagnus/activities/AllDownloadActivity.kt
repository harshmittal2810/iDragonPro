package com.idragonpro.andmagnus.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import com.idragonpro.andmagnus.R
import com.idragonpro.andmagnus.fragments.AllDownloadFragment
import com.idragonpro.andmagnus.helpers.GlobalModule
import com.idragonpro.andmagnus.helpers.SaveSharedPreference
import com.idragonpro.andmagnus.utility.NetworkUtils


class AllDownloadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        if (SaveSharedPreference.getLoginFromGoogle(this)) {
            GlobalModule.startActivity = AllDownloadActivity::class.java.simpleName
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        val tvNetworkError = findViewById<AppCompatTextView>(R.id.tvNetworkError)

        if (!NetworkUtils.isNetworkAvailable(this@AllDownloadActivity)) {
            tvNetworkError.text = "Network not available"
            tvNetworkError.visibility = View.VISIBLE
        } else {
            tvNetworkError.visibility = View.GONE
        }

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar

        toolbar.setNavigationOnClickListener {
            // back button pressed
            onBackPressed()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.container, AllDownloadFragment())
                .commitNow()
        }
    }

    override fun onResume() {
        super.onResume()
        if (SaveSharedPreference.getLoginFromGoogle(this)) {
            finish()
        }
    }
}