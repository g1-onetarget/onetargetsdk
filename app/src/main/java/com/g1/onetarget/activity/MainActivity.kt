package com.g1.onetarget.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.g1.onetarget.R
import com.g1.onetargetsdk.ApiUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
    }

    private fun setupViews() {
        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            toolbar.title = getString(R.string.app_name)
            toolbar.setTitleTextColor(Color.WHITE)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }

        btTestTracking.setOnClickListener {
            track()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun track() {
        tv.text = "Loading..."
        ApiUtils.sOService.answers.enqueue(object : Callback<Any> {
            override fun onResponse(
                call: Call<Any>,
                response: Response<Any>
            ) {
                tv.text = "onResponse ${Gson().toJson(response.body())}"
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                tv.text = "onFailure $t"
            }
        })
    }
}
