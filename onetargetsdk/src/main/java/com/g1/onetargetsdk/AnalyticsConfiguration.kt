package com.g1.onetargetsdk

class AnalyticsConfiguration {
    companion object {
        //TODO correct BASE_URL_STG, BASE_URL_PROD
        const val BASE_URL_DEV = "https://dev-pixel.cdp.link/"
        const val BASE_URL_STG = "https://dev-pixel.cdp.link/"
        const val BASE_URL_PROD = "https://dev-pixel.cdp.link/"
    }

    var writeKey: String? = null
    private var baseUrl: String = BASE_URL_DEV//default dev environment

    fun setEnvironmentDev() {
        this.baseUrl = BASE_URL_DEV
    }

    fun setEnvironmentStag() {
        this.baseUrl = BASE_URL_STG
    }

    fun setEnvironmentProd() {
        this.baseUrl = BASE_URL_PROD
    }

    fun getBaseUrl(): String {
        return baseUrl
    }
}
