package com.g1.onetargetsdk

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class Analytics {
    companion object {
        private var analyticsConfiguration: AnalyticsConfiguration? = null

        private fun logD(msg: String) {
            Log.e("loitpp", msg)
        }

        fun setup(analyticsConfiguration: AnalyticsConfiguration) {
            if (analyticsConfiguration.writeKey.isNullOrEmpty()) {
                throw IllegalArgumentException("writeKey cannot be null or empty")
            }
            this.analyticsConfiguration = analyticsConfiguration
        }

        @JvmStatic
        private fun service(): TrackingService? {
            if (this.analyticsConfiguration == null) {
                throw IllegalArgumentException("analyticsConfiguration not found")
            }
            val baseUrl = this.analyticsConfiguration?.getBaseUrl()
            if (baseUrl.isNullOrEmpty()) {
                throw IllegalArgumentException("base url cannot be null or empty")
            }
            return RetrofitClient.getClient(baseUrl).create(TrackingService::class.java)
        }

        @JvmStatic
        fun track(
            eventName: String?,
            properties: String?,
            onResponse: ((Response<Void>) -> Unit)? = null,
            onFailure: ((Throwable) -> Unit)? = null,
        ) {
            if (eventName.isNullOrEmpty()) {
                return
            }
            if (properties.isNullOrEmpty()) {
                return
            }
            val workspaceId = this.analyticsConfiguration?.writeKey
            if (workspaceId.isNullOrEmpty()) {
                return
            }

            val deviceId = this.analyticsConfiguration?.deviceId
            var dataDeviceId = ""
            if (deviceId.isNullOrEmpty()) {
                //do nothing
            } else {
                dataDeviceId = "web_push_player_id:$deviceId"
            }

            val email = this.analyticsConfiguration?.email
            var dataEmail = ""
            if (email.isNullOrEmpty()) {
                //do nothing
            } else {
                dataEmail = ",email:$email"
            }

            val phone = this.analyticsConfiguration?.phone
            var dataPhone = ""
            if (phone.isNullOrEmpty()) {
                //do nothing
            } else {
                dataPhone = ",phone:$phone"
            }

            val identityId = "{$dataDeviceId$dataEmail$dataPhone}"
            logD("identityId $identityId")
            val eventDate = System.currentTimeMillis().toString()
            service()?.track(
                workspace_id = workspaceId,
                identity_id = identityId,
                event_name = eventName,
                event_date = eventDate,
                eventData = properties,
            )?.enqueue(object : Callback<Void> {
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    onResponse?.invoke(response)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    onFailure?.invoke(t)
                }
            })
        }

        @SuppressLint("HardwareIds")
        fun getDeviceId(context: Context): String {

            val androidId = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
            logD("androidId $androidId")
            if (androidId.isNotEmpty()) {
                return androidId
            }

            val uniquePseudoID =
                "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10
            logD("uniquePseudoID $uniquePseudoID")
            val serial = Build.getRadioVersion()
            logD("serial $serial")
            val uuid: String =
                UUID(uniquePseudoID.hashCode().toLong(), serial.hashCode().toLong()).toString()
            logD("uuid $uuid")
            return uuid
        }
    }
}
