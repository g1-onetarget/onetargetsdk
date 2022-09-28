package com.g1.onetargetsdk.db

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager

/**
 * Created by Loitp on 27,September,2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */

object LocalBroadcastUtil {
    private const val ACTION = "IAM_RUNNING"
    const val KEY_DATA = "KEY_DATA"

    fun sendMessage(context: Context, isActivityIAMRunning: Boolean) {
        val intent = Intent(ACTION)
        intent.putExtra(KEY_DATA, isActivityIAMRunning)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    fun registerReceiver(context: Context, broadcastReceiver: BroadcastReceiver) {
        LocalBroadcastManager.getInstance(context).registerReceiver(
            broadcastReceiver,
            IntentFilter(ACTION)
        )
    }

    fun unregisterReceiver(context: Context, broadcastReceiver: BroadcastReceiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver)
    }
}
