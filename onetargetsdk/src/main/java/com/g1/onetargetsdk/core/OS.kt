package com.g1.onetargetsdk.core

import android.content.Context
import com.g1.onetargetsdk.common.Utils
import com.onesignal.OneSignal

/**
 * Created by Loitp on 25,October,2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class OS {

    companion object {
        fun setup(context: Context, appId: String) {
            // Enable verbose OneSignal logging to debug issues if needed.
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

            // OneSignal Initialization
            OneSignal.initWithContext(context)
            OneSignal.setAppId(appId)

            // promptForPushNotifications will show the native Android notification permission prompt.
            // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
            OneSignal.promptForPushNotifications()
        }

        fun getAppPushPlayerId(): String? {
            val userId = OneSignal.getDeviceState()?.userId
            Utils.logE("OS", ">>>getAppPushPlayerId userId $userId")
            return userId
        }
    }

}
