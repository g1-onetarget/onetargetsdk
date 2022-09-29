package com.g1.onetargetsdk.common

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

/**
 * Created by Loitp on 29,September,2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class G1ActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    var currentActivity: Activity? = null

    private fun logD(msg: String) {
        Log.d(G1ActivityLifecycleCallbacks::class.java.simpleName, "g1mobile$msg")
    }

    override fun onActivityPaused(activity: Activity) {
        currentActivity = activity
        logD("onActivityPaused ${activity::class.java.simpleName}")
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
        logD("onActivityResumed ${activity::class.java.simpleName}")
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = activity
        logD("onActivityCreated ${activity::class.java.simpleName}")
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
        logD("onActivityStarted ${activity::class.java.simpleName}")
    }

    override fun onActivityDestroyed(activity: Activity) {
        logD("onActivityDestroyed ${activity::class.java.simpleName}")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        logD("onActivitySaveInstanceState ${activity::class.java.simpleName}")
    }

    override fun onActivityStopped(activity: Activity) {
        logD("onActivityStopped ${activity::class.java.simpleName}")
    }
}
