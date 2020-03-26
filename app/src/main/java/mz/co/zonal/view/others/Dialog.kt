package mz.co.zonal.view.others

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import mz.co.zonal.R

object Dialog {
    fun dialog(context: Context): Dialog {
        val dialogLoading = Dialog(context)
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLoading.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLoading.setContentView(R.layout.loading)
        dialogLoading.setCancelable(false)
        dialogLoading.window?.setLayout(
            (Resources.getSystem().displayMetrics.widthPixels * 0.80).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        return dialogLoading
    }

    fun alertDialog(onButtonClicked: (isClicked: Boolean) -> Unit, context: Context, message: String, title: String, buttonText: String): Dialog {
        val alerta = Dialog(context)
        alerta.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alerta.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alerta.setContentView(R.layout.alert)
        val mMessage = alerta.findViewById<TextView>(R.id.tv_message)
        val mTitle = alerta.findViewById<TextView>(R.id.tv_title)
        val mButton = alerta.findViewById<MaterialButton>(R.id.bt_button)
        mMessage.text = message
        mTitle.text = title
        mButton.text = buttonText
        alerta.setCancelable(true)
        alerta.window?.setLayout(
            (Resources.getSystem().displayMetrics.widthPixels * 0.80).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        mButton.setOnClickListener {
            alerta.dismiss()
            onButtonClicked.invoke(true)
        }
        return alerta
    }
}