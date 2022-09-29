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

    override fun onCreate(savedInstanceState: Bundle?) {
//        setupSDK()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
    }

//    private fun setupSDK() {
//        C.setEnv(C.DEV)
//
//        val configuration = Configuration(this)
//        if (C.isEnvDev()) {
//            configuration.setEnvironmentDev()
//        } else if (C.isEnvStag()) {
//            configuration.setEnvironmentStag()
//        } else if (C.isEnvProd()) {
//            configuration.setEnvironmentProd()
//        }
//        configuration.writeKey = C.getWorkSpaceId()
//        configuration.isShowLog = true
//        configuration.isEnableIAM = true
//        val resultSetupTracking = Analytics.setup(configuration)
//        val resultSetupIAM = IAM.setup(configuration, this)
//        Log.d(G1Application::class.java.simpleName, "resultSetupTracking $resultSetupTracking")
//        Log.d(G1Application::class.java.simpleName, "resultSetupIAM $resultSetupIAM")
//    }

    private fun setupViews() {
        setupActionBar()
    }

    private fun setupActionBar() {
        toolbar = findViewById(R.id.toolbar)
        btSampleTracking = findViewById(R.id.btSampleTracking)

        setSupportActionBar(toolbar)
        supportActionBar?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.title = getString(R.string.app_name_sample)
            toolbar?.apply {
                setTitleTextColor(Color.WHITE)
                setNavigationOnClickListener {
                    finish()
                }
            }
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
