package com.g1.onetargetsdk.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
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
            Log.d(logTag, s)
        }
    }

    private var iamData: IAMData? = null
    private var htmlContent: String = ""

    //    private var screenWidth = 1.0 //from 0.0 -> 1.0
//    private var screenHeight = 1.0 //from 0.0 -> 1.0
    private var isEnableTouchOutside = true
    private var isShowLog = false

    private var layoutDebugView: LinearLayoutCompat? = null
    private var tvDebug: AppCompatTextView? = null
    private var layoutRoot: RelativeLayout? = null
    private var layoutBody: LinearLayoutCompat? = null
    private var wv: WebView? = null
    private var btClose: AppCompatImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        logD("onCreate")
        LocalBroadcastUtil.sendMessage(context = this, isActivityIAMRunning = true)
        setupData()
        setTheme(R.style.AppTheme_DialogTheme)

        setContentView(R.layout.activity_iam)
        setupViews()

//        if (!isFullScreen()) {
//            setupScreenSize()
//        }
        configAutoCloseDialog()
        setupDebugView()
    }

    override fun onDestroy() {
//        logD("onDestroy")
        LocalBroadcastUtil.sendMessage(context = this, isActivityIAMRunning = false)
        super.onDestroy()
    }

//    private fun isFullScreen(): Boolean {
//        return screenWidth == 1.0 && screenHeight == 1.0
//    }

    @SuppressLint("SetTextI18n")
    private fun setupDebugView() {
        tvDebug?.text = "activeType: ${iamData?.activeType}" +
                "\nactiveValue: ${iamData?.activeValue}" +
                "\nclosingAfter: ${iamData?.closingAfter}" +
                "\nname: ${iamData?.name}"

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
//            getDoubleExtra(KEY_SCREEN_WIDTH, 1.0).let { screenWidth ->
//                this@ActivityIAM.screenWidth = screenWidth
//            }
//            getDoubleExtra(KEY_SCREEN_HEIGHT, 1.0).let { screenHeight ->
//                this@ActivityIAM.screenHeight = screenHeight
//            }
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

//    private fun setupScreenSize() {
//        layoutBody?.layoutParams?.apply {
//            width = (resources.displayMetrics.widthPixels * screenWidth).toInt()
//            height = (resources.displayMetrics.heightPixels * screenHeight).toInt()
//        }
//    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupViews() {
        layoutDebugView = findViewById(R.id.layoutDebugView)
        tvDebug = findViewById(R.id.tvDebug)
        layoutRoot = findViewById(R.id.layoutRoot)
        layoutBody = findViewById(R.id.layoutBody)
        wv = findViewById(R.id.wv)
        btClose = findViewById(R.id.btClose)

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
        btClose?.setOnClickListener {
            finish()
        }

        wv?.let { v ->
            v.setBackgroundColor(Color.TRANSPARENT)
            v.settings.javaScriptEnabled = true
            v.settings.loadWithOverviewMode = true
            v.settings.useWideViewPort = true
//            v.setInitialScale(1)
//            v.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING

//            v.settings.builtInZoomControls = true
//            v.setInitialScale(1)
//            v.setPadding(0, 0, 0, 0)

            v.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                val w = v.width
                val h = v.height
                val screenHeight = Utils.screenHeight
                val ratio = h * 100 / screenHeight
                logD("addOnLayoutChangeListener w: $w, h: $h, screenHeight: $screenHeight, ratio: $ratio")
                if (ratio >= 80) {
                    logD("ad full")
                } else {
                    logD("ad center")
                }
            }
            v.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
//                    logD("onPageFinished $url, ${view.height}, ${view.contentHeight}")
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    logD(">>>>shouldOverrideUrlLoading ${view?.url}")
                    return true
                }
            }

//            val content = htmlContent

            //phải gỡ hết các height của html FE thì mới run webview wrap_content được
            var content = htmlContent.replace(oldValue = "height:", newValue = "height_:")
            //thêm event onClickBody
            content = content.replace(
                oldValue = "class=\"pmp-message-image\"",
                newValue = "class=\"pmp-message-image\" onclick=\"onClickBody.performClick(this.value);\""
            )

            v.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)

            v.addJavascriptInterface(object : Any() {
                @JavascriptInterface
                fun performClick(string: String?) {
                    onClickBody()
                }
            }, "onClickBody")
        }
    }

    private fun onClickBody() {
        logD(">>>onClickBody")
        //TODO iplm
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
