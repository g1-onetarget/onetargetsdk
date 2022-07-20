package com.g1.onetargetsdk

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Analytics {
    companion object {
        private var analyticsConfiguration: AnalyticsConfiguration? = null

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
            //TODO iplm
            val identityId =
                "{web_push_player_id:cda154be-a37d-11ec-9d5f-52ceecedd8ea,email:example@gmail.com,phone:039889981}"
            val eventDate = System.currentTimeMillis()
            service()?.track(
                workspace_id = workspaceId,
                identity_id = identityId,
                event_name = eventName,
                event_date = eventDate.toString(),
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
    }
}
