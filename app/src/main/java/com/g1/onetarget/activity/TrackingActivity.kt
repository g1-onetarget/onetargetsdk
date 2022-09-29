package com.g1.onetarget.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import com.g1.onetarget.R
import com.g1.onetarget.common.C
import com.g1.onetargetsdk.core.Analytics
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
    private var btAddToCart: AppCompatButton? = null
    private var btInputPassengerInfo: AppCompatButton? = null
    private var btPurchase: AppCompatButton? = null
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
        btAddToCart = findViewById(R.id.btAddToCart)
        btInputPassengerInfo = findViewById(R.id.btInputPassengerInfo)
        btPurchase = findViewById(R.id.btPurchase)
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
        btAddToCart?.setOnClickListener {
            trackEventAddToCart()
        }
        btInputPassengerInfo?.setOnClickListener {
            trackEventInputPassengerInfo()
        }
        btPurchase?.setOnClickListener {
            trackEventPurchase()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun trackEventByParams() {
        val workSpaceId = C.getWorkSpaceId()
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
        monitorEvent.workspaceId = C.getWorkSpaceId()
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

    @SuppressLint("SetTextI18n")
    private fun trackEventAddToCart() {
        val workSpaceId = C.getWorkSpaceId()
        val identityId = hashMapOf<String, Any>()
        val profile = ArrayList<HashMap<String, Any>>()
        val eventName = "add_to_cart"
        val eventDate = System.currentTimeMillis()
        val eventData = hashMapOf<String, Any>(
            "platform" to "APP",
            "ecommerce.currency" to "VND",
            "ecommerce.trip_from" to "HAN",
            "ecommerce.trip_to" to "SGN",
            "ecommerce.trip_start_date" to "2022-09-23",
            "ecommerce.trip_return_date" to "2022-09-23",
            "ecommerce.trip_passengers" to "1",
            "ecommerce.trip_passengers_adult" to "2",
            "ecommerce.trip_passengers_children" to "1",
            "ecommerce.trip_passengers_infant" to "0",
            "ecommerce.trip_type" to "roundTrip",
            "ecommerce.items.0.item_id" to "VJ197",
            "ecommerce.items.0.item_name" to "Flight:HAN:SGN",
            "ecommerce.items.0.item_category" to "FareOption",
            "ecommerce.items.0.item_variant" to "Eco",
            "ecommerce.items.0.item_list_id" to "Outbound:HAN:SGN",
            "ecommerce.items.0.index" to "1",
            "ecommerce.items.0.quantity" to "1",
            "ecommerce.items.0.price" to "39.000đ",
            "ecommerce.items.0.flight_time" to "06:20-08:30",
            "ecommerce.items.0.flight_from" to "HAN",
            "ecommerce.items.0.flight_to" to "SGN",
            "ecommerce.items.0.flight_date" to "2022-09-23",
            "ecommerce.trip_route" to "SGN-HN",
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
    private fun trackEventInputPassengerInfo() {
        val workSpaceId = C.getWorkSpaceId()
        val identityId = hashMapOf<String, Any>()
        val profile = ArrayList<HashMap<String, Any>>()
        profile.add(
            hashMapOf(
                "index" to 1,
                "full_name" to "Loi Android Native ${Build.MODEL}",
                "gender" to "Male",
                "address" to "45A Nguyễn Thị Minh Khai, phường 3, quận 3",
                "skyclub" to "hoangkim2512",
                "city" to "Ho Chi Minh",
                "country" to "Viet Nam",
                "email" to "myemail@gmail.com",
                "phone" to "0969696969",
                "Unsubscribed from emails" to "False",
            )
        )
        val eventName = "input_passenger_info"
        val eventDate = System.currentTimeMillis()
        val eventData = hashMapOf<String, Any>(
            "ecommerce.trip_type" to "roundTrip",
            "ecommerce.trip_route" to "SGN-HN",
            "ecommerce.value" to "hoangkim2512",
            "profile.0.full_name" to "Loi Android Native",
            "profile.0.gender" to "Male",
            "profile.0.address" to "45A Nguyễn Thị Minh Khai, phường 3, quận 3",
            "profile.0.city" to "Ho Chi Minh",
            "profile.0.country" to "Vietnam",
            "profile.0.unsubscribed_from_emails" to "false",
            "profile.0.email" to "myemail@gmail.com",
            "profile.0.phone" to "0969696969",
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
    private fun trackEventPurchase() {
        val workSpaceId = C.getWorkSpaceId()
        val identityId = hashMapOf<String, Any>()
        val profile = ArrayList<HashMap<String, Any>>()
        val eventName = "purchase"
        val eventDate = System.currentTimeMillis()
        val eventData = hashMapOf<String, Any>(
            "platform" to "Android",
            "ecommerce.currency" to "VND",
            "ecommerce.transaction_id" to "33AJV3",
            "ecommerce.value" to "2899800",
            "ecommerce.items.0.item_id" to "VJ197",
            "ecommerce.items.0.item_name" to "Flight:SGN:HAN",
            "ecommerce.items.0.item_category" to "FareOption",
            "ecommerce.items.0.item_variant" to "Deluxe",
            "ecommerce.items.0.flight_time" to "05:15-07:25",
            "ecommerce.items.0.flight_date" to "2020-11-19",
            "ecommerce.items.0.flight_from" to "SGN",
            "ecommerce.items.0.flight_to" to "HAN",
            "ecommerce.items.0.item_list_id" to "Outbound:HAN:SGN",
            "ecommerce.items.0.index" to "1",
            "ecommerce.items.0.quantity" to "1",
            "ecommerce.items.0.price" to "899000",
            "ecommerce.items.1.item_id" to "VJ120",
            "ecommerce.items.1.item_name" to "Flight:HAN:SGN",
            "ecommerce.items.1.item_category" to "FareOption",
            "ecommerce.items.1.item_variant" to "SkyBoss",
            "ecommerce.items.1.flight_time" to "20:50-23:45",
            "ecommerce.items.1.flight_date" to "2020-11-27",
            "ecommerce.items.1.flight_from" to "HAN",
            "ecommerce.items.1.flight_to" to "SGN",
            "ecommerce.items.1.item_list_id" to "Outbound:HAN:SGN",
            "ecommerce.items.1.index" to "2",
            "ecommerce.items.1.quantity" to "1",
            "ecommerce.items.1.price" to "2000800",
            "ecommerce.items.2.item_id" to "VJ120",
            "ecommerce.items.2.item_name" to "Flight:HAN:SGN",
            "ecommerce.items.2.item_category" to "FareOption",
            "ecommerce.items.2.item_variant" to "SkyBoss",
            "ecommerce.items.2.flight_time" to "20:50-23:45",
            "ecommerce.items.2.flight_date" to "2020-11-27",
            "ecommerce.items.2.flight_from" to "HAN",
            "ecommerce.items.2.flight_to" to "SGN",
            "ecommerce.items.2.item_list_id" to "Outbound:HAN:SGN",
            "ecommerce.items.2.index" to "2",
            "ecommerce.items.2.quantity" to "1",
            "ecommerce.items.2.price" to "2000800",
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

    private fun printBeautyJson(o: Any, textView: TextView?) {
        textView?.let { tv ->
            val gson = GsonBuilder().setPrettyPrinting().create()
            val json = gson.toJson(o)
            tv.text = json
        }
    }
}
