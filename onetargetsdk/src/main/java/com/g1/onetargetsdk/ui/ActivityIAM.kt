package com.g1.onetargetsdk.ui

import android.os.Bundle
import android.util.Log
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
    }

    private val logTag = "loitp${ActivityIAM::class.java.simpleName}"
    private fun logD(s: String) {
        Log.d(logTag, s)
    }

    private var htmlContent: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.getStringExtra(KEY_HTML_CONTENT)?.let { htmlContent ->
            this.htmlContent = htmlContent
        }
//        logD(">>>onCreate: $htmlContent")
        this.setFinishOnTouchOutside(true)
        setTheme(R.style.AppTheme_DialogTheme)
        setContentView(R.layout.activity_iam)
    }

}
