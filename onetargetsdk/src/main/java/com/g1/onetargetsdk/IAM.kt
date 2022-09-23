package com.g1.onetargetsdk

import android.content.Context
import android.util.Log
import android.widget.Toast
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

        fun setup(configuration: Configuration, context: Context?): Boolean {
            if (configuration.writeKey.isNullOrEmpty()) {
                logE("writeKey cannot be null or empty")
                return false
            }
            if (configuration.getBaseUrlIAM().isEmpty()) {
                logE("base url cannot be null or empty")
                return false
            }
            this.configuration = configuration
            if (configuration.isEnableIAM) {
                checkIAM(context)
            }
            return true
        }

        private fun checkIAM(context: Context?) {
            checkIAM(
                context,
                onResponse = { isSuccessful, code, response, data ->
                    logD("isSuccessful $isSuccessful")
                    logD("code $code")
                    logD("response $response")

                    val gson = Gson()
                    data?.message?.let { jsonStringMessage ->
//                        logD("jsonString: $jsonStringMessage")
                        var mapJsonContent: Map<String, Any> = HashMap()
                        mapJsonContent =
                            gson.fromJson(jsonStringMessage, mapJsonContent.javaClass)

                        val jsonContent = mapJsonContent["jsonContent"]
//                        logD("jsonContent: $jsonContent")
//                        logD("jsonContent: " + gson.toJson(jsonContent))

                        gson.toJson(jsonContent)?.let { jsonStringJsonContent ->
                            var mapMessage: Map<String, Any> = HashMap()
                            mapMessage =
                                gson.fromJson(jsonStringJsonContent, mapMessage.javaClass)

                            val message = mapMessage["message"]
//                            logD("message: $message")

                            message?.toString()?.let { jsonString ->
                                var mapHtmlContent: Map<String, Any> = HashMap()
                                mapHtmlContent =
                                    gson.fromJson(jsonString, mapHtmlContent.javaClass)

                                val htmlContent = mapHtmlContent["htmlContent"]
                                logD("loitpp htmlContent: $htmlContent")

                            }
                        }
                    }

                    checkIAM(context)
                },
                onFailure = { t ->
                    t.printStackTrace()
                    checkIAM(context)
                },
            )
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

        private fun checkIAM(
            context: Context?,
            onResponse: ((isSuccessful: Boolean, code: Int, response: IAMResponse?, data: IAMData?) -> Unit)? = null,
            onFailure: ((Throwable) -> Unit)? = null,
        ) {

            fun isValid(): Boolean {
//                logD("checkIMA activity == null ${activity == null}")
//                logD("checkIMA activity.isDestroyed ${activity?.isDestroyed}")
//                logD("checkIMA activity.isFinishing ${activity?.isFinishing}")
//                if (activity == null || activity.isDestroyed || activity.isFinishing) {
//                    return false
//                }

                if (context?.applicationContext == null) {
                    return false
                }
                return true
            }
            if (!isValid()) {
                return
            }
            val workSpaceId = this.configuration?.writeKey
            val identityId = this.configuration?.deviceId
            if (workSpaceId.isNullOrEmpty() || identityId.isNullOrEmpty()) {
                return
            }
//            logD(">>>>>>>checkIAM workSpaceId $workSpaceId, identityId $identityId")
            if (this.configuration?.isShowLog == true && BuildConfig.DEBUG) {
                Toast.makeText(context, "checkIAM", Toast.LENGTH_LONG).show()
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
                        var iamData: IAMData? = null
                        jsonString?.let { s ->
                            iamData = Gson().fromJson(s, IAMData::class.java)
                        }
                        onResponse?.invoke(
                            response.isSuccessful, response.code(), response.body(), iamData
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
