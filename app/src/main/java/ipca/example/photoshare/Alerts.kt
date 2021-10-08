package ipca.example.photoshare

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.*

fun alertNotificatio(context: Activity, message: String) : AlertDialog {

    val dialogView: View = context.layoutInflater.inflate(R.layout.alert_discard, null)

    val builder = AlertDialog.Builder(context)
    builder.setCancelable(false)
    builder.setView(dialogView)
    val alertDialog = builder.create()

    alertDialog.window?.setGravity(Gravity.CENTER)
    alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    val textViewTitle = dialogView.findViewById<TextView>(R.id.textViewAlertMessage)
    textViewTitle.text = message

    alertDialog.show()

    GlobalScope.launch (Dispatchers.Main){
        delay(2000)
        alertDialog.dismiss()
    }
    return alertDialog
}