package com.g1.onetarget.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
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
    private var btNext: AppCompatButton? = null
    private var tvResponse: AppCompatTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iam)

        setupViews()
    }

    private fun setupViews() {
        setupActionBar()
    }

    private fun setupActionBar() {
        toolbar = findViewById(R.id.toolbar)
        btNext = findViewById(R.id.btNext)
        tvResponse = findViewById(R.id.tvResponse)

        setSupportActionBar(toolbar)
        supportActionBar?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.title = getString(R.string.sample_iam)
            toolbar?.apply {
                setTitleTextColor(Color.WHITE)
                setNavigationOnClickListener {
                    onBackPressed()
                }
            }
        }

        btNext?.setOnClickListener {
            onClickButtonNext()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onClickButtonNext() {
        val workSpaceId = C.workSpaceIdForIAM
        val identityId = C.identityIdForIAM
        tvResponse?.text = "Loading"
        IAM.checkIAM(
            workSpaceId = workSpaceId,
            identityId = identityId,
            onResponse = { isSuccessful, code, response ->
                tvResponse?.text =
                    "onResponse isSuccessful $isSuccessful\ncode $code\nresponse $response"
            },
            onFailure = { t ->
                tvResponse?.text = "onFailure $t"
            }
        )
    }
}
