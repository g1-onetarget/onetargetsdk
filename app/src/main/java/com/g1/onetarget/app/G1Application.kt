package com.g1.onetarget.app

import android.app.Application
import android.util.Log
import com.g1.onetargetsdk.Analytics
import com.g1.onetargetsdk.Configuration

class G1Application : Application() {

    override fun onCreate() {
        super.onCreate()

        setupTracking()
    }

    private fun setupTracking() {
        val configuration = Configuration()
        configuration.setEnvironmentDev()
//        configuration.setEnvironmentProd()
        configuration.writeKey = "490bf1f1-2e88-4d6d-8ec4-2bb7de74f9a8"
        val result = Analytics.setup(configuration)
        Log.d(G1Application::class.java.simpleName, "setup result $result")
    }
}
