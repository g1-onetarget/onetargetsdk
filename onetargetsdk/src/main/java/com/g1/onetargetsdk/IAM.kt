package com.g1.onetargetsdk

import android.app.Activity
import android.util.Log
import com.g1.onetargetsdk.model.IAMData
import com.g1.onetargetsdk.model.IAMResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Loitp on 13.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class IAM {
    companion object {
        private var configuration: Configuration? = null

        private fun logD(msg: String) {
            Log.d(IAM::class.java.simpleName, msg)
        }

        private fun logE(msg: String) {
            Log.d(IAM::class.java.simpleName, msg)
        }

        fun setup(configuration: Configuration): Boolean {
//            if (configuration.writeKey.isNullOrEmpty()) {
//                logE("writeKey cannot be null or empty")
//                return false
//            }
            if (configuration.getBaseUrlIAM().isEmpty()) {
                logE("base url cannot be null or empty")
                return false
            }
            this.configuration = configuration
            return true
        }

        @JvmStatic
        private fun service(): OneTargetService? {
            if (this.configuration == null) {
                logE("configuration not found")
                return null
            }
            val baseUrl = this.configuration?.getBaseUrlIAM()
            if (baseUrl.isNullOrEmpty()) {
                logE("base url cannot be null or empty")
                return null
            }
            val isShowLog = this.configuration?.isShowLog
            return RetrofitClient.getClientIAM(
                baseUrl = baseUrl,
                isShowLogAPI = isShowLog,
            ).create(OneTargetService::class.java)
        }

        fun checkIAM(
            activity: Activity?,
            workSpaceId: String?,
            onResponse: ((isSuccessful: Boolean, code: Int, response: IAMResponse?, data: IAMData?) -> Unit)? = null,
            onFailure: ((Throwable) -> Unit)? = null,
        ) {

            fun isValid(): Boolean {
//                logD("checkIMA activity == null ${activity == null}")
//                logD("checkIMA activity.isDestroyed ${activity?.isDestroyed}")
//                logD("checkIMA activity.isFinishing ${activity?.isFinishing}")
                if (activity == null || activity.isDestroyed || activity.isFinishing) {
                    return false
                }
                return true
            }
            if (!isValid()) {
                return
            }
            val identityId = this.configuration?.deviceId
            if (workSpaceId.isNullOrEmpty() || identityId.isNullOrEmpty()) {
                return
            }
            service()?.checkIAM(
                workspaceId = workSpaceId,
                identityId = identityId,
            )?.enqueue(object : Callback<IAMResponse> {
                override fun onResponse(
                    call: Call<IAMResponse>, response: Response<IAMResponse>
                ) {
                    if (isValid()) {
                        val jsonString = response.body()?.data

//                        var map: Map<String, Any> = HashMap()
//                        map = Gson().fromJson(jsonString, map.javaClass)
//                        logD("closingAfter: ${map["closingAfter"]}")
//                        logD("activeType: ${map["activeType"]}")
//                        logD("activeValue: ${map["activeValue"]}")
//                        logD("actionClick: ${map["actionClick"]}")
//                        logD("name: ${map["name"]}")
//                        logD("message: ${map["message"]}")

                        var iamData: IAMData? = null
                        jsonString?.let { s ->
                            iamData = Gson().fromJson(s, IAMData::class.java)
                        }
                        onResponse?.invoke(
                            response.isSuccessful,
                            response.code(),
                            response.body(),
                            iamData
                        )
                    }
                }

                override fun onFailure(call: Call<IAMResponse>, t: Throwable) {
                    if (isValid()) {
                        onFailure?.invoke(t)
                    }
                }
            })
        }
    }
}
