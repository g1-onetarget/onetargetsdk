package com.g1.onetargetsdk.core

import android.content.Context
import com.g1.onetargetsdk.common.Utils
import com.g1.onetargetsdk.model.MonitorEvent
import com.g1.onetargetsdk.model.request.RequestTrack
import com.g1.onetargetsdk.services.OneTargetService
import com.g1.onetargetsdk.services.RetrofitClient
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

        fun setup(configuration: Configuration, context: Context): Boolean {
            if (configuration.writeKey.isNullOrEmpty()) {
                Utils.logE(logTag, "writeKey cannot be null or empty")
                return false
            }
            if (configuration.getBaseUrlTracking().isEmpty()) {
                Utils.logE(logTag, "base url cannot be null or empty")
                return false
            }
            Companion.configuration = configuration

            val resultSetupIAM = IAM.setup(configuration = configuration, context = context)
            Utils.logD(logTag, "resultSetupIAM $resultSetupIAM")

            configuration.onetargetAppPushID?.let { id ->
                OS.setup(context = context, appId = id)
            }

            return resultSetupIAM
        }

        private fun onMsg(msg: String) {
            configuration?.let { cf ->
                cf.onMsg?.invoke(msg)
            }
        }

        @JvmStatic
        private fun service(): OneTargetService? {
            if (configuration == null) {
                Utils.logE(logTag, "configuration not found")
                return null
            }
            if (configuration?.writeKey.isNullOrEmpty()) {
                Utils.logE(logTag, "writeKey cannot be null or empty")
                return null
            }
            val baseUrl = configuration?.getBaseUrlTracking()
            if (baseUrl.isNullOrEmpty()) {
                Utils.logE(logTag, "base url cannot be null or empty")
                return null
            }
            val isShowLog = configuration?.isShowLog
            return RetrofitClient.getClientTracking(
                baseUrl = baseUrl,
                isShowLogAPI = isShowLog,
                timeout = 30,
                onMsg = { curl ->
                    onMsg(curl)
                }
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
            val deviceId = configuration?.deviceId ?: ""
            val tmpIdentityId = hashMapOf<String, Any>(
                "one_target_user_id" to deviceId
            )
            monitorEvent.identityId?.let { map ->
                tmpIdentityId.putAll(map)
            }
            monitorEvent.identityId = tmpIdentityId

            if (monitorEvent.profile.isNullOrEmpty()) {
                val tmpProfile = ArrayList<HashMap<String, Any>>()
                val itemFirst = hashMapOf<String, Any>(
                    "one_target_user_id" to deviceId
                )
                tmpProfile.add(itemFirst)
                monitorEvent.profile = tmpProfile
            } else {
                monitorEvent.profile?.firstOrNull()?.put("one_target_user_id", deviceId)
            }
            monitorEvent.eventDate = System.currentTimeMillis()
            val tmpEventData = hashMapOf<String, Any>(
                "platform" to "Android"
            )
            monitorEvent.eventData?.let { map ->
                tmpEventData.putAll(map)
            }
            monitorEvent.eventData = tmpEventData

            val jsonIdentityId = Gson().toJson(monitorEvent.identityId)
            val jsonProfile = Gson().toJson(monitorEvent.profile)
            val jsonEventData = Gson().toJson(monitorEvent.eventData)
            onPreExecute?.invoke(monitorEvent)
            callApiTrackPost(
                jsonIdentityId,
                jsonProfile,
                monitorEvent.eventName,
                monitorEvent.eventDate,
                jsonEventData,
                onResponse,
                onFailure,
            )
        }

        @JvmStatic
        fun trackEvent(
            identityId: HashMap<String, Any>?,
            profile: List<HashMap<String, Any>>?,
            eventName: String?,
            eventData: HashMap<String, Any>?,
            onPreExecute: ((MonitorEvent) -> Unit)? = null,
            onResponse: ((isSuccessful: Boolean, code: Int, Any?) -> Unit)? = null,
            onFailure: ((Throwable) -> Unit)? = null,
        ) {
            val workspaceId = configuration?.writeKey
            if (workspaceId.isNullOrEmpty()) {
                return
            }
            val monitorEvent = MonitorEvent()

            val deviceId = configuration?.deviceId ?: ""
            val tmpIdentityId = hashMapOf<String, Any>(
                "one_target_user_id" to deviceId
            )
            identityId?.let { map ->
                tmpIdentityId.putAll(map)
            }
            val tmpProfile = ArrayList<HashMap<String, Any>>()
            if (profile.isNullOrEmpty()) {
                val itemFirst = hashMapOf<String, Any>(
                    "one_target_user_id" to deviceId
                )
                tmpProfile.add(itemFirst)
            } else {
                tmpProfile.addAll(profile)
                val itemFirst = profile.first()
                itemFirst["one_target_user_id"] = deviceId
            }

            val tmpEventData = hashMapOf<String, Any>(
                "platform" to "Android"
            )
            eventData?.let { map ->
                tmpEventData.putAll(map)
            }

            monitorEvent.workspaceId = workspaceId
            monitorEvent.identityId = tmpIdentityId
            monitorEvent.profile = tmpProfile
            monitorEvent.eventName = eventName
            monitorEvent.eventDate = System.currentTimeMillis()
            monitorEvent.eventData = tmpEventData
            onPreExecute?.invoke(monitorEvent)

            val jsonIdentityId = Gson().toJson(monitorEvent.identityId)
            val jsonProfile = Gson().toJson(monitorEvent.profile)
            val jsonEventData = Gson().toJson(monitorEvent.eventData)

            callApiTrackPost(
                jsonIdentityId,
                jsonProfile,
                eventName,
                monitorEvent.eventDate,
                jsonEventData,
                onResponse,
                onFailure,
            )
        }

//        @Deprecated("It would be better if tracking by POST")
//        private fun callApiTrackGet(
//            jsonIdentityId: String?,
//            jsonProfile: String?,
//            eventName: String?,
//            eventDate: Long?,
//            jsonEventData: String?,
//            onResponse: ((isSuccessful: Boolean, code: Int, Any?) -> Unit)? = null,
//            onFailure: ((Throwable) -> Unit)? = null,
//        ) {
//            val workSpaceId = configuration?.writeKey
//            if (workSpaceId.isNullOrEmpty()) {
//                return
//            }
//            service()?.trackGet(
//                workspaceId = workSpaceId,
//                identityId = jsonIdentityId,
//                profile = jsonProfile,
//                eventName = eventName,
//                eventDate = eventDate.toString(),
//                eventData = jsonEventData,
//            )?.enqueue(object : Callback<Void> {
//                override fun onResponse(
//                    call: Call<Void>,
//                    response: Response<Void>
//                ) {
//                    onResponse?.invoke(response.isSuccessful, response.code(), response.body())
//                }
//
//                override fun onFailure(call: Call<Void>, t: Throwable) {
//                    onFailure?.invoke(t)
//                }
//            })
//        }

        private fun callApiTrackPost(
            jsonIdentityId: String?,
            jsonProfile: String?,
            eventName: String?,
            eventDate: Long?,
            jsonEventData: String?,
            onResponse: ((isSuccessful: Boolean, code: Int, Any?) -> Unit)? = null,
            onFailure: ((Throwable) -> Unit)? = null,
        ) {
            val workSpaceId = configuration?.writeKey
            if (workSpaceId.isNullOrEmpty()) {
                return
            }
            val requestTrack = RequestTrack()
            requestTrack.workspace_id = workSpaceId
            requestTrack.identity_id = jsonIdentityId
            requestTrack.profile = jsonProfile
            requestTrack.event_name = eventName
            requestTrack.event_date = eventDate
            requestTrack.eventData = jsonEventData

            service()?.trackPost(
                requestTrack
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
