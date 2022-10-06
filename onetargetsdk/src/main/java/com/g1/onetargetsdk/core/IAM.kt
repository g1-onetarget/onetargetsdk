package com.g1.onetargetsdk.core

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.g1.onetargetsdk.R
import com.g1.onetargetsdk.common.Utils
import com.g1.onetargetsdk.db.LocalBroadcastUtil
import com.g1.onetargetsdk.model.*
import com.g1.onetargetsdk.services.OneTargetService
import com.g1.onetargetsdk.services.RetrofitClient
import com.g1.onetargetsdk.ui.ActivityIAM
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

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
        private var compositeDisposable = CompositeDisposable()
        private var configuration: Configuration? = null
        private var isAppInForeground: Boolean? = null
        private val listIAM = ArrayList<IAMData>()
        private var isActivityIAMRunning = false
        private var isWaitingIAMTime = false
        private var dialogIAM: Dialog? = null

        private const val TIME_OUT = 30L
        private const val TIME_INTERVAL = 1L
        private var countApiPolling = 0

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

        private fun onMsg(msg: String) {
            configuration?.let { cf ->
                cf.onMsg?.invoke(msg)
            }
        }

        fun clear() {
            compositeDisposable.clear()
        }

        private val observable: Observable<out Long>
            get() = Observable.interval(0, TIME_INTERVAL, TimeUnit.SECONDS)

        private fun getObserver(context: Context?): DisposableObserver<Long?> {
            return object : DisposableObserver<Long?>() {
                override fun onNext(value: Long) {
                    logD("\nDisposableObserver onNext : value : $value")
                    checkIAM(context = context)
                }

                override fun onError(e: Throwable) {
                    logD("\nDisposableObserver onError : ${e.message}")
                }

                override fun onComplete() {
                    logD("\nDisposableObserver onComplete")
                }
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
            onMsg("setup configuration deviceId: ${configuration.deviceId}")
            if (configuration.isEnableIAM) {

                fun doLongPolling(c: Context) {
//                    clear()
                    compositeDisposable.add(
                        observable
                            .subscribeOn(Schedulers.io()) // Be notified on the main thread
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(getObserver(c))
                    )
                }

                fun handleOnStart(c: Context, onFirstActivityInit: Boolean) {
                    if (isAppInForeground != true || onFirstActivityInit) {
                        logD(">>>onAppInForeground")
                        LocalBroadcastUtil.registerReceiver(c, mMessageReceiver)
                        isAppInForeground = true
                        doLongPolling(c)
                    }
                }

                fun handleOnStop(c: Context) {
                    if (isAppInForeground != false) {
                        logD(">>>onAppInBackground")
                        LocalBroadcastUtil.unregisterReceiver(c, mMessageReceiver)
                        isAppInForeground = false
//                        clear()
                        doLongPolling(c)
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
//                onMsg("BroadcastReceiver isActivityIAMRunning $isActivityIAMRunning")
                if (!isActivityIAMRunning) {
                    handleIAMData()
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
//                    checkIAM(context = context)
                    data?.let { dt ->
                        if (data.message.isNullOrEmpty()) {
                            //do nothing
                        } else {
                            onMsg(">>>response activeType ${dt.activeType}")
                            listIAM.add(dt)
                        }
                    }
                    handleIAMData()
                },
                onFailure = { t ->
//                    checkIAM(context = context)
                    t.printStackTrace()
                },
            )
        }

        private fun handleIAMData(
        ) {
            val firstIAMData = listIAM.firstOrNull()
            onMsg("handleIAMData listIAM.size ${listIAM.size}")
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
            return RetrofitClient.getClientIAM(baseUrl = baseUrl,
                isShowLogAPI = isShowLog,
                timeout = TIME_OUT,
                onMsg = { curl ->
                    onMsg(curl)
                }).create(OneTargetService::class.java)
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
                if (countApiPolling >= (configuration?.maxNumberApiPolling
                        ?: Configuration.DEFAULT_MAX_NUMBER_API_POLLING)
                ) {
                    return false
                }
                return true
            }

            val isValid = isValid()
            onMsg(">>>checkIAM isValid $isValid, isAppInForeground $isAppInForeground, countApi $countApiPolling")
            onMsg("queue size: ${listIAM.size}")
            if (!isValid) {
                onMsg("<<<isValid false => return")
                return
            }
            countApiPolling++
            val workSpaceId = configuration?.writeKey
            val identityId = configuration?.deviceId
            if (workSpaceId.isNullOrEmpty() || identityId.isNullOrEmpty()) {
                return
            }
//            onMsg(">>>checkIAM workSpaceId $workSpaceId, identityId $identityId")
//            if (configuration?.isShowLog == true) {
//                Toast.makeText(
//                    context, "checkIAM isAppInForeground: $isAppInForeground", Toast.LENGTH_SHORT
//                ).show()
//            }
            service()?.checkIAM(
                workspaceId = workSpaceId,
                identityId = identityId,
            )?.enqueue(object : Callback<IAMResponse> {
                override fun onResponse(
                    call: Call<IAMResponse>, response: Response<IAMResponse>
                ) {
                    countApiPolling--
                    onMsg("<<<checkIAM countApi $countApiPolling, onResponse ${response.body()}")
//                    onMsg("queue size: ${listIAM.size}")
                    if (isValid()) {
                        val jsonString = response.body()?.data
                        var iamData: IAMData? = null
                        jsonString?.let { s ->
                            iamData = Gson().fromJson(s, IAMData::class.java)
                        }
                        if (iamData?.message.isNullOrEmpty()) {
                            //do nothing
                        } else {
                            onMsg(
                                "activeType: ${iamData?.activeType}" + "\nactiveValue: ${iamData?.activeValue}" + "\nclosingAfter: ${iamData?.closingAfter}" + "\nname: ${iamData?.name}" + "\nqueue size: ${listIAM.size}"
                            )
                        }
                        onResponse?.invoke(
                            response.isSuccessful, response.code(), response.body(), iamData
                        )
                    }
                }

                override fun onFailure(call: Call<IAMResponse>, t: Throwable) {
                    countApiPolling--
                    onMsg("<<<checkIAM countApi $countApiPolling, onFailure $t")
                    onMsg("queue size: ${listIAM.size}")
                    if (isValid()) {
                        onFailure?.invoke(t)
                    }
                }
            })
        }

        fun showIAMActivity(
            context: Context, htmlContent: String, iamData: IAMData
        ) {
            isActivityIAMRunning = true
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
            activity: Activity, htmlContent: String, iamData: IAMData
        ) {
            dialogIAM = genDialogIAM(
                activity = activity, htmlContent = htmlContent, iamData = iamData
            )
            show(dialogIAM)
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

        @SuppressLint("InflateParams, SetTextI18n, SetJavaScriptEnabled")
        private fun genDialogIAM(
            activity: Activity,
            htmlContent: String,
            iamData: IAMData,
        ): Dialog {

            LocalBroadcastUtil.sendMessage(context = activity, isActivityIAMRunning = true)
            val dialog = Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.activity_iam)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)

            val layoutDebugView = dialog.findViewById<LinearLayoutCompat>(R.id.layoutDebugView)
            val tvDebug = dialog.findViewById<AppCompatTextView>(R.id.tvDebug)
//            val layoutRoot = dialog.findViewById<RelativeLayout>(R.id.layoutRoot)
//            val layoutBody = dialog.findViewById<RelativeLayout>(R.id.layoutBody)
            val wv = dialog.findViewById<WebView>(R.id.wv)
            val pb = dialog.findViewById<ProgressBar>(R.id.pb)
//            val btCloseOutside = dialog.findViewById<AppCompatImageButton>(R.id.btCloseOutside)
//            val btCloseInside = dialog.findViewById<AppCompatImageButton>(R.id.btCloseInside)

            fun setupDebugView() {
                tvDebug?.text =
                    "activeType: ${iamData.activeType}" + "\nactiveValue: ${iamData.activeValue}" + "\nclosingAfter: ${iamData.closingAfter}" + "\nname: ${iamData.name}" + "\nqueue size: ${listIAM.size}"
            }

            fun onClickClose() {
                dialog.dismiss()
            }

            fun onClickBody(uri: Uri) {
                iamData.actionClick?.let { actionClick ->
                    val link = "$actionClick$uri"
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(link)
                    activity.startActivity(i)
                    activity.finishAfterTransition()
                }
            }

            fun setupViews() {
                if (configuration?.isShowLog == true) {
                    layoutDebugView?.visibility = View.VISIBLE
                } else {
                    layoutDebugView?.visibility = View.GONE
                }

//                layoutRoot?.setOnClickListener {
//                    if (isEnableTouchOutside) {
//                        finish()
//                    }
//                }
//                layoutBody?.setOnClickListener {
//                    if (isEnableTouchOutside) {
//                        finish()
//                    }
//                }
//                btCloseOutside?.setOnClickListener {
//                    finish()
//                }
//                btCloseInside?.setOnClickListener {
//                    finish()
//                }

                wv?.let { v ->
//                    if (configuration?.isShowLog == true) {
//                        v.setBackgroundColor(Color.YELLOW)
//                    } else {
//                        v.setBackgroundColor(Color.TRANSPARENT)
//                    }

                    v.setBackgroundColor(Color.TRANSPARENT)
                    v.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)
                    v.settings.javaScriptEnabled = true
                    v.settings.loadWithOverviewMode = true
                    v.settings.useWideViewPort = true

                    v.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView, url: String) {
                            v.visibility = View.VISIBLE
                            pb.visibility = View.GONE
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?, request: WebResourceRequest?
                        ): Boolean {
                            logD(">>>>shouldOverrideUrlLoading ${request?.url}")
                            request?.url?.let { u ->
                                if (Utils.isExistWebView(u)) {
                                    onClickClose()
                                } else {
                                    onClickBody(u)
                                }
                                return true
                            }
                            return false
                        }
                    }

                    v.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                }
            }

            fun configAutoCloseDialog() {
                logD(">>>closingAfter: ${iamData.closingAfter}")
                iamData.closingAfter?.let { closingAfter ->
                    if (closingAfter > 0) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            dialog.dismiss()
                        }, (closingAfter * 1000).toLong())
                    }
                }
            }

            setupViews()
            configAutoCloseDialog()
            setupDebugView()

            dialog.setOnDismissListener {
                LocalBroadcastUtil.sendMessage(context = activity, isActivityIAMRunning = false)
            }

            dialog.window?.let {
                it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                it.statusBarColor = activity.window.statusBarColor
                it.navigationBarColor = activity.window.navigationBarColor

                if (configuration?.isShowLog == true) {
                    it.setBackgroundDrawable(
                        ColorDrawable(
                            Utils.getColor(
                                activity, R.color.red62
                            )
                        )
                    )
                } else {
                    it.setBackgroundDrawable(
                        ColorDrawable(
                            Utils.getColor(
                                activity, R.color.transparent
                            )
                        )
                    )
                }

                val wlp = it.attributes
                wlp.gravity = Gravity.CENTER
                wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()

                it.attributes = wlp
                it.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
                )
            }
            return dialog
        }
    }
}
