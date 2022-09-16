package com.g1.onetarget.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import com.g1.onetarget.R
import com.g1.onetarget.common.C
import com.g1.onetargetsdk.IAM
import java.util.*

/**
 * Created by Loitp on 12.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class IAMActivity : AppCompatActivity() {
    private var toolbar: Toolbar? = null
    private var tvResponse: AppCompatTextView? = null
    private var responseData = ""

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
        tvResponse = findViewById(R.id.tvResponse)

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
        val workSpaceId = C.workSpaceId
        if (responseData.length > 50_000) {
            responseData = "..."
        }
        responseData = "$responseData\n\n\n>>>Loading ${Calendar.getInstance().time}"
        tvResponse?.text = responseData
        IAM.checkIAM(
            activity = this,
            workSpaceId = workSpaceId,
            onResponse = { isSuccessful, code, response ->
                responseData = "$responseData\n<<<onResponse isSuccessful $isSuccessful, " +
                        "code $code, response $response"
                tvResponse?.text = responseData
                checkIAM()
            },
            onFailure = { t ->
                responseData = "$responseData\n<<<onFailure $t"
                tvResponse?.text = responseData
                checkIAM()
            }
        )
    }

}
