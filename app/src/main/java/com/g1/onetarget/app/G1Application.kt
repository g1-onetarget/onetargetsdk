package com.g1.onetarget.app

import android.app.Application
import com.g1.onetarget.common.C
import com.g1.onetargetsdk.common.Utils
import com.g1.onetargetsdk.core.Analytics
import com.g1.onetargetsdk.core.Configuration
import com.g1.onetargetsdk.core.IAM
import com.g1.onetargetsdk.model.IAMData

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
        C.setEnv(C.DEV)
//        C.setEnv(C.STAG)
//        C.setEnv(C.PROD)

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
        configuration.onShowIAM = { htmlContent: String, iamData: IAMData ->
            IAM.showIAMActivity(context = this, htmlContent = htmlContent, iamData = iamData)
        }
        configuration.onMsg = {
            logD("configuration.onMsg $it")
        }
        val resultSetup = Analytics.setup(configuration = configuration, context = this)
        logD("resultSetup $resultSetup")
    }

    private fun logD(msg: String) {
        Utils.logD(G1Application::class.java.simpleName, "g1mobile: $msg")
    }
}
