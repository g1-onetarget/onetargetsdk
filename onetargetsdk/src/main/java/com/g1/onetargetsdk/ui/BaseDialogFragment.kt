package com.g1.onetargetsdk.ui

import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Loitp on 04,August,2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
open class BaseDialogFragment : DialogFragment() {

    fun <T : ViewModel> getViewModel(className: Class<T>): T? {

        return activity?.let { ViewModelProvider(it)[className] }
    }

    fun <T : ViewModel> getSelfViewModel(className: Class<T>): T {

        return ViewModelProvider(this)[className]
    }

    fun lockScreen(isLock: Boolean) {
        if (isLock) {
            activity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

            activity?.window?.decorView?.alpha = 0.5f
        } else {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            activity?.window?.decorView?.alpha = 1.0f
        }
    }

    fun logD(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    fun logE(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    /**
     * fix bug: Can not perform this action after onSaveInstanceState
     * https://medium.com/@waseefakhtar/demystifying-androids-fragmenttransaction-and-solving-can-not-perform-this-action-after-3d45004aa22f
     */
    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
