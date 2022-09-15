package com.g1.onetarget.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import com.g1.onetarget.R

/**
 * Created by Loitp on 12.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
class MainActivity : AppCompatActivity() {
    private var toolbar: Toolbar? = null
    private var btSampleTracking: AppCompatButton? = null
    private var btSampleIAM: AppCompatButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
    }

    private fun setupViews() {
        setupActionBar()
    }

    private fun setupActionBar() {
        toolbar = findViewById(R.id.toolbar)
        btSampleTracking = findViewById(R.id.btSampleTracking)
        btSampleIAM = findViewById(R.id.btSampleIAM)

        setSupportActionBar(toolbar)
        supportActionBar?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.title = getString(R.string.app_name_sample)
            toolbar?.apply {
                setTitleTextColor(Color.WHITE)
                setNavigationOnClickListener {
                    onBackPressed()
                }
            }
        }

        btSampleTracking?.setOnClickListener {
            goToTrackingScreen()
        }
        btSampleIAM?.setOnClickListener {
            goToIAMScreen()
        }
    }

    private fun goToTrackingScreen() {
        val i = Intent(this, TrackingActivity::class.java)
        startActivity(i)
    }

    private fun goToIAMScreen() {
        val i = Intent(this, IAMActivity::class.java)
        startActivity(i)
    }
}
