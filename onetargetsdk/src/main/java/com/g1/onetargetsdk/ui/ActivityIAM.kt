package com.g1.onetargetsdk.ui

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setFinishOnTouchOutside(true)
        setTheme(R.style.AppTheme_DialogTheme)
        setContentView(R.layout.activity_iam)
    }

}
