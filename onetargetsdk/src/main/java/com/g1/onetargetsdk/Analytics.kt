package com.g1.onetargetsdk

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Analytics {
    companion object {
        private const val baseURL = "https://dev-pixel.cdp.link/"
        private var analyticsConfiguration: AnalyticsConfiguration? = null

        fun setup(analyticsConfiguration: AnalyticsConfiguration) {
            if (analyticsConfiguration.writeKey.isNullOrEmpty()) {
                throw IllegalArgumentException("writeKey cannot be null or empty")
            }
            this.analyticsConfiguration = analyticsConfiguration
        }

        @JvmStatic
        private val service: TrackingService
            get() = RetrofitClient.getClient(baseURL).create(TrackingService::class.java)

        @JvmStatic
        fun track(
            eventName: String?,
            eventData: String?,
            onResponse: ((Response<Void>) -> Unit)? = null,
            onFailure: ((Throwable) -> Unit)? = null,
        ) {
            if (eventName.isNullOrEmpty()) {
                return
            }
            if (eventData.isNullOrEmpty()) {
                return
            }
            val workspaceId = this.analyticsConfiguration?.writeKey
            if (workspaceId.isNullOrEmpty()) {
                return
            }
            val identityId =
                "{web_push_player_id:cda154be-a37d-11ec-9d5f-52ceecedd8ea,email:example@gmail.com,phone:039889981}"
            val eventDate = System.currentTimeMillis()
            service.track(
                workspace_id = workspaceId,
                identity_id = identityId,
                event_name = eventName,
                event_date = eventDate.toString(),
                eventData = eventData,
            ).enqueue(object : Callback<Void> {
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
