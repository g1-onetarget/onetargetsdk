package com.g1.onetargetsdk

class Configuration {
    companion object {
        const val BASE_URL_DEV = "https://dev-pixel.cdp.link/"
        const val BASE_URL_PROD = "https://pixel.cdp.link/"
    }

    var writeKey: String? = null
    private var baseUrl: String = BASE_URL_DEV//default dev environment
    var isShowLog = false

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
