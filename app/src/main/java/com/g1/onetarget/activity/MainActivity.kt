package com.g1.onetarget.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.g1.onetarget.R
import com.g1.onetargetsdk.Analytics
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
        Analytics.service.track(
            workspace_id = "ab44219f-dc9e-4080-943c-a127bd071da3",
            identity_id = "{web_push_player_id:cda154be-a37d-11ec-9d5f-52ceecedd8ea,email:example@gmail.com,phone:039889981}",
            event_name = "page_view",
            event_date = "1649302246132",
            eventData = "{pageTitle:Passenger Information,pagePath:/passengers/}",
        ).enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                Log.e("loitpp", "response ${response.isSuccessful}")
                tv.text = "onResponse ${Gson().toJson(response.body())}"
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("loitpp", "onFailure $t")
                tv.text = "onFailure $t"
            }
        })
    }
}
