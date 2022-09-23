package com.g1.onetarget.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.g1.onetarget.R
import com.g1.onetarget.common.C
import com.g1.onetargetsdk.IAM

/**
 * Created by Loitp on 12.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class IAMActivity : AppCompatActivity() {
    private var toolbar: Toolbar? = null

    private fun logD(s: String) {
        Log.d(IAMActivity::class.java.simpleName, s)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iam)

        setupViews()
        checkIAM()
    }

    private fun setupViews() {
        setupActionBar()
    }

    private fun setupActionBar() {
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.title = getString(R.string.sample_iam)
            toolbar?.apply {
                setTitleTextColor(Color.WHITE)
                setNavigationOnClickListener {
                    finish()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkIAM() {
        logD("loitpp ~~~~~~currentTimeMillis ${System.currentTimeMillis()}")
        val workSpaceId = C.getWorkSpaceId()
        IAM.checkIAM(
            activity = this,
            workSpaceId = workSpaceId,
            onResponse = { isSuccessful, code, response, data ->
                logD("loitpp isSuccessful $isSuccessful")
                logD("loitpp code $code")
                logD("loitpp response $response")
                logD("loitpp checkIAM data $data")
                checkIAM()
            },
            onFailure = { t ->
                t.printStackTrace()
                checkIAM()
            }
        )
    }
}
