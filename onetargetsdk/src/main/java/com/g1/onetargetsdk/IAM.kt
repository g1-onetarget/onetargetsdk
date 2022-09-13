package com.g1.onetargetsdk

import android.util.Log
import com.g1.onetargetsdk.model.IAMResponse
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
            return RetrofitClient.getClient(
                baseUrl = baseUrl,
                isShowLogAPI = isShowLog,
            )
                .create(OneTargetService::class.java)
        }

        fun checkIAM(
            workSpaceId: String?,
            identityId: String?,
            onResponse: ((isSuccessful: Boolean, code: Int, Any?) -> Unit)? = null,
            onFailure: ((Throwable) -> Unit)? = null,
        ) {
            if (workSpaceId.isNullOrEmpty() || identityId.isNullOrEmpty()) {
                return
            }
            service()?.checkIAM(
                workspaceId = workSpaceId,
                identityId = identityId,
            )?.enqueue(object : Callback<IAMResponse> {
                override fun onResponse(
                    call: Call<IAMResponse>,
                    response: Response<IAMResponse>
                ) {
                    onResponse?.invoke(response.isSuccessful, response.code(), response.body())
                }

                override fun onFailure(call: Call<IAMResponse>, t: Throwable) {
                    onFailure?.invoke(t)
                }
            })
        }
    }
}
