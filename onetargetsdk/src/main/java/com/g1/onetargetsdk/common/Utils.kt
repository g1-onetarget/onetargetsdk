package com.g1.onetargetsdk.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import java.util.*

/**
 * Created by Loitp on 15,September,2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
object Utils {

    private val logTag = Utils::class.java.simpleName

    fun logD(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    fun logE(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context?): String? {
        if (context == null) {
            return null
        }
        val androidId = Settings.Secure.getString(
            context.contentResolver, Settings.Secure.ANDROID_ID
        )
        logD(logTag, "androidId $androidId")
        if (androidId.isNotEmpty()) {
            return androidId
        }

        val uniquePseudoID =
            "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10
        logD(logTag, "uniquePseudoID $uniquePseudoID")
        val serial = Build.getRadioVersion()
        logD(logTag, "serial $serial")
        val uuid: String =
            UUID(uniquePseudoID.hashCode().toLong(), serial.hashCode().toLong()).toString()
        logD(logTag, "uuid $uuid")
        return uuid
    }

    val screenWidth: Int
        get() = Resources.getSystem().displayMetrics.widthPixels

    val screenHeight: Int
        get() = Resources.getSystem().displayMetrics.heightPixels

    fun setVisibilityButton(bt: AppCompatImageButton?, visibility: Int) {
        bt?.apply {
            post {
                this.visibility = visibility
            }
        }
    }

    fun isExitWebView(uri: Uri): Boolean {
        return uri.toString() == "onetarget://exit"
    }

    fun getDrawable(context: Context, @DrawableRes drawableRes: Int): Drawable? {
        return ContextCompat.getDrawable(context, drawableRes)
    }

    fun getColor(context: Context, @ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(context, colorRes)
    }
}
