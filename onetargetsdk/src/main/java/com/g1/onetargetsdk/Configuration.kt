package com.g1.onetargetsdk

/**
 * Created by Loitp on 12.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class Configuration {
    companion object {
        const val BASE_URL_TRACKING_DEV = "https://dev-pixel.cdp.link/"
        const val BASE_URL_TRACKING_PROD = "https://pixel.cdp.link/"

        const val BASE_URL_IAM_DEV = "https://api-dev.predict.marketing/"

        //TODO
        const val BASE_URL_IAM_PROD = ""
    }

    var writeKey: String? = null
    private var baseUrlTracking: String = BASE_URL_TRACKING_DEV//default dev environment
    private var baseUrlIAM: String = BASE_URL_IAM_DEV//default dev environment
    var isShowLog = false

    fun setEnvironmentDev() {
        this.baseUrlTracking = BASE_URL_TRACKING_DEV
        this.baseUrlIAM = BASE_URL_IAM_DEV
    }

    fun setEnvironmentProd() {
        this.baseUrlTracking = BASE_URL_TRACKING_PROD
        this.baseUrlIAM = BASE_URL_IAM_PROD
    }

    fun getBaseUrlTracking(): String {
        return baseUrlTracking
    }

    fun getBaseUrlIAM(): String {
        return baseUrlIAM
    }
}
