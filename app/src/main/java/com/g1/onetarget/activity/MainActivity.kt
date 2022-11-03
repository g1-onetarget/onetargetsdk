package com.g1.onetarget.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.g1.onetarget.R

/**
 * Created by Loitp on 12.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class MainActivity : Activity() {
    private var btSampleTracking: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
    }

    private fun setupViews() {
        setupActionBar()
    }

    private fun setupActionBar() {
        btSampleTracking = findViewById(R.id.btSampleTracking)

        findViewById<View>(R.id.btBack).setOnClickListener {
            finish()
        }

        btSampleTracking?.setOnClickListener {
            goToTrackingScreen()
        }
    }

    private fun goToTrackingScreen() {
        val i = Intent(this, TrackingActivity::class.java)
        startActivity(i)
    }
}
