package com.g1.onetargetsdk

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.g1.onetargetsdk.model.MonitorEvent
import com.google.gson.Gson
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
//            if (configuration.writeKey.isNullOrEmpty()) {
//                logE("writeKey cannot be null or empty")
//                return false
//            }
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
            val isShowLog = this.configuration?.isShowLog
            return RetrofitClient.getClient(
                baseUrl = baseUrl,
                isShowLogAPI = isShowLog,
            )
                .create(TrackingService::class.java)
        }

        @JvmStatic
        fun trackEvent(
            monitorEvent: MonitorEvent,
            onPreExecute: ((MonitorEvent) -> Unit)? = null,
            onResponse: ((isSuccessful: Boolean, code: Int, Any?) -> Unit)? = null,
            onFailure: ((Throwable) -> Unit)? = null,
        ) {
            val jsonIdentityId = Gson().toJson(monitorEvent.identityId)
            val jsonEventData = Gson().toJson(monitorEvent.eventData)
            val tmpEventDate = monitorEvent.eventDate ?: System.currentTimeMillis()
            onPreExecute?.invoke(monitorEvent)
            callApiTrack(
                monitorEvent.workspaceId,
                jsonIdentityId,
                monitorEvent.eventName,
                tmpEventDate,
                jsonEventData,
                onResponse,
                onFailure,
            )
        }

        @JvmStatic
        fun trackEvent(
            workSpaceId: String?,
            identityId: HashMap<String, Any>?,
            eventName: String?,
            eventDate: Long?,
            eventData: HashMap<String, Any>?,
            onPreExecute: ((MonitorEvent) -> Unit)? = null,
            onResponse: ((isSuccessful: Boolean, code: Int, Any?) -> Unit)? = null,
            onFailure: ((Throwable) -> Unit)? = null,
        ) {
            val tmpWorkspaceId = if (workSpaceId.isNullOrEmpty()) {
                this.configuration?.writeKey
            } else {
                workSpaceId
            }
//            if (tmpWorkspaceId.isNullOrEmpty()) {
//                onFailure?.invoke(Throwable("writeKey is invalid"))
//                return
//            }
//            logD("identityId $identityId")
//            if (eventName.isNullOrEmpty()) {
//                onFailure?.invoke(Throwable("Event name is invalid"))
//                return
//            }
//            if (identityId.isNullOrEmpty()) {
//                onFailure?.invoke(Throwable("identityId is invalid"))
//                return
//            }
//            if (properties.isNullOrEmpty()) {
//                onFailure?.invoke(Throwable("properties is invalid"))
//                return
//            }
            val tmpEventDate = eventDate ?: System.currentTimeMillis()
            val monitorEvent = MonitorEvent()
            monitorEvent.workspaceId = tmpWorkspaceId
            monitorEvent.identityId = identityId
            monitorEvent.eventName = eventName
            monitorEvent.eventDate = tmpEventDate
            monitorEvent.eventData = eventData
            onPreExecute?.invoke(monitorEvent)

            val jsonIdentityId = Gson().toJson(identityId)
            val jsonEventData = Gson().toJson(eventData)
            callApiTrack(
                tmpWorkspaceId,
                jsonIdentityId,
                eventName,
                tmpEventDate,
                jsonEventData,
                onResponse,
                onFailure,
            )
        }

        private fun callApiTrack(
            workSpaceId: String?,
            jsonIdentityId: String?,
            eventName: String?,
            eventDate: Long,
            jsonEventData: String?,
            onResponse: ((isSuccessful: Boolean, code: Int, Any?) -> Unit)? = null,
            onFailure: ((Throwable) -> Unit)? = null,
        ) {
            service()?.track(
                workspace_id = workSpaceId,
                identity_id = jsonIdentityId,
                event_name = eventName,
                event_date = eventDate.toString(),
                eventData = jsonEventData,
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
