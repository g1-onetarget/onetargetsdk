package com.g1.onetargetsdk.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
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
    }

    private val logTag = "loitp${ActivityIAM::class.java.simpleName}"
    private fun logD(s: String) {
        Log.d(logTag, s)
    }

    private var htmlContent: String = ""
    private var screenWidth = 1.0 //from 0.0 -> 1.0
    private var screenHeight = 1.0 //from 0.0 -> 1.0

    private var wv: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupData()
        if (!isFullScreen()) {
            this.setFinishOnTouchOutside(true)//TODO iplm
            setTheme(R.style.AppTheme_DialogTheme)
        }

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
        intent?.getStringExtra(KEY_HTML_CONTENT)?.let { htmlContent ->
            this.htmlContent = htmlContent
        }
        intent?.getDoubleExtra(SCREEN_WIDTH, 1.0)?.let { screenWidth ->
            this.screenWidth = screenWidth
        }
        intent?.getDoubleExtra(SCREEN_HEIGHT, 1.0)?.let { screenHeight ->
            this.screenHeight = screenHeight
        }
        logD(">>>onCreate: $htmlContent")
        logD(">>>screenWidth: $screenWidth")
        logD(">>>screenHeight: $screenHeight")
    }

    private fun setupScreenSize() {
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(this.window.attributes)

        val width = (resources.displayMetrics.widthPixels * screenWidth).toInt()
        val height = (resources.displayMetrics.heightPixels * screenHeight).toInt()
        lp.width = width
        lp.height = height

        this.window.attributes = lp
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupViews() {
        wv = findViewById(R.id.wv)
        wv?.let { v ->
//            setBackgroundColor(Color.TRANSPARENT)//TODO revert
            v.setBackgroundColor(Color.YELLOW)
            v.settings.javaScriptEnabled = true
            v.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        }
    }

}
