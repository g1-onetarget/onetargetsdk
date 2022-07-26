package com.g1.onetargetsdk

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.g1.onetargetsdk.model.Input
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class Analytics {
    companion object {
        private var configuration: Configuration? = null

        private fun logD(msg: String) {
            Log.d(Analytics::class.java.simpleName, msg)
        }

        private fun logE(msg: String) {
            Log.d(Analytics::class.java.simpleName, msg)
        }

        fun setup(configuration: Configuration): Boolean {
            if (configuration.writeKey.isNullOrEmpty()) {
                logE("writeKey cannot be null or empty")
                return false
            }
            if (configuration.getBaseUrl().isEmpty()) {
                logE("base url cannot be null or empty")
                return false
            }
            this.configuration = configuration
            return true
        }

        @JvmStatic
        private fun service(): TrackingService? {
            if (this.configuration == null) {
                logE("analyticsConfiguration not found")
                return null
            }
            val baseUrl = this.configuration?.getBaseUrl()
            if (baseUrl.isNullOrEmpty()) {
                logE("base url cannot be null or empty")
                return null
            }
            return RetrofitClient.getClient(baseUrl).create(TrackingService::class.java)
        }

        @JvmStatic
        fun trackEvent(
            eventName: String?,
            properties: String?,
            onPreExecute: ((Input) -> Unit)? = null,
            onResponse: ((isSuccessful: Boolean, code: Int, Any?) -> Unit)? = null,
            onFailure: ((Throwable) -> Unit)? = null,
        ) {
            if (eventName.isNullOrEmpty()) {
                onFailure?.invoke(Throwable("Event name is invalid"))
                return
            }
            if (properties.isNullOrEmpty()) {
                onFailure?.invoke(Throwable("properties is invalid"))
                return
            }
            val workspaceId = this.configuration?.writeKey
            if (workspaceId.isNullOrEmpty()) {
                onFailure?.invoke(Throwable("writeKey is invalid"))
                return
            }

            val deviceId = this.configuration?.deviceId
            var dataDeviceId = ""
            if (deviceId.isNullOrEmpty()) {
                //do nothing
            } else {
                dataDeviceId = "web_push_player_id:$deviceId"
            }

            val email = this.configuration?.email
            var dataEmail = ""
            if (email.isNullOrEmpty()) {
                //do nothing
            } else {
                dataEmail = ",email:$email"
            }

            val phone = this.configuration?.phone
            var dataPhone = ""
            if (phone.isNullOrEmpty()) {
                //do nothing
            } else {
                dataPhone = ",phone:$phone"
            }

            val identityId = "{$dataDeviceId$dataEmail$dataPhone}"
            logD("identityId $identityId")
            val eventDate = System.currentTimeMillis().toString()
            val input = Input()
            input.workspaceId = workspaceId
            input.identityId = identityId
            input.eventName = eventName
            input.eventDate = eventDate
            input.eventData = properties
            onPreExecute?.invoke(input)
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
                    onResponse?.invoke(response.isSuccessful, response.code(), response.body())
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
