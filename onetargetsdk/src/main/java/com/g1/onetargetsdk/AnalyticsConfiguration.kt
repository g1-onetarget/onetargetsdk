package com.g1.onetargetsdk

class AnalyticsConfiguration {
    companion object {
        const val BASE_URL_DEV = "https://dev-pixel.cdp.link/"
        const val BASE_URL_PROD = "https://pixel.cdp.link/"
    }

    var writeKey: String? = null
    var email: String? = null
    var phone: String? = null
    var deviceId: String? = null
    private var baseUrl: String = BASE_URL_DEV//default dev environment

    fun setEnvironmentDev() {
        this.baseUrl = BASE_URL_DEV
    }

    fun setEnvironmentProd() {
        this.baseUrl = BASE_URL_PROD
    }

    fun getBaseUrl(): String {
        return baseUrl
    }
}
