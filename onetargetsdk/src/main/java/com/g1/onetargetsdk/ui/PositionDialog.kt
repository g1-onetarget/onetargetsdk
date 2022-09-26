package com.g1.onetargetsdk.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.g1.onetargetsdk.R

class PositionDialog : BaseDialogFragment() {
    private val logTag = javaClass.simpleName

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(context, R.style.FullDialogTheme)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_position, null)
        dialogBuilder.setView(dialogView)
        isCancelable = true
        init(dialogView)
        val alertDialog = dialogBuilder.create()
        alertDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        return alertDialog
    }

    private fun init(v: View) {
        val btOK = v.findViewById(R.id.btOK) as View
        val btCancel = v.findViewById(R.id.btCancel) as View
        btOK.setOnClickListener { dismiss() }
        btCancel.setOnClickListener { dismiss() }
    }

    fun showImmersivePos(
        context: Context,
        htmlContent: String?,
        sizeWidthPx: Int?,
        sizeHeightPx: Int?,
    ) {

        Toast.makeText(context, htmlContent, Toast.LENGTH_LONG).show()
//        if (activity is AppCompatActivity) {
//            activity.supportFragmentManager.let { fm ->
//                show(fm, logTag)
//                fm.executePendingTransactions()
//                dialog?.window?.let { w ->
//                    w.decorView.systemUiVisibility = activity.window.decorView.systemUiVisibility
//                    w.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
//                    if (sizeWidthPx != null && sizeHeightPx != null) {
//                        w.setLayout(sizeWidthPx, sizeHeightPx)
//                    }
//                }
//            }
//        }
    }
}
