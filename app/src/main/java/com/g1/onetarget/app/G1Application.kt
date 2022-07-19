package com.g1.onetarget.app

import android.app.Application
import com.g1.onetargetsdk.Analytics
import com.g1.onetargetsdk.AnalyticsConfiguration

class G1Application : Application() {

    override fun onCreate() {
        super.onCreate()

        val configuration = AnalyticsConfiguration()
        configuration.writeKey = "ab44219f-dc9e-4080-943c-a127bd071da3"

        Analytics.setup(configuration)
    }
}
