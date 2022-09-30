package com.g1.onetargetsdk.core

import android.content.Context
import com.g1.onetargetsdk.common.Utils

/**
 * Created by Loitp on 12.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class Configuration(context: Context) {
    companion object {
        //https://app-dev.predict.marketing/login
        const val BASE_URL_TRACKING_DEV = "https://dev-pixel.cdp.link/"

        //https://app-stag.onetarget.vn/
        const val BASE_URL_TRACKING_STAG = "https://pixel-stag.onetarget.vn/"
        const val BASE_URL_TRACKING_PROD = "https://pixel.cdp.link/"

        const val BASE_URL_IAM_DEV = "https://api-dev.predict.marketing/"
        const val BASE_URL_IAM_STAG = "https://api-stag.onetarget.vn/"
        const val BASE_URL_IAM_PROD = "https://api.onetarget.vn/"
    }

    var writeKey: String? = null
    private var baseUrlTracking: String = BASE_URL_TRACKING_PROD//default dev environment
    private var baseUrlIAM: String = BASE_URL_IAM_PROD//default dev environment
    var isShowLog = false
    var isEnableIAM = true
    var deviceId: String? = null

    init {
        deviceId = Utils.getDeviceId(context)
    }

    fun setEnvironmentDev() {
        this.baseUrlTracking = BASE_URL_TRACKING_DEV
        this.baseUrlIAM = BASE_URL_IAM_DEV
    }

    fun setEnvironmentStag() {
        this.baseUrlTracking = BASE_URL_TRACKING_STAG
        this.baseUrlIAM = BASE_URL_IAM_STAG
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
