package com.g1.onetargetsdk.ext

import android.content.Intent
import android.os.Build
import java.io.Serializable

/**
 * Created by Loitp on 27,September,2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */

@Suppress("DEPRECATION", "UNCHECKED_CAST")
fun <T : Serializable?> Intent.getSerializable(key: String, clazz: Class<T>): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        this.getSerializableExtra(key, clazz)!!
    else
        this.getSerializableExtra(key) as T
}


