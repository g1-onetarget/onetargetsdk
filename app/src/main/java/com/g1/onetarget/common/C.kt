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
    private const val workSpaceIdStag = "4d963ee6-9ccd-4cf0-b89f-b1730e1ff0e1"
    private const val workSpaceIdProd = ""//TODO fill

//    const val ONESIGNAL_APP_ID = "5ec0f33d-624b-41de-9ab4-c65b0218dc02"
    const val ONESIGNAL_APP_ID = "d355f0df-6d85-4258-a871-82aaa4031b53"

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
}
