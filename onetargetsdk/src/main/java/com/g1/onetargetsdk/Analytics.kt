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
            val deviceId = configuration?.deviceId ?: ""
            val tmpIdentityId = hashMapOf<String, Any>(
                "one_target_user_id" to deviceId
            )
            monitorEvent.identityId?.let { map ->
                tmpIdentityId.putAll(map)
            }
            monitorEvent.identityId = tmpIdentityId

//            val tmpProfile = hashMapOf<String, Any>(
//                "one_target_user_id" to deviceId
//            )
//            monitorEvent.profile?.let { map ->
//                tmpProfile.putAll(map)
//            }
//            monitorEvent.profile = tmpProfile

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

            val jsonIdentityId = Gson().toJson(monitorEvent.identityId)
            val jsonProfile = Gson().toJson(monitorEvent.profile)
            val jsonEventData = Gson().toJson(monitorEvent.eventData)
            val tmpEventDate = monitorEvent.eventDate ?: System.currentTimeMillis()
            onPreExecute?.invoke(monitorEvent)
            callApiTrack(
                monitorEvent.workspaceId,
                jsonIdentityId,
                jsonProfile,
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
            profile: List<HashMap<String, Any>>?,
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
            val tmpEventDate = eventDate ?: System.currentTimeMillis()
            val monitorEvent = MonitorEvent()

            val deviceId = configuration?.deviceId ?: ""
            val tmpIdentityId = hashMapOf<String, Any>(
                "one_target_user_id" to deviceId
            )
            identityId?.let { map ->
                tmpIdentityId.putAll(map)
            }
//            val tmpProfile = hashMapOf<String, Any>(
//                "one_target_user_id" to deviceId
//            )
//            profile?.let { map ->
//                tmpProfile.putAll(map)
//            }

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

            monitorEvent.workspaceId = tmpWorkspaceId
            monitorEvent.identityId = tmpIdentityId
            monitorEvent.profile = tmpProfile
            monitorEvent.eventName = eventName
            monitorEvent.eventDate = tmpEventDate
            monitorEvent.eventData = eventData
            onPreExecute?.invoke(monitorEvent)

            val jsonIdentityId = Gson().toJson(monitorEvent.workspaceId)
            val jsonProfile = Gson().toJson(monitorEvent.profile)
            val jsonEventData = Gson().toJson(eventData)

            callApiTrack(
                tmpWorkspaceId,
                jsonIdentityId,
                jsonProfile,
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
            jsonProfile: String?,
            eventName: String?,
            eventDate: Long,
            jsonEventData: String?,
            onResponse: ((isSuccessful: Boolean, code: Int, Any?) -> Unit)? = null,
            onFailure: ((Throwable) -> Unit)? = null,
        ) {
            service()?.track(
                workspaceId = workSpaceId,
                identityId = jsonIdentityId,
                profile = jsonProfile,
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
