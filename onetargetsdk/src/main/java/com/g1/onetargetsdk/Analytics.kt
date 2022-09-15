package com.g1.onetargetsdk

import com.g1.onetargetsdk.Utils.logE
import com.g1.onetargetsdk.model.MonitorEvent
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Loitp on 12.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class Analytics {
    companion object {
        private val logTag = Analytics::class.java.simpleName
        private var configuration: Configuration? = null

        fun setup(configuration: Configuration): Boolean {
//            if (configuration.writeKey.isNullOrEmpty()) {
//                logE("writeKey cannot be null or empty")
//                return false
//            }
            if (configuration.getBaseUrlTracking().isEmpty()) {
                logE(logTag, "base url cannot be null or empty")
                return false
            }
            this.configuration = configuration
            return true
        }

        @JvmStatic
        private fun service(): OneTargetService? {
            if (this.configuration == null) {
                logE(logTag, "configuration not found")
                return null
            }
            val baseUrl = this.configuration?.getBaseUrlTracking()
            if (baseUrl.isNullOrEmpty()) {
                logE(logTag, "base url cannot be null or empty")
                return null
            }
            val isShowLog = this.configuration?.isShowLog
            return RetrofitClient.getClientTracking(
                baseUrl = baseUrl,
                isShowLogAPI = isShowLog,
            )
                .create(OneTargetService::class.java)
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
                workspaceId = workSpaceId,
                identityId = jsonIdentityId,
                eventName = eventName,
                eventDate = eventDate.toString(),
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
    }
}
