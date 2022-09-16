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
import com.g1.onetarget.common.C
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
                    finish()
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
        val workSpaceId = C.workSpaceId
        val identityId = hashMapOf<String, Any>(
            "phone" to "0766040293",
            "email" to "loitp@galaxy.one",
        )
        val profile = ArrayList<HashMap<String, Any>>()
        profile.add(
            hashMapOf(
                "profile" to "Loi1",
                "email" to "Loi1@galaxy.one",
            )
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
            profile = profile,
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
        monitorEvent.workspaceId = C.workSpaceId
        monitorEvent.identityId = hashMapOf(
            "phone" to "0766040293",
            "email" to "loitp@galaxy.one",
        )
        val profile = ArrayList<HashMap<String, Any>>()
        profile.add(
            hashMapOf(
                "profile" to "Loi1",
                "email" to "Loi1@galaxy.one",
            )
        )
        profile.add(
            hashMapOf(
                "profile" to "Loi2",
                "email" to "Loi2@galaxy.one",
            )
        )
        profile.add(
            hashMapOf(
                "profile" to "Loi3",
                "email" to "Loi3@galaxy.one",
            )
        )
        monitorEvent.profile = profile
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
