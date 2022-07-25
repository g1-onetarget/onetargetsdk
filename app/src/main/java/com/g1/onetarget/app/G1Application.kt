package com.g1.onetarget.app

import android.app.Application
import android.util.Log
import com.g1.onetargetsdk.Analytics
import com.g1.onetargetsdk.AnalyticsConfiguration

class G1Application : Application() {

    override fun onCreate() {
        super.onCreate()

        val configuration = AnalyticsConfiguration()
        configuration.setEnvironmentDev()
//        configuration.setEnvironmentProd()
        configuration.writeKey = "ab44219f-dc9e-4080-943c-a127bd071da3"
        configuration.email = "example@gmail.com"
        configuration.phone = "039889981"
        configuration.deviceId = Analytics.getDeviceId(this)

        val result = Analytics.setup(configuration)
        Log.d(G1Application::class.java.simpleName, "setup result $result")
    }
}
