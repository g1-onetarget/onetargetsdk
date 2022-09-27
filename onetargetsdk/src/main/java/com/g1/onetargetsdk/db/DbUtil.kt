package com.g1.onetargetsdk.db

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

/**
 * Created by Loitp on 27,September,2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */

object DbUtil {
    private val PREFS_NAME = DbUtil::class.java.simpleName
    const val KEY_POPUP_IAM_IS_SHOWING = "KEY_POPUP_IAM_IS_SHOWING"

    private fun getPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
    }

    fun putBoolean(context: Context, key: String, value: Boolean) {
        getPref(context).edit().apply {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBoolean(context: Context, key: String, dfValue: Boolean): Boolean {
        return getPref(context).getBoolean(key, dfValue)
    }
}
