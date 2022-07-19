package com.g1.onetargetsdk

class ApiUtils {
    companion object {
        private const val baseURL = "https://api.stackexchange.com/2.2/"

        @JvmStatic
        val sOService: TrackingService
            get() = RetrofitClient.getClient(baseURL).create(TrackingService::class.java)
    }
}
