package com.g1.onetargetsdk

class Analytics {
    companion object {
        private const val baseURL = "https://dev-pixel.cdp.link/"

        @JvmStatic
        val service: TrackingService
            get() = RetrofitClient.getClient(baseURL).create(TrackingService::class.java)
    }
}
