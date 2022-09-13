package com.g1.onetarget.app

import android.app.Application
import android.util.Log
import com.g1.onetarget.common.C
import com.g1.onetargetsdk.Analytics
import com.g1.onetargetsdk.Configuration
import com.g1.onetargetsdk.IAM

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

        setupSDK()
    }

    private fun setupSDK() {
        val configuration = Configuration()
        configuration.setEnvironmentDev()
//        configuration.setEnvironmentProd()
        configuration.writeKey = C.workSpaceIdForTracking
        configuration.isShowLog = true
        val resultSetupTracking = Analytics.setup(configuration)
        val resultSetupIAM = IAM.setup(configuration)
        Log.d(G1Application::class.java.simpleName, "resultSetupTracking $resultSetupTracking")
        Log.d(G1Application::class.java.simpleName, "resultSetupIAM $resultSetupIAM")
    }
}
