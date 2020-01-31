package mz.co.zonal.view.others

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty

object Message {
    fun messageSucess(context: Context, message: String) {
        Toasty.success(context, message, Toast.LENGTH_LONG).show()
    }
    fun messageError(context: Context, message: String) {
        Toasty.error(context, message, Toast.LENGTH_LONG).show()
    }
    fun messageInfo(context: Context, message: String) {
        Toasty.info(context, message, Toast.LENGTH_LONG).show()
    }


}
fun View.snackMessage( message: String){
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).also { snackBar->
        snackBar.setAction("OK"){
            snackBar.dismiss()
        }
    }.show()
}