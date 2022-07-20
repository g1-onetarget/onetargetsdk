package com.g1.onetarget.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.g1.onetarget.R
import com.g1.onetargetsdk.Analytics
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
    }

    private fun setupViews() {
        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            toolbar.title = getString(R.string.app_name)
            toolbar.setTitleTextColor(Color.WHITE)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }

        btTestTracking.setOnClickListener {
            track()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun track() {
        tv.text = "Loading..."
        Analytics.track(
            eventName = "page_view",
            properties = "{pageTitle:Passenger Information,pagePath:/passengers/}",
            { response ->
                tv.text =
                    "onResponse" +
                            "\nisSuccessful: ${response.isSuccessful}" +
                            "\ncode: ${response.code()}" +
                            "\nbody: ${Gson().toJson(response.body())}"
            },
            { t ->
                tv.text = "onFailure $t"
            }
        )
    }
}
