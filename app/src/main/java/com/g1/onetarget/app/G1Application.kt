package com.g1.onetarget.app

import android.app.Application
import android.util.Log
import com.g1.onetarget.common.C
import com.g1.onetargetsdk.core.Analytics
import com.g1.onetargetsdk.core.Configuration
import com.g1.onetargetsdk.core.IAM

/**
 * Created by Loitp on 12.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */

class G1Application : Application() {

    override fun onCreate() {
        super.onCreate()

        C.setEnv(C.DEV)
//        C.setEnv(C.STAG)
//        C.setEnv(C.PROD)
        setupSDK()
    }

    private fun setupSDK() {
        val configuration = Configuration(this)
        if (C.isEnvDev()) {
            configuration.setEnvironmentDev()
        } else if (C.isEnvStag()) {
            configuration.setEnvironmentStag()
        } else if (C.isEnvProd()) {
            configuration.setEnvironmentProd()
        }
        configuration.writeKey = C.getWorkSpaceId()
        configuration.isShowLog = true
        configuration.isEnableIAM = true
        val resultSetupTracking = Analytics.setup(configuration)
        val resultSetupIAM = IAM.setup(configuration, this)
        Log.d(G1Application::class.java.simpleName, "resultSetupTracking $resultSetupTracking")
        Log.d(G1Application::class.java.simpleName, "resultSetupIAM $resultSetupIAM")
    }

}
