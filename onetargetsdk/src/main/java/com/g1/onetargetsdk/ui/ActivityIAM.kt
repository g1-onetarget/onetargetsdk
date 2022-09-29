package com.g1.onetargetsdk.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.g1.onetargetsdk.R
import com.g1.onetargetsdk.Utils
import com.g1.onetargetsdk.db.LocalBroadcastUtil
import com.g1.onetargetsdk.ext.getSerializable
import com.g1.onetargetsdk.model.IAMData

/**
 * Created by Loitp on 12.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */

class ActivityIAM : AppCompatActivity() {
    companion object {
        const val KEY_IAM_DATA = "KEY_IAM_DATA"
        const val KEY_HTML_CONTENT = "KEY_HTML_CONTENT"

        //        const val KEY_SCREEN_WIDTH = "KEY_SCREEN_WIDTH"
//        const val KEY_SCREEN_HEIGHT = "KEY_SCREEN_HEIGHT"
        const val KEY_ENABLE_TOUCH_OUTSIDE = "KEY_ENABLE_TOUCH_OUTSIDE"
        const val KEY_IS_SHOW_LOG = "KEY_IS_SHOW_LOG"
    }

    private val logTag = "loitpp${ActivityIAM::class.java.simpleName}"
    private fun logD(s: String) {
        if (isShowLog) {
            Utils.logD(logTag, s)
        }
    }

    private var iamData: IAMData? = null
    private var htmlContent: String = ""

    private var isEnableTouchOutside = true
    private var isShowLog = false

    private var layoutDebugView: LinearLayoutCompat? = null
    private var tvDebug: AppCompatTextView? = null
    private var layoutRoot: RelativeLayout? = null
    private var layoutBody: RelativeLayout? = null
    private var wv: WebView? = null
    private var btCloseOutside: AppCompatImageButton? = null
    private var btCloseInside: AppCompatImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocalBroadcastUtil.sendMessage(context = this, isActivityIAMRunning = true)
        setupData()
        setTheme(R.style.AppTheme_DialogTheme)

        setContentView(R.layout.activity_iam)
        setupViews()
        configAutoCloseDialog()
        setupDebugView()
    }

    override fun onDestroy() {
        LocalBroadcastUtil.sendMessage(context = this, isActivityIAMRunning = false)
        super.onDestroy()
    }

    @SuppressLint("SetTextI18n")
    private fun setupDebugView() {
        tvDebug?.text =
            "activeType: ${iamData?.activeType}" + "\nactiveValue: ${iamData?.activeValue}" + "\nclosingAfter: ${iamData?.closingAfter}" + "\nname: ${iamData?.name}"

    }

    private fun setupData() {
        intent?.apply {
            getSerializable(
                KEY_IAM_DATA,
                IAMData::class.java,
            ).let { iamData ->
                this@ActivityIAM.iamData = iamData
            }
            getStringExtra(KEY_HTML_CONTENT)?.let { htmlContent ->
                this@ActivityIAM.htmlContent = htmlContent
            }
            getBooleanExtra(KEY_ENABLE_TOUCH_OUTSIDE, true).let { isEnableTouchOutside ->
                this@ActivityIAM.isEnableTouchOutside = isEnableTouchOutside
            }
            getBooleanExtra(KEY_IS_SHOW_LOG, true).let { isShowLog ->
                this@ActivityIAM.isShowLog = isShowLog
            }
        }
        logD("~~~~~~~~~~~~~~setupData")
        logD(">>>iamData: $iamData")
//        logD(">>>htmlContent: $htmlContent")
//        logD(">>>screenWidth: $screenWidth")
//        logD(">>>screenHeight: $screenHeight")
//        logD(">>>isEnableTouchOutside: $isEnableTouchOutside")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupViews() {
        layoutDebugView = findViewById(R.id.layoutDebugView)
        tvDebug = findViewById(R.id.tvDebug)
        layoutRoot = findViewById(R.id.layoutRoot)
        layoutBody = findViewById(R.id.layoutBody)
        wv = findViewById(R.id.wv)
        btCloseOutside = findViewById(R.id.btCloseOutside)
        btCloseInside = findViewById(R.id.btCloseInside)

        if (isShowLog) {
            layoutDebugView?.visibility = View.VISIBLE
        } else {
            layoutDebugView?.visibility = View.GONE
        }

        layoutRoot?.setOnClickListener {
            if (isEnableTouchOutside) {
                finish()
            }
        }
        layoutBody?.setOnClickListener {
            if (isEnableTouchOutside) {
                finish()
            }
        }
        btCloseOutside?.setOnClickListener {
            finish()
        }
        btCloseInside?.setOnClickListener {
            finish()
        }

        wv?.let { v ->
//            if (isShowLog) {
//                v.setBackgroundColor(Utils.getColor(this, R.color.red30))
//            } else {
//                v.setBackgroundColor(Color.TRANSPARENT)
//            }

            v.setBackgroundColor(Color.TRANSPARENT)
            v.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)
            v.settings.javaScriptEnabled = true
            v.settings.loadWithOverviewMode = true
            v.settings.useWideViewPort = true

            //listener get height of web view
//            v.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
//                val w = v.width
//                val h = v.height
//                if (h <= 0) {
//                    return@addOnLayoutChangeListener
//                }
//                val screenHeight = Utils.screenHeight
//                val ratio = h * 100 / screenHeight
//                logD("addOnLayoutChangeListener w: $w, h: $h, screenHeight: $screenHeight, ratio: $ratio")
//                if (ratio >= 80) {
//                    logD("ad full")
//                    setVisibilityButton(btCloseOutside, View.GONE)
//                    setVisibilityButton(btCloseInside, View.VISIBLE)
//                } else {
//                    logD("ad center")
//                    setVisibilityButton(btCloseOutside, View.VISIBLE)
//                    setVisibilityButton(btCloseInside, View.GONE)
//                }
//            }
            v.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
//                    logD("onPageFinished $url, ${view.height}, ${view.contentHeight}")
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

            val content = htmlContent

            //phải gỡ hết các height của html FE thì mới run webview wrap_content được
//            val content = htmlContent.replace(oldValue = "height:", newValue = "height_:")

            //thêm event onClickBody
//            content = content.replace(
//                oldValue = "class=\"pmp-message-image\"",
//                newValue = "class=\"pmp-message-image\" onclick=\"onClickBody.performClick(this.value);\""
//            )

            v.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)

//            v.addJavascriptInterface(object : Any() {
//                @JavascriptInterface
//                fun performClick(string: String?) {
//                    onClickBody()
//                }
//            }, "onClickBody")
        }
    }

    private fun onClickClose() {
        finish()
    }

    private fun onClickBody(uri: Uri) {
        iamData?.actionClick?.let { actionClick ->
            val link = "$actionClick$uri"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(link)
            startActivity(i)
            finishAfterTransition()
        }
    }

    private fun configAutoCloseDialog() {
        logD(">>>closingAfter: ${iamData?.closingAfter}")
        iamData?.closingAfter?.let { closingAfter ->
            if (closingAfter > 0) {
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, (closingAfter * 1000).toLong())
            }
        }
    }

}
