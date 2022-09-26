package com.g1.onetargetsdk.ui

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
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
        const val IS_FULL_SCREEN = "IS_FULL_SCREEN"
    }

    private val logTag = "loitp${ActivityIAM::class.java.simpleName}"
    private fun logD(s: String) {
        Log.d(logTag, s)
    }

    private var htmlContent: String = ""
    private var isFullScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupData()
        if (!isFullScreen) {
            this.setFinishOnTouchOutside(true)//TODO iplm
            setTheme(R.style.AppTheme_DialogTheme)
        }

        setContentView(R.layout.activity_iam)
        setupViews()

        if (!isFullScreen) {
            setupScreenSize()
        }
    }

    private fun setupData() {
        intent?.getStringExtra(KEY_HTML_CONTENT)?.let { htmlContent ->
            this.htmlContent = htmlContent
        }
        intent?.getBooleanExtra(IS_FULL_SCREEN, true)?.let { isFullScreen ->
            this.isFullScreen = isFullScreen
        }
        logD(">>>onCreate: $htmlContent")
        logD(">>>isFullScreen: $isFullScreen")
    }

    private fun setupScreenSize() {
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(this.window.attributes)

        val width = (resources.displayMetrics.widthPixels * 0.50).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.50).toInt()
        lp.width = width
        lp.height = height

        this.window.attributes = lp
    }

    private fun setupViews() {

    }

}
