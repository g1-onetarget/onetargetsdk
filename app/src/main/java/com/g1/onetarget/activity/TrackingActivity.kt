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
import com.g1.onetargetsdk.model.MonitorEvent
import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Created by Loitp on 12.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class TrackingActivity : AppCompatActivity() {
    private var toolbar: Toolbar? = null
    private var btTestTrackingByParams: AppCompatButton? = null
    private var btTestTrackingByObject: AppCompatButton? = null
    private var tvInput: AppCompatTextView? = null
    private var tvOutput: AppCompatTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        setupViews()
    }

    private fun setupViews() {
        setupActionBar()
    }

    private fun setupActionBar() {
        toolbar = findViewById(R.id.toolbar)
        btTestTrackingByParams = findViewById(R.id.btTestTrackingByParams)
        btTestTrackingByObject = findViewById(R.id.btTestTrackingByObject)
        tvInput = findViewById(R.id.tvInput)
        tvOutput = findViewById(R.id.tvOutput)

        setSupportActionBar(toolbar)
        supportActionBar?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.title = getString(R.string.sample_tracking)
            toolbar?.apply {
                setTitleTextColor(Color.WHITE)
                setNavigationOnClickListener {
                    onBackPressed()
                }
            }
        }

        btTestTrackingByParams?.setOnClickListener {
            trackEventByParams()
        }
        btTestTrackingByObject?.setOnClickListener {
            trackEventByObject()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun trackEventByParams() {
        val workSpaceId = "490bf1f1-2e88-4d6d-8ec4-2bb7de74f9a8"
        val identityId = hashMapOf<String, Any>(
            "user_id" to "Params${System.currentTimeMillis()}",
            "phone" to "0123456789",
            "email" to "loitp@galaxy.one",
            "deviceId" to Analytics.getDeviceId(this)
        )
        val eventName = "event_name"
        val eventDate = System.currentTimeMillis()
        val eventData = hashMapOf<String, Any>(
            "pageTitle" to "Passenger Information",
            "pagePath" to "/home"
        )

        Analytics.trackEvent(
            workSpaceId = workSpaceId,
            identityId = identityId,
            eventName = eventName,
            eventDate = eventDate,
            eventData = eventData,
            onPreExecute = { input ->
                printBeautyJson(input, tvInput)
                tvOutput?.text = "Loading..."
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

    @SuppressLint("SetTextI18n")
    private fun trackEventByObject() {
        val monitorEvent = MonitorEvent()
        monitorEvent.workspaceId = "490bf1f1-2e88-4d6d-8ec4-2bb7de74f9a8"
        monitorEvent.identityId = hashMapOf(
            "user_id" to "Object${System.currentTimeMillis()}",
            "phone" to "0123456789",
            "email" to "loitp@galaxy.one",
            "deviceId" to Analytics.getDeviceId(this)
        )
        monitorEvent.eventName = "track_now_event"
        monitorEvent.eventDate = System.currentTimeMillis()
        monitorEvent.eventData = hashMapOf(
            "name" to "Loitp",
            "bod" to "01/01/2000",
            "player_id" to 123456
        )

        Analytics.trackEvent(
            monitorEvent = monitorEvent,
            onPreExecute = { input ->
                printBeautyJson(input, tvInput)
                tvOutput?.text = "Loading..."
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
