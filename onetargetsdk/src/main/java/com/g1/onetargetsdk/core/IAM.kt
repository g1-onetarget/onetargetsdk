package com.g1.onetargetsdk.core

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.g1.onetargetsdk.BuildConfig
import com.g1.onetargetsdk.R
import com.g1.onetargetsdk.common.Utils
import com.g1.onetargetsdk.db.LocalBroadcastUtil
import com.g1.onetargetsdk.model.*
import com.g1.onetargetsdk.services.OneTargetService
import com.g1.onetargetsdk.services.RetrofitClient
import com.g1.onetargetsdk.ui.ActivityIAM
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Loitp on 13.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class IAM {

    companion object {
        private val logTag = "g1mobile${IAM::class.java.simpleName}"
        private var configuration: Configuration? = null
        private var isAppInForeground: Boolean? = null
        private val listIAM = ArrayList<IAMData>()
        private var isActivityIAMRunning = false
        private var isWaitingIAMTime = false

        private fun logD(msg: String) {
            if (configuration?.isShowLog == true) {
                Log.d(logTag, msg)
            }
        }

        private fun logE(msg: String) {
            if (configuration?.isShowLog == true) {
                Log.e(logTag, msg)
            }
        }

        fun setup(configuration: Configuration, context: Context?): Boolean {
            if (configuration.writeKey.isNullOrEmpty()) {
                logE("writeKey cannot be null or empty")
                return false
            }
            if (configuration.getBaseUrlIAM().isEmpty()) {
                logE("base url cannot be null or empty")
                return false
            }

            Companion.configuration = configuration
            if (configuration.isEnableIAM) {

                fun handleOnStart(c: Context, onFirstActivityInit: Boolean) {
                    if (isAppInForeground != true || onFirstActivityInit) {
                        logD(">>>onAppInForeground")
                        LocalBroadcastUtil.registerReceiver(c, mMessageReceiver)
                        isAppInForeground = true
                        checkIAM(context = c)
                    }
                }

                fun handleOnStop(c: Context) {
                    if (isAppInForeground != false) {
                        logD(">>>onAppInBackground")
                        LocalBroadcastUtil.unregisterReceiver(c, mMessageReceiver)
                        isAppInForeground = false
                        checkIAM(context = c)
                    }
                }

                ProcessLifecycleOwner.get().lifecycle.addObserver(LifecycleEventObserver { _, event ->
                    context?.let { c ->
                        when (event) {
                            Lifecycle.Event.ON_START -> {
//                                logD(">>>Lifecycle.Event.ON_START")
                                handleOnStart(c, false)
                            }
                            Lifecycle.Event.ON_STOP -> {
//                                logD(">>>Lifecycle.Event.ON_STOP")
                                handleOnStop(c)
                            }
                            else -> {}
                        }
                    }
                })
            }
            return true
        }

        private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val result = intent.getBooleanExtra(LocalBroadcastUtil.KEY_DATA, false)
                isActivityIAMRunning = result
//                logD("BroadcastReceiver isActivityIAMRunning $isActivityIAMRunning")
                if (!isActivityIAMRunning) {
                    handleIAMData(context)
                }
            }
        }

        private fun checkIAM(context: Context?) {
            if (context == null) {
                logE(">>>checkIAM context == null => return")
                return
            }
            checkIAM(
                context,
                onResponse = { isSuccessful, code, response, data ->
                    logD("isSuccessful $isSuccessful")
                    logD("code $code")
                    logD("response $response")

                    data?.let { dt ->
                        if (data.message.isNullOrEmpty()) {
                            //do nothing
                        } else {
//                            logE(">>>response activeType ${dt.activeType}")
                            listIAM.add(dt)
                        }
                    }
                    handleIAMData(context)
                    checkIAM(context = context)
                },
                onFailure = { t ->
                    t.printStackTrace()
                    checkIAM(context = context)
                },
            )
        }

        private fun handleIAMData(
            context: Context?,
        ) {
            val firstIAMData = listIAM.firstOrNull()
            logD("handleIAMData listIAM.size ${listIAM.size}")
            firstIAMData?.let { iamData ->
                getHtmlContent(iamData)?.let { htmlContent ->
                    if (isAppInForeground == true) {
                        logD("handleIAMData dt.activeType ${iamData.activeType}")
                        logD("isActivityIAMRunning $isActivityIAMRunning")
                        if (isActivityIAMRunning) {
                            logE("popupAIMIsShowing true -> return")
                        } else {
                            fun handleActiveTypeImmediately() {
                                if (isActivityIAMRunning || isWaitingIAMTime) {
                                    logE("handleActiveTypeImmediately isActivityIAMRunning || isWaitingIAMTime -> return")
                                    return
                                }
                                configuration?.onShowIAM?.invoke(htmlContent, iamData)
                            }

                            fun handleActiveTypeTime() {
                                iamData.activeValue?.let { activeValue ->
                                    logE(">>>handleActiveTypeTime activeValue $activeValue")
                                    isWaitingIAMTime = true
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        isWaitingIAMTime = false
                                        handleActiveTypeImmediately()
                                    }, (activeValue * 1000).toLong())
                                }
                            }

                            when (iamData.activeType) {
                                IMMEDIATELY -> {
                                    handleActiveTypeImmediately()
                                }
                                TIME -> {
                                    handleActiveTypeTime()
                                }
                                SCROLL_PERCENTAGE -> {
                                    // do nothing, out of sdk's scope
                                }
                                else -> {
                                    //do nothing}
                                }
                            }
                        }
                    }
                }
            }
        }

        private fun getHtmlContent(data: IAMData?): String? {
            val htmlContent = data?.getJsonContent()?.htmlContent
            logE(">>>getHtmlContent $htmlContent")
            return htmlContent
        }

        @JvmStatic
        private fun service(): OneTargetService? {
            if (configuration == null) {
                logE("configuration not found")
                return null
            }
            val baseUrl = configuration?.getBaseUrlIAM()
            if (baseUrl.isNullOrEmpty()) {
                logE("base url cannot be null or empty")
                return null
            }
            val isShowLog = configuration?.isShowLog
            return RetrofitClient.getClientIAM(
                baseUrl = baseUrl,
                isShowLogAPI = isShowLog,
            ).create(OneTargetService::class.java)
        }

        private fun checkIAM(
            context: Context?,
            onResponse: ((isSuccessful: Boolean, code: Int, response: IAMResponse?, data: IAMData?) -> Unit)? = null,
            onFailure: ((Throwable) -> Unit)? = null,
        ) {

            fun isValid(): Boolean {
                if (context == null) {
                    return false
                }
                if (isAppInForeground == null || isAppInForeground == false) {
                    return false
                }
                return true
            }

            val isValid = isValid()
//            logD(">>>>>>>checkIAM isValid $isValid, isAppInForeground $isAppInForeground")
            if (!isValid) {
                return
            }
            val workSpaceId = configuration?.writeKey
            val identityId = configuration?.deviceId
            if (workSpaceId.isNullOrEmpty() || identityId.isNullOrEmpty()) {
                return
            }
//            logD(">>>>>>>checkIAM workSpaceId $workSpaceId, identityId $identityId")
            if (configuration?.isShowLog == true && BuildConfig.DEBUG) {
                Toast.makeText(
                    context,
                    "checkIAM isAppInForeground: $isAppInForeground",
                    Toast.LENGTH_LONG
                ).show()
            }
            service()?.checkIAM(
                workspaceId = workSpaceId,
                identityId = identityId,
            )?.enqueue(object : Callback<IAMResponse> {
                override fun onResponse(
                    call: Call<IAMResponse>, response: Response<IAMResponse>
                ) {
                    if (isValid()) {
                        val jsonString = response.body()?.data
                        var iamData: IAMData? = null
                        jsonString?.let { s ->
                            iamData = Gson().fromJson(s, IAMData::class.java)
                        }
                        onResponse?.invoke(
                            response.isSuccessful, response.code(), response.body(), iamData
                        )
                    }
                }

                override fun onFailure(call: Call<IAMResponse>, t: Throwable) {
                    if (isValid()) {
                        onFailure?.invoke(t)
                    }
                }
            })
        }

        fun showIAMActivity(
            context: Context,
            htmlContent: String,
            iamData: IAMData
        ) {
            val intent = Intent(context, ActivityIAM::class.java)
            intent.putExtra(ActivityIAM.KEY_IAM_DATA, iamData)
            intent.putExtra(
                ActivityIAM.KEY_HTML_CONTENT, htmlContent
            )
            intent.putExtra(ActivityIAM.KEY_ENABLE_TOUCH_OUTSIDE, false)
            intent.putExtra(
                ActivityIAM.KEY_IS_SHOW_LOG, configuration?.isShowLog
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            if (listIAM.isNotEmpty()) {
                listIAM.removeFirst()
            }
        }

        fun showIAMDialog(
            activity: Activity,
            htmlContent: String,
            iamData: IAMData
        ) {
            val alertDialogProgress = genDialogIAM(activity = activity)
            show(alertDialogProgress)
            if (listIAM.isNotEmpty()) {
                listIAM.removeFirst()
            }
        }

        private fun show(dialog: Dialog?) {
            if (dialog != null && !dialog.isShowing) {
                dialog.show()
            }
        }

        private fun hide(dialog: Dialog?) {
            if (dialog != null && dialog.isShowing) {
                dialog.cancel()
            }
        }

        @SuppressLint("InflateParams")
        private fun genDialogIAM(
            activity: Activity
        ): Dialog {
            val dialog = Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_iam)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)

            val progressBar = dialog.findViewById<ProgressBar>(R.id.progressBar)

            dialog.window?.let {
                it.setBackgroundDrawable(ColorDrawable(Utils.getColor(activity, R.color.red30)))

                val wlp = it.attributes
                wlp.gravity = Gravity.CENTER
                wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()

                it.attributes = wlp
                it.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
                )
            }
            return dialog
        }
    }
}
