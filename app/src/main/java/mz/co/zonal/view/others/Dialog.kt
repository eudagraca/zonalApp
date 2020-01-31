package mz.co.zonal.view.others

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.LinearLayout
import mz.co.zonal.R

object Dialog {
    fun dialog(context: Context): Dialog {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.loading)
            dialog.setCancelable(false)
            dialog.window?.setLayout(
                (Resources.getSystem().displayMetrics.widthPixels * 0.80).toInt(),
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            return dialog
    }
}