package com.g1.onetargetsdk.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.LinearLayoutCompat
import com.g1.onetargetsdk.R


/**
 * Created by Loitp on 12.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class ActivityIAM : AppCompatActivity() {
    companion object {
        const val KEY_HTML_CONTENT = "KEY_HTML_CONTENT"
        const val SCREEN_WIDTH = "SCREEN_WIDTH"
        const val SCREEN_HEIGHT = "SCREEN_HEIGHT"
        const val ENABLE_TOUCH_OUTSIDE = "ENABLE_TOUCH_OUTSIDE"
    }

    private val logTag = "loitp${ActivityIAM::class.java.simpleName}"
    private fun logD(s: String) {
        Log.d(logTag, s)
    }

    private var htmlContent: String = ""
    private var screenWidth = 1.0 //from 0.0 -> 1.0
    private var screenHeight = 1.0 //from 0.0 -> 1.0
    private var isEnableTouchOutside = true

    private var layoutRoot: LinearLayoutCompat? = null
    private var layoutBody: LinearLayoutCompat? = null
    private var wv: WebView? = null
    private var btClose: AppCompatImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupData()
        setTheme(R.style.AppTheme_DialogTheme)

        setContentView(R.layout.activity_iam)
        setupViews()

        if (!isFullScreen()) {
            setupScreenSize()
        }
    }

    private fun isFullScreen(): Boolean {
        return screenWidth == 1.0 && screenHeight == 1.0
    }

    private fun setupData() {
        intent?.apply {
            getStringExtra(KEY_HTML_CONTENT)?.let { htmlContent ->
                this@ActivityIAM.htmlContent = htmlContent
            }
            getDoubleExtra(SCREEN_WIDTH, 1.0).let { screenWidth ->
                this@ActivityIAM.screenWidth = screenWidth
            }
            getDoubleExtra(SCREEN_HEIGHT, 1.0).let { screenHeight ->
                this@ActivityIAM.screenHeight = screenHeight
            }
            getBooleanExtra(ENABLE_TOUCH_OUTSIDE, true).let { isEnableTouchOutside ->
                this@ActivityIAM.isEnableTouchOutside = isEnableTouchOutside
            }
        }
        logD(">>>onCreate: $htmlContent")
        logD(">>>screenWidth: $screenWidth")
        logD(">>>screenHeight: $screenHeight")
    }

    private fun setupScreenSize() {
        layoutBody?.layoutParams?.apply {
            width = (resources.displayMetrics.widthPixels * screenWidth).toInt()
            height = (resources.displayMetrics.heightPixels * screenHeight).toInt()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupViews() {
        layoutRoot = findViewById(R.id.layoutRoot)
        layoutBody = findViewById(R.id.layoutBody)
        wv = findViewById(R.id.wv)
        btClose = findViewById(R.id.btClose)

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
            v.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    logD("onPageFinished $url")
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
    }

}
