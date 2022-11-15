package com.g1.onetarget.common

/**
 * Created by Loitp on 13,September,2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
object C {
    const val DEV = 1
    const val STAG = 2
    const val PROD = 3

    private const val workSpaceIdDev = "490bf1f1-2e88-4d6d-8ec4-2bb7de74f9a8"
    private const val workSpaceIdStag = "60378690-27a8-430d-b491-34d858a93485"
    private const val workSpaceIdProd = "4b970d32-261f-4dff-abf7-4f4ae76c2fb5"

    private const val ONESIGNAL_APP_ID_DEV = "d355f0df-6d85-4258-a871-82aaa4031b53"
    private const val ONESIGNAL_APP_ID_STAG = "4543aa42-4d7c-4666-a68c-077dfd79d752"
    private const val ONESIGNAL_APP_ID_PROD = "89fda2ae-7af9-4887-89a7-9b73656a2865"

    private var env = DEV

    fun setEnv(env: Int) {
        if (env != DEV && env != STAG && env != PROD) {
            return
        }
        this.env = env
    }

    fun isEnvDev(): Boolean {
        return this.env == DEV
    }

    fun isEnvStag(): Boolean {
        return this.env == STAG
    }

    fun isEnvProd(): Boolean {
        return this.env == PROD
    }

    fun getWorkSpaceId(): String {
        return when (env) {
            DEV -> {
                workSpaceIdDev
            }
            STAG -> {
                workSpaceIdStag
            }
            else -> {
                workSpaceIdProd
            }
        }
    }

    fun getOneSignalAppId(): String {
        return when (env) {
            DEV -> {
                ONESIGNAL_APP_ID_DEV
            }
            STAG -> {
                ONESIGNAL_APP_ID_STAG
            }
            else -> {
                ONESIGNAL_APP_ID_PROD
            }
        }
    }
}
