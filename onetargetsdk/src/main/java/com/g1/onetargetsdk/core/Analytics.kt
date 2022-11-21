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

            configuration.oneTargetAppPushID?.let { id ->
                OS.setup(context = context, appId = id)
            }

            val resultSetupIAM = IAM.setup(configuration = configuration, context = context)
            Utils.logD(logTag, "resultSetupIAM $resultSetupIAM")

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
            val tmpIdentityId = hashMapOf<String, Any>()
            tmpIdentityId.apply {
                OS.getDeviceId(configuration)?.let { deviceId ->
                    this["one_target_user_id"] = deviceId
                }
                OS.getAppPushPlayerId()?.let { osUserId ->
                    this.put("app_push_player_id", osUserId)
                }
                monitorEvent.identityId?.let { map ->
                    this.putAll(map)
                }
                monitorEvent.identityId = this
            }

            if (monitorEvent.profile.isNullOrEmpty()) {
                val tmpProfile = ArrayList<HashMap<String, Any>>()
                tmpProfile.apply {
                    val itemFirst = hashMapOf<String, Any>()
                    OS.getDeviceId(configuration)?.let { deviceId ->
                        itemFirst["one_target_user_id"] = deviceId
                    }
                    OS.getAppPushPlayerId()?.let { osUserId ->
                        itemFirst.put("app_push_player_id", osUserId)
                    }
                    this.add(itemFirst)
                    monitorEvent.profile = this
                }
            } else {
                monitorEvent.profile?.firstOrNull()?.let { itemFirst ->
                    OS.getDeviceId(configuration)?.let { deviceId ->
                        itemFirst.put("one_target_user_id", deviceId)
                    }
                    OS.getAppPushPlayerId()?.let { osUserId ->
                        itemFirst.put("app_push_player_id", osUserId)
                    }
                }
            }

            monitorEvent.eventDate = System.currentTimeMillis()

            val tmpEventData = hashMapOf<String, Any>()
            tmpEventData.apply {
                this["platform"] = "Android"
                monitorEvent.eventData?.let { map ->
                    this.putAll(map)
                }
                monitorEvent.eventData = this
            }

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

            val tmpIdentityId = hashMapOf<String, Any>()
            tmpIdentityId.apply {
                OS.getDeviceId(configuration)?.let { deviceId ->
                    this.put("one_target_user_id", deviceId)
                }
                OS.getAppPushPlayerId()?.let { osUserId ->
                    this.put("app_push_player_id", osUserId)
                }
                identityId?.let { map ->
                    this.putAll(map)
                }
            }

            val tmpProfile = ArrayList<HashMap<String, Any>>()
            tmpProfile.apply {
                if (profile.isNullOrEmpty()) {
                    val itemFirst = hashMapOf<String, Any>()
                    OS.getDeviceId(configuration)?.let { deviceId ->
                        itemFirst.put("one_target_user_id", deviceId)
                    }
                    OS.getAppPushPlayerId()?.let { osUserId ->
                        itemFirst.put("app_push_player_id", osUserId)
                    }
                    this.add(itemFirst)
                } else {
                    this.addAll(profile)
                    val itemFirst = profile.first()
                    OS.getDeviceId(configuration)?.let { deviceId ->
                        itemFirst["one_target_user_id"] = deviceId
                    }
                    OS.getAppPushPlayerId()?.let { osUserId ->
                        itemFirst["app_push_player_id"] = osUserId
                    }
                }
            }

            val tmpEventData = hashMapOf<String, Any>()
            tmpEventData.apply {
                this["platform"] = "Android"
                eventData?.let { map ->
                    this.putAll(map)
                }
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

            if (configuration?.isEnvStag() == true) {
                service()?.trackPostStg(
                    body = requestTrack
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
            } else {
                service()?.trackPost(
                    body = requestTrack
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
}
