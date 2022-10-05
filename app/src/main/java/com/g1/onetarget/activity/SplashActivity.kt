package com.g1.onetarget.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.g1.onetarget.R
import com.g1.onetarget.app.G1Application
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
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
//        setupSDK()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        goToHome()
    }

    private fun setupSDK() {
        C.setEnv(C.DEV)

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
            IAM.showIAMDialog(activity = this, htmlContent = htmlContent, iamData = iamData)
        }
        configuration.onMsg = {
            Utils.logD(SplashActivity::class.java.simpleName, "configuration.onMsg $it")
        }
        val resultSetupTracking = Analytics.setup(configuration)
        val resultSetupIAM = IAM.setup(configuration, this)
        Log.d(G1Application::class.java.simpleName, "resultSetupTracking $resultSetupTracking")
        Log.d(G1Application::class.java.simpleName, "resultSetupIAM $resultSetupIAM")
    }

    private fun goToHome() {
        Handler(Looper.getMainLooper()).postDelayed(
            /* r = */ {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                this.finishAfterTransition()
            }, /* delayMillis = */ 1000
        )
    }

}
