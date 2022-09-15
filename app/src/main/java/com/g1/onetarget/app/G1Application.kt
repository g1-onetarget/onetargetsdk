package com.g1.onetarget.app

import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
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
class G1Application : MultiDexApplication() {

    override fun attachBaseContext(c: Context?) {
        super.attachBaseContext(c)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        setupSDK()
    }

    private fun setupSDK() {
        val configuration = Configuration(this)
        configuration.setEnvironmentDev()
//        configuration.setEnvironmentProd()
        configuration.writeKey = C.workSpaceId
        configuration.isShowLog = true
        val resultSetupTracking = Analytics.setup(configuration)
        val resultSetupIAM = IAM.setup(configuration)
        Log.d(G1Application::class.java.simpleName, "resultSetupTracking $resultSetupTracking")
        Log.d(G1Application::class.java.simpleName, "resultSetupIAM $resultSetupIAM")
    }
}
