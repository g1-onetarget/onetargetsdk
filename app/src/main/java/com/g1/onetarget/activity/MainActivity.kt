package com.g1.onetarget.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import com.g1.onetarget.R
import com.g1.onetargetsdk.Analytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class MainActivity : AppCompatActivity() {
    private var toolbar: Toolbar? = null
    private var btTestTracking: AppCompatButton? = null
    private var tvInput: AppCompatTextView? = null
    private var tvOutput: AppCompatTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
    }

    private fun setupViews() {
        setupActionBar()
    }

    private fun setupActionBar() {
        toolbar = findViewById(R.id.toolbar)
        btTestTracking = findViewById(R.id.btTestTracking)
        tvInput = findViewById(R.id.tvInput)
        tvOutput = findViewById(R.id.tvOutput)

        setSupportActionBar(toolbar)
        supportActionBar?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            toolbar?.apply {
                title = getString(R.string.app_name_sample)
                setTitleTextColor(Color.WHITE)
                setNavigationOnClickListener {
                    onBackPressed()
                }
            }
        }

        btTestTracking?.setOnClickListener {
            trackEvent()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun trackEvent() {
        tvInput?.text = ""
        tvOutput?.text = "Loading..."

        val workSpaceId = "490bf1f1-2e88-4d6d-8ec4-2bb7de74f9a8"
        val mapIdentityId = HashMap<String, String>()
        mapIdentityId["user_id"] = "User${System.currentTimeMillis()}"
        mapIdentityId["phone"] = "0123456789"
        mapIdentityId["email"] = "loitp@galaxy.one"
        mapIdentityId["deviceId"] = Analytics.getDeviceId(this)
        val identityId = Gson().toJson(mapIdentityId)
        val eventName = "event_name"
        val eventDate = System.currentTimeMillis().toString()
        val mapEventData = HashMap<String, String>()
        mapEventData["pageTitle"] = "Passenger Information"
        mapEventData["pagePath"] = "/home"
        val eventData = Gson().toJson(mapEventData)

        Analytics.trackEvent(
            workSpaceId = workSpaceId,
            identityId = identityId,
            eventName = eventName,
            eventDate = eventDate,
            eventData = eventData,
            onPreExecute = { input ->
                printBeautyJson(input, tvInput)
            },
            onResponse = { isSuccessful, code, response ->
                tvOutput?.text =
                    "onResponse" +
                            "\nisSuccessful: $isSuccessful" +
                            "\ncode: $code" +
                            "\nresponse body: ${Gson().toJson(response)}"
            },
            onFailure = { t ->
                tvOutput?.text = "onFailure $t"
            }
        )
    }

    private fun printBeautyJson(o: Any, textView: TextView?) {
        textView?.let { tv ->
            val gson = GsonBuilder().setPrettyPrinting().create()
            val json = gson.toJson(o)
            tv.text = json
        }
    }
}
